package com.mycompany.a3;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;

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

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt) {
		g.setColor(this.getColor());
		int x = (int)(this.getLocation().getX() + pCmpRelPrnt.getX());
		int y = (int)(this.getLocation().getY() + pCmpRelPrnt.getY());
		int size = this.getSize();
		if (isSelected()) {
			g.drawArc(x - size / 2, y - size / 2, size, size, 0, 360);
		} else {
			g.fillArc(x - size / 2, y - size / 2, size, size, 0, 360);
		}
		g.setColor(ColorUtil.BLACK);
		Font f = g.getFont();
		String toWrite = Integer.toString(this.getCapacity());
		int width = f.stringWidth(toWrite);
		int height = f.getHeight();
		g.drawString(toWrite, x - width / 2, y - height / 2);
	}
}
