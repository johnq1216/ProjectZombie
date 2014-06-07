package com.JohnQ1216.ScavengerZombieDefense;

import com.QuachProjectZombie.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class SplashScreen extends Activity {
	//
	// MediaPlayer ourSong;

	Thread timer;

	SharedPreferences someData;

	int loadSavedInt = 999;
	TextView tvLoadScore;

	private boolean minimizePause = true;

	@Override
	protected void onCreate(Bundle TravisLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(TravisLoveBacon);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splash);
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		SoundBox.ourSong = MediaPlayer.create(this, R.raw.scavenging);
		SoundBox.ourSong.start();

//		someData = getSharedPreferences(CommandCenter.filename, 0);
//
//		loadSavedInt = someData.getInt("StoredScore", 0);

//		tvLoadScore = (TextView) findViewById(R.id.tvLoadScore);
//		tvLoadScore.setText(loadSavedInt + " KILLS!");

	}

	@Override
	// this method is called by the startActivity() method.
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// ourSong.release();
		finish();
		if (minimizePause == true)
			SoundBox.ourSong.pause();
	}
	
	protected void onResume(){
		super.onResume();
		if (minimizePause == true)
			SoundBox.ourSong.start();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			minimizePause = false;
			Intent openStartingPoint = new Intent("android.intent.action.INTRO");
			startActivity(openStartingPoint);

		}
		return true;
	}

	public static class SoundBox {

		public static MediaPlayer ourSong;

		public static void stopSong() {
			ourSong.release();
		}

	}

}
