package data;

import java.util.List;

import com.JohnQ1216.ScavengerZombieDefense.Scouter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WiFiScanReceiver extends BroadcastReceiver {
	private static final String TAG = "WiFiScanReceiver";
	Scouter scouter;

	public WiFiScanReceiver(Scouter scouter) {
		super();
		this.scouter = scouter;
	}

	@Override
	public void onReceive(Context c, Intent intent) {
		List<ScanResult> results = scouter.wifi.getScanResults();
		scouter.setScannedList(results);
		
	}
}
