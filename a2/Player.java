package com.mycompany.a2;

public class Player extends Cyborg{
	private static Player player;
	
	private Player(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
		super.setMaxDamage(4);
	}
	
	public static Player getPlayer(float x, float y, int size, int rgb) {
		if (player == null) player = new Player(x, y, size, rgb);
		return player;
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	public static void destroy() {
		player = null;
	}
	
	public void setEnergyConsumption(int rate) {
//		ability disabled
	}
	
	public void setMaxDamage(int damage) {
//		ability disabled
	}
	
	public String toString() {
		String title = "Player: ";
		String parentDesc = super.toString();
		String desc = "";
		return title + parentDesc + desc;
	}
}
