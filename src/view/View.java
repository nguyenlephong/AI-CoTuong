package view;

import model.*;
import control.GameController;
import control.MenuController;
import dao.DAOImage;
import dao.DAOSave;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class View {
	private static final int CHESS_BOARD_WIDTH = 700, CHESS_BOARD_HEIGHT = 712;
	private static final int  SIZE_NODES_START_X = 0,SIZE_NODES_START_Y=0;
	private static final int PIECE_WIDTH = 67, PIECE_HEIGHT = 67;
	private static final int NODE_BORDER_X = 68, NODE_BORDER_Y = 68;
	private static final int OFFSET_X = 50, OFFSET_Y = 15;

	private Map<String, JLabel> mapNodeObjects = new HashMap<String, JLabel>();
	private Map<String, JLabel> mapNodePositionObjects = new HashMap<String, JLabel>();
	private Board board;
	private String selectedPieceKey;
	private JFrame frame;
	private JLayeredPane pane;
	private GameController controller;
	private JLabel lblPlayer;
	
	JPanel jpnEatenByAI = new JPanel();
	JPanel jpnEatenByYou = new JPanel();

	//menu
	public static JMenu jmnGames;
	public static JMenu jmnLevel;
	public static JMenu jmnHelp;

	public static JMenuItem jmnMiniMax;
	public static JMenuItem jmnAlphaBeta;

	public static JMenuItem jmnitPC;
	public static JMenuItem jmnitCC;
	public static JMenuItem jmnitExit;
	//end menu
	public View(GameController gameController) {
		this.controller = gameController;
	}

	public void init(final Board board) {
		this.board = board;
		JPanel root = new JPanel();
		frame = new JFrame("AI Chinse chess IT_NLU");
		frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());
		frame.setSize(CHESS_BOARD_WIDTH + 295, CHESS_BOARD_HEIGHT + 40);
		// frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pane = new JLayeredPane();

		/* Initialize chess board and listeners on each slot. */
		JLabel bgBoard = new JLabel(new ImageIcon("res/img/board.png"));
		bgBoard.setLocation(0, 0);
		bgBoard.setSize(CHESS_BOARD_WIDTH, CHESS_BOARD_HEIGHT);
		bgBoard.addMouseListener(new BoardClickListener());
		pane.add(bgBoard, 1);

		/* Initialize player image. */
		lblPlayer = new JLabel(new ImageIcon("res/img/r.png"));
		lblPlayer.setLocation(10, 320);
		lblPlayer.setSize(PIECE_WIDTH, PIECE_HEIGHT);
		pane.add(lblPlayer, 0);

		/* Initialize chess pieces and listeners on each piece position. */
		
		
		/* Initialize chess pieces and listeners on each piece. */
		Map<String, Node> pieces = board.mapNode;
		for (Map.Entry<String, Node> stringPieceEntry : pieces.entrySet()) {
			String key = stringPieceEntry.getKey();
			int[] pos = stringPieceEntry.getValue().position;
			int[] sPos = modelToViewConverter(pos);
			JLabel lblPiece = new JLabel(new ImageIcon("res/img/" + key.substring(0, 2) + ".png"));

			lblPiece.setLocation(sPos[0], sPos[1]);
			lblPiece.setSize(PIECE_WIDTH, PIECE_HEIGHT);
			lblPiece.addMouseListener(new PieceOnClickListener(key));
			
			//panel add piece eated by AI
			
			mapNodeObjects.put(stringPieceEntry.getKey(), lblPiece);
			pane.add(lblPiece, 0);
		}
		Map<String, Node> piecesPosition = board.mapPositionNode;
		for (Map.Entry<String, Node> stringPieceEntry : piecesPosition.entrySet()) {
//			String key = stringPieceEntry.getKey();
			int[] pos = stringPieceEntry.getValue().position;
			int[] sPos = modelToViewConverter(pos);
			
			JLabel lblPiece = new JLabel();
			
			lblPiece.setLocation(sPos[0], sPos[1]);
			lblPiece.setSize(PIECE_WIDTH, PIECE_HEIGHT);
//			lblPiece.addMouseListener(new PieceOnClickListener(key));
			
			mapNodePositionObjects.put(stringPieceEntry.getKey(), lblPiece);
			
			
			pane.add(lblPiece, 0);
		}

		
		
		root.setLayout(new BorderLayout());
		pane.setSize(CHESS_BOARD_WIDTH, CHESS_BOARD_HEIGHT);
		root.add(pane, BorderLayout.CENTER);

		JPanel jpnRight = showPanelRight();
		jpnRight.setSize(new Dimension(250, CHESS_BOARD_HEIGHT + 40));

		JScrollPane jspRight = new JScrollPane(jpnRight);
		jspRight.setPreferredSize(new Dimension(278, CHESS_BOARD_HEIGHT+40));
		root.add(jspRight, BorderLayout.EAST);

		JLabel jlbDesign = new JLabel("Designer by IT-NLU");
		root.add(jlbDesign, BorderLayout.SOUTH);

		showMenuBar();
		
		
		root.setBackground(Color.GREEN);
		frame.add(root);
		frame.setVisible(true);

	}



	public void showMenuBar() {
		JMenuBar jmnBar = new JMenuBar();
		jmnBar.setBackground(Color.CYAN);
		frame.setJMenuBar(jmnBar);

		jmnGames = new JMenu("Games");
		jmnLevel = new JMenu("Level");
		jmnHelp = new JMenu("Help");

		jmnMiniMax = new JMenuItem("MiniMax" );
		jmnAlphaBeta = new JMenuItem("Alpha-Beta");

		jmnLevel.add(jmnMiniMax);
		jmnLevel.add(jmnAlphaBeta);

		jmnGames.setIcon(DAOImage.iconGames);
		jmnHelp.setIcon(DAOImage.helps);
		jmnLevel.setIcon(DAOImage.levels);

		jmnitPC = new JMenuItem("People-Computer");
		jmnitCC = new JMenuItem("Computer-Computer");
		jmnitExit = new JMenuItem("Exit");
		jmnitExit.setIcon(DAOImage.exit);
		jmnitPC.setIcon(DAOImage.newGames);
		jmnitPC.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.VK_P));

		jmnitCC.setIcon(DAOImage.newGames);
		jmnitCC.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.VK_C));

		jmnitExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));

		jmnGames.add(jmnitPC);
		jmnGames.add(jmnitCC);
		jmnGames.add(jmnitExit);
		jmnBar.add(jmnGames);
		jmnBar.add(jmnLevel);
		jmnBar.add(jmnHelp);
		
		
		
		MenuController menuItemListener = new MenuController();
		jmnHelp.addMenuListener(menuItemListener);
		jmnitPC.addActionListener(menuItemListener);
		jmnitCC.addActionListener(menuItemListener);
		jmnAlphaBeta.addActionListener(menuItemListener);
		jmnMiniMax.addActionListener(menuItemListener);
		jmnitExit.addActionListener(menuItemListener);
	}

	public JPanel showPanelRight() {
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());

		JPanel jpnCenter = new JPanel();
		jpnCenter.setLayout(new GridLayout(4, 1));

		JPanel jpnAI = new JPanel();
		JPanel jpnPlayer = new JPanel();

		ImageIcon imgAI = new ImageIcon("res/img/NLU.png");
		ImageIcon imgPlayer = new ImageIcon("res/img/FIT1.png");
		JLabel jlbAI = new JLabel(imgAI);
		JLabel jlbPlayer = new JLabel(imgPlayer);

		jpnAI.add(jlbAI);
		jpnPlayer.add(jlbPlayer);

		// jpnEatenByAI.setLayout(new GridLayout(4, 4));
		// jpnEatenByYou.setLayout(new GridLayout(4, 4));

		jpnCenter.add(jpnAI);
		jpnCenter.add(jpnEatenByAI);
		jpnCenter.add(jpnPlayer);
		jpnCenter.add(jpnEatenByYou);

		// JLabel jlbTitle = new JLabel("AI SYSTEMS TEST");

		JPanel jpnBox = new JPanel();
		jpnBox.setLayout(new BoxLayout(jpnBox, BoxLayout.Y_AXIS));
		// jpnBox.add(jlbTitle);
		jpnBox.add(jpnCenter);

		root.add(jpnBox, BorderLayout.CENTER);
		return root;
	}

	public JLabel movePieceFromModel(String pieceKey, int[] to) {
		JLabel pieceObject = mapNodeObjects.get(pieceKey);

		int[] sPos = modelToViewConverter(to);
		pieceObject.setLocation(sPos[0], sPos[1]);
		/* Clear 'from' and 'to' info on the board */
		selectedPieceKey = null;
		return pieceObject;
	}

	public JLabel movePieceFromAI(String pieceKey, int[] to) {
		removesPathNodeAI();
		
		Stack<JLabel> stackReLight = DAOSave.stackNodeReLightAI;
		if (!stackReLight.isEmpty()) {
			JLabel jlbAI = stackReLight.pop();
			String key = jlbAI.getName();
			ImageIcon icon = new ImageIcon(DAOImage.getImage(key).getLink());
			jlbAI.setIcon(icon);
		}

		Node inNewPos = board.getNode(to);
		if (inNewPos != null) {
			pane.remove(mapNodeObjects.get(inNewPos.key));
			mapNodeObjects.remove(inNewPos.key);
		}
		ImageIcon icon = new ImageIcon(DAOImage.getImageLight(pieceKey).getLink());

		JLabel pieceObject = mapNodeObjects.get(pieceKey);
		int[] sPos = modelToViewConverter(to);
		pieceObject.setLocation(sPos[0], sPos[1]);
		/* Clear 'from' and 'to' info on the board */

		pieceObject.setName(pieceKey);
		DAOSave.stackNodeReLightAI.push(pieceObject);
		pieceObject.setIcon(icon);

		selectedPieceKey = null;
		return pieceObject;
	}
	public void removesPathNodeAI() {
		Stack<JLabel> stack = DAOSave.stackNodeRePathPlayer;
		while (!stack.isEmpty()) {
			JLabel jlbPathNodes = stack.pop();
			jlbPathNodes.setLocation(4000, 4000);
		}
	}


	private int[] modelToViewConverter(int pos[]) {
		int sx = pos[1] * NODE_BORDER_Y + OFFSET_X, sy = pos[0] * NODE_BORDER_X + OFFSET_Y;
		return new int[] { sx, sy };
	}
	private int[] modelToViewConverterSearchMoves(int pos[]) {
		int sx = (pos[1] * NODE_BORDER_Y + OFFSET_X)+ SIZE_NODES_START_X, sy = (pos[0] * NODE_BORDER_X + OFFSET_Y)+ SIZE_NODES_START_Y;
		return new int[] { sx, sy };
	}

	private int[] viewToModelConverter(int sPos[]) {
		/*
		 * To make things right, I have to put an 'additional sy offset'. God
		 * knows why.
		 */
		int ADDITIONAL_SY_OFFSET = 25;
		int y = (sPos[0] - OFFSET_X) / NODE_BORDER_Y, x = (sPos[1] - OFFSET_Y - ADDITIONAL_SY_OFFSET) / NODE_BORDER_X;
		return new int[] { x, y };
	}
	public int[] viewToModelConverterSearchMoves(int sPos[]) {
		/*
		 * To make things right, I have to put an 'additional sy offset'. God
		 * knows why.
		 */
		int ADDITIONAL_SY_OFFSET = 25;
		int y = (sPos[0] - OFFSET_X)+SIZE_NODES_START_X / NODE_BORDER_Y, x = (sPos[1] - OFFSET_Y - ADDITIONAL_SY_OFFSET)+SIZE_NODES_START_Y / NODE_BORDER_X;
		return new int[] { x, y };
	}

	public void showPlayer(char player) {
		lblPlayer.setIcon(new ImageIcon("res/img/" + player + ".png"));
		frame.setVisible(true);
	}

	public void showWinner(char player) {
		JOptionPane.showMessageDialog(null, (player == 'r') ? "Red player has won!" : "Black player has won!",
				"Intelligent Chinese Chess", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	public void addPieceEaten() {
		jpnEatenByAI.removeAll();
		jpnEatenByYou.removeAll();

		// System.out.println(key+" you new click");
		List<Node> listOfNodeEaten = controller.getListNodeEaten("b", board);
		List<Image> listImage = new DAOImage().getListImgFromListEaten(listOfNodeEaten);

		List<Node> listOfNodeEatenPlayer = controller.getListNodeEaten("r", board);
		List<Image> listImagePlayer = new DAOImage().getListImgFromListEaten(listOfNodeEatenPlayer);

		jpnEatenByAI.setLayout(new GridLayout((listImage.size() / 4) + 1, 4));
		jpnEatenByYou.setLayout(new GridLayout((listImagePlayer.size() / 4) + 1, 4));

		for (int i = 0; i < listImage.size(); i++) {
			ImageIcon img = new ImageIcon(listImage.get(i).getLink());
			JLabel jlbImg = new JLabel(img);
			jpnEatenByAI.add(jlbImg);
		}
		for (int i = 0; i < listImagePlayer.size(); i++) {
			ImageIcon img = new ImageIcon(listImagePlayer.get(i).getLink());
			JLabel jlbImg = new JLabel(img);
			jpnEatenByYou.add(jlbImg);
		}
	}
	
	class PieceOnClickListener extends MouseAdapter {
		private String key;
		PieceOnClickListener(String key) {
			this.key = key;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
				removesPathNode();
			if (selectedPieceKey != null && key.charAt(0) != board.player) {
				int[] pos = board.mapNode.get(key).position;
				System.out.println("x: " + pos[0] + ",y: " + pos[1]);
				int[] selectedPiecePos = board.mapNode.get(selectedPieceKey).position;
				/* If an enemy piece already has been selected. */
				// Rules.toStringPath(Rules.getListNextMove(selectedPieceKey,
				// selectedPiecePos, board));
				for (int[] each : Rules.getListNextMove(selectedPieceKey, selectedPiecePos, board)) {
					if (Arrays.equals(each, pos)) {
						// Kill self and move that piece.
						pane.remove(mapNodeObjects.get(key));
						mapNodeObjects.remove(key);
						controller.moveChess(selectedPieceKey, pos, board);

						ImageIcon icon = new ImageIcon(DAOImage.getImage(selectedPieceKey).getLink());
						movePieceFromModel(selectedPieceKey, pos).setIcon(icon);

						addPieceEaten();
						break;
					}
				}
			} else if (key.charAt(0) == board.player) {
				/* Select the piece. */
				selectedPieceKey = key;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			addPieceEaten();
			removeImageReLight();
			removesPathNode();

			if (selectedPieceKey != null && key.charAt(0) == board.player) {
				JLabel pieceObject = mapNodeObjects.get(selectedPieceKey);
				ImageIcon iconLight = new ImageIcon(DAOImage.getImageLight(selectedPieceKey).getLink());
				pieceObject.setIcon(iconLight);
				pieceObject.setName(selectedPieceKey);
				DAOSave.stackNodeReLightPlayer.push(pieceObject);
			}

			searchPathNode();

		}

		public void removesPathNode() {
			Stack<JLabel> stack = DAOSave.stackNodeRePathPlayer;
			while (!stack.isEmpty()) {
				JLabel jlbPathNodes = stack.pop();
				jlbPathNodes.setLocation(4000, 4000);
			}
		}

		public void searchPathNode() {

			if (selectedPieceKey != null) {
				int[] selectedPiecePos = board.mapNode.get(selectedPieceKey).position;
				if (selectedPiecePos != null) {
					for (int[] moves : Rules.getListNextMove(selectedPieceKey, selectedPiecePos, board)) {
//						System.out.println(Arrays.toString(moves)+" moves");
						int[] sPos = modelToViewConverterSearchMoves(moves);
//						System.out.println(Arrays.toString(sPos)+ " spos");
						/*
						 * xữ lý nhận vào tọa độ x y
						 * duyệt vòng for lấy ra cái key của map position rồi từ đó lấy được jlabel ij+count
						 *
						 */
//						System.out.println("toa do x: " + moves[0] + ", toa do y: " + moves[1]);
						String key =controller.getJlabelKeyPosition(moves);
						
						if(key!=null){
//							System.out.println(" label thu : "+key);
							JLabel k = mapNodePositionObjects.get(key);
							k.setLocation(sPos[0], sPos[1]);
							k.setIcon(DAOImage.nodesPath);
							k.setName(key);
							
							for (Node n :board.mapNode.values()) {
								if(n.position[0]==moves[0] && n.position[1]==moves[1]){
									k.setIcon(DAOImage.killed);
								}
							}
							
							DAOSave.stackNodeRePathPlayer.push(k);
						}
					}
				}
			}
		}

		public void addPieceEaten() {
			jpnEatenByAI.removeAll();
			jpnEatenByYou.removeAll();

			// System.out.println(key+" you new click");
			List<Node> listOfNodeEaten = controller.getListNodeEaten("b", board);
			List<Image> listImage = new DAOImage().getListImgFromListEaten(listOfNodeEaten);

			List<Node> listOfNodeEatenPlayer = controller.getListNodeEaten("r", board);
			List<Image> listImagePlayer = new DAOImage().getListImgFromListEaten(listOfNodeEatenPlayer);

			jpnEatenByAI.setLayout(new GridLayout((listImage.size() / 4) + 1, 4));
			jpnEatenByYou.setLayout(new GridLayout((listImagePlayer.size() / 4) + 1, 4));

			for (int i = 0; i < listImage.size(); i++) {
				ImageIcon img = new ImageIcon(listImage.get(i).getLink());
				JLabel jlbImg = new JLabel(img);
				jpnEatenByAI.add(jlbImg);
			}
			for (int i = 0; i < listImagePlayer.size(); i++) {
				ImageIcon img = new ImageIcon(listImagePlayer.get(i).getLink());
				JLabel jlbImg = new JLabel(img);
				jpnEatenByYou.add(jlbImg);
			}
		}

		public void removeImageReLight() {
			Stack<JLabel> stackReLight = DAOSave.stackNodeReLightPlayer;
			if (!stackReLight.isEmpty()) {
				JLabel jlbPlayer = stackReLight.pop();
				String key = jlbPlayer.getName();
				ImageIcon icon = new ImageIcon(DAOImage.getImage(key).getLink());
				jlbPlayer.setIcon(icon);
			}
		}
	}

	class BoardClickListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			removesPathNode();
			if (selectedPieceKey != null) {
				int[] sPos = new int[] { e.getXOnScreen() - frame.getX(), e.getYOnScreen() - frame.getY() };
				int[] pos = viewToModelConverter(sPos);
				int[] selectedPiecePos = board.mapNode.get(selectedPieceKey).position;
				// Rules.toStringPath(Rules.getListNextMove(selectedPieceKey,
				// selectedPiecePos, board));
				for (int[] each : Rules.getListNextMove(selectedPieceKey, selectedPiecePos, board)) {
					if (Arrays.equals(each, pos)) {
						controller.moveChess(selectedPieceKey, pos, board);
						ImageIcon icon = new ImageIcon(DAOImage.getImage(selectedPieceKey).getLink());
						movePieceFromModel(selectedPieceKey, pos).setIcon(icon);
						break;
					}
				}
			}
		}
		public void removesPathNode() {
			Stack<JLabel> stack = DAOSave.stackNodeRePathPlayer;
			while (!stack.isEmpty()) {
				JLabel jlbPathNodes = stack.pop();
				jlbPathNodes.setLocation(4000, 4000);
			}
		}

	}

}
