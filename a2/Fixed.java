package com.mycompany.a2;

public abstract class Fixed extends GameObject{
	public Fixed(float x, float y, int size, int rgb) {
		super(x, y, size, rgb);
	}
	
	public void move(float x, float y) {
		//well, fixed objects cant be moved
	}
}
