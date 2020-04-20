package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandSteerRight extends Command {
	private GameWorld gw;
	public CommandSteerRight(String command, GameWorld world) {
		super(command);
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.steerRight();
	}
}
