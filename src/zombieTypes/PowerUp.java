package zombieTypes;

import graphics.SpriteFactory;
import graphics.TempSprite;

import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Briefing;
import com.JohnQ1216.ScavengerZombieDefense.Main;
import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.view.SurfaceView;

public class PowerUp extends PrimSprite {

	// the crux of this class is the bool itemDropped. This determines when the
	// item box needs to be drawn
	// another important var is the int currentItem. This determines what type
	// of image and effect are in play

	// killAll might be disabled later on, this is just a test
	private Bitmap bmpSlowAll, bmpKillAll, bmpAmmoUp, bmpRepairWall;
	public static final int NULL_ITEM = 0, SLOW_ALL = 1, KILL_ALL = 2,
			AMMO_UP = 3, REPAIR_WALL = 4;
	private int currentItem = NULL_ITEM;
	private boolean itemDropped, whiteOut, freezeBlast;

	public PowerUp() {
		super();
		setMaxHP(100);
		whiteOut = false;
		freezeBlast = false;
	}

	public int getItemType() {
		return currentItem;
	}

	public boolean getWhiteOut() {
		return whiteOut;
	}

	public void setWhiteOut(boolean b) {
		whiteOut = b;
	}
	
	public boolean getFreezeBlast(){
		return freezeBlast;
	}
	
	public void setFreezeBlast(boolean b){
		freezeBlast = b;
	}

	// called by zombie class upon its death in its runDeath(). Make this class
	// static in Factory. Call SpriteFactory.powerUp.dropRandom();
	public void dropRandom(int x, int y, SurfaceView gameView) {
		if (!itemDropped) {
			itemDropped = true;
			setX(x);
			setY(y);
			revive();
			// setMaxHP(100);
			int dice = new Random().nextInt(3) + 1;
			currentItem = dice;
			switch (currentItem) {
			case SLOW_ALL:
				// assign bmp and wait
				if (bmpSlowAll == null) {
					bmpSlowAll = BitmapFactory.decodeResource(
							gameView.getResources(), R.drawable.snowflake);
				}
				setBMP(bmpSlowAll);
				break;
			case KILL_ALL:
				// assign bmp and wait
				if (bmpKillAll == null) {
					bmpKillAll = BitmapFactory.decodeResource(
							gameView.getResources(), R.drawable.nuke);
				}
				setBMP(bmpKillAll);
				break;
			case AMMO_UP:
				// assign bmp and wait
				if (bmpAmmoUp == null) {
					bmpAmmoUp = BitmapFactory.decodeResource(
							gameView.getResources(), R.drawable.ammo);
				}
				setBMP(bmpAmmoUp);
				break;
			// case REPAIR_WALL:
			// // create bmp for this
			// if (bmpRepairWall == null) {
			// bmpRepairWall = BitmapFactory.decodeResource(
			// gameView.getResources(), R.drawable.nuke);
			// }
			// setBMP(bmpRepairWall);
			// break;
			}

		}
	}

	// test this later to see if it draws
	public void onDraw(Canvas canvas, long sleepSuggested, long startTime) {
		if (itemDropped) {
			super.onDraw(canvas, sleepSuggested, startTime);
		}
	}

	public void runDeath() {
		super.runDeath();
		itemDropped = false;
		if (currentItem == KILL_ALL) {
			whiteOut = true;
		}
		if (currentItem == SLOW_ALL) {
			freezeBlast = true;
		}
	}

	protected void update(long sleepSuggested, long startTime, Canvas canvas) {
		super.update(sleepSuggested, startTime, canvas);

		if (getHP() > getMaxHP() / 2) {

			if (getFrame() == 3) {
				setFrameTime(2000);
			} else {
				setFrameTime(50);
			}
		}
	}

	public void revive() {
		super.revive();
		whiteOut = false;
	}

	public void activateEffect(List<Sprite> spriteList) {

		if (currentItem != NULL_ITEM) {
			if (currentItem == KILL_ALL) {
				for (int i = spriteList.size() - 1; i >= 0; i--) {
					Sprite sprite = spriteList.get(i);
					if (sprite.isAlive()) {
						sprite.setHP(0);
					}
				}
			} else if (currentItem == SLOW_ALL) {
				for (int i = spriteList.size() - 1; i >= 0; i--) {
					Sprite sprite = spriteList.get(i);
					if (sprite.isAlive()) {
						sprite.slowSprite();
					}
				}

//				Briefing.currentEquippedGun.addIceAmmo(20);
			} else if (currentItem == AMMO_UP) {

//				Briefing.currentEquippedGun.addToTotalAmmo(50);
				
				Briefing.currentEquippedGun.addExRound(5);

			} else if (currentItem == REPAIR_WALL) {

				Main.restoreHP();
			}
			currentItem = NULL_ITEM;
		}

	}

}
