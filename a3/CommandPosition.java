package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandPosition extends Command {
	Game g;
	GameWorld gw;

	public CommandPosition(String command, Game game, GameWorld world) {
		super(command);
		g = game;
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.togglePosition();
		g.togglePosition();
	}
}
