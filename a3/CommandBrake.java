package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandBrake extends Command {
	private GameWorld gw;
	public CommandBrake(String command, GameWorld world) {
		super(command);
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.brake();
	}
}
