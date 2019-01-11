package model;

import java.util.Arrays;

public class Node implements Cloneable{
	public String key;
	public char color;
	public char character;
	public char index;
	public int[]position = new int[2];
	public Node(String key,int[] position) {
		super();
		this.key = key;
		this.color = key.charAt(0);
		this.character = key.charAt(1);
		this.index = key.charAt(2);
		this.position = position;
	}
	@Override
	public String toString() {
		return "Node [x=" + position[0]+",y="+position[1] + "]";
	}
	public String toStringName(){
		return "key: "+key+" : "+"[x=" + position[0]+",y="+position[1]  ;
	}
}
