package com.cjdesign.cjtd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cjdesign.cjtd.game.*;
import com.cjtd.globals.G;

public class MainGame extends Activity{
	private static final int DIALOG_PAUSE_ID = 0;
	private GLSurfaceView mGLSurfaceView;
	private float prevX;
	private float prevY;
	
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
	
	/*@Override 
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
    
                float dx = x - G.viewX;
                float dy = y - G.viewY;
    
                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                  dx = dx * -1 ;
                }
    
                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                  dy = dy * -1 ;
                }
              
                mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;
                requestRender();
        }

        G.viewX = x;
        G.viewY = y;
        return true;
    }*/
	
	/*@Override public boolean onTrackballEvent(MotionEvent e) {
        mRenderer.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR;
        mRenderer.mAngleY += e.getY() * TRACKBALL_SCALE_FACTOR;
        requestRender();
        return true;
    }*/

    @Override public boolean onTouchEvent(MotionEvent e) {
        float x = p2wx(e.getX());
        float y = p2wy(e.getY());
        switch (e.getAction()) {
	        case MotionEvent.ACTION_MOVE:
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
	            mGLSurfaceView.requestRender();
	            break;
        }
        prevX = x;
        prevY = y;
        return true;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // Create our Preview view and set it as the content of our
	    // Activity
	    mGLSurfaceView = new GLSurfaceView(this);
	    mGLSurfaceView.setRenderer(new Renderer(this));
	    setContentView(mGLSurfaceView);
	}
	
	@Override
	protected void onResume() {
	    // Ideally a game should implement onResume() and onPause()
	    // to take appropriate action when the activity looses focus
	    super.onResume();
	    mGLSurfaceView.onResume();
	}
	
	@Override
	protected void onPause() {
	    // Ideally a game should implement onResume() and onPause()
	    // to take appropriate action when the activity looses focus
	    super.onPause();
	    mGLSurfaceView.onPause();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
	    	mGLSurfaceView.onPause();
	    	showDialog(DIALOG_PAUSE_ID);
	    	//moveTaskToBack(true);
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
    	final String difficulty;
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
