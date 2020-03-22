package com.mycompany.a2;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentSelector;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.Toolbar;

public class Game extends Form{
	private GameWorld gw;
	private MapView mv;	
	private ScoreView sv;
	private Toolbar toolBar;
	
	public Game() {
		gw = new GameWorld(); 	// create Observable GameWorld
		mv = new MapView(); 	// create an Observer for the map
		sv = new ScoreView(); 	// create an Observer for the game/player-cyborg
		// state data
		gw.addObserver(mv); // register the map observer
		gw.addObserver(sv); // register the score observer
		this.show();
		
//		define layout	
		setToolbar(toolBar = new Toolbar());		
		this.setLayout(new BorderLayout());
		this.setTitle("Sili-Challenge Game");
		
//		North section
		Container scoreContainer = new Container(new FlowLayout(Component.CENTER));
		this.add(BorderLayout.NORTH, scoreContainer);
		scoreContainer.add(sv);
		ComponentSelector.$("Label", scoreContainer).getAllStyles().setPadding(Component.RIGHT, 5);
		ComponentSelector.$("Label", scoreContainer).setFgColor(ColorUtil.BLUE);

//		Center section
		Container mapContainer = new Container();
		this.add(BorderLayout.CENTER, mapContainer);
		mapContainer.add(mv);
		mapContainer.getAllStyles().setBorder(Border.createLineBorder(4,ColorUtil.rgb(255,0,0)));
		
//		Generate buttons 
		Button accelerateButton = new Button("Accelerate");
		Button brakeButton = new Button("Brake");
		Button steerLeftButton = new Button("Left");
		Button steerRightButton = new Button("Right");
		Button changeStrategiesButton = new Button("Change Strategies");
		Button collideWithNPCButton = new Button("Collide with NPC");
		Button collideWithBaseButton = new Button("Collide with Base");
		Button collideWithEnergyStationButton = new Button("Collide with Energy Station");
		Button collideWithDroneButton = new Button("Collide with Drone");
		Button tickButton = new Button("Tick");
		
//		West section
		Container leftSideBar = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		this.add(BorderLayout.WEST, leftSideBar);
		leftSideBar.add(accelerateButton);
		leftSideBar.add(steerLeftButton);
		leftSideBar.add(changeStrategiesButton);
		leftSideBar.getAllStyles().setPadding(Component.TOP, 100);
		
//		East section
		Container rightSideBar = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		this.add(BorderLayout.EAST, rightSideBar);
		rightSideBar.add(brakeButton);
		rightSideBar.add(steerRightButton);
		rightSideBar.getAllStyles().setPadding(Component.TOP, 100);
		
//		South section
		Container bottomSideBar = new Container(new FlowLayout(Component.CENTER));
		this.add(BorderLayout.SOUTH, bottomSideBar);
		bottomSideBar.add(collideWithNPCButton);
		bottomSideBar.add(collideWithBaseButton);
		bottomSideBar.add(collideWithEnergyStationButton);
		bottomSideBar.add(collideWithDroneButton);
		bottomSideBar.add(tickButton);
//		Styling all buttons
		ComponentSelector.$("Button").setBgTransparency(255);
		ComponentSelector.$("Button").setBgColor(ColorUtil.BLUE);
		ComponentSelector.$("Button").setFgColor(ColorUtil.WHITE);
		ComponentSelector.$("Button").getAllStyles().setPadding(4, 4, 2, 2);
		ComponentSelector.$("Button").getAllStyles().setAlignment(CENTER);
		ComponentSelector.$("Button", leftSideBar).getAllStyles().setMargin(Component.BOTTOM, 10);
		ComponentSelector.$("Button", rightSideBar).getAllStyles().setMargin(Component.BOTTOM, 10);
		ComponentSelector.$("Button", bottomSideBar).getAllStyles().setMargin(Component.RIGHT, 5);

//		Create command objects
		Command accelerateCommand = new CommandAccelerate("Accelerate", gw);
		Command brakeCommand = new CommandBrake("Brake", gw);
		Command steerLeftCommand = new CommandSteerLeft("Left", gw);
		Command steerRightCommand = new CommandSteerRight("Right", gw);
		Command changeStrategiesCommand = new CommandChangeStrategies("Change Strategies", gw);
		Command collideWithNPCCommand = new CommandCollideWithNPC("Collide With NPC", gw);
		Command collideWithBaseCommand = new CommandCollideWithBase("Collide With Base", gw);
		Command collideWithEnergyStationCommand = new CommandCollideWithEnergyStation("Collide With Energy Station", gw);
		Command collideWithDroneCommand = new CommandCollideWithDrone("Collide With Drone", gw);
		Command tickCommand = new CommandTick("Tick", gw);
		Command soundCommand = new CommandSound("Toggle Sound", gw);
		Command aboutCommand = new CommandAbout("About", gw);
		Command helpCommand = new CommandHelp("Help", gw);
		Command exitCommand = new CommandExit("Exit", gw);
		
//		Assign command to GUI and keystroke
		accelerateButton.setCommand(accelerateCommand);
		addKeyListener('a', accelerateCommand);
		brakeButton.setCommand(brakeCommand);
		addKeyListener('b', brakeCommand);
		steerLeftButton.setCommand(steerLeftCommand);
		addKeyListener('l', steerLeftCommand);
		steerRightButton.setCommand(steerRightCommand);
		addKeyListener('r', steerRightCommand);
		changeStrategiesButton.setCommand(changeStrategiesCommand);
		collideWithNPCButton.setCommand(collideWithNPCCommand);
		collideWithBaseButton.setCommand(collideWithBaseCommand);
		collideWithEnergyStationButton.setCommand(collideWithEnergyStationCommand);
		addKeyListener('e', collideWithEnergyStationCommand);
		collideWithDroneButton.setCommand(collideWithDroneCommand);
		addKeyListener('g', collideWithDroneCommand);
		tickButton.setCommand(tickCommand);
		addKeyListener('t', tickCommand);
		
//		Sidebar
		toolBar.addCommandToSideMenu(accelerateCommand);
		CheckBox soundBox = new CheckBox("Toggle Sound");
		soundBox.getAllStyles().setBgTransparency(255);
		soundBox.getAllStyles().setBgColor(ColorUtil.LTGRAY);
		soundBox.getAllStyles().setFgColor(ColorUtil.WHITE);
		soundBox.setCommand(soundCommand);
		toolBar.addComponentToSideMenu(soundBox);
		toolBar.addCommandToSideMenu(aboutCommand);
		toolBar.addCommandToSideMenu(exitCommand);
		toolBar.addCommandToRightBar(helpCommand);
		
//		show final form
		this.show();
//		fetch mv size
		float w = (float) mapContainer.getWidth();
		float h = (float) mapContainer.getHeight();
		System.out.println("GW size: " + w + " " + h);
//		init world
		gw.init(w, h);
	}
}
