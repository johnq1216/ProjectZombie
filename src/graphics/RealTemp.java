package graphics;

import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;
 
public abstract class RealTemp {
       private float x;
       private float y;
       private Bitmap bmp;
       private int decayTime;
       private List<TempSprite> temps;
    
       
       public RealTemp(List<TempSprite> temps, SurfaceView gameView, float x,
               float y, int decayTime) {
	   //sprite cannot be printed off screen or there will be errors. See if you can minimize this by preventing sprites from being drawn at the draw method if x and y are out of bounds
        this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),
                      gameView.getWidth() - bmp.getWidth());
        this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),
                      gameView.getHeight() - bmp.getHeight());

        this.temps = temps;
        this.decayTime = decayTime;
  }
 
       public void onDraw(Canvas canvas) {
             update();
             canvas.drawBitmap(bmp, x, y, null);
       }
 
       private void update() {
             if (--decayTime < 1) {
                    temps.remove(this);
             }
       }
       
       public void setBMP(Bitmap bmp){
    	   this.bmp = bmp;
       }
       
       public void setDecay(int l){
    	   decayTime = l;
       }
}