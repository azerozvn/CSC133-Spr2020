package com.mycompany.a2;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;

public class CommandCollideWithBase extends Command {
	private GameWorld gw;
	public CommandCollideWithBase(String command, GameWorld world) {
		super(command);
		gw = world;
	}
	
	public void actionPerformed(ActionEvent e) {
		Command cOk = new Command("Okay");
		TextField myTF = new TextField();
		Dialog.show("Enter base number to collide:", myTF, cOk);
		gw.collideWithBase(Integer.parseInt(myTF.getText()));
	}
}
