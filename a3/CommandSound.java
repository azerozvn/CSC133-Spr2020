package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandSound extends Command{
	private Game g;
	private GameWorld gw;
	
	public CommandSound(String command, Game game, GameWorld world) {
		super(command);
		g = game;
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.toggleSound();
		g.toggleSound();
	}
}
