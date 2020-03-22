package com.mycompany.a2;

public class Base extends Fixed {
	private int sequenceNumber;
	public Base(float x, float y, int size, int rgb, int seq) {
		super(x, y, size, rgb);
//		set sequence number
		this.sequenceNumber = seq;
	}	
	
	public void fade(double value) {
		//Base can't change color
	}
	
	public int getSeqNum() {
		return this.sequenceNumber;
	}
	
	public String toString() {
		String title = "Base: ";
		String parentDesc = super.toString();
		String desc = " seqNum=" + this.sequenceNumber;
		return title + parentDesc + desc;
	}
}
