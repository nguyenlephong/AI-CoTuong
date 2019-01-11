package model;

import java.util.Map;

import model.Node;

public class Board {
	public final int BOARD_WIDTH = 9;
	public final int BOARD_HEIGHT = 10;
	public Map<String, Node> mapNode;
	public Map<String, Node> mapPositionNode;
	public char player = 'r';
	public Node[][] cells = new Node[BOARD_HEIGHT][BOARD_WIDTH];

	public boolean isInside(int[] pos) {
		return insides(pos[0], pos[1]);
	}

	public boolean insides(int i, int j) {
		return !(i < 0 || j < 0 || i >= BOARD_HEIGHT || j >= BOARD_WIDTH);
	}

	public boolean isEmpty(int[] pos) {
		return emptys(pos[0], pos[1]);
	}

	public boolean emptys(int i, int j) {
		return insides(i, j) && cells[i][j] == null;
	}

	 public boolean update(Node piece) {
	        int[] pos = piece.position;
	        cells[pos[0]][pos[1]] = piece;
	        return true;
	    }
	/*
	 * key ở đây là tên quân cờ trong mapNode là map ban đầu
	 * còn newPos là vị trí mới vừa thay đổi trên bàn cờ, được lưu vào 
	 * mãng cells
	 */
	public Node updateNode(String key, int[] newPos) {
		Node curr = mapNode.get(key);
		Node newNode = getNode(newPos);
		/* Nếu có con cờ ở đây rồi thì xử lý nó thôi, (nói vui là ăn nó đấy)  */
		if (newNode != null) {
			mapNode.remove(newNode.key);
			
		}
		/* Xóa con cờ ở vị trí ban đầu đi, gán lại con cờ mới vào vị trí đó */
		int[] orig = curr.position;
		cells[orig[0]][orig[1]] = null;
		cells[newPos[0]][newPos[1]] = curr;
		curr.position = newPos;
		player = (player == 'r') ? 'b' : 'r';
		
		return newNode;
	}

	public boolean backNode(String key) {
		int[] orignal = mapNode.get(key).position;
		cells[orignal[0]][orignal[1]] = mapNode.get(key);
		return true;
	}

	public Node getNode(int[] pos) {
		return getNodes(pos[0], pos[1]);
	}

	public Node getNodes(int i, int j) {
		return cells[i][j];
	}


	
	
}
