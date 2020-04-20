package com.mycompany.a3;

public class ObjectiveStrategy extends Strategy implements IStrategy{
	private Base nextBase;
	public ObjectiveStrategy(NPC npc, Base base) {
		super(npc);
		this.nextBase = base;
	}

	public void apply() {
		super.followTarget(this.nextBase);
	}
}
