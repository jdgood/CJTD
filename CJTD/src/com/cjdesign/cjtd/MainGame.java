package com.cjdesign.cjtd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cjdesign.cjtd.game.*;
import com.cjtd.globals.G;

public class MainGame extends Activity{
	private static final int DIALOG_PAUSE_ID = 0;
	
	private GLSurfaceView mGLSurfaceView;
	
	private float oldDist = 1f;
	private float prevX;
	private float prevY;
	
	private static final int NONE = 0;
    private static final int ZOOM = 1;
	private int mode = NONE;
	
	MediaPlayer mpGame;
	
	float p2wx(float xp) {
		//return 2 * ((float)(2*G.W*xp) / (G.H*(G.W-1)) - ((float)G.W / G.H));
		return 2 * (2*G.W*xp) / (G.H*(G.W-1) - G.W / G.H);
	}
	
	float p2wy(float yp) {
		//return 2 * (((float)(2*yp) / (G.H-1)) - 1);
		return 2 * (2*yp / (G.H-1) - 1);
	}
	
	float g2py(float yp) {
		//return (G.H-1) - yp;
		return (G.H-1) - yp;
	}

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = p2wx(e.getX());
        float y = p2wy(e.getY());
        switch (e.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_POINTER_DOWN:
	            oldDist = spacing(e);
	            //System.out.println("oldDist=" + oldDist);
	            if (oldDist > 10f) {
	               mode = ZOOM;
	               //System.out.println("mode=ZOOM");
	            }
	            break;
	            
	        case MotionEvent.ACTION_POINTER_UP:
	            mode = NONE;
	            //System.out.println("mode=NONE");
	            break;
	            
	        case MotionEvent.ACTION_MOVE:
	        	if(mode == NONE){
		        	if(G.viewX + x - prevX < G.viewXlimit && G.viewX + x - prevX > -G.viewXlimit){
		        		G.viewX += x - prevX;
		        	}
		        	if(G.viewY + y - prevY < G.viewYlimit && G.viewY + y - prevY > -G.viewYlimit){
		        		G.viewY += y - prevY;
		        	}
		            //G.viewX++;
		            //G.viewY++;
		            //float dx = x - prevX;
		            //float dy = y - prevY;
		            //mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
		            //mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
	        	}
	        	else if(mode == ZOOM){
	        		float newDist = spacing(e);
	        		//System.out.println("newDist=" + newDist);
					if (newDist > 10f) {
						float scale = newDist / oldDist;
						//System.out.println("scale=" + scale);
						if(scale < 1){
							if(G.viewZ - 1 >= G.viewZlimit)
								G.viewZ--;
						}
						else if(scale > 1){
							if(G.viewZ + 1 <= 0)
								G.viewZ++;
						}
						oldDist = newDist;
					}
	        	}
	        	mGLSurfaceView.requestRender();
	            break;
        }
        prevX = x;
        prevY = y;
        return true;
    }
    
    private float spacing(MotionEvent event) {
    	   float x = event.getX(0) - event.getX(1);
    	   float y = event.getY(0) - event.getY(1);
    	   return FloatMath.sqrt(x * x + y * y);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // Create our Preview view and set it as the content of our
	    // Activity
	    mGLSurfaceView = new GLSurfaceView(this);
	    mGLSurfaceView.setKeepScreenOn(true);
	    mGLSurfaceView.setRenderer(new Renderer(this));
	    setContentView(mGLSurfaceView);
	    
	    mpGame = MediaPlayer.create(this, R.raw.game);
        mpGame.setLooping(true);
        mpGame.start();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    mGLSurfaceView.onResume();
	    if(mpGame!=null)
	    	mpGame.start();
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    mGLSurfaceView.onPause();
	    if(mpGame!=null)
	    	mpGame.pause();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    mGLSurfaceView.onPause();
	    mpGame.release();
	    mpGame = null;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MENU) {
	    	mGLSurfaceView.onPause();
	    	showDialog(DIALOG_PAUSE_ID);
	    	return true;
	    }
	    /*else if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
	    	mGLSurfaceView.onPause();
	    	//moveTaskToBack(true);
	    }*/
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	//final String difficulty;
    	AlertDialog.Builder builder;
        
        switch(id) {
	        case DIALOG_PAUSE_ID:
	        	builder = new AlertDialog.Builder(this);
	        	builder.setMessage("Paused")
	        	       .setCancelable(false)
	        	       .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   MainGame.this.finish();
	        	           }
	        	       })
	        	       .setNegativeButton("Resume", new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	                mGLSurfaceView.onResume();
	        	           }
	        	       });
	        	return builder.create();
	        default:
	            return null;
        }
    }
}
