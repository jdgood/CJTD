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

import com.cjdesign.cjtd.globals.G;

public class MainGame extends Activity{
	private static final int DIALOG_PAUSE_ID = 0;
	
	private GLSurfaceView mGLSurfaceView;
	
	private float oldDist = 1f;
	private float prevX;
	private float prevY;
	private long startTime = 0;
	private long endTime = 0;
	
	private static final int NONE = 0;
    private static final int ZOOM = 1;
    private static final int TARGET = 2;
    private static final int MOVE = 3;
	private int mode = NONE;
	
	float p2wx(float xp) {
		//return 2 * ((float)(2*G.W*xp) / (G.H*(G.W-1)) - ((float)G.W / G.H));
		return 2 * (2*G.W*xp) / (G.H*(G.W-1) - G.W / G.H);
	}
	
	float p2wy(float yp) {
		//return 2 * (((float)(2*yp) / (G.H-1)) - 1);
		return 2 * (2*yp / (G.H-1) - 1);
	}
	
	float xCon(float xp) {
		return 2 * (2*G.W*xp) / (G.H*(G.W-1) - G.W / G.H) / G.viewZ + G.viewX;
	}
	
	float yCon(float yp) {
		return 2 * (2*yp / (G.H-1) - 1) / G.viewZ + G.viewY;
	}
	
	float g2py(float yp) {
		//return (G.H-1) - yp;
		return (G.H-1) - yp;
	}

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        endTime = System.currentTimeMillis();
        float dt = (float)(endTime - startTime)/1000f;
        float x = p2wx(e.getX());
        float y = p2wy(e.getY());
        switch (e.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_POINTER_DOWN://second finger detected
	            oldDist = spacing(e);
	            //System.out.println("oldDist=" + oldDist);
	            if (oldDist > 10f) {
	               mode = ZOOM;
	               //System.out.println("mode=ZOOM");
	            }
	            break;
	            
	        case MotionEvent.ACTION_POINTER_UP://second finger removed
	            mode = NONE;
	            //System.out.println("mode=NONE");
	            break;
	            
	        case MotionEvent.ACTION_UP://single finger up
	        	if(mode == TARGET){//if no movement was detected
	        		//pixel space 0,0 top left corner G.W, G.H bottom right corner
	        		//Pixel G.W/2 == World G.viewX
	        		//Pixel G.H/2 == World G.viewY
	        		//Use viewZ to calculate the limits of the world coordinates on screen
	        		//Using proportions calculate World targetX and targetY
	        		System.out.println("selected x: " + e.getX() + " y: " + e.getY());
	        	}
	        	mode = NONE;
	            break;
	            
	        case MotionEvent.ACTION_DOWN://single finger down
	        	mode = TARGET;//default case is that you want to target something
	        	break;
	            
	        case MotionEvent.ACTION_MOVE:
                G.velX = G.velY = 0;
                if(mode == TARGET && (x - prevX > .1 || x - prevX < -.1 || y - prevY > .1 || y - prevY < -.1)){//change default case of targeting to movement if significant movement was detected
                	mode = MOVE;
                }
	        	if(mode == MOVE){//single finger move
                    G.velX = -(x - prevX)/dt;
                    G.velY = (y - prevY)/dt;
	        	}
	        	else if(mode == ZOOM){//two finger movement
	        		float newDist = spacing(e);
	        		//System.out.println("newDist=" + newDist);
					if (newDist > 10f) {
						float scale = newDist / oldDist;
						//System.out.println("scale=" + scale);
						if(scale < 1){//zoom out
							if(G.viewZ + 1 <= G.viewZlimit)
								G.viewZ++;
						}
						else if(scale > 1){//zoom in
							if(G.viewZ - 1 >= 1)
								G.viewZ--;
						}
						oldDist = newDist;
					}
	        	}
	        	mGLSurfaceView.requestRender();
	            break;
        }
        prevX = x;
        prevY = y;
        startTime = System.currentTimeMillis();
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
	
	    // Create our Preview view and set it as the content of our Activity
	    mGLSurfaceView = new GameView(this);
	    mGLSurfaceView.setKeepScreenOn(true);
	    setContentView(mGLSurfaceView);
	    
	    G.mpGame = MediaPlayer.create(this, R.raw.game);
        G.mpGame.setLooping(true);
        G.mpGame.start();
	    //mGLSurfaceView.startGame();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    if(G.updater==null){
		    G.updater = new com.cjdesign.cjtd.game.Updater();//Initially paused
			G.updaterThread = new Thread(G.updater);
			G.updater.running = true;
			G.paused = false;
			G.updaterThread.start();
	    }
		
	    G.paused = false;
	    mGLSurfaceView.onResume();
	    if(G.mpGame!=null)
	    	G.mpGame.start();
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    
	    if(G.updater!=null){
		    G.updater.running = false;
		    Thread.currentThread();
			try {
				Thread.sleep(100);//make sure the updater's current iteration of run is finished
			} catch (InterruptedException e) {
				
			}
			G.updaterThread = null;
			G.updater = null;
	    }
		
	    G.paused = true;
	    mGLSurfaceView.onPause();
	    if(G.mpGame!=null)
	    	G.mpGame.pause();
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	    if(G.updater!=null){
	    	G.updater.running = false;
	    	
		    Thread.currentThread();
			try {
				Thread.sleep(100);//make sure the updater's current iteration of run is finished
			} catch (InterruptedException e) {
				
			}
			G.updaterThread = null;
			G.updater = null;
	    }
	    
	    if(G.mpGame!=null){
	    	G.mpGame.release();
	    	G.mpGame = null;
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MENU) {
	    	G.paused = true;
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
	        	                G.paused = false;
	        	                mGLSurfaceView.onResume();
	        	           }
	        	       });
	        	return builder.create();
	        default:
	            return null;
        }
    }
}
