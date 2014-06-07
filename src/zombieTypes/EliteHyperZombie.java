package zombieTypes;

import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Main;
import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.view.SurfaceView;

public class EliteHyperZombie extends Sprite {

	public EliteHyperZombie(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.zrunner);
		setBMP(bmp);
		setMaxHP(100 + (gameLevel * 10));
		setMaxYSpeed(4);
		setFrameTime(10);
		setZombieID(ID_E_HYPER);

		Random rnd = new Random();
		// prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));
		setY(0);

		Paint paint = new Paint();
		ColorFilter filter = new LightingColorFilter(Color.RED, 1);
		paint.setColorFilter(filter);
		setPaint(paint);
	}
	public void attackWallIfCollided() {
		// zombie will stop if it reaches the wall and the wall is still alive
		super.attackWallIfCollided();
		
		if (isAlive() && getY() >= gameView.getHeight() - (gameView.getHeight()/6)){
			setFrameTime(100);
		}
		
	}

}
