package graphics;

import com.JohnQ1216.ScavengerZombieDefense.Main.GameView;
import com.QuachProjectZombie.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceView;

public class Wall extends PrimSprite {

	Bitmap bmp;
	SurfaceView gameView;
	private float scaledWidthPercentage, scaledHeightPercentage, scaledWidth,
			scaledHeight;
	private final float PREVAIL_WIDTH = 320f;
	protected final float PREVAIL_HEIGHT = 480f;
	int x, y;

	// implement this later
	int hp;

	// this rectangle will show part of the bitmap
	Rect src = new Rect();
	// this rectangle will paint the exact coordinates of the bitmap
	RectF dst = new RectF();

	public Wall(int x, int y, SurfaceView gameView) {
		super(x, y, gameView);
		bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.fence5);
		setBitmap(bmp);
		this.x = x;
		this.y = y;
		this.gameView = gameView;
		scaledWidthPercentage = bmp.getWidth() / PREVAIL_WIDTH;
		scaledHeightPercentage = bmp.getHeight() / PREVAIL_HEIGHT;

	}

	public Wall(SurfaceView gameView) {
		super(gameView);
		bmp = BitmapFactory.decodeResource(gameView.getResources(),
				R.drawable.fence5);
		setBitmap(bmp);
		this.gameView = gameView;
		scaledWidthPercentage = bmp.getWidth() / PREVAIL_WIDTH;
		scaledHeightPercentage = bmp.getHeight() / PREVAIL_HEIGHT;
	}

	public boolean touchWall(float xEvent, float yEvent) {
		boolean collided = yEvent > getY() && yEvent < getY() + bmp.getHeight();
		return collided;
	}

	public void onDraw(Canvas canvas) {

		int fenceTiles = gameView.getWidth() / bmp.getWidth();
		// setY(gameView.getHeight() - bmp.getHeight());
		// setY(gameView.getHeight() - (gameView.getHeight()/11));

		scaledWidth = canvas.getWidth() * scaledWidthPercentage;
		scaledHeight = canvas.getHeight() * scaledHeightPercentage;

		// setY((int) (canvas.getHeight() - scaledHeight));
		setY((int) (canvas.getHeight() - (canvas.getHeight() / 11)));

		// this rectangle will show part of the bitmap
		// src.set(0, 0, (int) scaledWidth, (int) scaledHeight);
		// this rectangle will paint the exact coordinates of the bitmap
		dst.set(x, getY(), canvas.getWidth(), getY() + scaledHeight);

		// Rect dst = new Rect(x, y, x + 10, y + 10);
		// src will be the rectangle that is the visible part of the bmp,
		// dst
		// will be where src will be painted
		canvas.drawBitmap(bmp, null, dst, null);

		//
		// for (int i = fenceTiles; i >= 0; i--) {
		// canvas.drawBitmap(bmp, i * bmp.getWidth(), getY(), null);
		// }
	}
}
