package com.csc133.ass1;

import com.codename1.ui.Form;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import java.lang.String;

public class Game extends Form{
	private GameWorld gw;
	public Game() {
		gw = new GameWorld();
		gw.init();
		this.play();
	}
	
	private void play() {
		Label myLabel = new Label("Enter a Command:");
		this.addComponent(myLabel);
		final TextField myTextField = new TextField();
		this.addComponent(myTextField);
		this.show();
		
		myTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				String sCommand=myTextField.getText().toString();
				myTextField.clear();
				if(sCommand.length() != 0)
					switch (sCommand.charAt(0)) {
						case 'a':
							gw.accelerate();
							break;
						case 'b':
							gw.brake();
							break;
						case 'l':
							gw.steerLeft();
							break;
						case 'r':
							gw.steerRight();
							break;
						case 'c':
							gw.collideWithCyborg();
							break;
						case 'e':
							gw.reachStation();
							break;
						case 'g':
							gw.collideWithDrone();
							break;
						case 't':
							gw.tick();
							break;
						case 'd':
							gw.display();
							break;
						case 'm':
							gw.showMap();
							break;
						case 'x':
							gw.exit();
							break;
						case 'y':
						case 'n':
							gw.exit(sCommand.charAt(0));
							break;
						case '1':
						case '2':							
						case '3':							
						case '4':							
						case '5':							
						case '6':							
						case '7':							
						case '8':					
						case '9':
							gw.reachBase(Integer.parseInt(sCommand.substring(0,1)));
							break;
						default:
							System.out.println("Error: undefined key input!");
							break;
					} 
				}
		}); //addActionListener
	}
}
