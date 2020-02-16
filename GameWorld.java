package com.csc133.ass1;

import java.util.Random;
import java.util.ArrayList;
import com.codename1.charts.util.ColorUtil;

public class GameWorld {
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private Cyborg player;								//quick ref to cyborg
	private int baseNum;								//number of base in game
	private int lives = 3;								//number of lives
	private int clock = 0;								//number of time elapsed
	private float worldBound = (float) 1000.0;			//store gw upper boundary
	private boolean exitPrompt = false;					//track user prompt to exit
	
//	generate a random float in d.d form for location usage
	private float getRandomFloat() {
		Random randomizer = new Random();
		return (float) (Math.round((randomizer.nextFloat() + randomizer.nextInt(1000))*10.0)/10.0);
	}
	
	public void init() {
		Random randomizer = new Random();
//		Generate text
		System.out.println("GW Initialized!");
//		Generate 4~9 bases
		this.baseNum = randomizer.nextInt(6)+4;
		for (int i = 0; i < this.baseNum; i++) {
			float x = this.getRandomFloat();
			float y = this.getRandomFloat();
			int size = 10;
			int rgb = ColorUtil.rgb(0,0,255);				//blue
			objects.add(new Base(x, y, size, rgb, i+1));
		}
//		Generate 2~4 energy stations
		for (int i = 0; i < randomizer.nextInt(3)+2; i++) {
			float x = this.getRandomFloat();
			float y = this.getRandomFloat();
			int size = randomizer.nextInt(40/5)*5+10; 		//size = 10~50
			int rgb = ColorUtil.rgb(0,255,0);				//green
			objects.add(new EnergyStation(x, y, size, rgb));
		}
//		Generate 2~4 drones
		for (int i = 0; i < randomizer.nextInt(3)+2; i++) {
			float x = (float) (Math.round((randomizer.nextFloat() + randomizer.nextInt(1000))*10.0)/10.0);
			float y = (float) (Math.round((randomizer.nextFloat() + randomizer.nextInt(1000))*10.0)/10.0);
			int size = randomizer.nextInt(40/5)*5+10; 		//size = 10~50
			int heading = randomizer.nextInt(355/5)*5; 		//heading = 0~355
			int speed = randomizer.nextInt(10/5)*5+5; 		//speed = 5~10
			int rgb = ColorUtil.rgb(255,0,255);	//purple
			objects.add(new Drone(x, y, size, rgb, heading, speed));
		}
//		Generate 1 cyborg
		for (int i = 0; i < 1; i++) {
			float x = objects.get(0).getLocation().getX();
			float y = objects.get(0).getLocation().getY();
			int size = 40;
			int rgb = ColorUtil.rgb(255,0,0);	//red
			objects.add(new Cyborg(x, y, size, rgb));
		}
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Cyborg) {
				this.player = (Cyborg) objects.get(i);
			}
		}
		this.showMap();
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
	public void reachBase(int base) {
		int baseCount = 0;
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Base) {
				baseCount++;
			}
		}
		if (base > baseCount) return;
		System.out.print("Base " + base + " collided. ");
		if (this.player.getBaseReached() + 1 == base) {
			this.player.setBaseReached(base);
			System.out.print("You reach a new base!\n");
			if (this.baseNum == this.player.getBaseReached()) {
				this.playerWin();
			}
		} else {
			System.out.print("But nothing happen!\n");
		}
	}
	
//	cyborg reach a random station
	public void reachStation() {
		Random randomizer = new Random();
//		get number of non-empty stations
		int stationCount = 0;
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof EnergyStation) {
				if (((EnergyStation)(objects.get(i))).getCapacity() != 0) {
					stationCount++;
				}
			}
		}
		int chosen = randomizer.nextInt(stationCount) + 1;	//choose a random station
		stationCount = 0;	//reset count
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof EnergyStation) {
				if (((EnergyStation)(objects.get(i))).getCapacity() != 0) {
					stationCount++;
				}
				if (stationCount == chosen) {
//					consume energy
					GameObject station = objects.get(i);
					int capacity = ((EnergyStation)station).getCapacity();
					this.player.setEnergyLevel(this.player.getEnergyLevel() + capacity);
//					drain and fade station
					((EnergyStation)station).exhaust();
					station.fade(0.5);
//					log
					System.out.println("Energy station at loc=(" + station.getLocation().getX() + "," + station.getLocation().getY()  + ") capacity=" + capacity + " visited. Your energy level now is " + this.player.getEnergyLevel());
//					create a new station
					System.out.println("A new energy station has been created.");
					float x = this.getRandomFloat();
					float y = this.getRandomFloat();
					int size = randomizer.nextInt(20/5)*5+10; 
					int rgb = ColorUtil.rgb(0,255,0);	//green
					objects.add(new EnergyStation(x, y, size, rgb));
					break;
				}
			}
		}
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
	}
	
//	cyborg collide with another cyborg
	public void collideWithCyborg() {
		int currentDamage = this.player.getDamageLevel();
		int maxDamage = this.player.getMaxDamage();
		currentDamage += 2;
		this.player.setDamageLevel(currentDamage);
		this.player.fade((double)currentDamage / (double)maxDamage);
		System.out.println("You have collided with another cyborg and took 2 damage! Your damage level now is " + currentDamage + "/" + maxDamage);
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
	}
	
//	display current status of cyborg and gw
	public void display() {
		System.out.println("Lives left: " + this.lives 
							+ "\nCurrent clock: " + this.clock
							+ "\nHighest base reached: " + this.player.getBaseReached()
							+ "\nEnergy level: " + this.player.getEnergyLevel()
							+ "\nDamage level: " + this.player.getDamageLevel()
							+ "/" + this.player.getMaxDamage());
	}
	
//	show the gw map
	public void showMap() {
		System.out.println("*************************GAME MAP*************************");
		for (int i = 0; i < objects.size(); i++) {
			System.out.println(objects.get(i).toString());
		}
		System.out.println("**********************************************************");
	}
	
//	tick the clock by 1s and let the game order happens
	public void tick() {
		System.out.println("Tick ... tock ...");
//		cyborg part
		if (this.player.getEnergyLevel() != 0 && this.player.getSpeed() != 0 && this.player.getDamageLevel() < this.player.getMaxDamage()) {
			this.player.steer();
			this.player.move();
			if (this.player.getLocation().getX() > this.worldBound || this.player.getLocation().getY() > this.worldBound || this.player.getLocation().getX() < 0 || this.player.getLocation().getY() < 0) {
				this.player.bounce(worldBound);
			}
			this.player.consumeEnergy();
			if (this.player.getEnergyLevel() == 0) {
				System.out.println("You have run out of energy!");
				this.playerLoseLife();
			}
		}
//		drone part
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Drone) {
				((Drone)objects.get(i)).flick();
				((Drone)objects.get(i)).move();
				if (objects.get(i).getLocation().getX() > this.worldBound || objects.get(i).getLocation().getY() > this.worldBound || objects.get(i).getLocation().getX() < 0 || objects.get(i).getLocation().getY() < 0) {
					((Movable)objects.get(i)).bounce(worldBound);
				}
			}
		}
//		increase tick by 1
		this.clock++;
	}
	
//	player lose a life 
	public void playerLoseLife() {
		this.lives--;
		System.out.println("*************************************************");
		System.out.println("You have lost 1 life. Lives left: " + this.lives);
		if (this.lives == 0) {
			System.out.println("Game over, you failed!");
			this.exitPrompt = !this.exitPrompt; 
			this.exit('y');
		} else {
			System.out.println("The game is reset!");
		}
		System.out.println("**************************************************");
		if (this.lives > 0) {
			this.reset();
		}
	}
	
//	player win the game
	public void playerWin() {
		System.out.println("**************************************");
		System.out.println("Game over, you win! Total time: " + this.clock);
		System.out.println("**************************************");
		this.exitPrompt = !this.exitPrompt; 
		this.exit('y');
	}
	
//	reset the game
	public void reset() {
		this.objects.clear();
		this.init();
	}
	
//	exit prompt
	public void exit() {
		System.out.println("Do you want to exit the game?");
		System.out.println("Enter \'y\' for yes or \'n\' for no");
		this.exitPrompt = true;
	}
	
//	real exit
	public void exit(char prompt) {
		if (this.exitPrompt) {
			if (prompt == 'y') {
				System.out.println("Exit game!");
				System.exit(0);
			} else if (prompt == 'n') {
				System.out.println("The game continues");
				this.exitPrompt = !this.exitPrompt;
			}
		}
	}
	
}
