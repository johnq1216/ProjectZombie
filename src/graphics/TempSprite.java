package graphics;

import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceView;

/**
 * The TempSprite class is used to produce Sprites that are not going to live
 * for long. Such as bullets, explosions, zombie guts flying.
 * 
 * Recently, the TempSprite class has been augmented to also provide perpetual
 * sprites that animate over and over. Its life must be -999 for it to not die
 * and for its animation to perpetuate
 * 
 * Placing -999 in its life will make it immortal
 */
public class TempSprite {

	private float scaledWidthPercentage, scaledHeightPercentage, scaledWidth, scaledHeight;
	private final float PREVAIL_WIDTH = 320f, PREVAIL_HEIGHT = 480f;
	
	private float x;
	private float y;
	private int xSpeed, ySpeed;
	private Bitmap bmp;
	private int life;
	private static final int BMP_COLUMNS = 5;
	private static List<TempSprite> temps;
	private long totalTime;
	private int frameTime;
	private int frameWidth;
	private int currentFrame;
	private Paint paint;
	private boolean animLoopInfinite;


	public TempSprite(List<TempSprite> temps, SurfaceView gameView, float x,
			float y, Bitmap bmp, int frameTime, int life, boolean animLoopInfinite) {

		totalTime = System.currentTimeMillis();
		this.bmp = bmp;
		frameWidth = bmp.getWidth() / BMP_COLUMNS;
		TempSprite.temps = temps;
		this.frameTime = frameTime;
		this.life = life;
		this.x = (x - (bmp.getWidth() / BMP_COLUMNS) / 2);
		this.y = (y - bmp.getHeight() / 2);
		paint = new Paint();
		scaledWidthPercentage = frameWidth/PREVAIL_WIDTH;
		scaledHeightPercentage = this.bmp.getHeight()/PREVAIL_HEIGHT;
		this.animLoopInfinite = animLoopInfinite;
	}

	public TempSprite(SurfaceView gameView, float x, float y, Bitmap bmp,
			int frameTime, int life, boolean animLoopInfinite) {

		totalTime = System.currentTimeMillis();
		this.bmp = bmp;
		frameWidth = bmp.getWidth() / BMP_COLUMNS;
		this.frameTime = frameTime;
		this.life = life;
		this.x = (x - (bmp.getWidth() / BMP_COLUMNS) / 2);
		this.y = (y - bmp.getHeight() / 2);
		paint = new Paint();
		scaledWidthPercentage = frameWidth/PREVAIL_WIDTH;
		scaledHeightPercentage = this.bmp.getHeight()/PREVAIL_HEIGHT;
		this.animLoopInfinite = animLoopInfinite;
	}
	public TempSprite(List<TempSprite> temps, SurfaceView gameView, float x, float y, Bitmap bmp,
			int frameTime, int life, int alpha, boolean animLoopInfinite) {

		totalTime = System.currentTimeMillis();
		this.bmp = bmp;
		frameWidth = bmp.getWidth() / BMP_COLUMNS;
		this.frameTime = frameTime;
		this.life = life;
		this.x = (x - (bmp.getWidth() / BMP_COLUMNS) / 2);
		this.y = (y - bmp.getHeight() / 2);
		paint = new Paint();
		paint.setAlpha(alpha);
		scaledWidthPercentage = frameWidth/PREVAIL_WIDTH;
		scaledHeightPercentage = this.bmp.getHeight()/PREVAIL_HEIGHT;
		this.animLoopInfinite = animLoopInfinite;
	}


	public void draw(Canvas canvas, long sleepSuggested, long startTime) {
		update();
		
		long timePassed = System.currentTimeMillis() - startTime;
		long realTimeXSpeed, realTimeYSpeed;
			
		if (timePassed > sleepSuggested) {
			realTimeXSpeed = timePassed / sleepSuggested;
			realTimeYSpeed = timePassed / sleepSuggested;
		} else {
			realTimeXSpeed = 1;
			realTimeYSpeed = 1;
		}

		x += xSpeed * realTimeXSpeed;

		// if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
		// ySpeed = -ySpeed;
		// }
		y += ySpeed * realTimeYSpeed;

		long currTime = (System.currentTimeMillis());

		if (!animLoopInfinite) {
			if (currTime - totalTime >= frameTime) {

				if (currentFrame < BMP_COLUMNS - 1) {
					currentFrame++;
				} else {
					currentFrame = BMP_COLUMNS - 1;
				}
				totalTime = System.currentTimeMillis();
			}
		} else {
			if (currTime - totalTime >= frameTime) {
				currentFrame = ++currentFrame % BMP_COLUMNS;
				totalTime = System.currentTimeMillis();
			}
		}

		int srcX = currentFrame * frameWidth + 1;
		int srcY = 0;
		
		scaledWidth = canvas.getWidth() * scaledWidthPercentage;
		scaledHeight = canvas.getHeight() * scaledHeightPercentage;
		
		// this rectangle will show part of the bitmap
		Rect src = new Rect(srcX, srcY, srcX + frameWidth, srcY
				+ bmp.getHeight());
		// this rectangle will paint the exact coordinates of the bitmap
		RectF dst = new RectF(x, y, x + scaledWidth, y
				+ scaledHeight);
		canvas.drawBitmap(bmp, src, dst, paint);
		
	}

	private void update() {
		// -999 means the sprite is immortal. If the sprite has this value,
		// the second part of the condition will be ignored and will not
		// decrement
		// thus, the sprite will not die.
		if (life != -999 && --life < 1) {
			temps.remove(this);
		}
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getY(){
		return (int) y;
	}
	
	public int getX(){
		return(int)x;
	}
	public void setXSpeed(int xSpeed){
		this.xSpeed = xSpeed;
	}
	
	public void setYSpeed(int ySpeed){
		this.ySpeed = ySpeed;
	}
	
	public int getWidth(){
		return frameWidth;
	}

}