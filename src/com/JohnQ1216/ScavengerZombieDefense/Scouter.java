package com.JohnQ1216.ScavengerZombieDefense;

import java.util.ArrayList;
import java.util.List;

import com.JohnQ1216.ScavengerZombieDefense.Briefing.ActivityLoader;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

//prepares user for next fight, and stores any constant data
public class Scouter extends Activity implements OnClickListener {
	// wifi switch must be turned on or app will crash

	private static final String TAG = "WiFiDemo";
	BroadcastReceiver receiver;
	Button bSearch;
	TextView tvDebug;
	public WifiManager wifi;
	public static boolean newGame = true;
	
	ScanResult firstSignal = null;
	
	private boolean scanInProgress = false;
	// instantiated in wifiscanner
	private static List<ScanResult> networkList;
	private static List<ScanResult> usedList = new ArrayList<ScanResult>();
	private static boolean jackpot;


	@Override
	protected void onCreate(Bundle TravisLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(TravisLoveBacon);
		setContentView(R.layout.scouter);
		//create new gun if first time playing
//		if (newGame) {
//			newGame = false;
//			Weapon.addDefaultPistol();
//		}

		// Setup UI
		bSearch = (Button) findViewById(R.id.bSearch);
		bSearch.setOnClickListener(this);
		tvDebug = (TextView) findViewById(R.id.tvDebug);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Register Broadcast Receiver
		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");

		Log.d(TAG, "onClick() wifi.startScan()");
		wifi.startScan();
	}

	@Override
	// this method is called by the startActivity() method.
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();

	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.bSearch && !scanInProgress) {
			scanInProgress = true;
			// this means wifi is turned off and default values must be used
			if (networkList == null || networkList.isEmpty()) {
				// ***write code that applies default values here
	
			}
			// if this is the first time scanning, user will not have blacklist.
			// just assign first element to firstSignal
			else if (usedList.isEmpty()) {
				firstSignal = networkList.get(0);
				
			} else {
				for (ScanResult result : networkList) {
					firstSignal = result;
					for (ScanResult used : usedList) {
						//if there is a match, firstSignal is nullified and 
						//we'll try our luck in the next network in networkList
						if (used.SSID.equalsIgnoreCase(result.SSID)) {
							firstSignal = null;
							break;
						}
					}
				}
			}
		}
		/** the next statements are deciding factors on whether you get something
		 * special or you get something decent
		 */
		if (firstSignal != null) {
			tvDebug.setText(firstSignal.SSID + ". Number of usedNetworks is "
					+ usedList.size() + "" +
							"\nNumber of networks scanned: " + networkList.size());
			usedList.add(firstSignal);
//			Weapon.generateRandom();
			Food.generateFood(5);
			jackpot = true;

		} else {
			tvDebug.setText("firstSignal is null");
			Food.generateFood(2);
			jackpot = false;
		}
		//comment the loader for debugging
		ActivityLoader.loadBriefing(this);
		scanInProgress = false;
	}

	
	public static String checkJackpot(){
		if (jackpot){
			return "\n\nYou've found a new gun";
		}
		else{
			return "\n\nMinimal Resources found";
		}
	}

	public void setScannedList(List<ScanResult> results) {

		networkList = results;
	}

}
