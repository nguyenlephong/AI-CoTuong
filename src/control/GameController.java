package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import alogrithm.AlphaBetaNode;
import alogrithm.SearchModel;
import model.Board;
import model.Node;
import view.View;

public class GameController {

	private Map<String,Node> initPositon(){
		Map<String, Node> pieces = new HashMap<String, Node>();
		int count=0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <9; j++) {
				count++;
				pieces.put("ij"+count, new Node("ij"+count, new int[] { i, j }));
			}
		}
		return pieces;
	}
	
	public String getJlabelKeyPosition(int[] pos){
		String resuilt = null;
		Map<String, Node> mapPos = initPositon();
//		System.out.println("Search nodes");
		for (Node n :mapPos.values()) {
//			System.out.println(n.toString());
			if(n.position[0]==pos[0] && n.position[1]==pos[1]){
//				System.out.println(n.toString()+" day ne");
				return n.key;
			}
		}
//		System.out.println("End search nodes");
		return resuilt;
		
	}
	
	
	private Map<String, Node> initPieces() {
		Map<String, Node> pieces = new HashMap<String, Node>();
		pieces.put("bx0", new Node("bx0", new int[] { 0, 0 }));
		pieces.put("bm0", new Node("bm0", new int[] { 0, 1 }));
		pieces.put("bt0", new Node("bt0", new int[] { 0, 2 }));
		pieces.put("bs0", new Node("bs0", new int[] { 0, 3 }));
		pieces.put("bv0", new Node("bv0", new int[] { 0, 4 }));
		pieces.put("bs1", new Node("bs1", new int[] { 0, 5 }));
		pieces.put("bt1", new Node("bt1", new int[] { 0, 6 }));
		pieces.put("bm1", new Node("bm1", new int[] { 0, 7 }));
		pieces.put("bx1", new Node("bx1", new int[] { 0, 8 }));
		pieces.put("bp0", new Node("bp0", new int[] { 2, 1 }));
		pieces.put("bp1", new Node("bp1", new int[] { 2, 7 }));
		pieces.put("bc0", new Node("bc0", new int[] { 3, 0 }));
		pieces.put("bc1", new Node("bc1", new int[] { 3, 2 }));
		pieces.put("bc2", new Node("bc2", new int[] { 3, 4 }));
		pieces.put("bc3", new Node("bc3", new int[] { 3, 6 }));
		pieces.put("bc4", new Node("bc4", new int[] { 3, 8 }));

		pieces.put("rx0", new Node("rx0", new int[] { 9, 0 }));
		pieces.put("rm0", new Node("rm0", new int[] { 9, 1 }));
		pieces.put("rt0", new Node("rt0", new int[] { 9, 2 }));
		pieces.put("rs0", new Node("rs0", new int[] { 9, 3 }));
		pieces.put("rv0", new Node("rv0", new int[] { 9, 4 }));
		pieces.put("rs1", new Node("rs1", new int[] { 9, 5 }));
		pieces.put("rt1", new Node("rt1", new int[] { 9, 6 }));
		pieces.put("rm1", new Node("rm1", new int[] { 9, 7 }));
		pieces.put("rx1", new Node("rx1", new int[] { 9, 8 }));
		pieces.put("rp0", new Node("rp0", new int[] { 7, 1 }));
		pieces.put("rp1", new Node("rp1", new int[] { 7, 7 }));
		pieces.put("rc0", new Node("rc0", new int[] { 6, 0 }));
		pieces.put("rc1", new Node("rc1", new int[] { 6, 2 }));
		pieces.put("rc2", new Node("rc2", new int[] { 6, 4 }));
		pieces.put("rc3", new Node("rc3", new int[] { 6, 6 }));
		pieces.put("rc4", new Node("rc4", new int[] { 6, 8 }));
		return pieces;
	}
	

	private Board initBoard() {
		Board board = new Board();
		board.mapNode = initPieces();
		board.mapPositionNode=initPositon();
		for (Map.Entry<String, Node> stringPieceEntry : initPieces().entrySet())
			board.update(stringPieceEntry.getValue());
		return board;
	}

	public Board playChess() {
		/**
		 * Start game.
		 */
		initPieces();
		return initBoard();
	}
	public void moveChess2(Board board,View view, boolean people) {
		/**
		 * Implements AI action with minimax algorithms.
		 */
		SearchModel searchModel = new SearchModel();
		AlphaBetaNode res = searchModel.minimaxSearch(board, people);
		
		view.movePieceFromAI(res.piece, res.to);
		board.updateNode(res.piece, res.to);
		
	}
	public void moveChess(String key, int[] position, Board board) {
		/**
		 * Implements user's action.
		 */
		board.updateNode(key, position);
		
	}

	public void responseMoveChess(Board board, View view) {
		/**
		 * Implements artificial intelligence.
		 */
		SearchModel searchModel = new SearchModel();
		AlphaBetaNode result = searchModel.search(board);

		view.movePieceFromAI(result.piece, result.to);
		board.updateNode(result.piece, result.to);
	}

	public void printBoard(Board board) {
		/**
		 * Piece position is stored internally as [row, col], but output
		 * standard requires [col,row]. Here comes the conversion. eg. [0, 4]
		 * --> [E, 0]
		 */
		Map<String, Node> pieces = board.mapNode;
		for (Map.Entry<String, Node> stringPieceEntry : pieces.entrySet()) {
			Node piece = stringPieceEntry.getValue();
			System.out.println(stringPieceEntry.getKey() + ":" + (char) (piece.position[1] + 'A') + piece.position[0]);
		}

		System.out.println();
	}

	public char hasWin(Board board) {
		/**
		 * Judge has the game ended.
		 * 
		 * @return 'r' for RED wins, 'b' for BLACK wins, 'x' for game continues.
		 */
		boolean isRedWin = board.mapNode.get("bv0") == null;
		boolean isBlackWin = board.mapNode.get("rv0") == null;
		if (isRedWin)
			return 'r';
		else if (isBlackWin)
			return 'b';
		else
			return 'x';
	}
	
	public List<Node> getListNodeEaten(String playerName,Board board){
		List<Node> listOfEaten = new ArrayList<>();
		Map<String, Node> mapInitNode = initPieces();
		for(Node n : mapInitNode.values()){
			String player = n.key.charAt(0)+"";
			if(!player.equals(playerName)){
				if(!board.mapNode.containsKey(n.key)){
					/*
					 * Nếu con cờ trong map khởi tạo ban đầu không còn trên bàn cờ hiện tại thì có nghĩa nó đã
					 * bị kill
					 */
					listOfEaten.add(n);
				}
			}
			
		}
		
		return listOfEaten;
		
	}
	public String listNodeEatenToString(String playerName,Board board){
		String resuit ="";
		List<Node> listOfEaten =getListNodeEaten(playerName, board);
		for (int i = 0; i < listOfEaten.size(); i++) {
			System.out.println(listOfEaten.get(i).key+" used to kill by player "+playerName);
			resuit+= listOfEaten.get(i).key+"\t";
		}
		return resuit;
	}

}
