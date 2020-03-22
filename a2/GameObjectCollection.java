package com.mycompany.a2;

import java.util.ArrayList;

public class GameObjectCollection implements ICollection{
	private ArrayList<GameObject> objects;
	private int baseCount, stationCount, droneCount, npcCount;
	
	public GameObjectCollection() {
		objects = new ArrayList<GameObject>();
		baseCount = 0;
		stationCount = 0;
		droneCount = 0;
		npcCount = 0;
	}
	
	public int getBaseCount() {
		return this.baseCount;
	}
	
	public int getStationCount() {
		return this.stationCount;
	}
	
	public int getDroneCount() {
		return this.droneCount;
	}
	
	public int getNPCCount() {
		return this.npcCount;
	}
	
	public GameObject get(int i) {
		return this.objects.get(i);
	}
	
	public void add(GameObject obj) {
		this.objects.add(obj);		//arrayList add method
		if (obj instanceof Base) baseCount++;
		if (obj instanceof EnergyStation) stationCount++;
		if (obj instanceof Drone) droneCount++;
		if (obj instanceof Cyborg) npcCount++;
	}
	
//	helper return a base for npc to follow 
	public Base getBaseBySeq(int num) {
		IIterator<GameObject> objs = this.getIterator();
		while (objs.hasNext()) {
			GameObject obj = objs.getNext();
			if (obj instanceof Base) {
				if (((Base)(obj)).getSeqNum() == num) {
					return (Base)obj;
				}
			}
		}
		return null;
	}
	
	public void clear() {
		this.objects.clear();
		baseCount = 0;
		stationCount = 0;
		droneCount = 0;
		npcCount = 0;
	}
	
	public GameObjectIterator getIterator() {
		return new GameObjectIterator();
	}
	
	private class GameObjectIterator implements IIterator<GameObject> {
		private int current = -1;	//need to be -1 to reference a[0]
		
		public GameObjectIterator() {
			
		}
		
		public boolean hasNext() {
			if (objects.size() <= 0) return false;
			if (current == objects.size() - 1) return false;
			return true;
		}
		
		public GameObject getNext() {
			current++;
			return objects.get(current);
		}

//		optional, never used
		public void remove() {
			if (current >=0 || current < objects.size()) {
				GameObject obj = objects.get(current);
				if (obj instanceof Base) baseCount--;
				if (obj instanceof EnergyStation) stationCount--;
				if (obj instanceof Drone) droneCount--;
				if (obj instanceof Cyborg) npcCount--;
				objects.remove(current);
			}
		}
	}
}
