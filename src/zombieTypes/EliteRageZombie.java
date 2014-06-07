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

public class EliteRageZombie extends Sprite {

	private final static int STATE_RUN = 3;
	Paint paint = new Paint();

	public EliteRageZombie(SurfaceView gameView, int gameLevel) {
		super(gameView, gameLevel);
		Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.zrage3);
		setBMP(bmp);
		setMaxHP(300 + (gameLevel * 10));
		setMaxYSpeed(2);
		setFrameTime(200);
		setXSpeed(0);
		setZombieID(ID_E_RAGE);

		Random rnd = new Random();
		// prevents sprite from spawning outside screen
		setX(rnd.nextInt(gameView.getWidth() - getWidth()));
		setY(0);

		ColorFilter filter = new LightingColorFilter(Color.YELLOW, 1);
		paint.setColorFilter(filter);
		setPaint(paint);
	}
	
	public boolean checkIfRunning(){
		return getHP() <= getMaxHP() / 2;
		
	}
	
	

	protected void update(long sleepSuggested, long startTime, Canvas canvas) {
		super.update(sleepSuggested, startTime, canvas);

		// if (getFrameTime() > 25 && (getHP() > (getMaxHP() / 2))) {
		// if (getFrame() == 1 || getFrame() == 3) {
		// setYSpeed(0);
		// setFrameTime(400);
		// } else {
		// restoreYSpeed();
		// setFrameTime(200);
		// }
		// }

		// if zombie has taken damage but is still at least half max hp
		if (!checkIfRunning()) {

			if (getFrame() == 1 || getFrame() == 3) {
				setYSpeed(0);
				setFrameTime(400);
			} else {
				restoreYSpeed();
				setFrameTime(200);
			}
		}

	}

	public void revive() {
		super.revive();
		setYSpeed(2);
		setFrameTime(100);
	}

	public int getSpriteState() {
		if (getAttackMode()) {
			return STATE_ATTACK;
		}
		if (checkIfRunning()) {
			return STATE_RUN;
		}
		return STATE_WALK;
	}

	public void runDamage(int debugDamageDealt) {
		// this will call the SuperClass's version of this method first.
		super.runDamage(debugDamageDealt);
		// setYSpeed(getYSpeed() + extraXSpeed++);
		if (getHP() < getMaxHP()) {
			if (getHP() > 300)
				setYSpeed(3);
			else if (getHP() > 200)
				setYSpeed(4);
			else if (getHP() <= 200)
				setYSpeed(6);

		}
		// if (getFrameTime() > 25 && (getHP() > (getMaxHP() / 2)))
		// setFrameTime(getFrameTime() - 25);

		if (getFrameTime() > 25)
			setFrameTime(getFrameTime() - 25);
	}
}
