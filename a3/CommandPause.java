package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

public class CommandPause extends Command {
	Game g;

	public CommandPause(String command, Game game) {
		super(command);
		g = game;
	}
	
	public void actionPerformed(ActionEvent e) {
		g.togglePause();
	}
}
