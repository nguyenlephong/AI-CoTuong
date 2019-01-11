package application;

import control.GameController;
import model.Board;
import view.View;

public class Play {
	private Board board;

	private GameController controller;
	private View view;
	public static boolean startC = false;
	public static boolean startP = false;

	public static String algorithms = "Alpha-Beta";

	public static void main(String[] args) throws InterruptedException {
		Play game = new Play();
		game.init();
		game.run();
	}

	public void init() {
		controller = new GameController();
		board = controller.playChess();

		view = new View(controller);
		view.init(board);
	}

	public void run() throws InterruptedException {

		while (controller.hasWin(board) == 'x') {
			view.showPlayer('r');
			if (startC) {
				view.showPlayer('r');
				/* AI in minimax. */
				if (controller.hasWin(board) != 'x') view.showWinner('r');
				controller.moveChess2(board, view, false);
				view.addPieceEaten();
				Thread.sleep(2000);
				view.showPlayer('b');
				/* AI in alpha beta. */
				if (controller.hasWin(board) != 'x') view.showWinner('b');
				view.showPlayer('r');
				controller.responseMoveChess(board, view);
				Thread.sleep(2000);
				view.addPieceEaten();

			}
			if (startP) {
				/* User in. */
				while (board.player == 'r')
					Thread.sleep(1000);

				if (controller.hasWin(board) != 'x') view.showWinner('r');
				view.showPlayer('b');
				/* AI in. */
				switch (algorithms) {
				case "Alpha-Beta":
					controller.responseMoveChess(board, view);
					break;
				case "Minimax":
					controller.moveChess2(board, view, true);
					break;
				default:
					controller.responseMoveChess(board, view);
					break;
				}
			}
		}
		view.showWinner('b');
	}
}
