package zombieTypes;

import java.util.Random;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.view.SurfaceView;

import com.QuachProjectZombie.R;

public class ZombieRunner extends Sprite{

	
	public ZombieRunner(SurfaceView gameView, int gameLevel){
		super(gameView, gameLevel);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zrunner2);
		setBMP(bmp);
		setMaxHP(80 + (gameLevel * 10));
		setMaxYSpeed(3);
		setFrameTime(75);
		setZombieID(ID_ZRUNNER);
	
		Random rnd = new Random();
		//prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));
		Paint paint = new Paint();
		ColorFilter filter = new LightingColorFilter(Color.GRAY, 0);
		paint.setColorFilter(filter);
		setPaint(paint);
	}

}
