package com.csc133.ass1;

public class EnergyStation extends Fixed{
	private int capacity;
	public EnergyStation(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
//		set sequence number
		this.capacity = (int)((double)size * 1.0);	//capacity is 1.0 the size
	}	
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public void exhaust() {
		this.capacity = 0;
	}
	
	public String toString() {
		String title = "EnergyStation: ";
		String parentDesc = super.toString();
		String desc = " capacity=" + this.capacity;
		return title + parentDesc + desc;
	}
}
