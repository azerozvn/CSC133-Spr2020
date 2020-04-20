package com.mycompany.a3;

import com.codename1.util.MathUtil;

public abstract class Strategy{
	private NPC npc;
	public Strategy(NPC npc) {
		this.npc = npc;
	}
	
	public void followTarget(GameObject target) {
//		locking target	
		float pX = target.getLocation().getX();
		float pY = target.getLocation().getY();
		float aX = this.npc.getLocation().getX();
		float aY = this.npc.getLocation().getY();
		float deltaX = aX - pX;
		float deltaY = aY - pY;
		double beta = Math.toDegrees(MathUtil.atan2((double)deltaY, (double)deltaX));	//(-180,180)
		double currentHeading = (double)this.npc.getHeading();							//(0,359)
		double headingToTarget = 90 - beta;												//(-90,270)
		if (headingToTarget < 0) headingToTarget += 360;	
//		if target is ahead already
		if (currentHeading == headingToTarget) {
//			boost to max speed
			int scaledMaxSpeed = this.npc.getScaledMaxSpeed();
			this.npc.setSpeed(scaledMaxSpeed);
//			reset steering
			this.npc.setSteerDirection(0);			
		} else {
//			otherwise steer
			if (currentHeading < headingToTarget && currentHeading > headingToTarget - 180) {
//				steerRight
				double angleToTarget = headingToTarget - currentHeading;
				if (angleToTarget < 0) angleToTarget += 360;
				if (angleToTarget > 40) {
					this.npc.setSteerDirection(40);
				} else {
					this.npc.setSteerDirection((int)angleToTarget);
//					boost to max speed
					int scaledMaxSpeed = this.npc.getScaledMaxSpeed();
					this.npc.setSpeed(scaledMaxSpeed);
				}
			} else {
//				steerLeft
				double angleToTarget = currentHeading - headingToTarget;
				if (angleToTarget < 0) angleToTarget += 360;
				if (angleToTarget > 40) {
					this.npc.setSteerDirection(-40);
				} else {
					this.npc.setSteerDirection(-(int)angleToTarget);
//					boost to max speed
					int scaledMaxSpeed = this.npc.getScaledMaxSpeed();
					this.npc.setSpeed(scaledMaxSpeed);
				}
			}
		}
//		steer
		this.npc.steer();
//		Note: No collision detection yet, once npc passes the target, it will try to revolve back to target
	}
}
