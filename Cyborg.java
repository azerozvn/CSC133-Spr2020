package com.csc133.ass1;

public class Cyborg extends Movable implements ISteerable{
	private int steeringDirection = 0;
	private int maximumSpeed = 40;
	private int energyLevel = 40;
	private int energyConsumptionRate = 5;
	private int damageLevel = 0;
	private int lastBaseReached	= 1;
	private int maxDamage = 4;							//assume cyborg can take 4 hits before dead
	
	public Cyborg(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
//		set heading = 0
		this.setHeading(0);
//		set speed = 5
		this.setSpeed(5);
	}
	
	public int getMaxSpeed() {
		return this.maximumSpeed;
	}
	
	public int getSteerDirection() {
		return this.steeringDirection;
	}
	
	public int getEnergyLevel() {
		return this.energyLevel;
	}
	
	public int getDamageLevel() {
		return this.damageLevel;
	}
	
	public int getBaseReached() {
		return this.lastBaseReached;
	}
	
	public int getMaxDamage() {
		return this.maxDamage;
	}
	
	public int getScaledMaxSpeed() {
		double speedScale = 1 - (double)this.getDamageLevel() / (double)this.maxDamage;
		return (int) ((double)this.getMaxSpeed() * speedScale);
	}
	
	public void setEnergyLevel(int energy) {
		this.energyLevel = energy;
	}
	
	public void consumeEnergy() {
		int energyLeft = this.energyLevel - this.energyConsumptionRate;
		if (energyLeft > 0) {
			this.setEnergyLevel(energyLeft);
		} else {
			this.setEnergyLevel(0);
		}
	}
	
	public void setDamageLevel(int damage) {
		this.damageLevel = damage;
	}
	
	public void setBaseReached(int base) { 
		this.lastBaseReached = base;
	}
	
	public void setSteerDirection(int value) {
		this.steeringDirection = value;
	}
	
	public void steer() {
		this.setHeading(this.getHeading() + this.getSteerDirection());
		this.adjustHeading();
	}
	
	public String toString() {
		String title = "Cyborg: ";
		String parentDesc = super.toString();
		String desc = " maxSpeed=" + this.getMaxSpeed()
					+ " steeringDirection=" + this.getSteerDirection()
					+ " energyLevel=" + this.getEnergyLevel()
					+ " damageLevel=" + this.getDamageLevel()
					+ " lastBaseReached=" + this.getBaseReached();
		return title + parentDesc + desc;
	}
}
