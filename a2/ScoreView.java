package com.mycompany.a2;

import java.util.Observable;
import java.util.Observer;

import com.codename1.ui.Container;
import com.codename1.ui.Label;

public class ScoreView extends Container implements Observer {
	private Label	time = new Label("Time: 0"), 
					lives = new Label("Lives Left: 3"),
					lastBaseReached = new Label("Last Base Reached: 1"),
					energyLevel = new Label("Energy Level: 0"),
					damageLevel = new Label("Damage Level: 0"),
					sound = new Label("Sound: OFF");
	
	public ScoreView() {
//		Add elements
		this.add(time);
		this.add(lives);
		this.add(lastBaseReached);
		this.add(energyLevel);
		this.add(damageLevel);
		this.add(sound);
	}
	
	public void update (Observable o, Object arg) {
		time.setText("Time: " + ((GameWorld) o).getTime());
		lives.setText("Lives Left: " + ((GameWorld)o).getLives());
		lastBaseReached.setText("Last Base Reached: " + ((GameWorld)o).getBaseReached());
		energyLevel.setText("Energy Level: " + ((GameWorld)o).getEnergyLevel());
		damageLevel.setText("Damage Level: " + ((GameWorld)o).getDamageLevel());
		sound.setText("Sound: " + (((GameWorld)o).getSound()? "ON" : "OFF")) ;
	}
}
