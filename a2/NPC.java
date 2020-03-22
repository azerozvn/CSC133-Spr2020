package com.mycompany.a2;

public class NPC extends Cyborg {
	private IStrategy strategy;
	
	public NPC(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
		super.setMaxDamage(8);	//well-armed
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
}
