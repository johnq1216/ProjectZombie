package zombieTypes;

import graphics.SpriteFactory;
import graphics.TempSprite;

import java.util.List;
import java.util.Random;

import com.JohnQ1216.ScavengerZombieDefense.Main;
import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.SurfaceView;

//log: Implement the Debuff and Debuff Cycle

/**
 * This class uses a 4x4 sprite sheet template. The variable currentFrame has
 * the primary use of cycling through different images and has a secondary but
 * important task of timing the Sprite's damage dealt to the wall.
 * 
 * This Sprite has been tested primarily on the Prevail which has a Y Axis
 * Resolution of 480. This means that if the Sprite travels 1 pixel per second
 * on the Prevail, it will go very very slow on a Y Axis Resolution of 1200. To
 * compensate, the sprite will travel at Y velocities proportionate with the
 * native screen resolution on the Y Axis.
 * 
 * This means that slowing down a sprite by manipulating its Y-Velocity is no
 * longer an option. To compensate, there will be a boolean called slowDebuff.
 * When its true, the onDraw will cycle only one half of the time due to a int
 * counter that counts down.
 *
 *
 * all scaling is based on what appears on my phone.  Thus the default values of the screen size are the same as my phone
 */
public class PrimSprite {

	//new untested Variables.  These are the size of the sprite in proportion to the screensize on my prevail.  On other mobile devices
	//with bigger resolutions, theses variables will help scale the sprite to the exact proportion of the native screen resolutions
		
		//should rename scaledWidth into scaledWidth for better commenation
	private float scaledWidth, scaledHeight;
	private final float PREVAIL_WIDTH = 320f;
	protected final float PREVAIL_HEIGHT = 480f;
	

	// the following is how many times the sprite sheet will be split into rows
	// and columns
	private int bmpColumns = 4;
	private int bmpRows = 4;
	// these indicate rows on the spritesheet which are animation states.
	protected static final int STATE_WALK = 0;
	protected static final int STATE_ATTACK = 1;
	protected static final int STATE_DEATH = 2;

	private int x, y, xSpeed, ySpeed, maxXSpeed, maxYSpeed;
	protected SurfaceView gameView;
	private Bitmap spriteBMP, bmpBloodDeath;
	private int width, height, currentFrame, currentHP, maxHP, effectFlag,
			storedXSpeed, storedYSpeed;
	private long totalTime = System.currentTimeMillis();
	private int frameTime, gameLevel;

	private boolean invulnerable = false;

	private Paint paint;
	private boolean attackMode = false;
	private boolean startDeathAnim = false;
	public boolean isDead = false;

	public long storedModulusValue;
	
	Rect src = new Rect();
	RectF dst = new RectF();

	Random rnd = new Random();

	// new untested variable. slowDebuff boolean will start the slow process if
	// the zombie is afflicted with it.
	// the slowDebuff Cycle will cause the update() to be ignored until the
	// counter reaches zero. Then the update method will run and
	// the counter will be restored.
	private boolean slowDebuff = false;
	private int slowDebuffCounter = 0;

	public PrimSprite() {
		totalTime = System.currentTimeMillis();
		maxHP = 1;
		currentHP = 1;
		isDead = false;
		//
		// currentHP = 0;
		// isDead = true;
	}

	public PrimSprite(SurfaceView gameView, int gameLevel) {
		this.gameView = gameView;
		this.gameLevel = gameLevel;
		// spawn at the top of scree
		x = rnd.nextInt(gameView.getWidth() - (int)scaledWidth);
		setY((int)-scaledHeight);
		xSpeed = rnd.nextInt(2);
		totalTime = System.currentTimeMillis();
		maxHP = 1;
		currentHP = 1;
		isDead = false;
		//
		// currentHP = 0;
		// isDead = true;
		runDeath();
		bmpBloodDeath = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.blood6);
	}

	public void setBmpRows(int n){
	bmpRows = n;
	}

	public void setBmpColumns(int n){
	bmpColumns = n;
	}


	/**
	 * will theorically slow down the sprite. Invoke this if hit with an ice gun
	 * or something
	 **/
	public void slowSprite() {
		slowDebuff = true;
		ColorFilter filter = new LightingColorFilter(Color.BLUE, 1);
		paint.setColorFilter(filter);
		setPaint(paint);
	}

	protected void update(long sleepSuggested, long startTime, Canvas canvas) {

		int debuffZeroMultiplier = 1;

		if (!slowDebuff) {
			debuffZeroMultiplier = 1;
		} else {
			if (slowDebuffCounter > 0) {
				debuffZeroMultiplier = 0;
				slowDebuffCounter--;
			} else {
				debuffZeroMultiplier = 1;
				slowDebuffCounter = 1;
			}
		}

		long timePassed = System.currentTimeMillis() - startTime;
		long realTimeXSpeed, realTimeYSpeed, smallestResolution = 240;
		if (timePassed > sleepSuggested) {
			realTimeXSpeed = timePassed / sleepSuggested;
			realTimeYSpeed = timePassed / sleepSuggested;
		} else {
			realTimeXSpeed = 1;
			realTimeYSpeed = 1;
		}

		// border collision and velocity reversal
		if (x >= canvas.getWidth() - scaledWidth - xSpeed || x + xSpeed <= 0) {
			xSpeed = -xSpeed;
		}

		x += xSpeed * realTimeXSpeed;

		// if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
		// ySpeed = -ySpeed;
		// }
		y += (ySpeed * canvas.getHeight() / smallestResolution)
				* realTimeYSpeed * debuffZeroMultiplier;
		storedModulusValue = (((canvas.getHeight() % smallestResolution) * 10) / smallestResolution);
		if (storedModulusValue >= 10) {
			storedModulusValue = storedModulusValue - 10;
			y++;
		}

		long currTime = (System.currentTimeMillis());
		// cycles through number used for animation frame. In this case, it's;
		// 0, 1, 2.

		// distance in this case is currentFrame. figure out the formula
		if (currTime - totalTime >= frameTime) {
			currentFrame = ++currentFrame % bmpColumns;
			totalTime = System.currentTimeMillis();
		}

	}

	public void onDraw(Canvas canvas, long sleepSuggested, long startTime) {

		if (isAlive()) {
			update(sleepSuggested, startTime, canvas);
			int srcX = currentFrame * width + 1;
			int srcY = getSpriteState() * height;
						
			//new untested variables
			
			scaledWidth = canvas.getWidth() * (width/PREVAIL_WIDTH);
			scaledHeight = canvas.getHeight() * (height/PREVAIL_HEIGHT );
			
			// this rectangle will show part of the bitmap
			src.set(srcX, srcY, srcX + width, srcY + height);

			// this rectangle will paint the exact coordinates of the bitmap
			dst.set(x, y, x + scaledWidth, y + scaledHeight);
		
//			Rect dst = new Rect(x, y, x + 10, y + 10);
			// src will be the rectangle that is the visible part of the bmp,
			// dst
			// will be where src will be painted

			if (paint != null) {
				canvas.drawBitmap(spriteBMP, src, dst, paint);
			} else {
				canvas.drawBitmap(spriteBMP, src, dst, null);
			}
		}
	}

	public int getSpriteState() {
		if (attackMode) {
			return STATE_ATTACK;
		}
		return STATE_WALK;
	}

	public void setBMP(Bitmap bmp) {
		this.spriteBMP = bmp;
		width = bmp.getWidth() / bmpColumns;
		height = bmp.getHeight() / bmpRows;
		


	}
	
	public Bitmap getBMP(){
		
		return spriteBMP;
	}

	/**
	 * Revives a sprite out of state of death, restoring its velocity and HP
	 */
	public void revive() {
		if (!isAlive()) {
			setAttackMode(false);
			isDead = false;
			currentHP = maxHP;
			if (gameView != null) {
				x = rnd.nextInt(gameView.getWidth() - width);
			}
			restoreYSpeed();
			restoreXSpeed();
		}
		
		Paint paint = new Paint();
		paint.setColorFilter(null);
		setPaint(paint);

	}

	public void setMaxHP(int enteredHP) {
		currentHP = enteredHP + ((gameLevel - 1) * 25);
		maxHP = enteredHP + ((gameLevel - 1) * 25);
	}

	public void setHP(int enteredHP) {
		if (enteredHP > maxHP) {
			currentHP = maxHP;
		} else {
			currentHP = enteredHP;
		}
	}

	public int getHP() {
		return currentHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setFrameTime(int frameTime) {
		this.frameTime = frameTime;
	}

	public int getFrameTime() {
		return frameTime;
	}

	public void addHP(int addedHP) {
		if (invulnerable == false) {
			if (maxHP < currentHP + addedHP)
				currentHP = maxHP;
			else
				currentHP = currentHP + addedHP;
		}

	}

	public boolean isAlive() {
		return (currentHP > 0);
	}

	public int getX() {
		return x;
	}

	public int getCenterX() {
		return x + (width / 2);
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * sets Y velocity
	 * 
	 * @param ySpeed
	 */
	public void setYSpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * sets Maximum Y Velocity and also sets regular Y Velocity to this value
	 * 
	 * @param maxYSpeed
	 */
	public void setMaxYSpeed(int maxYSpeed) {
		this.maxYSpeed = maxYSpeed;
		setYSpeed(maxYSpeed);
	}

	/**
	 * Restores this sprites original Y Speed
	 */
	public void restoreYSpeed() {
		ySpeed = maxYSpeed;
	}

	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setMaxXSpeed(int maxXSpeed) {
		this.maxXSpeed = maxXSpeed;
		setXSpeed(maxXSpeed);
	}

	/**
	 * Restores this sprites original X Speed
	 */
	public void restoreXSpeed() {
		xSpeed = maxXSpeed;
	}

	public int getYSpeed() {
		return ySpeed;
	}

	public int getXSpeed() {
		return xSpeed;
	}

	public int getCenterY() {
		return y + (height / 2);
	}

	public int getWidth() {
		return (int) scaledWidth;
	}

	public int getHeight() {
		return (int) scaledHeight;
	}

	public int getEffect() {
		return effectFlag;
	}

	public void setEffect(int effectFlag) {
		this.effectFlag = effectFlag;
	}

	public void setInvulnerable(boolean bool) {
		invulnerable = bool;
	}

	/**
	 * Checks to see if the sprite collides with the onTouch coordinates
	 * 
	 * @param xEvent
	 * @param yEvent
	 * @return
	 */
	public boolean isCollision(float xEvent, float yEvent) {
		// TODO Auto-generated method stub
		boolean collided = xEvent > x && xEvent < x + scaledWidth && yEvent > y
				&& yEvent < y + scaledHeight;
		if (collided == true) {
			// pushes zombie back if stunnable
		}
		return collided;
	}

	/**
	 * Sets the paint object for this Sprite. Mostly used for filters
	 * 
	 * @param paint
	 */
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setAttackMode(boolean attackMode) {
		this.attackMode = attackMode;
	}

	public boolean getAttackMode() {
		return attackMode;
	}

	public void runDamage(int debugDamageDealt) {
		addHP(-debugDamageDealt);
		// TODO Auto-generated method stub
		if (getY() + getHeight() > Main.damageDropOff) {
			setY(getY() - 10);
		}
	}

	public void runDeath() {
		if (!isAlive() && !isDead) {
			isDead = true;
			storedXSpeed = getXSpeed();
			storedYSpeed = getYSpeed();
			setHP(0);
			setY(200);
			setX(200);
			setYSpeed(0);
			setXSpeed(0);
			slowDebuff = false;
		}
	}

	public void runDeath(Canvas canvas, List<TempSprite> temps) {
		// distance in this case is currentFrame. figure out the formula
		if (!isAlive() && !isDead) {
			isDead = true;
			temps.add(new TempSprite(temps, gameView, getX() + getWidth() / 2,
					getY() + getHeight() / 2, bmpBloodDeath, 5, 20, false));

			// stores Velocity values to be reused again upon Resurrection
			storedXSpeed = getXSpeed();
			storedYSpeed = getYSpeed();
			setHP(0);
			setY(-100);
			setX(0);
			setYSpeed(0);
			setXSpeed(0);
			slowDebuff = false;
		}

	}

	public int getFrame() {
		return currentFrame;
	}
}
