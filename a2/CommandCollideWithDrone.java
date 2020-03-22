package com.mycompany.a2;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandCollideWithDrone extends Command {
	private GameWorld gw;
	public CommandCollideWithDrone(String command, GameWorld world) {
		super(command);
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.collideWithDrone();
	}
}
