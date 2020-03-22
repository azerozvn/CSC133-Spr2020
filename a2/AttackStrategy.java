package com.mycompany.a2;

public class AttackStrategy extends Strategy implements IStrategy{
	public AttackStrategy(NPC npc) {
		super(npc);
	}

	public void apply() {
		Player player = Player.getPlayer();
		super.followTarget(player);
	}
}