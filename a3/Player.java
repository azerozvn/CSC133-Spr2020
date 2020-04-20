package com.mycompany.a3;

import com.codename1.charts.models.Point;
import com.codename1.ui.Graphics;

public class Player extends Cyborg{
	private static Player player;
	
	private Player(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
		super.setMaxDamage(100);
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

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt) {
		g.setColor(this.getColor());
		int x = (int)(this.getLocation().getX() + pCmpRelPrnt.getX());
		int y = (int)(this.getLocation().getY() + pCmpRelPrnt.getY());
		int radius = this.getSize() / 2;
		g.fillRect(x - radius, y - radius, radius * 2, radius * 2);
	}
}
