package alogrithm;

import control.GameController;
import model.Board;
import model.Node;
import model.Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardUpLeftHandler;
public class SearchModel {
    private static int DEPTH = 3;
    private Board board;
    private GameController controller = new GameController();

    public AlphaBetaNode search(Board board) {
        this.board = board;
//        if (board.mapNode.size() < 28)
//            DEPTH = 3;
//        if (board.mapNode.size() < 16)
//            DEPTH = 4;
//        if (board.mapNode.size() < 6)
//            DEPTH = 5;
//        if (board.mapNode.size() < 4)
//            DEPTH = 6;
        long startTime = System.currentTimeMillis();
        AlphaBetaNode best = null;
        ArrayList<AlphaBetaNode> moves = generateMovesForAll(true);
        for (AlphaBetaNode n : moves) {
            /* Move*/
            Node eaten = board.updateNode(n.piece, n.to);
            n.value = alphaBeta(DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            /* Select a best move during searching to save time*/
            if (best == null || n.value >= best.value)  best = n;
            /* Back move*/
            board.updateNode(n.piece, n.from);
           
            if (eaten != null) {
                board.mapNode.put(eaten.key, eaten);
                board.backNode(eaten.key);
            }
        }
        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTime+" is time find path of AI with alpha beta");
        return best;
    }


    private int alphaBeta(int depth, int alpha, int beta, boolean isMax) {
        /* Return evaluation if reaching leaf node or any side won.*/
    	char keyMan = (isMax) ? 'r':'b';
        if (depth == 0 || controller.hasWin(board) != 'x')
            return new EvalModel().eval(board, keyMan); // b
        ArrayList<AlphaBetaNode> moves = generateMovesForAll(isMax);

        synchronized (this) {
            for (final AlphaBetaNode n : moves) {
            	Node eaten = board.updateNode(n.piece, n.to);
                    if (isMax) alpha = Math.max(alpha, alphaBeta(depth - 1, alpha, beta, false));
                    else beta = Math.min(beta, alphaBeta(depth - 1, alpha, beta, true));
                board.updateNode(n.piece, n.from);
                if (eaten != null) {
                    board.mapNode.put(eaten.key, eaten);
                    board.backNode(eaten.key);
                }
            /* Cut-off */
                if (beta <= alpha) break;
            }
        }
        return isMax ? alpha : beta;
    }
    
    public AlphaBetaNode minimaxSearch(Board board, boolean people){
		this.board = board;
//		DEPTH=3;
//		if (board.mapNode.size() < 28) DEPTH = 3;
//		if (board.mapNode.size() < 16) DEPTH = 4;
//		if (board.mapNode.size() < 6) DEPTH = 5;
//		if (board.mapNode.size() < 4) DEPTH = 6;
		long startTime = System.currentTimeMillis();
		AlphaBetaNode best = null;
		ArrayList<AlphaBetaNode> moves = generateMovesForAll(people);
		for (AlphaBetaNode n : moves) {
			/* Move */
			Node eaten = board.updateNode(n.piece, n.to);
			if (eaten != null) System.out.println("Node eaten: " + eaten.toStringName());
			n.value = minimax(DEPTH, people);
			/* Select a best move during searching to save time */
			if (best == null || n.value >= best.value) best = n;
//			System.out.println(best.value+" is value duoc chon");
			/* Back move */
			Node a = board.updateNode(n.piece, n.from);
			if (a != null) System.out.println("Node a: " + a.toStringName());
			if (eaten != null) {
				board.mapNode.put(eaten.key, eaten);
				board.backNode(eaten.key);
			}
		}
		long finishTime = System.currentTimeMillis();
		System.out.println(finishTime - startTime + " is time find path of AI with minimax");
//		DEPTH=3;
		return best;
	}
	public int minimax(int depth, boolean isMax){
		char keyMan = (!isMax) ? 'r':'b';
		if (depth == 0 || controller.hasWin(board) != 'x') {
//			System.out.println(new EvalModel().eval(board, keyMan)+"---"+keyMan+" ham danh ggia");
			return new EvalModel().eval(board, keyMan);
		}
		ArrayList<AlphaBetaNode> moves = generateMovesForAll(isMax);
		List<Integer> list = new ArrayList<>();
		synchronized (this) {
			for (final AlphaBetaNode n : moves) {
				Node eaten = board.updateNode(n.piece, n.to);
				if (isMax){
					int best = Integer.MIN_VALUE;
					int val =minimax(depth - 1,  false);
					best = Math.max(best, val);
					list.add(best);
					board.updateNode(n.piece, n.from);
					return best;
				}
				else if(!isMax){
					int best = Integer.MAX_VALUE;
					int val =  minimax(depth - 1,  true);
					best = Math.min(best, val);
					list.add(best);
					
					board.updateNode(n.piece, n.from);
					return best;
				}
				if (eaten != null) {
					board.mapNode.put(eaten.key, eaten);
					board.backNode(eaten.key);
				}
					
			}
		}

		return isMax ? minValue(list) : maxValue(list);
	}
	
	public int minValue(List<Integer> list) {
		int min = Integer.MAX_VALUE;
		for (Integer i : list)
			if (i < min)
				min = i;
		return min;
	}

	public int maxValue(List<Integer> list) {
		int max = Integer.MIN_VALUE;
		for (Integer i : list)
			if (i > max)
				max = i;
		return max;
	}
    private ArrayList<AlphaBetaNode> generateMovesForAll(boolean isMax) {
        ArrayList<AlphaBetaNode> moves = new ArrayList<AlphaBetaNode>();
        for (Map.Entry<String, Node> stringPieceEntry : board.mapNode.entrySet()) {
        	Node piece = stringPieceEntry.getValue();
            if (isMax && piece.color == 'r') continue;
            if (!isMax && piece.color == 'b') continue;
            for (int[] nxt : Rules.getListNextMove(piece.key, piece.position, board))
                moves.add(new AlphaBetaNode(piece.key, piece.position, nxt));
        }
        return moves;
    }

}