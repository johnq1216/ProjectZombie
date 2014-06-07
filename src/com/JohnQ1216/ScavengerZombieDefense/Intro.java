package com.JohnQ1216.ScavengerZombieDefense;



import com.QuachProjectZombie.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class Intro extends Activity{

	
	SharedPreferences someData;

	int loadSavedInt = 999;

	private TextView tvRecord;
	
	@Override
	protected void onCreate(Bundle TravisLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(TravisLoveBacon);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.intro);
				
		
		someData = getSharedPreferences(CommandCenter.filename, 0);
		loadSavedInt = someData.getInt("StoredScore", 0);
		
		
		
		tvRecord = (TextView) findViewById(R.id.tvRecord);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		tvRecord.setTextSize((float) (metrics.heightPixels * 0.06));
		if (loadSavedInt <= 5){
			tvRecord.setText("Zombies continue rampage with no hero to stop them");
		}else{
			tvRecord.setText("A gunman makes final stand killing " + loadSavedInt + " zombies");
		}
	

		//write in code that takes loadSavedInt and puts it in textfield
		
//		Thread timer = new Thread(){
//			public void run(){
//				try{
//					sleep(3000);
//				}catch(InterruptedException e){
//					e.printStackTrace();
//				}finally{
//					Intent openStartingPoint = new Intent("android.intent.action.COMMANDCENTER");
//					startActivity(openStartingPoint);
//				}
//				
//			}
//		};
//		timer.start();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			Intent openStartingPoint = new Intent("android.intent.action.COMMANDCENTER");
			startActivity(openStartingPoint);

		}
		return true;
	}

	@Override
	//this method is called by the startActivity() method.
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		ourSong.release();
		finish();
	}
	

}

