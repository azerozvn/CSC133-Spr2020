package com.mycompany.a2;

import java.util.Random;

public class Drone extends Movable {
	public Drone(float x, float y, int size, int rgb, int heading, int speed) {
		super(x, y, size, rgb);
		super.setHeading(heading);
		super.setSpeed(speed);
	}
	
	public void fade(double value) {
		//Drone can't change color
	}
	
	public void flick() {
		int currentHeading = this.getHeading();
		Random randomizer = new Random();
		if (randomizer.nextInt(2) > 0) {
			currentHeading += 5;
		} else {
			currentHeading -= 5;
		}
		this.setHeading(currentHeading);
		super.adjustHeading();
	}
	
	public String toString() {
		String title = "Drone: ";
		return title + super.toString();
	}
}
