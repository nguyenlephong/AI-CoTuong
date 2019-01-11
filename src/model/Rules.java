package model;

import java.util.ArrayList;

public class Rules {
	private static int[] pos;
	private static Board board;
	private static char player;

	/*  Class này định nghĩa luật chơi của các quân cờ trên bàn cờ. Ký hiệu của
	 * từng ký tự  */
	/*  p: pháo p0 tương đương cho số quân từ trái qua phải, ví dụ có 2 quân pháo
	 * ký hiệu là pháo 0 và pháo 1 */
	/* c: là tốt (có người gọi là con chốt nè), gồm có z0-->z4 */
	/* x: là xe, gồm có x0,x1 m: là mã( con ngựa ấy), gồm có m0,m1 */
	/* t: là bồ( hay gọi là con tượng ấy), gồm có x0,x1 */
	/* s: là sỉ. gồm có s0,s1 */
	/*  v: là con vua( hay còn gọi là con tướng đó) gồm một quân b0
	 * tất cả gồm có 2 bên nên mỗi quân double lên */
	
	public static void toStringPath(ArrayList<int[]>list){
		for(int[] a : list){
			System.out.println("Toa do x:"+a[0]+", toa do y: "+a[1]);
		}
	}
	
	public static ArrayList<int[]> getListNextMove(String nodeName,int[]pos,Board board){
		Rules.board=board;
		Rules.pos=pos;
		Rules.player= nodeName.charAt(0);
		switch (nodeName.charAt(1)) {
		case 'p':
			return pMoveRules();
		case 'x':
			return xMoveRules();	
		case 'm':
			return mMoveRules();
		case 't':
			return tMoveRules();
		case 's':
			return sMoveRules();
		case 'v':
			return vMoveRules();
		case 'c':
			return cMoveRules();

		default:
			return null;
		}
	}

	private static ArrayList<int[]> cMoveRules() {
		/*
		 * Ở quân tốt, ta để ý ta phải xét 2 bên vì tốt không thể đi lùi, khi tốt qua sông rồi thì có thể
		 * sang trái hoặc sang phải. Do đó ta phải tách thành 2 bên, nếu là người chơi phía trên(b) thì quân chốt
		 * ở hoành độ lớn hơn 4 thì coi như là qua sông, ngược lại thì chỉ được quyền đi thẳng
		 * tương tự ở người chơi phía dưới (r) nếu hoành độ nhỏ hơn 5 là coi như chưa qua sông, chỉ được đi thẳng
		 */
		 ArrayList<int[]> moves = new ArrayList<int[]>();
	        int[][] targetU = new int[][]{{0, 1}, {0, -1}, {-1, 0}};
	        int[][] targetD = new int[][]{{0, 1}, {0, -1}, {1, 0}};
	        if (player == 'r') {
	            if (pos[0] > 4) {
	                int[] e = new int[]{pos[0] - 1, pos[1]};
	                if (board.isEmpty(e)) moves.add(e);
	                else if (board.getNode(e).color != player) moves.add(e);
	            } else {
	                for (int[] aTarget : targetU) {
	                    int[] e = new int[]{pos[0] + aTarget[0], pos[1] + aTarget[1]};
	                    if (!board.isInside(e)) continue;
	                    if (board.isEmpty(e)) moves.add(e);
	                    else if (board.getNode(e).color != player) moves.add(e);
	                }
	            }
	        }
	        if (player == 'b') {
	            if (pos[0] < 5) {
	                int[] e = new int[]{pos[0] + 1, pos[1]};
	                if (board.isEmpty(e)) moves.add(e);
	                else if (board.getNode(e).color != player) moves.add(e);
	            } else {
	                for (int[] aTarget : targetD) {
	                    int[] e = new int[]{pos[0] + aTarget[0], pos[1] + aTarget[1]};
	                    if (!board.isInside(e)) continue;
	                    if (board.isEmpty(e)) moves.add(e);
	                    else if (board.getNode(e).color != player) moves.add(e);
	                }
	            }
	        }

	        return moves;
	}

	private static ArrayList<int[]> vMoveRules() {
		 ArrayList<int[]> moves = new ArrayList<int[]>();
		 /*
		  * Tương tự như luật đi của nhưng quân khác, ta định nghĩa luật đi cho quân tướng
		  * Tuy nhiên ở quân tướng ta cần xét thêm trường hợp 2 quân tướng đối diện nhau
		  * Hướng giải quyết là ta đặt một cờ(true or false), ta xét tung độ( trục ngang) xem có bằng nhau không
		  * Nếu nó bằng thì tức nhiên nó sẽ đối diện nhau, ta sẽ xét thêm trên đường thẳng nối 2 quân tướng này có
		  * quân nào khác cảng mặt không, nếu có bất kỳ 1 quân nào thì trả về cờ false, ngược lại là cờ true 
		  */
	        int[][] target = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
	        for (int[] aTarget : target) {
	            int[] e = new int[]{pos[0] + aTarget[0], pos[1] + aTarget[1]};
	            if (!board.isInside(e) || ((e[0] > 2 || e[1] < 3 || e[1] > 5) && player == 'b') 
	            		|| ((e[0] < 7 || e[1] < 3 || e[1] > 5) && player == 'r'))
	                continue;
	            if (board.isEmpty(e)) moves.add(e);
	            else if (board.getNode(e).color != player) moves.add(e);
	        }
	        
	        boolean flag = true;
	        int[] oppoBoss = (player == 'r') ? board.mapNode.get("bv0").position : board.mapNode.get("rv0").position;
	        if (oppoBoss[1] == pos[1]) {
	        	//dùng min max để đảm bảo chạy từ vị tri nhỏ tới lớn
	            for (int i = Math.min(oppoBoss[0], pos[0]) + 1; i < Math.max(oppoBoss[0], pos[0]); i++) {
	                if (board.getNodes(i, pos[1]) != null) {
	                    flag = false;
	                    break;
	                }
	            }
	            if (flag) moves.add(oppoBoss);
	        }
	        return moves;
	}

	private static ArrayList<int[]> sMoveRules() {
		 ArrayList<int[]> moves = new ArrayList<int[]>();
	        int[][] target = new int[][]{{-1, -1}, {1, 1}, {-1, 1}, {1, -1}};
	        for (int[] aTarget : target) {
	            int[] e = new int[]{pos[0] + aTarget[0], pos[1] + aTarget[1]};
	            /*Kiểm tra các vị trí có nằm ngoài phạm vi đi không. vì tọa độ của bàn cơ tính theo màn hình
	             nên gồm có nếu hoành độ(trục cao) > 2 nghĩa là vượt ra chiều cao của khung quân sỉ, nếu tung độ( trục ngang)
	            gồm 2 vị trí từ nhỏ hơn 3( tính từ 0) và lớn hơn 5 nếu ngoài phạm vi này thì quân sỉ không đi được
	             tóm lại là trừ 6 điều kiện dưới đây thì nó nằm trong ô vuông của quân sỉ*/
	            if (!board.isInside(e) || ((e[0] > 2 || e[1] < 3 || e[1] > 5) && player == 'b') 
	            		|| ((e[0] < 7 || e[1] < 3 || e[1] > 5) && player == 'r'))
	                continue;
	            if (board.isEmpty(e)) moves.add(e);
	            else if (board.getNode(e).color != player) moves.add(e);
	        }
	        return moves;
	}

	private static ArrayList<int[]> tMoveRules() {
		/*
		 * Luật đi của quân cờ này tương tự như quân mã, nhưng nhớ xét thêm vị trí quân này không thể qua 
		 * sông được.
		 */
		 ArrayList<int[]> moves = new ArrayList<int[]>();
	        int[][] target = new int[][]{{-2, -2}, {2, -2}, {-2, 2}, {2, 2}};
	        int[][] obstacle = new int[][]{{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
	        for (int i = 0; i < target.length; i++) {
	            int[] e = new int[]{pos[0] + target[i][0], pos[1] + target[i][1]};
	            int[] f = new int[]{pos[0] + obstacle[i][0], pos[1] + obstacle[i][1]};
	            if (!board.isInside(e) || (e[0] > 4 && player == 'b') || (e[0] < 5 && player == 'r')) continue;
	            if (board.isEmpty(f)) {
	                if (board.isEmpty(e)) moves.add(e);
	                else if (board.getNode(e).color != player) moves.add(e);
	            }
	        }
	        return moves;
	}

	private static ArrayList<int[]> mMoveRules() {
		ArrayList<int[]> moves = new ArrayList<int[]>();
        /*Mãng target lưu 8 trạng thái mà quân mã có thể đi, hình chữ L nằm đứng, vẽ ra giấy sẽ thấy, hii*/
        int[][] target = new int[][]{{1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}};
        /*Ở quân mã có điều đặt biệt là nếu có quân khác cảng chân nó sẽ không đi được, nên có bốn vị trí cảng
         * chân mã như ở dưới, do trùng nhau nên sẽ double lên*/
        int[][] obstacle = new int[][]{{0, -1}, {1, 0}, {1, 0}, {0, 1}, {0, 1}, {-1, 0}, {-1, 0}, {0, -1}};
        
        for (int i = 0; i < target.length; i++) {
        	//mãng chứa các vị trí hiện tại của pos mà quân mã có thể đi được
            int[] e = new int[]{pos[0] + target[i][0], pos[1] + target[i][1]};
            //mãng f chứa các vị trí hiện tại của pos mà quân mã bị cảng chân không đi được
            int[] f = new int[]{pos[0] + obstacle[i][0], pos[1] + obstacle[i][1]};
            /*
             * Ta cần phải kiểm tra xem vị trí đó có nằm trong bàn cờ không( trường hợp quân mã nằm gần biên ấy)
             * nếu không nằm trong thì next qua vị trí khác, nếu thỏa nằm trong bàn cờ thì ta xét vị trí đó 
             * có quân cờ nào chưa, nếu chưa thì duy chuyển tới, nếu đã có rồi thì kiểm tra quân cờ đang đứng
             * đó là của người chơi nào, nếu của mình thì không đi được, còn của đối thủ thì đi được( tức là bụp
             * nó đó)
             */
            if (!board.isInside(e)) continue;
            
            if (board.isEmpty(f)) {
                if (board.isEmpty(e)) moves.add(e);
                else if (board.getNode(e).color != player) moves.add(e);
            }
        }
        return moves;
	}

	private static ArrayList<int[]> xMoveRules() {
		/*Hiểu được quân pháo rồi thì quân này không thể không hiểu nè, hii
		 * Nó đi y chang quân pháo, chỉ khác quân pháo ở cách kill, nó kill trên đường nó đi luôn
		 * Nghĩa là trên đường đi mà gặp kẻ địch là kill được hết
		 */
		 ArrayList<int[]> moves = new ArrayList<int[]>();
	        int[] yOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
	        int[] xOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
	        for (int offset : yOffsets) {
	            int[] rMove = new int[]{pos[0], pos[1] + offset};
	            if (board.isEmpty(rMove)) moves.add(rMove);
	            else if (board.isInside(rMove) && board.getNode(rMove).color != player) {
	                moves.add(rMove);
	                break;
	            } else break;
	        }
	        for (int offset : yOffsets) {
	            int[] lMove = new int[]{pos[0], pos[1] - offset};
	            if (board.isEmpty(lMove)) moves.add(lMove);
	            else if (board.isInside(lMove) && board.getNode(lMove).color != player) {
	                moves.add(lMove);
	                break;
	            } else break;
	        }
	        for (int offset : xOffsets) {
	            int[] uMove = new int[]{pos[0] - offset, pos[1]};
	            if (board.isEmpty(uMove)) moves.add(uMove);
	            else if (board.isInside(uMove) && board.getNode(uMove).color != player) {
	                moves.add(uMove);
	                break;
	            } else break;
	        }
	        for (int offset : xOffsets) {
	            int[] dMove = new int[]{pos[0] + offset, pos[1]};
	            if (board.isEmpty(dMove)) moves.add(dMove);
	            else if (board.isInside(dMove) && board.getNode(dMove).color != player) {
	                moves.add(dMove);
	                break;
	            } else break;
	        }
	        return moves;
	}

	private static ArrayList<int[]> pMoveRules() {
		/*
		 * Luật đi cho quân này cũng tương tự như quân xe, tuy nhiên khi quân này kill quân khác thì nó trên đường 
		 * thẳng kill quân đó phải có một quân chen giữa. vì vậy mà ở dưới có dùng 4 biến rr,ll,uu,dd tương
		 * đương với phải trái lên xuống. 
		 * Về luật đi thì tương tự các quân khác thôi, ta xét cái trường hợp chen giữa nhé
		 * Nếu như biến này (rr or uu or ll or dd) là false nghĩa là xung quanh nó ko có quân khác, nó sẽ đi được
		 * Ngược lại nếu biến này là true thì ta xét Node kế đó ( vẫn trên cùng một hàng nhé) là node rổng hay ko rỗng
		 * nghĩa là có quân của kẻ địch không, nếu ko thì không làm gì, nếu có quân kẻ địch thì bụp nó. hehe
		 */
		 ArrayList<int[]> moves = new ArrayList<int[]>();
	        int[] yOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
	        int[] xOffsets = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
	        boolean rr = false, ll = false, uu = false, dd = false;
	        for (int offset : yOffsets) {
	            int[] rMove = new int[]{pos[0], pos[1] + offset};
	            if (!board.isInside(rMove)) break;
	            boolean e = board.isEmpty(rMove);
	            if (!rr) {
	                if (e) moves.add(rMove);
	                else rr = true;
	            } else if (!e) {
	                if (board.getNode(rMove).color != player) moves.add(rMove);
	                break;
	            }
	        }
	        for (int offset : yOffsets) {
	            int[] lMove = new int[]{pos[0], pos[1] - offset};
	            if (!board.isInside(lMove)) break;
	            boolean e = board.isEmpty(lMove);
	            if (!ll) {
	                if (e) moves.add(lMove);
	                else ll = true;
	            } else if (!e) {
	                if (board.getNode(lMove).color != player) {
	                    moves.add(lMove);
	                }
	                break;
	            }
	        }
	        for (int offset : xOffsets) {
	            int[] uMove = new int[]{pos[0] - offset, pos[1]};
	            if (!board.isInside(uMove)) break;
	            boolean e = board.isEmpty(uMove);
	            if (!uu) {
	                if (e) moves.add(uMove);
	                else uu = true;
	            } else if (!e) {
	                if (board.getNode(uMove).color != player) moves.add(uMove);
	                break;
	            }
	        }
	        for (int offset : xOffsets) {
	            int[] dMove = new int[]{pos[0] + offset, pos[1]};
	            if (!board.isInside(dMove)) break;
	            boolean e = board.isEmpty(dMove);
	            if (!dd) {
	                if (e) moves.add(dMove);
	                else dd = true;
	            } else if (!e) {
	                if (board.getNode(dMove).color != player) moves.add(dMove);
	                break;
	            }
	        }
	        return moves;
	}
}
