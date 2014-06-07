package graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Briefing;
import com.JohnQ1216.ScavengerZombieDefense.CommandCenter;
import com.JohnQ1216.ScavengerZombieDefense.Main;
import com.JohnQ1216.ScavengerZombieDefense.SurvBot;
import com.JohnQ1216.ScavengerZombieDefense.Main.GameView;
import com.QuachProjectZombie.R;

import data.SpawnScript;

import threadAndTimers.Timer;
import zombieTypes.*;

import Weapon.Weapon;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class SpriteFactory {

	/**
	 * SpriteFactory handles most of the sprite drawing and damage calculation.
	 */

	public static List<Sprite> spriteList = new ArrayList<Sprite>();
	private List<TempSprite> temps = new ArrayList<TempSprite>();
	private TempSprite uIChangeWeapon, uIChangeSpecial, crossHairs;
	private SurfaceView gameView;
	private int gameLevel;
	public int numberOfZombiesAlive;
	private Bitmap grass, bloodshot, shot, explosion, crosshair, fence2, crit,
			critHigh, weapon1, weapon0, changeWeapon, changeSpecial,
			changeSpecialTouch, changeWeaponTouch, street, fire, soundwave,
			fog, freezeBlast;
	private boolean crossHairsCreated = false, runExplode = false,
			bombKillsAll = false, allDead;
	public boolean touchDetected = false, introSound = false;
	

	private Activity activity;

	private float explodeX, explodeY, xTouch, yTouch, xSuicide, ySuicide;
	// this is only for seeing how much dmg is dealt. Will not be in final game
	public Wall wall;

	public int debugDamageDealt, counterZombieWalker, counterZombieRunner,
			counterZombieTank, counterEliteRage, counterEliteHyper,
			counterEliteBanshee, counterTotal;

	public Timer spawnTimer;

	private SoundPool sp;
	private int soundGunshot = 0, soundShotgun = 0;

	private int soundCorpseExplode = 0, powerUpSoundNuke = 0,
			soundGunDryFire = 0, soundGunCocking = 0, soundWallHit = 0, soundRicochet;
	private int dialogueTimeToDie = 0, dialogueBrains = 0, dialogueGrunt1 = 0,
			dialogueGrunt2 = 0, dialogueRageUp = 0, dialogueBanshee = 0, dialogueNewRecord = 0;

	private int powerUpSoundFreeze = 0;

	private Timer botTimeShoot;
	private boolean shotReadyBot = false;

	SurvBot survBot1 = new SurvBot();
	SurvBot survBot2 = new SurvBot();

	public static PowerUp powerUp;

	private int baseCritChance = 2;
	private int alpha;
	private Timer alphaTimer;
	private int alphaCounter;

	private Paint canvasPaint, fogPaint;
	private ColorFilter canvasFilter;
	private boolean exRoundRunDamage = true;

	RectF rectExplosiveRadius = new RectF();
	RectF rectSpriteHitBox = new RectF();

	public SpriteFactory(Activity activity, SurfaceView gameView, int gameLevel) {
		this.activity = activity;
		this.gameView = gameView;
		this.gameLevel = gameLevel;
		spawnTimer = new Timer(1);

		powerUp = new PowerUp();

		alphaTimer = new Timer((long) 200);

		// new code
		for (int i = getSpriteList().size() - 1; i >= 0; i--) {
			Sprite sprite = getSpriteList().get(i);
			sprite.setMaxHP(sprite.getMaxHP() + 1000);
		}

		SpawnScript.draft(gameLevel);

		shot = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.shot5);
		grass = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.grass);

		explosion = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.explosion7);
		fence2 = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.fence5);
		crit = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.crit);
		critHigh = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.crithigh);
		changeWeapon = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.changeweapon);
		changeSpecial = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.changespecial);
		changeWeaponTouch = BitmapFactory.decodeResource(
				gameView.getResources(), R.drawable.changeweapontouch);
		changeSpecialTouch = BitmapFactory.decodeResource(
				gameView.getResources(), R.drawable.changespecialtouch);
		bloodshot = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.blood6);
		weapon0 = BitmapFactory.decodeResource(gameView.getResources(),
				Weapon.getImageRCode(0));
		street = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.street);
		fire = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.fire);
		soundwave = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.soundwave2);
		fog = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.fog);
		freezeBlast = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.iceblast);

		// this is done so that there is no out of bounds exceptions on the
		// arraylist
		if (Weapon.getListSize() > 1) {
			weapon1 = BitmapFactory.decodeResource(gameView.getResources(),
					Weapon.getImageRCode(1));
		}
		uIChangeWeapon = new TempSprite(gameView, 0, 0, changeWeapon, 100,
				-999, true);
		wall = new Wall(gameView);

		sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		soundGunshot = sp.load(this.activity, R.raw.gunshot, 1);
		soundShotgun = sp.load(this.activity, R.raw.shotgun, 1);

		soundCorpseExplode = sp.load(this.activity, R.raw.explodedeath, 1);
		soundGunCocking = sp.load(this.activity, R.raw.guncocking, 1);
		soundGunDryFire = sp.load(this.activity, R.raw.gundryfire, 1);
		soundWallHit = sp.load(this.activity, R.raw.fencebash, 1);
		soundRicochet = sp.load(this.activity, R.raw.ricochet, 1);

		dialogueTimeToDie = sp.load(this.activity, R.raw.dialoguetimetodie, 1);
		dialogueBrains = sp.load(this.activity, R.raw.dialoguebrains, 1);
		dialogueGrunt1 = sp.load(this.activity, R.raw.grunt1, 1);
		dialogueGrunt2 = sp.load(this.activity, R.raw.grunt2, 1);
		dialogueRageUp = sp.load(this.activity, R.raw.rageup, 1);
		dialogueBanshee = sp.load(this.activity, R.raw.banshee, 1);
		dialogueNewRecord = sp.load(this.activity, R.raw.newrecord, 1);

		
		powerUpSoundFreeze = sp.load(this.activity, R.raw.freeze, 1);
		powerUpSoundNuke = sp.load(this.activity, R.raw.explosion, 1);
		

		botTimeShoot = new Timer(2);
		alpha = 0;
		alphaCounter = 0;

		canvasPaint = new Paint();
		canvasFilter = new LightingColorFilter(Color.GRAY, 0);
		canvasPaint.setColorFilter(canvasFilter);

		fogPaint = new Paint();
		fogPaint.setAlpha(75);

	}

	public void playRifleShot() {
		// TODO Auto-generated method stub
		if (soundGunshot != 0)
			sp.play(soundGunshot, 1f, 1f, 0, 0, 2f);
	}

	public void playSMGShot() {
		// TODO Auto-generated method stub
		if (soundGunshot != 0)
			sp.play(soundGunshot, 1f, 1f, 0, 0, 2f);
	}

	public void playGunShot() {
		if (soundGunshot != 0)
			sp.play(soundGunshot, 1f, 1f, 0, 0, 1f);
	}

	public void playShotgun() {
		if (soundShotgun != 0)
			sp.play(soundShotgun, 1f, 1f, 0, 0, 1f);
	}

	public void playTimeToDie() {
		if (dialogueTimeToDie != 0)
			sp.play(dialogueTimeToDie, 1f, 1f, 0, 0, 0.9f);
	}

	public void playBansheeScream() {
		if (dialogueBanshee != 0) {
			float randF;
			randF = new Random().nextFloat() * (0.5f) + 1;
			sp.play(dialogueBanshee, 1f, 1f, 0, 0, randF);
		}
	}

	public void playGunDryFire() {
		if (soundGunDryFire != 0)
			sp.play(soundGunDryFire, 1f, 1f, 0, 0, 1f);
	}

	public void playGunCocking() {
		if (soundGunCocking != 0)
			sp.play(soundGunCocking, 1f, 1f, 0, 0, 1f);
	}

	public void playAmmoUp() {
		if (soundGunCocking != 0)
			sp.play(soundGunCocking, 1f, 1f, 0, 0, 1.8f);
	}

	public void playExplosion() {
		if (powerUpSoundNuke != 0)
			sp.play(powerUpSoundNuke, 1f, 1f, 0, 0, 0.75f);
	}

	public void playFreeze() {
		if (powerUpSoundFreeze != 0)
			sp.play(powerUpSoundFreeze, 1f, 1f, 0, 0, 1.2f);
	}

	public void playCorpseExplosion() {
		if (soundCorpseExplode != 0) {
			sp.play(soundCorpseExplode, 1f, 1f, 0, 0, 0.5f);
			sp.play(soundCorpseExplode, 1f, 1f, 0, 0, 1.9f);
		}
	}

	public void playerWallHit() {
		if (soundWallHit != 0) {
			float randF;
			randF = new Random().nextFloat() * (0.5f) + 1;
			sp.play(soundWallHit, 0.5f, 0.5f, 0, 0, randF);
		}
	}
	
	public void playRicochet() {
		if (soundRicochet != 0) {
			float randF;
			randF = new Random().nextFloat() * (0.5f) + 1;
			sp.play(soundRicochet, 0.5f, 0.5f, 0, 0, randF);
		}
	}
	
	public void playNewRecord(){
		if (dialogueNewRecord != 0)
			sp.play(dialogueNewRecord, 1f, 1f, 0, 0, 1.2f);
	}
	

	// change to random grunt sounds
	public void playBrains() {
		int randInt = new Random().nextInt(5) + 1;
		if (randInt == 1 && dialogueBrains != 0) {
			float randF;
			randF = new Random().nextFloat() * (0.5f) + 1;
			sp.play(dialogueBrains, 1f, 1f, 0, 0, randF);
		} else if (randInt == 2 && dialogueGrunt1 != 0) {
			float randF;
			randF = new Random().nextFloat() * (0.5f) + 1;
			sp.play(dialogueGrunt1, 1f, 1f, 0, 0, randF);
		} else if (randInt == 3 && dialogueGrunt2 != 0) {
			float randF;
			randF = new Random().nextFloat() * (0.5f) + 1;
			sp.play(dialogueGrunt2, 1f, 1f, 0, 0, randF);
		}
	}

	public void clearSprites() {
		getSpriteList().clear();
	}

	public boolean spriteListEmpty() {
		return getSpriteList().isEmpty();
	}

	public void touchDetected(float xTouch, float yTouch) {

		this.xTouch = xTouch;
		this.yTouch = yTouch;
		touchDetected = true;
		if (!Briefing.currentEquippedGun.hasExRound()) {
			temps.add(new TempSprite(temps, gameView, xTouch, yTouch, shot, 50,
					2, false));
		} else {
			Briefing.currentEquippedGun.addExRound(-1);
			temps.add(new TempSprite(temps, gameView, xTouch, yTouch,
					explosion, 50, 2, false));
			playExplosion();
			exRoundRunDamage = true;

		}

	}

	public void render(Canvas canvas, long desiredFPSinMS, long startTime,
			int hp) {

		if (introSound == false && Main.countdown.getSeconds() <= 27) {
			sp.play(dialogueTimeToDie, 1f, 1f, 0, 0, 1.5f);
			introSound = true;
		}

		// SPAWN zombies while timer is going
		if (Main.countdown.getSeconds() >= 0 && (spawnTimer.getSeconds() <= 0)) {
			spawnTimer.reset();
			SpawnScript.spawnZombies(this);
		}
		drawBackground(canvas);
		drawTemp(canvas, desiredFPSinMS, startTime);
		powerUp.onDraw(canvas, desiredFPSinMS, startTime);
		updateAndDraw(canvas, desiredFPSinMS, startTime);
		drawFog(canvas);
		whiteFade(canvas);
		freezeBlast(canvas);
	}

	private void drawFog(Canvas canvas) {

		// canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight()/2,
		// fogPaint);

		// this rectangle will show part of the bitmap or stretch it
		// this rectangle will paint the exact coordinates of the bitmap
		RectF dst = new RectF(0, 0, canvas.getWidth(), canvas.getHeight() / 2);

		canvas.drawBitmap(fog, null, dst, fogPaint);

		dst = new RectF(0, canvas.getHeight() / 2, canvas.getWidth(),
				canvas.getHeight());
		canvas.drawBitmap(fog, null, dst, fogPaint);

	}

	private void freezeBlast(Canvas canvas) {

		if (powerUp.getFreezeBlast()) {
			powerUp.setFreezeBlast(false);
			temps.add(new TempSprite(temps, gameView, canvas.getWidth()/2,
					canvas.getHeight()/2, freezeBlast, 100, 3, 160, false));
		}
	}

	private void whiteFade(Canvas canvas) {
		if (powerUp.getWhiteOut()) {
			powerUp.setWhiteOut(false);
			alpha = 255;
			alphaCounter = 0;
		}

		if (alpha > 0) {

			if (alphaCounter == 0) {
				fogPaint.setAlpha(0);

				canvasFilter = new LightingColorFilter(
						Color.rgb(240, 128, 128), 0);
				canvasPaint.setColorFilter(canvasFilter);

				int randomX = new Random().nextInt(canvas.getWidth());
				int randomY = new Random().nextInt(canvas.getHeight());
				temps.add(new TempSprite(temps, gameView, randomX, randomY,
						fire, 50, -999, true));
				randomX = new Random().nextInt(canvas.getWidth());
				randomY = new Random().nextInt(canvas.getHeight());
				temps.add(new TempSprite(temps, gameView, randomX, randomY,
						fire, 50, -999, true));
				randomX = new Random().nextInt(canvas.getWidth());
				randomY = new Random().nextInt(canvas.getHeight());
				temps.add(new TempSprite(temps, gameView, randomX, randomY,
						fire, 50, -999, true));
			}

			canvas.drawARGB(alpha, 255, 255, 255);
			alpha = alpha - alphaCounter++;
		}
	}

	private void updateAndDraw(Canvas canvas, long desiredFPSinMS,
			long startTime) {
		/*
		 * Steps (in any other order, it may crash) 1. Draw sprite 2. Remove
		 * sprite if its HP is 0 3. Draw wall
		 */

		// touchDetected and shotfired help control collisions with zombies.
		// shotfired prevents zombies from being killed in one shot unless its
		// intended to
		// touchDetected prevents shots fired moments earlier to collide with a
		// zombie later on.
		// TouchDetected is flagged by Main's input to true. It will go through
		// the update loop and defaults to false at the end

		boolean shotUsed = false;

		counterTotal = counterZombieWalker + counterZombieRunner
				+ counterZombieTank + counterEliteRage + counterEliteHyper;
		for (int i = getSpriteList().size() - 1; i >= 0; i--) {
			Sprite sprite = getSpriteList().get(i);
			if (counterTotal > 0 && !sprite.isAlive()) {
				rezMachine(sprite);
			}

			sprite.attackWallIfCollided();

			if (sprite.checkWallHitAnimation()) {
				playerWallHit();
			}

			if (exRoundRunDamage) {
				// damage sprite if its in range
				int collisionWidth = canvas.getWidth() / 4;
				int collisionHeight = canvas.getHeight() / 6;

				rectExplosiveRadius.set(xTouch - collisionWidth, yTouch
						- collisionHeight, xTouch + collisionWidth, yTouch
						+ collisionHeight);
				rectSpriteHitBox.set(sprite.getX(), sprite.getY(),
						sprite.getX() + sprite.getWidth(), sprite.getY()
								+ sprite.getHeight());

				if (rectExplosiveRadius.intersect(rectSpriteHitBox)) {
					sprite.addHP(-Briefing.currentEquippedGun.getDamage() * 2);
					if (rectExplosiveRadius.centerX() > rectSpriteHitBox
							.centerX()) {
						// move sprite to left
						sprite.setX(sprite.getX() - sprite.getWidth() / 5);

					} else {
						// move sprite to right
						sprite.setX(sprite.getX() + sprite.getWidth() / 5);
					}
					if (rectExplosiveRadius.centerY() > rectSpriteHitBox
							.centerY()) {
						// move sprite back
						sprite.setY(sprite.getY() - sprite.getHeight() / 5);

					} else {
						// move sprite forward
						sprite.setY(sprite.getY() + sprite.getHeight() / 5);
					}
				}

			}

			sprite.onDraw(canvas, desiredFPSinMS, startTime);

			if (CommandCenter.countSurvivors() > 0) {
				survBot1.update(sprite);
				if (sprite.getZombieID() != Sprite.ID_E_BANSHEE
						&& survBot1.checkIfFire(sprite)) {
					temps.add(new TempSprite(temps, gameView, survBot1
							.getBotX(), survBot1.getBotY(), shot, 50, 2, false));
					temps.add(new TempSprite(temps, gameView, survBot1
							.getBotX(), survBot1.getBotY(), bloodshot, 1, 3,
							160, false));
					playGunShot();
				}
				if (CommandCenter.countSurvivors() > 1) {
					survBot2.update(sprite);
					if (sprite.getZombieID() != Sprite.ID_E_BANSHEE
							&& survBot2.checkIfFire(sprite)) {
						temps.add(new TempSprite(temps, gameView, survBot2
								.getBotX(), survBot2.getBotY(), shot, 50, 2,
								false));
						temps.add(new TempSprite(temps, gameView, survBot2
								.getBotX(), survBot2.getBotY(), bloodshot, 1,
								3, 160, false));
						playGunShot();
					}
				}
			}

			// if target is too far, the damage is halved
			// if (!shotUsed && touchDetected
			// && sprite.isCollision(xTouch, yTouch)) {

			if (!shotUsed && touchDetected
					&& sprite.isCollision(xTouch, yTouch)) {
				touchDetected = false;

				// special effects
				if (sprite.getZombieID() == Sprite.ID_E_BANSHEE
						&& sprite.getMaxHP() == sprite.getHP()) {
					playBansheeScream();
					Main.stunPlayer(EliteBansheeZombie.HP_LOSS_INTERVAL);
					temps.add(new TempSprite(temps, gameView, sprite
							.getCenterX(), sprite.getCenterY(), soundwave, 50,
							10, 160, true));

				}

				if (Briefing.currentEquippedGun.getIceAmmo() > 0) {
					Briefing.currentEquippedGun.addIceAmmo(-1);
					sprite.slowSprite();
				}
				// end special effects

				Random random = new Random();
				// if out of 100, number is within STATcritChance, do double
				// damage!
				int critMultiplier = 1;
				int rangeMultiplier = 1;
				// calculate crit multiplier
				if (random.nextInt(100) + 1 <= baseCritChance
						+ CommandCenter.boostCrit) {
					critMultiplier = 2;
					// paints CRIT on the zombie
					if (baseCritChance > CommandCenter.boostCrit) {
						temps.add(new TempSprite(temps, gameView,
								sprite.getX(), sprite.getY(), crit, 1, 5, false));
					} else {
						temps.add(new TempSprite(temps, gameView,
								sprite.getX(), sprite.getY(), critHigh, 1, 5,
								false));
					}
				}
				// calculate range multiplier
				if (sprite.getY() + sprite.getHeight() > Main.damageDropOff) {
					rangeMultiplier = Briefing.currentEquippedGun
							.getRangeMultiplier();
				}
				temps.add(new TempSprite(temps, gameView, sprite.getX()
						+ sprite.getWidth() / 2, sprite.getY()
						+ sprite.getHeight() / 2, bloodshot, 1, 3, 160, false));

				// calculate final damage
				debugDamageDealt = critMultiplier * rangeMultiplier
						* Briefing.currentEquippedGun.getDamage();

				sprite.runDamage(debugDamageDealt);
				// Death animation of Zombies if it died

				// resets. touchDetected will be true once the player touches
				// screen
				// again

				// this prevents multiple sprites from being killed by one shot
				if (!Briefing.currentEquippedGun.getWeaponClass()
						.equalsIgnoreCase("Shotgun")) {
					shotUsed = true;
				}
			}

			// after all that's happened above, if the sprite has no hp left,
			// kill the sprite
			if (!sprite.isAlive() && !sprite.isDead) {
				Main.addKills(1);
				sprite.runDeath(canvas, temps);
				playCorpseExplosion();

			}

		}

		// if this is on, after the spriteLoop and running explosive damage on
		// sprites, it resets to false.
		exRoundRunDamage = false;

		// check collision with powerup
		if (touchDetected == true && powerUp.isCollision(xTouch, yTouch)) {
			touchDetected = false;
			powerUp.runDamage(100);
			if (!powerUp.isAlive()) {
				powerUp.runDeath();
				// run special effect
				if (powerUp.getItemType() == PowerUp.KILL_ALL) {
					playExplosion();
				} else if (powerUp.getItemType() == PowerUp.SLOW_ALL) {
					playFreeze();
				} else if (powerUp.getItemType() == PowerUp.AMMO_UP) {
					playAmmoUp();
				}
				powerUp.activateEffect(spriteList);
			}
		}

		// let's allow early stages of game to spawn one at a time. No looping
		// to
		// get the counters to zero
		if (counterTotal > 0) {
			if (counterZombieWalker > 0) {
				spriteList.add(new ZombieWalker(gameView, gameLevel));
				playBrains();
				counterZombieWalker--;
			}

			if (counterZombieRunner > 0) {
				spriteList.add(new ZombieRunner(gameView, gameLevel));
				counterZombieRunner--;
			}
			if (counterZombieTank > 0) {
				spriteList.add(new ZombieTank(gameView, gameLevel));
				counterZombieTank--;
			}
			if (counterEliteRage > 0) {
				spriteList.add(new EliteRageZombie(gameView, gameLevel));
				counterEliteRage--;
			}
			if (counterEliteHyper > 0) {
				spriteList.add(new EliteHyperZombie(gameView, gameLevel));
				counterEliteHyper--;
			}

			if (counterEliteBanshee > 0) {
				spriteList.add(new EliteBansheeZombie(gameView, gameLevel));
				counterEliteBanshee--;
			}
		}
		// if after going through the sprite loop, there are still counters with
		// value left....then we must create new zombies

		// draw wall if hp is left
		if (Main.STATSplayerHP > 0) {

			wall.onDraw(canvas);
			// this prevents out of bounds array exception

			if (Weapon.getListSize() > 1) {
				if (Briefing.currentEquippedGun == Weapon.getWeaponIndex(0)) {
					canvas.drawBitmap(weapon0,
							canvas.getWidth() / 2 - weapon0.getWidth() / 2,
							canvas.getHeight() - weapon0.getHeight() - 10, null);
				} else {
					canvas.drawBitmap(weapon1,
							canvas.getWidth() / 2 - weapon1.getWidth() / 2,
							canvas.getHeight() - weapon1.getHeight() - 10, null);
				}
			}

			// draw arrows here
			if (Weapon.getListSize() > 1) {
				uIChangeWeapon.draw(canvas, desiredFPSinMS, startTime);
				if (!Main.fenceTouch) {
					uIChangeWeapon.setXSpeed(0);
					uIChangeWeapon.setX(1);
					uIChangeWeapon.setY(canvas.getHeight()
							- changeWeapon.getHeight());
				} else {
					uIChangeWeapon.setXSpeed(25);
					if (uIChangeWeapon.getX() >= canvas.getWidth()
							- uIChangeWeapon.getWidth()) {
						uIChangeWeapon.setX((canvas.getWidth() / 10 * 1)
								- changeWeapon.getWidth() / 2);
					}
				}
			}

		}

		touchDetected = false;
	}

	private void drawTemp(Canvas canvas, long desiredFPSinMS, long startTime) {
		// TODO Auto-generated method stub
		for (int i = temps.size() - 1; i >= 0; i--) {
			temps.get(i).draw(canvas, desiredFPSinMS, startTime);
		}
	}

	private void drawBackground(Canvas canvas) {
		// TODO Auto-generated method stub

		Rect rd = new Rect();

		rd.left = rd.top = 0;
		rd.right = canvas.getWidth();
		rd.bottom = canvas.getHeight();

		// Save this for later, this creates some sort of psychodelic effect.
		// Apply the paint object to the background the canvas is drawing
		// Paint paint = new Paint();
		// paint.setAlpha(100);

		canvas.drawBitmap(street, null, rd, canvasPaint);

		// if (powerUp.getWhiteOut()) {
		// powerUp.setWhiteOut(false);
		// alpha = 255;
		// alphaCounter = 0;
		// }
		//
		// if (alpha > 0) {
		//
		// if (alphaCounter == 0) {
		//
		// canvasFilter = new LightingColorFilter(Color.rgb(240, 128, 128), 0);
		// canvasPaint.setColorFilter(canvasFilter);
		//
		// int randomX = new Random().nextInt(canvas.getWidth());
		// int randomY = new Random().nextInt(canvas.getHeight());
		// temps.add(new TempSprite(temps, gameView, randomX, randomY,
		// fire, 50, -999));
		// randomX = new Random().nextInt(canvas.getWidth());
		// randomY = new Random().nextInt(canvas.getHeight());
		// temps.add(new TempSprite(temps, gameView, randomX, randomY,
		// fire, 50, -999));
		// randomX = new Random().nextInt(canvas.getWidth());
		// randomY = new Random().nextInt(canvas.getHeight());
		// temps.add(new TempSprite(temps, gameView, randomX, randomY,
		// fire, 50, -999));
		// }
		//
		// canvas.drawARGB(alpha, 255, 255, 255);
		// alpha = alpha - alphaCounter++;
		// }

		// Paint paint = new Paint();
		// ColorFilter filter = new LightingColorFilter(Color.GRAY, 0);
		// paint.setColorFilter(filter);
		// int tileColumns = canvas.getWidth() / grass.getWidth();
		// int tileRows = canvas.getHeight() / grass.getHeight();
		// for (int i = tileColumns; i >= 0; i--) {
		// for (int z = tileRows; z >= 0; z--) {
		// canvas.drawBitmap(grass, i * grass.getWidth(), z
		// * grass.getHeight(), paint);
		// }
		// }
	}

	public void setSpriteList(List<Sprite> spriteList) {
		this.spriteList = spriteList;
	}

	public List<Sprite> getSpriteList() {
		return spriteList;
	}

	public void bombKillsAll(float eventX, float eventY) {
		// this flags all zombies to die in the render loop
		bombKillsAll = true;
		temps.add(new TempSprite(temps, gameView, eventX, eventY, explosion,
				35, 30, false));
	}

	public void suicideBomb(float xSuicide, float ySuicide) {
		this.xSuicide = xSuicide;
		this.ySuicide = ySuicide;
	}

	// turn into inner class
	public void rezMachine(Sprite sprite) {
		// TODO Auto-generated method stub

		if (counterZombieWalker > 0
				&& sprite.getZombieID() == Sprite.ID_ZWALKER) {
			sprite.revive();
			playBrains();
			counterZombieWalker--;
		}
		if (counterZombieRunner > 0
				&& sprite.getZombieID() == Sprite.ID_ZRUNNER) {
			sprite.revive();
			counterZombieRunner--;
		}
		if (counterZombieTank > 0 && sprite.getZombieID() == Sprite.ID_ZTANK) {
			sprite.revive();
			counterZombieTank--;
		}
		if (counterEliteRage > 0 && sprite.getZombieID() == Sprite.ID_E_RAGE) {
			sprite.revive();
			counterEliteRage--;
		}
		if (counterEliteHyper > 0 && sprite.getZombieID() == Sprite.ID_E_HYPER) {
			sprite.revive();
			counterEliteHyper--;
		}
	}

	public boolean touchWall(float eventX, float eventY) {
		return wall.touchWall(eventX, eventY);
	}

	// this runs a sprite loop. Loops are expensive and should be used sparingly
	public boolean checkListForLive() {

		boolean listHasLive = false;
		for (int i = getSpriteList().size() - 1; i >= 0; i--) {
			Sprite sprite = getSpriteList().get(i);
			if (sprite.isAlive()) {
				listHasLive = true;
				break;
			}
		}
		return listHasLive;
	}

	// public void debugNoKillSprite() {
	// // TODO Auto-generated method stub
	// debugKillSprite = false;
	//
	// }
}
