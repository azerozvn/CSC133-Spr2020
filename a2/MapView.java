package com.mycompany.a2;

import java.util.Observable;
import java.util.Observer;
import com.codename1.ui.Container;

public class MapView extends Container implements Observer{
	public void update (Observable o, Object arg) {
		((GameWorld) o).showMap();
	}
}
