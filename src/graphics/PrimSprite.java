package graphics;

import com.JohnQ1216.ScavengerZombieDefense.Main.GameView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;


public abstract class PrimSprite {
	
	private Bitmap bitmap;
	private int x, y;
	
	
	public PrimSprite(int x, int y, SurfaceView gameView){
		this.x = x;
		this.y = y;
	}
	public PrimSprite(SurfaceView gameView){
		
	}
	
	public int getX() {
		return x;
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
	
	protected void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	protected int getHeight(){
		return bitmap.getHeight();
	}
	protected int getWidth(){
		return bitmap.getWidth();
	}
	
	abstract void onDraw(Canvas canvas);
}


