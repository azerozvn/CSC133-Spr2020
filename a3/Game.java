package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentSelector;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.util.UITimer;
import com.codename1.ui.Toolbar;

public class Game extends Form implements Runnable{
	public final int TICK_INTERVAL = 20;
	private GameWorld gw;
	private MapView mv;	
	private ScoreView sv;
	private Toolbar toolBar;
	private UITimer timer;
	private BGSound bgSound;
	private boolean paused = false;
	private boolean sound = true;
	
	private Button accelerateButton, brakeButton, steerLeftButton, steerRightButton, changeStrategiesButton, pauseButton, positionButton;
	private CheckBox soundBox;
	private CommandAccelerate accelerateCommand;
	private CommandBrake brakeCommand;
	private CommandSteerLeft steerLeftCommand;
	private CommandSteerRight steerRightCommand;
	private CommandChangeStrategies changeStrategiesCommand;
	private CommandAbout aboutCommand;
	private CommandExit exitCommand;
	private CommandHelp helpCommand;
	private CommandSound soundCommand;
	private CommandPause pauseCommand;
	private CommandPosition positionCommand;

	public Game() {
		gw = new GameWorld(); 	// create Observable GameWorld
		mv = new MapView(this); 	// create an Observer for the map
		sv = new ScoreView(); 	// create an Observer for the game/player-cyborg
		gw.addObserver(mv); 	// register the map observer
		gw.addObserver(sv); 	// register the score observer
		this.show();			// show pre-layout form
		this.createLayout();	// create layout
		this.show();			// show post-layout form
//		fetch mv size
		float w = (float) mv.getWidth();
		float h = (float) mv.getHeight();
		System.out.println("GW size: " + w + " " + h);
//		init world
		gw.init(w, h);
//		set timer
		timer = new UITimer(this);
		timer.schedule(TICK_INTERVAL, true, this);
//		set music
		bgSound = new BGSound("bg.wav");
		bgSound.play();
	}

	public void run() {
		gw.tick(TICK_INTERVAL);
		mv.repaint();
		if (gw.getLives() == 0) {
			bgSound.pause();
			timer.cancel();
		}
	}
	
	public void createLayout() {
//		define layout	
		setToolbar(toolBar = new Toolbar());		
		this.setLayout(new BorderLayout());
		this.setTitle("Sili-Challenge Game");
		
//		North section
		this.add(BorderLayout.NORTH, sv);
		ComponentSelector.$("Label", sv).getAllStyles().setPadding(Component.RIGHT, 5);
		ComponentSelector.$("Label", sv).setFgColor(ColorUtil.BLUE);

//		Center section
		this.add(BorderLayout.CENTER, mv);
		mv.getAllStyles().setBorder(Border.createLineBorder(4,ColorUtil.rgb(255,0,0)));
		
//		Generate buttons 
		accelerateButton = new Button("Accelerate");
		brakeButton = new Button("Brake");
		steerLeftButton = new Button("Left");
		steerRightButton = new Button("Right");
		changeStrategiesButton = new Button("Change Strategies");
		pauseButton = new Button("Pause");
		positionButton = new Button("Position");

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
		bottomSideBar.add(pauseButton);
		bottomSideBar.add(positionButton);

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
		accelerateCommand = new CommandAccelerate("Accelerate", gw);
		brakeCommand = new CommandBrake("Brake", gw);
		steerLeftCommand = new CommandSteerLeft("Left", gw);
		steerRightCommand = new CommandSteerRight("Right", gw);
		changeStrategiesCommand = new CommandChangeStrategies("Change Strategies", gw);
		soundCommand = new CommandSound("Toggle Sound", this, gw);
		aboutCommand = new CommandAbout("About", gw);
		helpCommand = new CommandHelp("Help", gw);
		exitCommand = new CommandExit("Exit", gw);
		pauseCommand = new CommandPause("Pause", this);
		positionCommand = new CommandPosition("Position", this, gw);
		
//		Assign command to GUI and keystroke
		accelerateButton.setCommand(accelerateCommand);
		brakeButton.setCommand(brakeCommand);
		steerLeftButton.setCommand(steerLeftCommand);
		steerRightButton.setCommand(steerRightCommand);
		changeStrategiesButton.setCommand(changeStrategiesCommand);
		pauseButton.setCommand(pauseCommand);
		positionButton.setCommand(positionCommand);
		addKeyListener('a', accelerateCommand);
		addKeyListener('b', brakeCommand);
		addKeyListener('l', steerLeftCommand);
		addKeyListener('r', steerRightCommand);

//		Sidebar
		toolBar.addCommandToSideMenu(accelerateCommand);
		toolBar.addCommandToSideMenu(aboutCommand);
		toolBar.addCommandToSideMenu(exitCommand);
		toolBar.addCommandToRightBar(helpCommand);
		soundBox = new CheckBox("Toggle Sound");
		toolBar.addComponentToSideMenu(soundBox);
		soundBox.getAllStyles().setBgTransparency(255);
		soundBox.getAllStyles().setBgColor(ColorUtil.LTGRAY);
		soundBox.getAllStyles().setFgColor(ColorUtil.WHITE);
		soundBox.setCommand(soundCommand);
		
//		Disable position
		positionButton.setEnabled(false);
	}

	public void toggleSound() {
		this.sound = !this.sound;
		if (this.sound) bgSound.play(); else bgSound.pause();
	}

	public void togglePause() {
		this.paused = !this.paused;
		positionButton.setEnabled(!positionButton.isEnabled());
		soundBox.setEnabled(!soundBox.isEnabled());
		accelerateButton.setEnabled(!accelerateButton.isEnabled());
		brakeButton.setEnabled(!brakeButton.isEnabled());
		steerLeftButton.setEnabled(!steerLeftButton.isEnabled());
		steerRightButton.setEnabled(!steerRightButton.isEnabled());
		changeStrategiesButton.setEnabled(!changeStrategiesButton.isEnabled());
		if (paused) {
//			freeze gameplay
			timer.cancel();
			if (this.sound) this.toggleSound();
			if (gw.getSound()) gw.toggleSound();
			pauseButton.setText("Play");
			removeKeyListener('a', accelerateCommand);
			removeKeyListener('b', brakeCommand);
			removeKeyListener('l', steerLeftCommand);
			removeKeyListener('r', steerRightCommand);
		}
		else {
//			resume gameplay
			timer.schedule(TICK_INTERVAL, true, this);
			if (!this.sound) this.toggleSound();
			if (!gw.getSound()) gw.toggleSound();
			pauseButton.setText("Pause");
			addKeyListener('a', accelerateCommand);
			addKeyListener('b', brakeCommand);
			addKeyListener('l', steerLeftCommand);
			addKeyListener('r', steerRightCommand);
		}
	}

	public void togglePosition() {
		if (gw.isPositioning()) {
			positionButton.getUnselectedStyle().setBgColor(ColorUtil.WHITE);
			positionButton.getUnselectedStyle().setFgColor(ColorUtil.BLUE);
		} else {
			positionButton.getUnselectedStyle().setFgColor(ColorUtil.WHITE);
			positionButton.getUnselectedStyle().setBgColor(ColorUtil.BLUE);
		}
		this.show();
	}
}
