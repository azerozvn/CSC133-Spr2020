package com.mycompany.a2;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandSteerLeft extends Command {
	private GameWorld gw;
	public CommandSteerLeft(String command, GameWorld world) {
		super(command);
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.steerLeft();
	}
}
