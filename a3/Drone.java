package com.mycompany.a3;

import java.util.Random;

import com.codename1.charts.models.Point;
import com.codename1.ui.Graphics;

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

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt) {
		g.setColor(this.getColor());
		int x = (int)(this.getLocation().getX() + pCmpRelPrnt.getX());
		int y = (int)(this.getLocation().getY() + pCmpRelPrnt.getY());
		int radius = this.getSize() / 2;
		int[] xPoints = {x - radius, x, x + radius};
		int[] yPoints = {y - radius, y + radius, y - radius};
		int nPoints = 3;
		g.drawPolygon(xPoints, yPoints, nPoints);
	}
}
