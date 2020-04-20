package com.mycompany.a3;

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
	
//	adjust heading to be in range 0~359
	public void adjustHeading() {
		if (this.heading < 0) {
			this.heading += 360;
		}
	}
	
	public void move(double interval) {
		int currentHeading = this.getHeading();
		double alpha = Math.toRadians(90 - currentHeading);
		float deltaX = (float) (Math.cos(alpha) * this.getSpeed() * (interval/1000));
		float deltaY = (float) (Math.sin(alpha) * this.getSpeed() * (interval/1000));
		super.move(deltaX, deltaY);
	}
	
	public void bounce(float width, float height) {
		float x = this.getLocation().getX();
		float y = this.getLocation().getY();
		float deltaX = 0, deltaY = 0;
		if (x > width) deltaX = x - width;
		if (y > height) deltaY = y - height;
		if (x < 0) deltaX = x;
		if (y < 0) deltaY = y;
		int currentHeading = this.getHeading();
		if (currentHeading < 180) {
			currentHeading += 180;
		} else currentHeading -= 180;
		this.setHeading(currentHeading);
		super.move(deltaX, deltaY);
	}
	
	//only movable object can handle collision
	public void handleCollision(ICollider otherObject) {
		//System.out.println(this.getClass() + " collided with " + otherObject.getClass());
	}
	
	public String toString() {
		String parentDesc = super.toString();
		String desc = " heading=" + this.getHeading()
					+ " speed=" + this.getSpeed();
		return parentDesc + desc;
	}
}
