package com.cjdesign.cjtd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cjdesign.cjtd.game.*;

public class MainGame extends Activity{
	private static final int DIALOG_PAUSE_ID = 0;
	private GLSurfaceView mGLSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // Create our Preview view and set it as the content of our
	    // Activity
	    mGLSurfaceView = new GLSurfaceView(this);
	    mGLSurfaceView.setRenderer(new CubeRenderer(false));
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
