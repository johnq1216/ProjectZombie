package zombieTypes;

import graphics.SpriteFactory;
import graphics.TempSprite;

import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.CommandCenter;
import com.JohnQ1216.ScavengerZombieDefense.Main;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class Sprite extends PrimSprite{
		final static int ZOMBIE_BASE_HP = 100;
		private int zombieID = 0;
		public final static int ID_ZWALKER = 0, ID_ZRUNNER = 1, ID_ZTANK = 2,
			ID_E_RAGE = 3, ID_E_HYPER = 4, ID_E_BANSHEE = 5;
		private boolean damageNow = true;
		private int baseChanceTabletDrop = 5;
		
		
	public Sprite(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		setMaxHP(ZOMBIE_BASE_HP);
	}

	private boolean checkWallHit(){
		return isAlive() && getY() >= gameView.getHeight() - (gameView.getHeight()/5);
	}
	
		/**
	 * If the sprite collides with the wall, it will begin attacking it
	 */
	public void attackWallIfCollided() {
		// zombie will stop if it reaches the wall and the wall is still alive

		if (checkWallHit()) {
			if (Main.STATSplayerHP > 0) {
				setYSpeed(0);
				setXSpeed(0);
				setAttackMode(true);
				// this will use the sprite's animation to time the actual
				// damage
				// however, the run method can run several times while
				// currentFrame has
				// the value of 1. therefore, it's checked by the damageNow
				// boolean
				// which will be restored when the currentFrame's value becomes
				// 2.
				if (checkWallHitAnimation() && damageNow && isAlive()) {
					Main.bleedHP(-1);
					damageNow = false;
				} else if (getFrame() == 2) {
					damageNow = true;
				}
			}
			// once the wall is down, zombie will continue on
			// we'll use this code later. Right now, the wall going down is
			// immediate defeat
			// else{
			// if (getYSpeed() == 0){
			// setYSpeed(2);
			// }
			// }
		}
	
	}
	

	public boolean checkWallHitAnimation() {
		return (checkWallHit() && getFrame()==1);
	}
	
	
	

	public void runDeath(Canvas canvas, List<TempSprite> temps){
//		SpriteFactory.powerUp.dropRandom(getX(), getY(), gameView);

		int chance = new Random().nextInt(100)+1;
		if (chance <= baseChanceTabletDrop + CommandCenter.boostTabletDrop){
			SpriteFactory.powerUp.dropRandom(getX(), getY(), gameView);
			if (CommandCenter.boostTabletDrop >= 20){
				CommandCenter.boostTabletDrop = 10;
			}
		}
		super.runDeath(canvas, temps);
	}
	
		/**
	 * Returns Zombie ID. This is crucial to identify which sprites need to be
	 * revived to avoid instantiating a new sprite
	 * 
	 * @return
	 */
	public int getZombieID() {
		return zombieID;
	}
	
		/**
	 * Sets Zombie ID. This is crucial to identify which sprites need to be
	 * revived to avoid instantiating a new sprite
	 * 
	 * @return
	 */
	public void setZombieID(int id) {
		zombieID = id;
	}
	
	
	public void revive(){
		super.revive();
		setY(-60);
	}

	public void setPushBack(int pushBack){

	double scaledPushBack = (pushBack/PREVAIL_HEIGHT) * gameView.getHeight();
	setY(getY()-(int)scaledPushBack);
	}



}