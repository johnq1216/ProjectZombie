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

public class EliteMegaTank extends Sprite {

	public EliteMegaTank(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.zwalker);
		setBMP(bmp);
		setMaxHP(900);
		setMaxYSpeed(1);
		setFrameTime(100);

		Random rnd = new Random();
		// prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));
		setY(0);

		Paint paint = new Paint();
		ColorFilter filter = new LightingColorFilter(Color.WHITE, 1);
		paint.setColorFilter(filter);
		setPaint(paint);
	}

}
