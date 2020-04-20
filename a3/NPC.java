package com.mycompany.a3;

import com.codename1.charts.models.Point;
import com.codename1.ui.Graphics;

public class NPC extends Cyborg {
	private IStrategy strategy;
	
	public NPC(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
		super.setMaxDamage(200);	//well-armed
	}
	
	public void restoreEnergy(int energy) {
		this.setEnergyLevel(energy);
	}
	
	public void setStrategy(IStrategy s) {
		strategy = s;
	}
	
	public IStrategy getStrategy() {
		return this.strategy;
	}
	
	public void invokeStrategy() {
		strategy.apply();
	}
	
	public String getStrategyType() {
		if (strategy instanceof AttackStrategy) return "Attack";
		if (strategy instanceof ObjectiveStrategy) return "Objective";
		return "No Strategy";
	}
	
	public String toString() {
		String title = "NPC: ";
		String parentDesc = super.toString();
		String desc = " strategy=" + this.getStrategyType();
		return title + parentDesc + desc;
	}

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt) {
		g.setColor(this.getColor());
		int x = (int)(this.getLocation().getX() + pCmpRelPrnt.getX());
		int y = (int)(this.getLocation().getY() + pCmpRelPrnt.getY());
		int radius = this.getSize() / 2;
		g.drawRect(x - radius, y - radius, radius * 2, radius * 2);
	}

}
