package com.mycompany.a3;

import java.util.Observable;
import java.util.Observer;
import com.codename1.charts.models.Point;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;

public class MapView extends Container implements Observer{
	private Game g;
	private GameWorld gw;
	
	public MapView(Game game) {
		g = game;
	}
	
	public void update (Observable o, Object arg) {
		gw = (GameWorld)o;
		this.repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Point pCmpRelPrnt = new Point(this.getX(), this.getY());
		IIterator<GameObject> objs = gw.getObjs().getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof IDrawable) {
				((IDrawable) obj).draw(g, pCmpRelPrnt);
			}
		}	
	}
	
	public void pointerPressed(int x, int y) {
		float pX = x - getParent().getAbsoluteX();
		float pY = y - getParent().getAbsoluteY();
		Point pPtrRelPrnt = new Point(pX, pY);
		Point pCmpRelPrnt = new Point(getX(), getY());
		IIterator<GameObject> objs = gw.getObjs().getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof ISelectable) {
				ISelectable selectable = (ISelectable)obj;
				if (gw.isPositioning() && selectable.isSelected()) {
//					pick a new position
					((GameObject) selectable).setLocation(pX - pCmpRelPrnt.getX(), pY - pCmpRelPrnt.getY());
					selectable.setSelected(false);
					gw.togglePosition();
					g.togglePosition();
				} else {
//					else select an object
					if (selectable.contains(pPtrRelPrnt, pCmpRelPrnt)) {
						selectable.setSelected(true);
					} else {
						selectable.setSelected(false);
					}
				}
			}
		}
		this.repaint();
	}
}
