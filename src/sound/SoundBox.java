package sound;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;

import com.QuachProjectZombie.R;

public class SoundBox {
	private static Activity activity;

	private static SoundPool sp= new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	private static int gunshot = sp.load(activity, R.raw.gunshot, 1);

	
}



