package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import application.Play;
import view.View;

public class MenuController implements MenuListener, ActionListener {

	public static JMenu jmnGames = View.jmnGames;
	public static JMenu jmnLevel = View.jmnLevel;

	public static JMenuItem jmnMiniMax = View.jmnMiniMax;
	public static JMenuItem jmnAlphaBeta = View.jmnAlphaBeta;

	public static JMenuItem jmnitPC = View.jmnitPC;
	public static JMenuItem jmnitCC = View.jmnitCC;
	public static JMenuItem jmnitExit = View.jmnitExit;

	@Override
	public void actionPerformed(ActionEvent e) {
		String menuText = e.getActionCommand();
		switch (menuText) {
		case "MiniMax":
			Play.startP=true;
			Play.algorithms="Minimax";
			Play.startC= false;
			View.jmnitCC.setEnabled(false);
			break;
		case "Alpha-Beta":
			Play.startP=true;
			Play.algorithms="Alpha-Beta";
			Play.startC= false;
			View.jmnitCC.setEnabled(false);
			break;
		case "Exit":
			System.exit(0);
			break;
		case "People-Computer":
			Play.startP=true;
			Play.startC= false;
			View.jmnitCC.setEnabled(false);
			break;
		case "Computer-Computer":
			Play.startC =true;
			Play.startP= false;
			View.jmnitPC.setEnabled(false);
			break;

		default:
			break;
		}
	}

	@Override
	public void menuCanceled(MenuEvent e) {

	}

	@Override
	public void menuDeselected(MenuEvent e) {

	}

	@Override
	public void menuSelected(MenuEvent e) {
		// System.out.println(e);
	}

}
