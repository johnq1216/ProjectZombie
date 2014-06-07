package com.JohnQ1216.ScavengerZombieDefense;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Briefing.ActivityLoader;
import com.QuachProjectZombie.R;

import data.Food;
import data.SpawnScript;
import graphics.SpriteFactory;
import threadAndTimers.Timer;
import Weapon.Weapon;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class Main extends Activity implements OnTouchListener {

	/**
	 * LOG
	 * 
	 * 
	 * Paint New Record on Main
	 * 
	 * Dramatic music upon victory or death
	 * 
	 * Find out why report is bugged. Why is gun unselectable despite report
	 * saying there's ammo found
	 * 
	 * Fix victory screen and its wording
	 * 
	 * Soundwave graphic must be bound by sprite's location
	 * 
	 * Wall will flash a different color when hit
	 * 
	 * 
	 * 
	 * Eliminate all sprites that are outside the boundaries
	 * 
	 * Change the name of SpriteZombieBase variable (located in Factory) called
	 * sprite to zombie to avoid confusion.
	 * 
	 * You can run out of "ammo" during the game. You then have unlimited ammo
	 * but reload takes longer. After the round, you won't be able to select the
	 * same gun again unless you found some more ammo. It appears I did not
	 * implement any "default gun" to take the place of the weapon that runs out
	 * of ammo
	 */

	private final float PREVAIL_WIDTH = 320f;
	private final float PREVAIL_HEIGHT = 480f;

	private boolean reloadInProcess, gameOver, gunFiring, debug = false,
			weaponSwitch = false, crosshairOn = false;

	// this is how long to stun player. Default value is 0
	private static int stunTime = 0;
	private Timer reloadTime, timerForTriggerHeld;
	public static Timer countdown;
	private GameView gameView;

	// STATSplayerHP are used in SpriteFactory
	public static int STATSplayerHP, STATSmaxHP, damageDropOff;

	// private SoundPool sp;
	private static int kills, rpm = 700, screenHeight = 1, screenWidth = 1;
	private SpriteFactory factory;
	private float crosshairX, crosshairY, eventX, eventY;
	public static boolean fenceTouch = false;

	public long reloadStarTime;

	private static boolean wallHitSendWarning = false;


	
	SharedPreferences someData;
	private int loadSavedInt;
	
	// MediaPlayer ourSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		SplashScreen.SoundBox.stopSong();

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		gameView = new GameView(this);
		gameView.setOnTouchListener(this);
		timerForTriggerHeld = new Timer((long) 1000 / (rpm / 60));
		setContentView(gameView);

		factory = new SpriteFactory(this, gameView, Briefing.getGameLevel());

		reloadInProcess = gameOver = false;
		// parameters don't matter. This just creates a default pistol
		//
		// ourSong = MediaPlayer.create(this, R.raw.belltoll);
		// ourSong.start();
		devConfig();
	}

	private void devConfig() {
		// the numbers for STATS variables are default values
		restoreHP();
		countdown = new Timer(30);
		kills = 0;
	}

	public static void restoreHP() {
		STATSplayerHP = STATSmaxHP = 10 + (CommandCenter.getBonusPermanentHP());
	}

	public static void addKills(int x) {
		kills = kills + x;
	}

	public static void stunPlayer(long n) {
		stunTime = (int) n;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		gameView.pause();
		countdown.onPauseTime();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gameView.resume();
		countdown.onResumeTime();
	}

	public boolean onTouch(View v, MotionEvent event) {
		// gameOverSlide(event);
		shotFired(event);
		touchChangeWeapons(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			debug = true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			debug = false;
		}
		return true;

	}

	// if touchDown is at bottom left, player might want to switch weapons
	private void touchChangeWeapons(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				// to prevent game from crashing out of bounds array error
				&& Weapon.getListSize() > 1
				&& event.getX() <= screenWidth * .30
				&& event.getY() >= screenHeight - screenHeight * .10) {
			fenceTouch = true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP && fenceTouch == true) {
			if (event.getX() >= screenWidth - screenWidth * .30
					&& event.getY() >= screenHeight - screenHeight * .10) {
				if (Briefing.currentEquippedGun
						.equals(Weapon.getWeaponIndex(0))) {
					Briefing.currentEquippedGun = Weapon.getWeaponIndex(1);
				} else if (Briefing.currentEquippedGun.equals(Weapon
						.getWeaponIndex(1))) {
					Briefing.currentEquippedGun = Weapon.getWeaponIndex(0);
				}
			}
			fenceTouch = false;
		}
	}

	private boolean shotFired(MotionEvent event) {
		eventX = event.getX();
		eventY = event.getY();
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN
					&& STATSplayerHP >= 0
					&& Briefing.currentEquippedGun.getTotalAmmoInClip() > 0
					&& !factory.touchWall(eventX, eventY)) {
				gunFiring = true;
			}

			if (Briefing.currentEquippedGun.getTotalAmmoInClip() <= 0) {
				factory.playGunDryFire();
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				gunFiring = false;
			}
			return true;
		}

	}

	// private boolean gameOverSlide(MotionEvent event) {
	// if (STATSplayerHP <= 0) {
	// float initialX = 0;
	// float initialY = 0;
	// float deltaX = 0;
	// float deltaY = 0;
	// // This prevents touchscreen events from flooding the main thread
	// synchronized (event) {
	// try {
	// // Waits 16ms.
	// event.wait(16);
	//
	// // when user touches the screen
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// // reset deltaX and deltaY
	// deltaX = deltaY = 0;
	//
	// // get initial positions
	// initialX = event.getRawX();
	// initialY = event.getRawY();
	// }
	//
	// // when screen is released
	// if (event.getAction() == MotionEvent.ACTION_UP) {
	// deltaX = event.getRawX() - initialX;
	// deltaY = event.getRawY() - initialY;
	//
	// Math.abs(deltaX);
	// Math.abs(deltaY);
	//
	// // swipped up
	// if (deltaX >= 350 || deltaY >= 350) {
	// gameOver = false;
	// factory.clearSprites();
	// devConfig();
	// }
	//
	// return true;
	// }
	// }
	//
	// catch (InterruptedException e) {
	// return true;
	// }
	// }
	// }
	// return true;
	//
	// }

	public class GameView extends SurfaceView implements Runnable {

		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false;
		static final long FPS = 20;
		int waitTotal = 100, wallHitWarning = 0;
		Bitmap victoryScreen, defeatScreen;
		Paint endRoundPaint;
		int endRoundAlpha = 0;


		// /view takes too much processing
		// /invalidate() is too much, need threads
		public GameView(Context context) {
			// TODO Auto-generated constructor stub
			super(context);
			ourHolder = getHolder();
			victoryScreen = BitmapFactory.decodeResource(getResources(),
					R.drawable.victory);
			defeatScreen = BitmapFactory.decodeResource(getResources(),
					R.drawable.defeat);
			endRoundPaint = new Paint();
		}

		private void victoryOrDefeat(Canvas canvas) {
			// VICTORY!!
			if (countdown.getSeconds() <= 0 && STATSplayerHP > 0
					&& !factory.checkListForLive()) {
				Paint textPaint = new Paint();
				textPaint.setColor(Color.RED);
				textPaint.setTextAlign(Align.CENTER);
				textPaint.setTextSize(20);
				waitTotal--;
				int nextRound = Briefing.getGameLevel() + 1;
				Rect rd = new Rect();

				rd.left = rd.top = 0;
				rd.right = canvas.getWidth();
				rd.bottom = canvas.getHeight();

				endRoundPaint.setAlpha(endRoundAlpha);

				canvas.drawBitmap(victoryScreen, null, rd, endRoundPaint);

				if (endRoundAlpha <= 240) {
					endRoundAlpha = endRoundAlpha + 5;
				} else {
					endRoundAlpha = 255;
				}
				
				

				// canvas.drawText("VICTORY!!!", canvas.getWidth() / 2,
				// canvas.getHeight() / 3, textPaint);
				canvas.drawText(kills + " zombies exterminated!!!",
						canvas.getWidth() / 2, canvas.getHeight() / 2,
						textPaint);
				canvas.drawText(CommandCenter.getTotalKills() + kills
						+ " kills total", canvas.getWidth() / 2,
						canvas.getHeight() - canvas.getHeight() / 3, textPaint);
				canvas.drawText("Round " + nextRound + " will begin",
						canvas.getWidth() / 2,
						canvas.getHeight() - canvas.getHeight() / 5, textPaint);
				if (waitTotal <= 0) {
					waitTotal = 200;

					CommandCenter.addTotalKills(kills);
					someData = getSharedPreferences(CommandCenter.filename,
							0);
					SharedPreferences.Editor editor = someData.edit();
					editor.putInt("StoredScore", CommandCenter.getTotalKills());
					editor.commit();
					
					ActivityLoader.loadCommandCenter(this);

					finish();
				}
				factory.clearSprites();

			}
			// MISSION FAILED
			if (STATSplayerHP <= 0) {
				Paint textPaint = new Paint();
				textPaint.setColor(Color.RED);
				textPaint.setTextAlign(Align.CENTER);
				textPaint.setTextSize(20);
				waitTotal--;
				int nextRound = Briefing.getGameLevel() + 1;
				Rect rd = new Rect();

				rd.left = rd.top = 0;
				rd.right = canvas.getWidth();
				rd.bottom = canvas.getHeight();

				endRoundPaint.setAlpha(endRoundAlpha);

				canvas.drawBitmap(defeatScreen, null, rd, endRoundPaint);

				if (endRoundAlpha <= 240) {
					endRoundAlpha = endRoundAlpha + 5;
				} else {
					endRoundAlpha = 255;
				}

				// canvas.drawText("DEFEAT!!", canvas.getWidth() / 2,
				// canvas.getHeight() / 3, textPaint);
				canvas.drawText(CommandCenter.getTotalKills() + kills
						+ " kills total", canvas.getWidth() / 2,
						canvas.getHeight() / 2 + canvas.getHeight() / 8,
						textPaint);
				// canvas.drawText(CommandCenter.getTotalKills() + kills
				// + " kills total", canvas.getWidth() / 2,
				// canvas.getHeight() / 4, textPaint);
				if (waitTotal <= 0) {

					waitTotal = 200;
					Briefing.gameLevel = 0;
					CommandCenter.addTotalKills(kills);

					someData = getSharedPreferences(CommandCenter.filename, 0);
					loadSavedInt = someData.getInt("StoredScore", 0);

					// if total kills is greater than stored kills
					if (loadSavedInt < CommandCenter.getTotalKills()) {

						
						someData = getSharedPreferences(CommandCenter.filename,
								0);
						SharedPreferences.Editor editor = someData.edit();
						editor.putInt("StoredScore", CommandCenter.getTotalKills());
						editor.commit();
						factory.playNewRecord();
						//draw "NEW RECORD!" as a blood paint marker in Main
						
						
						CommandCenter.resetTotalKills();
					}
					ActivityLoader.startOver(this);

					finish();
				}
				factory.clearSprites();

				gameOver = true;
			}
		}

		private void countdownAndHP(Canvas canvas) {
			float textSizeX = 20f;
			float scaledTextSizeX = canvas.getWidth()
					* (textSizeX / PREVAIL_WIDTH);

			Paint textPaint = new Paint();
			textPaint.setColor(Color.YELLOW);
			textPaint.setAlpha(100);
			textPaint.setShadowLayer(2, 2, 2, Color.GRAY);
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTextSize(scaledTextSizeX);
			canvas.drawText("Round " + Briefing.getGameLevel(),
					canvas.getWidth() / 2, canvas.getHeight() / 5, textPaint);

			if (countdown.getSeconds() > 0) {
				textPaint.setColor(Color.YELLOW);
				textPaint.setAlpha(100);
				textPaint.setShadowLayer(2, 2, 2, Color.GRAY);
				textPaint.setTextAlign(Align.CENTER);
				textPaint.setTextSize(30);
				canvas.drawText(countdown.getSeconds() + "",
						canvas.getWidth() / 4, canvas.getHeight() / 4,
						textPaint);
			}

			if (STATSplayerHP > 0) {

				// draws player HP

				if (wallHitSendWarning) {
					wallHitSendWarning = false;
					wallHitWarning = 255;
				}

				if (wallHitWarning > 0 || STATSplayerHP < STATSmaxHP / 2) {
					textPaint.setColor(Color.RED);
					if (STATSplayerHP < STATSmaxHP / 2) {
						wallHitWarning = wallHitWarning - 20;
					} else {
						wallHitWarning = wallHitWarning - 10;
					}

					textPaint.setShadowLayer(0, 0, 0, 0);
					textPaint.setAlpha(wallHitWarning);
					textPaint.setTextAlign(Align.CENTER);
					textPaint.setTextSize(scaledTextSizeX * 5);

					canvas.drawText(STATSplayerHP + "", canvas.getWidth() / 2,
							canvas.getHeight() / 2, textPaint);
					if (STATSplayerHP < STATSmaxHP / 2 && wallHitWarning <= 0) {
						wallHitWarning = 255;
					}
				}

				textPaint.setColor(Color.YELLOW);
				textPaint.setAlpha(100);
				textPaint.setShadowLayer(2, 2, 2, Color.BLACK);
				textPaint.setTextAlign(Align.CENTER);
				textPaint.setTextSize(scaledTextSizeX);
				canvas.drawText(
						"" + Briefing.currentEquippedGun.getTotalAmmoInClip()
								+ "/"
								+ Briefing.currentEquippedGun.getTotalAmmo(),
						canvas.getWidth() - canvas.getWidth() / 11,
						canvas.getHeight() - canvas.getHeight() / 20, textPaint);

				// draws RELOADING prompt
				textPaint.setColor(Color.GRAY);
				if (reloadInProcess) {
					canvas.drawText("RELOADING!!", canvas.getWidth() / 2,
							canvas.getHeight() / 2 + canvas.getHeight() / 4,
							textPaint);
				}
				if (debug) {
					canvas.drawText(eventX + " " + eventY,
							canvas.getWidth() / 2, canvas.getHeight() / 2
									+ canvas.getHeight() / 4, textPaint);
				}

				// draws damage dealt to last zombie
				textPaint.setColor(Color.BLUE);
				// canvas.drawText(Math.abs(factory.debugDamageDealt)
				// + " Damage!!", canvas.getWidth() / 2
				// + canvas.getWidth() / 4 - 20, canvas.getHeight() / 2,
				// textPaint);

				canvas.drawText(Math.abs(factory.debugDamageDealt)
						+ " Damage!!",
						canvas.getWidth() / 2 + canvas.getWidth() / 4 - 20,
						canvas.getHeight() / 2, textPaint);

				// this paints a line on the map where damage drops
				textPaint.setColor(Color.RED);
				damageDropOff = canvas.getHeight()
						- Briefing.currentEquippedGun.getRange()
						* (canvas.getHeight() / 4);
				for (int x = 10; x > 0; x--) {
					canvas.drawCircle(canvas.getWidth() / 10 * x,
							damageDropOff, 2, textPaint);
				}

			}
		}

		public void pause() {
			isRunning = false;
			// ourSong.release();
			while (true) {
				// stops thread and allows it to die
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;

		}

		public void resume() {
			isRunning = true;
			ourThread = new Thread(this);
			ourThread.start();
		}

		private void checkReload() {
			// TODO Auto-generated method stub

			if (Briefing.currentEquippedGun.getTotalAmmoInClip() <= 0) {
				if (!reloadInProcess) {
					reloadInProcess = true;
					reloadStarTime = System.currentTimeMillis();
				} else {
					if (Briefing.currentEquippedGun.getTotalAmmo() > 0
							&& System.currentTimeMillis() - reloadStarTime >= Briefing.currentEquippedGun
									.getReloadTime()
									/ CommandCenter.boostReload) {
						Briefing.currentEquippedGun.reload();
						factory.playGunCocking();
						reloadInProcess = false;
					}
					// Reload longer if out of ammo
					else if (System.currentTimeMillis() - reloadStarTime >= 2
							* Briefing.currentEquippedGun.getReloadTime()
							/ CommandCenter.boostReload) {
						Briefing.currentEquippedGun.freeReload();
						reloadInProcess = false;
						factory.playGunCocking();
					}

				}
			}
		}

		public void run() {

			long desiredFPSinMS = 1000 / FPS;
			long startTime;

			while (isRunning) {
				if (!ourHolder.getSurface().isValid())
					continue;

				checkReload();
				startTime = System.currentTimeMillis();

				// check if player is stunned
				if (stunTime != 0) {
					timerForTriggerHeld.setMS(stunTime);
					stunTime = 0;
				}

				// if gun is automatic, it will fire again after a certain ms
				if (gunFiring && STATSplayerHP >= 0
						&& Briefing.currentEquippedGun.getTotalAmmoInClip() > 0
						&& timerForTriggerHeld.getMS() <= 0) {
					Briefing.currentEquippedGun.subtractBullet();

					if (Briefing.currentEquippedGun.getWeaponClass()
							.equalsIgnoreCase("Pistol")) {
						factory.playGunShot();
					} else if (Briefing.currentEquippedGun.getWeaponClass()
							.equalsIgnoreCase("Shotgun")) {
						factory.playShotgun();
						timerForTriggerHeld.setMS(700);
					} else if (Briefing.currentEquippedGun.getWeaponClass()
							.equalsIgnoreCase("SMG")) {
						factory.playSMGShot();
						timerForTriggerHeld.setMS((long) 1000 / (rpm / 60));
					} else if (Briefing.currentEquippedGun.getWeaponClass()
							.equalsIgnoreCase("Rifle")) {
						factory.playRifleShot();
						timerForTriggerHeld.setMS((long) 1000 / (rpm / 60));
					}
					int random = new Random().nextInt(5)+1;
					if(random == 3){
						factory.playRicochet();
					}
					

					factory.touchDetected(eventX, eventY);

					// if the gun is not automatic, this stops firing
					if (!Briefing.currentEquippedGun.checkAuto()) {
						gunFiring = false;
					}
				}

				// // SPAWN zombies while timer is going
				// if (countdown.getSeconds() >= 0
				// && (spawnTimer.getSeconds() >= 1)) {
				// spawnTimer.reset();
				// RoundList.spawnZombies(gameView, Briefing.getGameLevel(),
				// factory.getSpriteList(), countdown);
				// }

				Canvas canvas = ourHolder.lockCanvas();
				screenWidth = canvas.getWidth();
				screenHeight = canvas.getHeight();
				factory.render(canvas, desiredFPSinMS, startTime, STATSplayerHP);
				countdownAndHP(canvas);
				victoryOrDefeat(canvas);
				ourHolder.unlockCanvasAndPost(canvas);

				long updateLag = System.currentTimeMillis() - startTime;
				long sleepTime = desiredFPSinMS - updateLag;
				try {
					/*
					 * 20 FPS means a task is run every 50 MS. Ideally, this
					 * should run every 50 MS.
					 * 
					 * However, the computer might run things too fast. So if
					 * the computer runs its task ahead of schedule, the game
					 * must sleep the rest of the difference to meet the 50 MS
					 * interval.
					 * 
					 * Sometimes, the lag gets bigger than the desired FPS of 50
					 * MS. Since the result sleep value from 50 MS minus Lag
					 * will go into negative and you cannot sleep negative
					 * values, whenever this happens, it'll force a sleep of 5
					 * MS.
					 */
					if (sleepTime > 0)
						Thread.sleep(sleepTime);
					else
						// if the updateLag is big enough to cause sleeptime to
						// go
						// into negative
						Thread.sleep(5);
				} catch (Exception e) {
				}

			}

		}
	}

	public static void bleedHP(int i) {
		STATSplayerHP = STATSplayerHP + i;
		wallHitSendWarning = true;

	}

}
