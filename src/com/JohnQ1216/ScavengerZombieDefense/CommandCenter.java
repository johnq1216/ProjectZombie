package com.JohnQ1216.ScavengerZombieDefense;

import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Briefing.ActivityLoader;
import com.QuachProjectZombie.R;

import Weapon.*;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//prepares user for next fight, and stores any constant data
public class CommandCenter extends Activity implements OnClickListener {
	// wifi switch must be turned on or app will crash

	public static String filename = "StoredScore";
	SharedPreferences someData;

	private static final String TAG = "WiFiDemo";
	private static boolean wallMatFoundForReport;
	Button bBeginScavenging, bSearchWeapon, bSearchWall, bSearchSurvivors;
	TextView tvSurvivors, tvBuildings, tvPistolAmmo, tvShotgunAmmo, tvSmgAmmo,
			tvRifleAmmo;
	int effortSearchWeapon = 0, effortSearchWall, effortSearchSurvivors;
	// Incrementation of buildings moved to Briefings to avoid player abusing
	// home
	// button and calling Oncreate over and over
	// didntComeBack will be subtracted from survivors
	private static int buildings, survivorsTotal, survivorsLost,
			reportSurvivorsFound, reportSurvivorsLost;

	static Pistol pistol = new Pistol();
	static Shotgun shotgun = new Shotgun();
	static SMG smg = new SMG();
	static Rifle rifle = new Rifle();
	// every round starts with fixed hp plus this bonusHP. This is permanent but
	// any added HP is subject to diminishing returns
	private static int bonusPermanentHP = 0;

	public static boolean newGame = true;
	private static int totalKills;

	public static int boostCrit, boostReload, boostTabletDrop;

	@Override
	protected void onCreate(Bundle TravisLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(TravisLoveBacon);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		
//		//put in main instead.....to count current kills in loss
//		someData = getSharedPreferences(filename, 0);
//		SharedPreferences.Editor editor = someData.edit();
//		editor.putInt("StoredScore", totalKills);
//		editor.commit();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.commandcenter);

		wallMatFoundForReport = false;
		survivorsLost = reportSurvivorsFound = reportSurvivorsLost = reportSurvivorsFound = 0;

		// if (newGame) {
		// newGame = false;
		// Weapon.addDummyPistol();
		// }

		buildings = 4 + survivorsTotal;

		// Setup UI
		tvSurvivors = (TextView) findViewById(R.id.tvSurvivors);
		tvSurvivors.setText(survivorsTotal + "");

		tvBuildings = (TextView) findViewById(R.id.tvBuildings);
		tvBuildings.setText(buildings + "");

		tvPistolAmmo = (TextView) findViewById(R.id.tvPistolAmmo);
		tvPistolAmmo.setText(pistol.getTotalAmmo() + "");

		tvSmgAmmo = (TextView) findViewById(R.id.tvSmgAmmo);
		tvSmgAmmo.setText(smg.getTotalAmmo() + "");

		tvShotgunAmmo = (TextView) findViewById(R.id.tvShotgunAmmo);
		tvShotgunAmmo.setText(shotgun.getTotalAmmo() + "");

		tvRifleAmmo = (TextView) findViewById(R.id.tvRifleAmmo);
		tvRifleAmmo.setText(rifle.getTotalAmmo() + "");

		// Setup UI Buttons
		bBeginScavenging = (Button) findViewById(R.id.bBeginScavenging);
		bBeginScavenging.setOnClickListener(this);

		bSearchWeapon = (Button) findViewById(R.id.bSearchWeapons);
		bSearchWeapon.setOnClickListener(this);

		bSearchWall = (Button) findViewById(R.id.bSearchWall);
		bSearchWall.setOnClickListener(this);

		bSearchSurvivors = (Button) findViewById(R.id.bSearchSurvivors);
		bSearchSurvivors.setOnClickListener(this);

		boostCrit = boostTabletDrop = 0;
		// this must be 1 or it will generate a divide by zero error
		boostReload = 1;
	}

	@Override
	// this method is called by the startActivity() method.
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	public static int getBonusPermanentHP() {
		return bonusPermanentHP;
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.bBeginScavenging) {
			searchForLoot();
			LootGenerate.convertWeaponPoints();
			LootGenerate.convertWallPoints();
			LootGenerate.killSurvivors();
			LootGenerate.convertSurvivorPoints();
			LootGenerate.createReport();
			ActivityLoader.loadBriefing(this);
		}
		if (arg0.getId() == R.id.bSearchWeapons) {
			if (buildings > 0) {
				buildings--;
				effortSearchWeapon++;
			} else {
				buildings = effortSearchWeapon;
				effortSearchWeapon = 0;
			}
			bSearchWeapon.setText(effortSearchWeapon + "");
			tvBuildings.setText(buildings + "");
		}
		if (arg0.getId() == R.id.bSearchWall) {
			if (buildings > 0) {
				buildings--;
				effortSearchWall++;
			} else {
				buildings = effortSearchWall;
				effortSearchWall = 0;
			}
			bSearchWall.setText(effortSearchWall + "");
			tvBuildings.setText(buildings + "");
		}
		if (arg0.getId() == R.id.bSearchSurvivors) {
			if (buildings > 0) {
				buildings--;
				effortSearchSurvivors++;
			} else {
				buildings = effortSearchSurvivors;
				effortSearchSurvivors = 0;
			}
			bSearchSurvivors.setText(effortSearchSurvivors + "");
			tvBuildings.setText(buildings + "");
		}
	}

	public void searchForLoot() {
		Random random = new Random();
		while (effortSearchWeapon > 0) {
			effortSearchWeapon--;
			LootGenerate.runLootGenerator(25, 7, 7);
			int chanceOfDeath = random.nextInt(100) + 1;
			if (chanceOfDeath <= 20) {
				survivorsLost++;
			}
		}
		while (effortSearchWall > 0) {
			effortSearchWall--;
			LootGenerate.runLootGenerator(7, 25, 7);
			int chanceOfDeath = random.nextInt(100) + 1;
			if (chanceOfDeath <= 20) {
				survivorsLost++;
			}
		}
		

		while (effortSearchSurvivors > 0) {
			effortSearchSurvivors--;
			LootGenerate.runLootGenerator(7, 7, 30);
			int chanceOfDeath = random.nextInt(100) + 1;
			if (chanceOfDeath <= 20) {
				survivorsLost++;
			}
		}

	}

	public static int countSurvivors() {
		// TODO Auto-generated method stub
		return survivorsTotal;
	}

	public static class LootGenerate {

		private static String report;
		private static Random random = new Random();
		// these increment as loot...but more processing is needed to determine
		// what kind of loot.
		private static int weaponLoot, wallLoot, survivorLoot;
		private static int convertedHP;
		private static final int PISTOL = 0, SHOTGUN = 1, SMG = 2, RIFLE = 3;
		private static int dice;
		private static int reportPistol, reportShotgun, reportSMG, reportRifle;

		/**
		 * 
		 * @param weaponChance
		 * @param wallChance
		 * @param survivorChance
		 */
		private static void runLootGenerator(int weaponChance, int wallChance,
				int survivorChance) {
			dice = random.nextInt(100) + 1;
			if (dice <= weaponChance) {
				weaponLoot++;
			}
			dice = random.nextInt(100) + 1;
			if (dice <= wallChance) {
				wallLoot++;
			}
			dice = random.nextInt(100) + 1;
			if (dice <= survivorChance) {
				survivorLoot++;
			}
		}

		// converts weapon points into ammo
		protected static void convertWeaponPoints() {

			reportPistol = 0;
			reportShotgun = 0;
			reportSMG = 0;
			reportRifle = 0;
			
			int addAmmo;
			while (weaponLoot > 0) {
				weaponLoot--;
				dice = random.nextInt(4);
				switch (dice) {
				case PISTOL:
					addAmmo = 30;
					pistol.addToTotalAmmo(addAmmo);
					reportPistol = reportPistol + addAmmo;
					break;
				case SHOTGUN:
					addAmmo = 30;
					shotgun.addToTotalAmmo(addAmmo);
					reportShotgun = reportShotgun + addAmmo;
					break;
				case SMG:
					addAmmo = 60;
					smg.addToTotalAmmo(addAmmo);
					reportSMG = reportSMG + addAmmo;
					break;
				case RIFLE:
					addAmmo = 30;
					rifle.addToTotalAmmo(addAmmo);
					reportRifle = reportRifle + addAmmo;
					break;
				}
			}
		}

		// convert wall points into HP
		protected static void convertWallPoints() {
			// unlike findammo or findsurvivors, this combines all wallpoints
			// together into a percentage : a chance to boost your stats
			// noticibly.

			if (wallLoot > 0) {
				wallMatFoundForReport = true;

				wallLoot--;
				dice = random.nextInt(3) + 1;
				// now the game will randomly assign you a type of multiplier
				switch (dice) {
				case 1:
					// divides reload time by this variable
					boostReload = boostReload + 2;
				case 2:
					// adds this variable to chance of a Random of 100 int.
					boostCrit = boostCrit + 20;
				case 3:
					// this increases by 20%. Lowers by 10% once a drop is made
					boostTabletDrop = boostTabletDrop + 20;
				}

			}

		}

		protected static void convertSurvivorPoints() {
			while (survivorLoot > 0) {
				survivorLoot--;
				reportSurvivorsFound++;
				survivorsTotal++;
			}

		}

		protected static void killSurvivors() {
			if (survivorsLost > survivorsTotal) {
				reportSurvivorsLost = survivorsTotal;
				survivorsTotal = 0;
			} else {
				reportSurvivorsLost = survivorsLost;
				survivorsTotal = survivorsTotal - survivorsLost;
			}
		}

		protected static void createReport() {
			report = "Report";

			// report any survivors lost
			if (reportSurvivorsLost > 0) {
				report += "\n- Lost " + reportSurvivorsLost + " people\n";
			}

			// report any weapons found

			if (reportPistol > 0) {
				report += "\n - Found " + reportPistol + " Pistol Ammo";
			}
			if (reportShotgun > 0) {
				report += "\n - Found " + reportShotgun + " Shotgun Ammo";
			}
			if (reportSMG > 0) {
				report += "\n - Found " + reportSMG + " SMG Ammo";
			}
			if (reportRifle > 0) {
				report += "\n - Found " + reportRifle + " Rifle Ammo";
			}

			// if any boosts are found
			if (boostReload > 1) {
				report += "\n\n - Found Food and Water: Reload Speed Increased";
			}
			if (boostCrit > 0) {
				report += "\n\n - Found Hallow Points: Chance to Crit Increased";
			}
			if (boostTabletDrop > 0) {
				report += "\n\n - Found a Smart Phone: Chance to drop Tablets Increased";
			}

			if (reportSurvivorsFound > 0) {
				report += "\n\n - Survivors found: " + reportSurvivorsFound;
			}

			if (survivorsTotal > 0) {
				report += "\n\n - Number of people left including me: "
						+ survivorsTotal;
			}
		}
//
//		protected static void clearReport() {
//			reportPistol = reportShotgun = reportSMG = reportRifle = 0;
//			report = "";
//		}

		public static String getReport() {
			return report;
		}

	}

	public static int getTotalKills() {
		return totalKills;
	}

	public static void addTotalKills(int kills) {
		// TODO Auto-generated method stub
		totalKills = totalKills + kills;

	}

	public static void resetTotalKills() {
		// TODO Auto-generated method stub
		totalKills = 0;
		
	}
}
