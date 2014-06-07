package com.JohnQ1216.ScavengerZombieDefense;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.CommandCenter.LootGenerate;
import com.JohnQ1216.ScavengerZombieDefense.Main.GameView;
import com.QuachProjectZombie.R;

import data.Food;
import data.WiFiScanReceiver;

import Weapon.Weapon;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

//prepares user for next fight, and stores any constant data
public class Briefing extends Activity implements OnClickListener {
	// wifi switch must be turned on or app will crash

	/**
	 * Briefing clears the keptWeaponList. Player will add weapons to the list
	 * by buttons as long as there's ammo. Weapons will not be added to list
	 * until game starts
	 */

	private Button bBegin, bSelectPistol, bSelectShotgun, bSelectSMG,
			bSelectRifle;
	private TextView tvReport;
	public static int gameLevel = 0;
	public static Weapon currentEquippedGun;

	private boolean selectedPistol, selectedShotgun, selectedSMG,
			selectedRifle;
	private int selectionCounter;

	@Override
	protected void onCreate(Bundle TravisLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(TravisLoveBacon);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.report);

		selectedPistol = selectedShotgun = selectedSMG = selectedRifle = false;

		selectionCounter = 0;

		tvReport = (TextView) findViewById(R.id.tvReport);

		bSelectPistol = (Button) findViewById(R.id.bSelectPistol);
		bSelectShotgun = (Button) findViewById(R.id.bSelectShotgun);
		bSelectSMG = (Button) findViewById(R.id.bSelectSMG);
		bSelectRifle = (Button) findViewById(R.id.bSelectRifle);

		bSelectPistol.setText(CommandCenter.pistol.getTotalAmmo() + "");
		bSelectShotgun.setText(CommandCenter.shotgun.getTotalAmmo() + "");
		bSelectSMG.setText(CommandCenter.smg.getTotalAmmo() + "");
		bSelectRifle.setText(CommandCenter.rifle.getTotalAmmo() + "");

		tvReport.setText(LootGenerate.getReport() + "");

		bSelectPistol.setOnClickListener(this);
		bSelectShotgun.setOnClickListener(this);
		bSelectSMG.setOnClickListener(this);
		bSelectRifle.setOnClickListener(this);

		bBegin = (Button) findViewById(R.id.bBegin);
		bBegin.setOnClickListener(this);
		Weapon.clearList();
//		
//		if (CommandCenter.shotgun.getTotalAmmo()<=0){
//			 bSelectShotgun.setVisibility(View.GONE);
//		}
//		
//		if (CommandCenter.smg.getTotalAmmo()<=0){
//			 bSelectSMG.setVisibility(View.GONE);
//		}
//		
//		if (CommandCenter.rifle.getTotalAmmo()<=0){
//			 bSelectRifle.setVisibility(View.GONE);
//		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bBegin) {
			if (!selectedPistol && !selectedShotgun && !selectedSMG && !selectedRifle) {
				Weapon.addWeapon(CommandCenter.pistol);
			} else {
				if (selectedPistol) {
					Weapon.addWeapon(CommandCenter.pistol);
				}
				if (selectedShotgun) {
					Weapon.addWeapon(CommandCenter.shotgun);
				}
				if (selectedSMG) {
					Weapon.addWeapon(CommandCenter.smg);
				}
				if (selectedRifle) {
					Weapon.addWeapon(CommandCenter.rifle);
				}
			}

			currentEquippedGun = Weapon.getWeaponListIndex(0);
			ActivityLoader.loadMain(this);
			finish();
		}
		if (v.getId() == R.id.bSelectPistol) {
			if (!selectedPistol && selectionCounter < 2) {
				selectedPistol = true;
				bSelectPistol.setBackgroundColor(Color.YELLOW);
				selectionCounter++;
			} else if (selectedPistol) {
				selectedPistol = false;
				bSelectPistol.setBackgroundDrawable(getResources().getDrawable(
						android.R.drawable.btn_default));
				selectionCounter--;
			}

		}
		if (v.getId() == R.id.bSelectShotgun) {
			if (!selectedShotgun && selectionCounter < 2
					&& CommandCenter.shotgun.getTotalAmmo() > 0) {
				selectedShotgun = true;
				bSelectShotgun.setBackgroundColor(Color.YELLOW);
				selectionCounter++;
			} else if (selectedShotgun) {
				selectedShotgun = false;
				bSelectShotgun.setBackgroundDrawable(getResources()
						.getDrawable(android.R.drawable.btn_default));
				selectionCounter--;
			}
		}
		if (v.getId() == R.id.bSelectSMG) {
			if (!selectedSMG && selectionCounter < 2
					&& CommandCenter.smg.getTotalAmmo() > 0) {
				selectedSMG = true;
				bSelectSMG.setBackgroundColor(Color.YELLOW);
				selectionCounter++;
			} else if (selectedSMG) {
				selectedSMG = false;
				bSelectSMG.setBackgroundDrawable(getResources().getDrawable(
						android.R.drawable.btn_default));
				selectionCounter--;
			}
		}

		if (v.getId() == R.id.bSelectRifle) {
			if (!selectedRifle && selectionCounter < 2
					&& CommandCenter.rifle.getTotalAmmo() > 0) {
				selectedRifle = true;
				bSelectRifle.setBackgroundColor(Color.YELLOW);
				selectionCounter++;
			} else if (selectedRifle) {
				selectedRifle = false;
				bSelectRifle.setBackgroundDrawable(getResources().getDrawable(
						android.R.drawable.btn_default));
				selectionCounter--;
			}
		}

	}

	public static int getGameLevel() {
		// TODO Auto-generated method stub
		return gameLevel;
	}

	public static class ActivityLoader {

		public static void loadCommandCenter(GameView gameView) {
			// scouter uses the Main intent filter
			Intent intent = new Intent("android.intent.action.MAIN");
			Context myContext = gameView.getContext();
			intent.setClass(myContext, CommandCenter.class);
			myContext.startActivity(intent);

		}

		public static void loadBriefing(Activity activity) {
			Intent intent = new Intent("android.intent.action.BRIEFING");
			activity.startActivity(intent);
			gameLevel++;
		}

		public static void loadMain(Activity activity) {
			Intent intent = new Intent("android.intent.action.MAINGAME");
			activity.startActivity(intent);
		}
		
		public static void startOver(GameView gameView) {
			Intent intent = new Intent("android.intent.action.MAIN");
			Context myContext = gameView.getContext();
			intent.setClass(myContext, SplashScreen.class);
			myContext.startActivity(intent);
		}

	}
}
