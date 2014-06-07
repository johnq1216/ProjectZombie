package com.JohnQ1216.ScavengerZombieDefense;

import graphics.TempSprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import threadAndTimers.Timer;

import zombieTypes.Sprite;

/**
 * SurvBot stores its own objects in a list Only 2 methods will be called and
 * they're NOT going to be static.
 * 
 * Method 1 will be called in onCreate and will create new SurvBots to match the
 * number of Survivors if there isn't enough already in the List.
 * 
 * Method 2 will loop through the List and have each SurvBot fire their shots if
 * they are ready.
 * 
 * It won't matter if the List contains more SurvBots than there are survivors
 * because only the exact number of SurvBots that equal the survivors will fire
 * 
 */
public class SurvBot {
	private Sprite survTargetSprite;
	private boolean survTargetLocked = false;
	private Timer botTimeShoot;
	private int xEventBot;
	private int yEventBot;
	private static Sprite staticSprite;

	public SurvBot() {
		botTimeShoot = new Timer(2);
	}
	

	public void update(Sprite sprite) {
		if (sprite.isAlive() && !survTargetLocked && staticSprite != null
				|| sprite != staticSprite || !staticSprite.isAlive()) {
			survTargetLocked = true;
			survTargetSprite = sprite;
			if (staticSprite == null || !staticSprite.isAlive())
				staticSprite = sprite;
		}
		if (!survTargetSprite.isAlive()) {
			survTargetLocked = false;
		}

	}

	public float getBotX() {
		// TODO Auto-generated method stub
		return xEventBot;
	}

	public float getBotY() {
		// TODO Auto-generated method stub
		return yEventBot;
	}

	public boolean checkIfFire(Sprite sprite) {
		// TODO Auto-generated method stub
		boolean didShotFire = false;
		if (botTimeShoot.getMS() <= 0 && survTargetLocked
				&& survTargetSprite.isAlive()) {
			didShotFire = true;
			 botTimeShoot.setMS(new Random().nextInt(1500) + 500);
//			botTimeShoot.setMS(500);

			xEventBot = survTargetSprite.getX() + survTargetSprite.getWidth()
					/ 2;
			yEventBot = survTargetSprite.getY() + survTargetSprite.getHeight()
					/ 2;

			survTargetSprite
					.runDamage(Briefing.currentEquippedGun.getDamage() / 2);

		}
		return didShotFire;
	}
}
