package zombieTypes;

import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Main;
import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.view.SurfaceView;

public class EliteBansheeZombie extends Sprite {

	private final static int STATE_RUN = 3;
	Paint paint = new Paint();
	private long timeElapsedMS, currTime, beginTime;
	public final static int HP_LOSS_INTERVAL = 1000;
	private boolean timeBombOn = false;

	public EliteBansheeZombie(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.banshee2);
		setBMP(bmp);
		setMaxHP(9999);
		setMaxYSpeed(2);
		setFrameTime(200);
		setXSpeed(0);
		setZombieID(ID_E_BANSHEE);

		Random rnd = new Random();
		// prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));
		setY(0);

		ColorFilter filter = new LightingColorFilter(Color.YELLOW, 1);
		paint.setColorFilter(filter);
		setPaint(paint);
	}

	protected void update(long sleepSuggested, long startTime, Canvas canvas) {
		super.update(sleepSuggested, startTime, canvas);

		if (getHP() == getMaxHP()) {
			if (getFrame() == 1 || getFrame() == 3) {
				setYSpeed(0);
				setFrameTime(400);
			} else {
				restoreYSpeed();
				setFrameTime(200);
			}
		} else {

			setYSpeed(0);

			if (timeBombOn == false) {
				timeBombOn = true;
				beginTime = System.currentTimeMillis();
				Main.stunPlayer(HP_LOSS_INTERVAL);

			}
			
			

			currTime = System.currentTimeMillis();
			if (currTime - beginTime >= HP_LOSS_INTERVAL) {

				setHP(0);
			}
		}

	}

	public void revive() {
		super.revive();
		setYSpeed(2);
		setFrameTime(100);
		timeBombOn = false;
	}

	public int getSpriteState() {
		if (getAttackMode()) {
			return STATE_ATTACK;
		}
		if (getHP() != getMaxHP()) {
			setFrameTime(50);
			return STATE_RUN;
		}
		return STATE_WALK;
	}

	public boolean screamCheck(){
		return timeBombOn;
	}
}
