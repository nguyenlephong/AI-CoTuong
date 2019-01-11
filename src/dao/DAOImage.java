package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import model.Image;
import model.Node;

public class DAOImage {
	public static Map<String, Image> listImageOfNode = getListImageOfNode();
	public static Map<String, Image> listImageOfNodeLight = getListImageLightOfNode();
	public static ImageIcon exit = new ImageIcon("res/menu/thoat.png");
	public static ImageIcon newGames = new ImageIcon("res/menu/new.png");
	public static ImageIcon helps = new ImageIcon("res/menu/giupDo.png");
	public static ImageIcon abouts = new ImageIcon("res/menu/gioiThieu.png");
	public static ImageIcon pause = new ImageIcon("res/menu/tamdung.png");
	public static ImageIcon save = new ImageIcon("res/menu/luuGame.png");
	public static ImageIcon iconGames = new ImageIcon("res/menu/icon.png");
	public static ImageIcon levels = new ImageIcon("res/menu/level.png");
	public static ImageIcon kings = new ImageIcon("res/img/rv.png");
	public static ImageIcon nodesPath = new ImageIcon("res/light/go.png");
	public static ImageIcon killed = new ImageIcon("res/light/kill2.png");

	private static Map<String, Image> getListImageOfNode() {
		Map<String, Image> map = new HashMap<>();
		String arrNameNode[] = { "x", "m", "t", "s", "v", "p", "c" };
		for (int i = 0; i < arrNameNode.length; i++) {
			String namePlayer = "r" + arrNameNode[i];
			Image imgPlayer = new Image(namePlayer, "res/img/" + namePlayer + ".png");
			map.put(namePlayer, imgPlayer);
			String nameAI = "b" + arrNameNode[i];
			Image imgAI = new Image(nameAI, "res/img/" + nameAI + ".png");
			map.put(nameAI, imgAI);
		}
		return map;
	}

	private static Map<String, Image> getListImageLightOfNode() {
		Map<String, Image> map = new HashMap<>();
		String arrNameNode[] = { "x", "m", "t", "s", "v", "p", "c" };
		for (int i = 0; i < arrNameNode.length; i++) {
			String namePlayer = "r" + arrNameNode[i];
			Image imgPlayer = new Image(namePlayer, "res/light/" + namePlayer + ".png");
			map.put(namePlayer, imgPlayer);
			String nameAI = "b" + arrNameNode[i];
			Image imgAI = new Image(nameAI, "res/light/" + nameAI + ".png");
			map.put(nameAI, imgAI);
		}
		return map;
	}

	public static Image getImageLight(String key) {
		return listImageOfNodeLight.get(key.substring(0, key.length()-1));
	}
	public static Image getImage(String key){
		return listImageOfNode.get(key.substring(0, key.length()-1));
	}

	public List<Image> getListImgFromListEaten(List<Node> listOfNodeEaten) {
		List<Image> list = new ArrayList<>();
		for (Node n : listOfNodeEaten) {
			Image img = listImageOfNode.get(n.key.substring(0, n.key.length() - 1));
			list.add(img);
		}
		return list;
	}

}
