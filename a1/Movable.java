package com.csc133.ass1;

public abstract class Movable extends GameObject {
	private int heading;
	private int speed;
	
	public Movable(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public int getHeading() {
		return this.heading;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setHeading(int heading) {
		this.heading = heading;
	}
	
//	adjust the heading to be 0~359 only
	public void adjustHeading() {
		if (this.heading < 0) {
			this.heading += 360;
		}
	}
	
	public void move() {
		int currentHeading = this.getHeading();
		double alpha = Math.toRadians(90 - currentHeading);
		float deltaX = Math.round((float) Math.cos(alpha) * (float) this.getSpeed()*1000)/1000;
		float deltaY = Math.round((float) Math.sin(alpha) * (float) this.getSpeed()*1000)/1000;
		super.move(deltaX, deltaY);
	}
	
	public void bounce(float upperBound) {
		float x = this.getLocation().getX();
		float y = this.getLocation().getY();
		float deltaX = 0, deltaY = 0;
		if (x > upperBound) deltaX = -(x - upperBound);
		if (y > upperBound) deltaY = -(y - upperBound);
		if (x < 0) deltaX = -x;
		if (y < 0) deltaY = -y;
		int currentHeading = this.getHeading();
		if (currentHeading < 180) {
			currentHeading += 180;
		} else currentHeading -= 180;
		this.setHeading(currentHeading);
		super.move(deltaX, deltaY);
	}
	
	public String toString() {
		String parentDesc = super.toString();
		String desc = " heading=" + this.getHeading()
		+ " speed=" + this.getSpeed();
		return parentDesc + desc;
	}
}
