package com.mycompany.a3;

public abstract class Cyborg extends Movable implements ISteerable{
	private int steeringDirection = 0;
	private int maximumSpeed = 40;
	private double energyLevel = 50;		//10 secs before energy run out
	private double energyConsumptionRate = 0.1;
	private int damageLevel = 0;
	private int lastBaseReached	= 1;
	private int maxDamage = 0;							//assume cyborg can take 4 hits before dead
	
	public Cyborg(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
		super.setHeading(0);
		super.setSpeed(0);
	}
	
	public int getMaxSpeed() {
		return this.maximumSpeed;
	}
	
	public int getSteerDirection() {
		return this.steeringDirection;
	}
	
	public double getEnergyLevel() {
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
	
	public void setMaxDamage(int damage) {
		this.maxDamage = damage;
	}
	
	public int getScaledMaxSpeed() {
		double speedScale = 1 - (double)this.getDamageLevel() / (double)this.maxDamage;
		return (int) ((double)this.getMaxSpeed() * speedScale);
	}
	
	public void setEnergyLevel(double energy) {
		this.energyLevel = energy;
	}
	
	public void consumeEnergy() {
		double energyLeft = this.energyLevel - this.energyConsumptionRate;
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
		super.adjustHeading();
	}
	
	public void takeDamage(int damage) {
		int currentDamage = this.getDamageLevel();
		int maxDamage = this.getMaxDamage();
		currentDamage += damage;
		this.setDamageLevel(currentDamage);
		this.fade((double)currentDamage / (double)maxDamage);
		if (currentDamage > 0 && currentDamage < maxDamage) {
//			rescale scale to cap
			int scaledMaxSpeed = this.getScaledMaxSpeed();
			if (this.getSpeed() > scaledMaxSpeed) {
				this.setSpeed(scaledMaxSpeed);
			}
		} else {
			this.setSpeed(0);
		}
	}
	
	private void collideWithBase(int baseNum) {
		if (this.getBaseReached() + 1 == baseNum) this.setBaseReached(baseNum);
		
	}
	
	private void collideWithEnergyStation(int capacity) {
		this.setEnergyLevel(this.getEnergyLevel() + capacity);
	}

	public void handleCollision(ICollider otherObject) {
		if (otherObject instanceof Base) {
			this.collideWithBase(((Base)otherObject).getSeqNum());
		}
		if (otherObject instanceof EnergyStation) {
			EnergyStation station = (EnergyStation)otherObject;
			if (station.getCapacity() != 0) {
				this.collideWithEnergyStation(station.getCapacity());
			}
		}
		if (otherObject instanceof Drone) {
			this.takeDamage(1);
		}
		if (otherObject instanceof Cyborg) {
			this.takeDamage(2);
		}
	}

	public String toString() {
		String parentDesc = super.toString();
		String desc = " maxSpeed=" + this.getMaxSpeed()
					+ " steeringDirection=" + this.getSteerDirection()
					+ " energyLevel=" + this.getEnergyLevel()
					+ " damageLevel=" + this.getDamageLevel()
					+ " lastBaseReached=" + this.getBaseReached();
		return parentDesc + desc;
	}
}
