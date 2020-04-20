package com.mycompany.a3;

import java.util.Random;
import java.util.Observable;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;

public class GameWorld extends Observable{
	private GameObjectCollection objects = new GameObjectCollection();
	private int lives = 3;								//number of lives
	private int time = 0;								//time elapsed
	private int second = 0;								//store time sec
	private float width;				
	private float height;
	private Player player;								//quick ref player
	private boolean sound = true;
	private boolean positioning = false;
	private Sound crashSound, interactSound, deadSound;
	
	public GameObjectCollection getObjs() {
		return this.objects;
	}
	
	public void init(float w, float h) {
		Random randomizer = new Random();
//		Generate text
		System.out.println("GW Initializing!");
//		Set up gw size
		this.width = w;
		this.height = h;
		
//		Generate 4~9 bases
		int count = randomizer.nextInt(6)+4;
		for (int i = 0; i < count; i++) {
			float x = this.randomDisplacement(this.width);
			float y = this.randomDisplacement(this.height);
			int size = 20;
			int rgb = ColorUtil.rgb(51,153,255);				//blue
			objects.add(new Base(x, y, size, rgb, i+1));
		}
//		Generate 2~4 energy stations
		count = randomizer.nextInt(3)+2;
		for (int i = 0; i < count; i++) {
			float x = this.randomDisplacement(this.width);
			float y = this.randomDisplacement(this.height);
			int size = randomizer.nextInt(20/5)*5+20; 		//size = 20~40
			int rgb = ColorUtil.rgb(0,204,153);				//green
			objects.add(new EnergyStation(x, y, size, rgb));
		}
//		Generate 2~4 drones
		count = randomizer.nextInt(3)+2;
		for (int i = 0; i < count; i++) {
			float x = this.randomDisplacement(this.width);
			float y = this.randomDisplacement(this.height);
			int size = randomizer.nextInt(20/5)*5+20; 		//size = 20~40
			int heading = randomizer.nextInt(355/5)*5; 		//heading = 0~355
			int speed = randomizer.nextInt(15/5)*5+10; 		//speed = 10~20
			int rgb = ColorUtil.rgb(255,0,255);	//purple
			objects.add(new Drone(x, y, size, rgb, heading, speed));
		}
//		Generate cyborgs
		{
//			Generate 3 NPCs
			Base firstBase = objects.getBaseBySeq(1); 
			int size = 40;
			int rgb = ColorUtil.rgb(255,0,0);	//red
			for (int i = 0; i < 3; i++) {
				//npc are created to be at least 50-150px away from first base
				float x = this.randomDonutDisplacement(firstBase.getLocation().getX(), this.width, 150, (int)(this.width * 0.2));
				float y = this.randomDonutDisplacement(firstBase.getLocation().getY(), this.height, 150, (int)(this.height * 0.2));
				int random = randomizer.nextInt(2);
				NPC npc = new NPC(x, y, size, rgb);
				if (random == 0) npc.setStrategy(new AttackStrategy(npc)); 
				else npc.setStrategy(new ObjectiveStrategy(npc, objects.getBaseBySeq(2)));
				objects.add(npc);
			}
//			Generate 1 player
			for (int i = 0; i < 1; i++) {
				float x = this.randomDonutDisplacement(firstBase.getLocation().getX(), this.width, 50, 100);
				float y = this.randomDonutDisplacement(firstBase.getLocation().getY(), this.height, 50, 100);
				objects.add(Player.getPlayer(x, y, size, rgb));
				this.player = Player.getPlayer();
			}
		}
		this.showMap();
		this.updateChanges();
		this.createSounds();
	}
	
	public void createSounds() {
		this.crashSound = new Sound("crash.wav");
		this.interactSound = new Sound("interact.wav");
		this.deadSound = new Sound("dead.wav");
	}
	
//	helper: generate a random float in d.d form for location usage
	public float randomDisplacement(float bound) {
		Random randomizer = new Random();
		return (float) (Math.round((randomizer.nextFloat() + randomizer.nextInt((int)bound))*10.0)/10.0);
	}
	
//	helper: random donut displacement around a specific loc
	public float randomDonutDisplacement(float loc, float bound, int inner, int outer) {
		Random randomizer = new Random();
		do {
			int displacement = randomizer.nextInt(outer - inner) + inner;
			if (loc + displacement < bound && loc - displacement > 0) {
				int coin = randomizer.nextInt(2);
				return (coin == 0)? (loc - displacement) : (loc + displacement);
			}
			if (loc + displacement < bound) return (loc + displacement);
			if (loc - displacement > 0) return (loc - displacement);
		}
		while (true);
	}
	
//	increase current speed of cyborg by 5
	public void accelerate() {
		if (this.player.getEnergyLevel() == 0) {
			System.out.println("Out of energy. You cannot move!");
		} else {
			int scaledMaxSpeed = this.player.getScaledMaxSpeed();
			if (scaledMaxSpeed == 0) {
				System.out.println("Too much damage. You cannot move!");
			} else {
				int currentSpeed = this.player.getSpeed();
				if (currentSpeed + 5 > scaledMaxSpeed) {
					this.player.setSpeed(scaledMaxSpeed);
					System.out.println("You are at maximum speed of " + scaledMaxSpeed);
				} else {
					this.player.setSpeed(currentSpeed + 5);
					System.out.println("Speed accelerates by 5. Your current speed is " + this.player.getSpeed());
				}
			}
		}
	}
	
//	decrease current speed of cyborg by 5
	public void brake() {
		if (this.player.getEnergyLevel() == 0) {
			System.out.println("Out of energy. You cannot move!");
		} else {
			int currentSpeed = this.player.getSpeed();
			if (currentSpeed - 5 < 0) {
				this.player.setSpeed(0);
				System.out.println("You are at minimum speed of 0");
			} else {
				this.player.setSpeed(currentSpeed - 5); 
				System.out.println("Speed brakes by 5. Your current speed is " + this.player.getSpeed());
			}
		}
	}
	
//	decrease steeringDirection of cyborg by 5
	public void steerLeft() {
		int currentSteer = this.player.getSteerDirection();
		if (currentSteer > -40) {
			this.player.setSteerDirection(currentSteer - 5);
			System.out.println("Steer left by 5. Your steering direction is " + this.player.getSteerDirection());
		} else {
			System.out.println("You cannot steer left anymore!");
		}
	}
	
//	increase steeringDirection of cyborg by 5
	public void steerRight() {
		int currentSteer = this.player.getSteerDirection();
		if (currentSteer < 40) {
			this.player.setSteerDirection(currentSteer + 5);
			System.out.println("Steer right by 5. Your steering direction is " + this.player.getSteerDirection());
		} else {
			System.out.println("You cannot steer right anymore!");
		}
	}
	
//	change strategies for all npc
	public void changeStrategies() {
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof NPC) {
				NPC npc = (NPC) obj;
//				pick a new strategy
				if (npc.getStrategy() instanceof AttackStrategy) {
					Base nextBase = objects.getBaseBySeq(npc.getBaseReached() + 1);
					npc.setStrategy(new ObjectiveStrategy(npc, nextBase));
				} else {
					npc.setStrategy(new AttackStrategy(npc));
				}
			}
		}
	}
	
//	show the gw map
	public void showMap() {
		System.out.println("*************************GAME MAP*************************");
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			System.out.println(obj.toString());
		}
		System.out.println("**********************************************************");
	}
	
//	tick the time by TICK_INTERVAL and let the game order happens
	public void tick(int interval) {
//		game status check
		if (this.player.getDamageLevel() == this.player.getMaxDamage()) {
			this.playerLoseLife();
			return;	//return immediately
		} else if (this.player.getDamageLevel() > this.player.getMaxDamage()){
			//do nothing, negate frame that overkill player
			return;	//return immediately
		}
//		player part
		if (this.player.getEnergyLevel() != 0 && this.player.getSpeed() != 0 && this.player.getDamageLevel() < this.player.getMaxDamage()) {
			this.player.steer();
			this.player.move(interval);
			if (this.player.getLocation().getX() > this.width || this.player.getLocation().getY() > this.height || this.player.getLocation().getX() < 0 || this.player.getLocation().getY() < 0) {
				this.player.bounce(this.width, this.height);
			}
			this.player.consumeEnergy();
			if (this.player.getEnergyLevel() == 0) {
				this.playerLoseLife();
				return; //return immediately
			}
		}
//		AI part
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
//			NPC
			if (obj instanceof NPC) {
				((NPC)obj).invokeStrategy();
				((NPC)obj).move(interval);
				if (obj.getLocation().getX() > this.width || obj.getLocation().getY() > this.height || obj.getLocation().getX() < 0 || obj.getLocation().getY() < 0) {
					((Movable)obj).bounce(this.width, this.height);
				}
				((NPC)obj).consumeEnergy();
				if (((NPC)obj).getEnergyLevel() <= 0) {
					((NPC)obj).restoreEnergy(40);
				}
			}
//			Drone
			if (obj instanceof Drone) {
				((Drone)obj).flick();
				((Drone)obj).move(interval);
				if (obj.getLocation().getX() > this.width || obj.getLocation().getY() > this.height || obj.getLocation().getX() < 0 || obj.getLocation().getY() < 0) {
					((Movable)obj).bounce(this.width, this.height);
				}
			}
		}
		this.second += interval;
		if (this.second % 1000 == 0) {
			this.second = 0;
			this.time++;
		}
		this.detectCollision();
		this.updateChanges();
	}

	private void detectCollision() {
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof Movable) {
				if (obj instanceof ICollider) {
					IIterator<GameObject> otherObjs = this.objects.getIterator();
					while (otherObjs.hasNext()) {
						GameObject otherObj = otherObjs.getNext();
						if (otherObj instanceof ICollider && !obj.equals(otherObj)) {
							if (obj.collidesWith(otherObj)) {
//								handleCollision at unit level
								obj.handleCollision(otherObj);
//								handleCollision at super level
//								a cyborg collide with an energy station
								if (obj instanceof Cyborg && otherObj instanceof EnergyStation) {
									EnergyStation station = (EnergyStation)otherObj;
									if (station.getCapacity() != 0) {
										station.exhaust();
										station.fade(0.5);
										Random randomizer = new Random();
										float x = this.randomDisplacement(this.width);
										float y = this.randomDisplacement(this.height);
										int size = randomizer.nextInt(20/5)*5+20; 		//size = 20~40
										int rgb = ColorUtil.rgb(0,204,153);				//green
										this.getObjs().add(new EnergyStation(x, y, size, rgb));
									}
									if (this.getSound()) this.interactSound.play(); 
								}
//								a cyborg collide with a base
								if (obj instanceof Cyborg && otherObj instanceof Base) {
									if (obj instanceof Player) {
										//check if player reached last base
										if (this.player.getBaseReached() == this.objects.getBaseCount()) {
											this.playerWin();
										}
									} else {
										//otherwise if npc reached last base
										if (((NPC)obj).getBaseReached() == this.objects.getBaseCount()) {
											String msg = "Oops! A NPC has reached the last base!\n\n";
											this.playerLose(msg);
										}
									}
									
									if (obj instanceof NPC) {
										//update strategy to get to next base
										NPC npc = (NPC)obj;
										if (npc.getStrategy() instanceof ObjectiveStrategy) {
											Base nextBase = this.objects.getBaseBySeq(npc.getBaseReached() + 1);
											npc.setStrategy(new ObjectiveStrategy(npc, nextBase));
										}
									}
								}
//								a player collide with another cyborg or drone
								if (obj instanceof Player && otherObj instanceof Movable) {
									if (this.player.getDamageLevel() >= this.player.getMaxDamage()) {
										if (this.getSound()) this.deadSound.play();
										return;	//return immediately
									} else {
										if (this.getSound()) this.crashSound.play();
									}
								}
//								a cyborg collide with another cyborg or drone
								else if (obj instanceof Cyborg && otherObj instanceof Movable) {
									if (this.getSound()) this.crashSound.play(); 
								}
							}
						}
					}
				}
			}
		}
		//2nd loop to remove dead npc
		objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof NPC) {
				if (((NPC)obj).getDamageLevel() >= ((NPC)obj).getMaxDamage()) {
					objs.remove();
				}
			}
		}
	}

//	player lose a life 
	public void playerLoseLife() {
		this.lives--;
		Command cOk = new Command("Okay");
		Dialog.show("Whammm!", "You have lost 1 life. Lives left: " + this.lives, cOk);
		if (this.lives <= 0) {
			String msg = "Oops!\n You have lost your last life!\n\n";
			this.playerLose(msg);
		} else {
			System.out.println("The game is reset!");
			this.reset();
		}
	}
	
//	player lose the game
	public void playerLose(String msg) {
		Command cOk = new Command("Failed!");
		Dialog.show("Game Over", msg + "Total time: " +  this.time, cOk);
		this.exit();
	}
	
//	player win the game
	public void playerWin() {
		Command cOk = new Command("Hurray!");
		Dialog.show("Game Over", "Congratulations!\n You win!\n\n Total time: " + this.time, cOk);
		this.exit();
	}
	
//	reset the game
	public void reset() {
		this.objects.clear();	
		Player.destroy();	//nullify player static mem
		this.init(this.width, this.height);
	}
	
//	exit prompt
	public void exit() {
		boolean exitPrompt = Dialog.show("Quit","Would you like to exit the game?", "Yes", "No");
		if (exitPrompt) Display.getInstance().exitApplication();
	}
	
	public void togglePosition() {
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof ISelectable) {
				if (((ISelectable)obj).isSelected()) {
					this.positioning = true;
					return;
				}
			}
		}
		this.positioning = false;
	}
	
	public boolean isPositioning() {
		return this.positioning;
	}
	
//	For observable call
	public double getTime() {
		return this.time;
	}

	public int getLives() {
		return this.lives;
	}
	
	public int getBaseReached() {
		return this.player.getBaseReached();
	}
	
	public double getEnergyLevel() {
		return this.player.getEnergyLevel();
	}

	public int getDamageLevel() {
		return this.player.getDamageLevel();
	}
	
	public boolean getSound() {
		return this.sound;
	}
	
//	Command call
	public void toggleSound() {
		this.sound = !this.sound;
		this.setChanged();
		this.notifyObservers();
	}

	public void about() {
		Dialog.show("About",
					"Author: Long Nguyen \n"
					+ "CSC 133 Assignment 3\n", 
					"Okay", null);
	}

	public void help() {
		Dialog.show("Help",
				"List of Hotkeys \n"
				+ "A: accelerate \n"
				+ "B: brake \n"
				+ "L: steer left \n"
				+ "R: steer right \n",
				"Okay", null);
	}
	
//	Invoke changes for observer
//	Stewpid JAVA
	private void updateChanges() {
		this.setChanged();
		this.notifyObservers();
	}
}
