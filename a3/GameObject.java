package com.mycompany.a3;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;

public abstract class GameObject implements IDrawable, ICollider {
	private Point location = new Point();			
	private int size;						
	private int color; 							
	
	public GameObject(float x, float y, int size, int rgb) {
		this.setLocation(x, y);
		this.setSize(size);
		this.setColor(rgb);
	}

	public int getSize() {
		return this.size;
	}
	
	public Point getLocation() {
		return this.location;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setLocation(float x, float y) {
		this.location.setX(x);
		this.location.setY(y);
	}
	
	private void setSize(int size) {
		this.size = size;
	}
	
	private void setColor(int rgb) {
		this.color = rgb;
	}
	
//	move an object to new displacement
	public void move(float x, float y) {
		float newX = this.location.getX() - x;
		float newY = this.location.getY() - y;
		this.setLocation(newX, newY);
	}
	
//	fade color of an object
	public void fade(double value) {
		int r = (int) (255 - ColorUtil.red(this.color) * (1-value));
		int g = (int) (255 - ColorUtil.green(this.color) * (1-value));
		int b = (int) (255 - ColorUtil.blue(this.color) * (1-value));
		int rgb = ColorUtil.rgb(r,g,b);
		this.setColor(rgb);
	}
	
//	detect collision, used rect algo
	public boolean collidesWith(ICollider otherObject) {
		int thisSize = this.getSize() / 2;
		float thisX = this.getLocation().getX();
		float thisY = this.getLocation().getY();
		int thatSize = ((GameObject)otherObject).getSize() / 2;
		float thatX = ((GameObject)otherObject).getLocation().getX();
		float thatY = ((GameObject)otherObject).getLocation().getY();
		boolean nonOverlapLR = (thisX + thisSize < thatX - thatSize || thisX - thisSize > thatX + thatSize)? true : false;
		boolean nonOverlapTB = (thisY + thisSize < thatY - thatSize || thisY - thisSize > thatY + thatSize)? true : false;
		return (nonOverlapLR || nonOverlapTB)? false : true;
	}
	
	public void handleCollision(ICollider otherObject) {
		//keep it clean & simple
	}
	
	public String toString() {
		String desc = "loc=(" + this.getLocation().getX() + "," + this.getLocation().getY() + ") "
				+ "color=[" + ColorUtil.red(this.getColor()) + "," + ColorUtil.green(this.getColor()) + "," + ColorUtil.blue(this.getColor()) + "]"
				+ " size=" + this.getSize();
		return desc;
	}
}
