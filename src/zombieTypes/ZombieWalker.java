package zombieTypes;

import java.util.Random;

import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.view.SurfaceView;

public class ZombieWalker extends Sprite {
	Paint paint;

	public ZombieWalker(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		// remove if game crashes
		setBmpColumns(6);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.zwalker3);
		
		setBMP(bmp);
		setMaxYSpeed(1);
		setMaxHP(80 + (gameLevel * 10));
		setFrameTime(500);
						
		Random rnd = new Random();
		// prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));

		paint = new Paint();
		ColorFilter filter = new LightingColorFilter(Color.GRAY, 0);
		paint.setColorFilter(filter);
		setPaint(paint);

		setZombieID(ID_ZWALKER);

	}

	protected void update(long sleepSuggested, long startTime, Canvas canvas) {
		super.update(sleepSuggested, startTime, canvas);

		// side movement
		if (getFrame() == 0) {
			setX(getX() - 2);
		}
		if (getFrame() == 3) {
			setX(getX() + 2);
		}
		
		// steps
		if (getFrame() == 0 ||getFrame() == 2 ||getFrame() == 4){
			//stop velocity
			setYSpeed(0);
		}else
		{
			//restore velocity
			this.restoreYSpeed();
		}

	}

	public void revive() {
		super.revive();

		ColorFilter filter = new LightingColorFilter(Color.GRAY, 0);
		paint.setColorFilter(filter);
		setPaint(paint);
	}

}
