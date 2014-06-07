package zombieTypes;

import java.util.Random;

import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.view.SurfaceView;

public class ZombieTank extends Sprite {

	public ZombieTank(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.ztank);
		setBMP(bmp);
		setMaxHP(400 + (gameLevel * 10));
		setMaxYSpeed(1);
		setFrameTime(300);

		Random rnd = new Random();
		// prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));
		Paint paint = new Paint();
		ColorFilter filter = new LightingColorFilter(Color.GRAY, 1);
		paint.setColorFilter(filter);
		setPaint(paint);
		setZombieID(ID_ZTANK);
	}
	
	public void attackWallIfCollided(){
		super.attackWallIfCollided();
		if (isAlive() && getY() >= gameView.getHeight() - (gameView.getHeight()/6)){
			setFrameTime(50);
		}
		
	}

}
