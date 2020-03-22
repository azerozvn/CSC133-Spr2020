package com.mycompany.a2;

import java.util.Random;
import java.util.Observable;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;

public class GameWorld extends Observable{
	private GameObjectCollection objects = new GameObjectCollection();
	private int lives = 3;								//number of lives
	private int time = 0;								//number of time elapsed
	private float width;				
	private float height;
	private Player player;								//quick ref player
	private boolean sound = true;
	
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
			int size = 10;
			int rgb = ColorUtil.rgb(0,0,255);				//blue
			objects.add(new Base(x, y, size, rgb, i+1));
		}
//		Generate 2~4 energy stations
		count = randomizer.nextInt(3)+2;
		for (int i = 0; i < count; i++) {
			float x = this.randomDisplacement(this.width);
			float y = this.randomDisplacement(this.height);
			int size = randomizer.nextInt(40/5)*5+10; 		//size = 10~50
			int rgb = ColorUtil.rgb(0,255,0);				//green
			objects.add(new EnergyStation(x, y, size, rgb));
		}
//		Generate 2~4 drones
		count = randomizer.nextInt(3)+2;
		for (int i = 0; i < count; i++) {
			float x = this.randomDisplacement(this.width);
			float y = this.randomDisplacement(this.height);
			int size = randomizer.nextInt(40/5)*5+10; 		//size = 10~50
			int heading = randomizer.nextInt(355/5)*5; 		//heading = 0~355
			int speed = randomizer.nextInt(10/5)*5+5; 		//speed = 5~10
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
				float x = this.randomDonutDisplacement(firstBase.getLocation().getX(), this.width, 50, 150);
				float y = this.randomDonutDisplacement(firstBase.getLocation().getY(), this.height, 50, 150);
				int random = randomizer.nextInt(2);
				NPC npc = new NPC(x, y, size, rgb);
				if (random == 0) npc.setStrategy(new AttackStrategy(npc)); 
				else npc.setStrategy(new ObjectiveStrategy(npc, objects.getBaseBySeq(2)));
				objects.add(npc);
			}
//			Generate 1 player
			for (int i = 0; i < 1; i++) {
				float x = firstBase.getLocation().getX();
				float y = firstBase.getLocation().getY();
				objects.add(Player.getPlayer(x, y, size, rgb));
				this.player = Player.getPlayer();
			}
		}
		this.updateChanges();
	}
	
//	helper: generate a random float in d.d form for location usage
	private float randomDisplacement(float bound) {
		Random randomizer = new Random();
		return (float) (Math.round((randomizer.nextFloat() + randomizer.nextInt((int)bound))*10.0)/10.0);
	}
	
//	helper: random donut displacement around a specific loc
	private float randomDonutDisplacement(float loc, float bound, int inner, int outer) {
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
		
//	cyborg reach a specific base
	public void collideWithBase(int base) {
		int baseCount = this.objects.getBaseCount();
//		return error 
		if (base > baseCount || base < 1) {
			Command cOk = new Command("Okay");
			Dialog.show("Error", "Invalid base number", cOk);
			return;
		}
//		otherwise reach base
		System.out.print("Base " + base + " collided. ");
		if (this.player.getBaseReached() + 1 == base) {
			this.player.setBaseReached(base);
			System.out.print("You reach a new base!\n");
			if (baseCount == this.player.getBaseReached()) {
				this.playerWin();
			}
		} else {
			System.out.print("But nothing happen!\n");
		}
		this.updateChanges();
	}
	
//	cyborg reach a random station
	public void collideWithEnergyStation() {
		Random randomizer = new Random();
//		get number of non-empty stations
		int stationCount = 0;
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof EnergyStation) {
				if (((EnergyStation)(obj)).getCapacity() != 0) {
					stationCount++;
				}
			}
		}
		int chosen = randomizer.nextInt(stationCount) + 1;	//choose a random station
		stationCount = 0;
		objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof EnergyStation) {
				if (((EnergyStation)(obj)).getCapacity() != 0) {
					stationCount++;
				}
				if (stationCount == chosen) {
//					consume energy
					GameObject station = obj;
					int capacity = ((EnergyStation)station).getCapacity();
					this.player.setEnergyLevel(this.player.getEnergyLevel() + capacity);
//					drain and fade station
					((EnergyStation)station).exhaust();
					station.fade(0.5);
//					log
					System.out.println("Energy station at loc=(" + station.getLocation().getX() + "," + station.getLocation().getY()  + ") capacity=" + capacity + " visited. Your energy level now is " + this.player.getEnergyLevel());
//					create a new station
					System.out.println("A new energy station has been created.");
					float x = this.randomDisplacement(this.width);
					float y = this.randomDisplacement(this.height);
					int size = randomizer.nextInt(20/5)*5+10; 
					int rgb = ColorUtil.rgb(0,255,0);	//green
					objects.add(new EnergyStation(x, y, size, rgb));
					break;
				}
			}
		}
		this.updateChanges();
	}
	
//	cyborg collide with a drone
	public void collideWithDrone() {
		int currentDamage = this.player.getDamageLevel();
		int maxDamage = this.player.getMaxDamage();
		currentDamage++;
		this.player.setDamageLevel(currentDamage);
		this.player.fade((double)currentDamage / (double)maxDamage);
		System.out.println("You have collided with a drone and took 1 damage! Your damage level now is " + currentDamage + "/" + maxDamage);
		if (currentDamage < maxDamage) {
//			rescale scale to cap
			int scaledMaxSpeed = this.player.getScaledMaxSpeed();
			if (this.player.getSpeed() > scaledMaxSpeed) {
				this.player.setSpeed(scaledMaxSpeed);
				System.out.println("Your speed has been reduced to " + this.player.getSpeed());
			}
		} else {
			System.out.println("You took maximum damage. You cannot move anymore!");
			this.playerLoseLife();
		}
		this.updateChanges();
	}
	
//	cyborg collide with another cyborg
	public void collideWithNPC() {
//		random npc takes damage
		int npcCount = this.objects.getNPCCount();
		Random randomizer = new Random();
		int chosen = randomizer.nextInt(npcCount) + 1;
		npcCount = 0;
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof NPC) {
				npcCount++;
				if (npcCount == chosen) {
					int currentDamage = ((NPC)obj).getDamageLevel();
					int maxDamage = ((NPC)obj).getMaxDamage();
					currentDamage += 2;
					((NPC)obj).setDamageLevel(currentDamage);
					System.out.println("NPC " + chosen + " has collided with you! NPC's damage level now is " + currentDamage + "/" + maxDamage);
					break;
				}
			}
		}
//		player takes damage
		int currentDamage = player.getDamageLevel();
		int maxDamage = player.getMaxDamage();
		currentDamage += 2;
		player.setDamageLevel(currentDamage);
		player.fade((double)currentDamage / (double)maxDamage);
		System.out.println("You have taken 2 damage! Your damage level now is " + currentDamage + "/" + maxDamage);
		if (currentDamage < maxDamage) {
//			rescale scale to cap
			int scaledMaxSpeed = player.getScaledMaxSpeed();
			if (player.getSpeed() > scaledMaxSpeed) {
				player.setSpeed(scaledMaxSpeed);
				System.out.println("Your speed has been reduced to " + player.getSpeed());
			}
		} else {
			System.out.println("You took maximum damage. You cannot move anymore!");
			this.playerLoseLife();
		}
		this.updateChanges();
	}
	
//	change strategies for all npc
	public void changeStrategies() {
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof NPC) {
				NPC npc = (NPC) obj;
//				side effect
				if (npc.getBaseReached() + 1 == this.objects.getBaseCount()) {
					String msg = "Oops! A NPC has reached the last base!\n\n";
					this.playerLose(msg);
					return;
				} else {
					(npc).setBaseReached(npc.getBaseReached() + 1);
				}
//				pick a new strategy
				if (npc.getStrategy() instanceof AttackStrategy) {
					Base nextBase = objects.getBaseBySeq(npc.getBaseReached() + 1);
					npc.setStrategy(new ObjectiveStrategy(npc, nextBase));
				} else {
					npc.setStrategy(new AttackStrategy(npc));
				}
				// print out changes
				System.out.println(npc.toString());
			}
		}
	}
	
//	display current status of cyborg and gw
	public void display() {
		System.out.println("Lives left: " + this.lives 
							+ "\nCurrent time: " + this.time
							+ "\nHighest base reached: " + this.player.getBaseReached()
							+ "\nEnergy level: " + this.player.getEnergyLevel()
							+ "\nDamage level: " + this.player.getDamageLevel()
							+ "/" + this.player.getMaxDamage());
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
	
//	tick the time by 1s and let the game order happens
	public void tick() {
		System.out.println("Tick ... tock ...");
//		player part
		if (this.player.getEnergyLevel() != 0 && this.player.getSpeed() != 0 && this.player.getDamageLevel() < this.player.getMaxDamage()) {
			this.player.steer();
			this.player.move();
			if (this.player.getLocation().getX() > this.width || this.player.getLocation().getY() > this.height || this.player.getLocation().getX() < 0 || this.player.getLocation().getY() < 0) {
				this.player.bounce(this.width, this.height);
			}
			this.player.consumeEnergy();
			if (this.player.getEnergyLevel() == 0) {
				System.out.println("You have run out of energy!");
				this.playerLoseLife();
			}
		}
//		AI part
		IIterator<GameObject> objs = this.objects.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
//			NPC
			if (obj instanceof NPC) {
				((NPC)obj).invokeStrategy();
				((NPC)obj).move();
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
				((Drone)obj).move();
				if (obj.getLocation().getX() > this.width || obj.getLocation().getY() > this.height || obj.getLocation().getX() < 0 || obj.getLocation().getY() < 0) {
					((Movable)obj).bounce(this.width, this.height);
				}
			}
		}
//		increase tick by 1
		this.time++;
		this.updateChanges();
	}
	
//	player lose a life 
	public void playerLoseLife() {
		this.lives--;
		Command cOk = new Command("Okay");
		Dialog.show("Whammm!", "You have lost 1 life. Lives left: " + this.lives, cOk);
		if (this.lives == 0) {
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
	
//	For observable call
	public int getTime() {
		return this.time;
	}

	public int getLives() {
		return this.lives;
	}
	
	public int getBaseReached() {
		return this.player.getBaseReached();
	}
	
	public int getEnergyLevel() {
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
					+ "CSC 133 Assignment 2\n", 
					"Okay", null);
	}

	public void help() {
		Dialog.show("Help",
				"List of Hotkeys \n"
				+ "A: accelerate \n"
				+ "B: brake \n"
				+ "L: steer left \n"
				+ "R: steer right \n"
				+ "E: reach an energy station \n"
				+ "G: get hit by a drone \n"
				+ "T: make a clock tick \n",
				"Okay", null);
	}
	
//	Invoke changes for observer
//	Stewpid JAVA
	public void updateChanges() {
		this.setChanged();
		this.notifyObservers();
	}
}
