package com.mycompany.a3;

import com.codename1.charts.models.Point;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;

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

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt) {
		g.setColor(this.getColor());
		int x = (int)(this.getLocation().getX() + pCmpRelPrnt.getX());
		int y = (int)(this.getLocation().getY() + pCmpRelPrnt.getY());
		int radius = this.getSize() / 2;
		int[] xPoints = {x - radius, x, x + radius};
		int[] yPoints = {y - radius, y + radius, y - radius};
		int nPoints = 3;
		if (this.isSelected()) {
			g.drawPolygon(xPoints, yPoints, nPoints);
		} else {
			g.fillPolygon(xPoints, yPoints, nPoints);
		}
		g.setColor(ColorUtil.BLACK);
		Font f = g.getFont();
		String toWrite = Integer.toString(this.getSeqNum());
		int width = f.stringWidth(toWrite);
		int height = f.getHeight();
		g.drawString(toWrite, x - width / 2, y - height / 2);
	}
}
