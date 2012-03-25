package com.cjdesign.cjtd;

import javax.microedition.khronos.opengles.GL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cjdesign.cjtd.game.GameView;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.*;

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
	
	float p2wx(float x) {
		return 2 * (2*G.W*x) / (G.H*(G.W-1) - G.W / G.H);
	}
	
	float p2wy(float y) {
		return 2 * (2*y / (G.H-1) - 1);
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
	        		float[] out = unproject(e.getX(), G.viewport[3] - e.getY(), distanceToDepth(G.viewZ - G.gridDepth));//gets the x,y coordinate
		        	if(G.state == G.STATE_PREPARATION){
	        			Ground g = G.level.getGround(out[0], out[1]);//gets the ground piece associated with the x,y found above
		        		if(g == null){//pressed outside of grid
		        			System.out.println("No ground chosen");
		        		}
		        		else if(g.isOccupied() || g.isTrapped()){//pressed on an occupied ground
		        			System.out.println("g.level.GroundArray[" + g.getxPos() + "][" + g.getyPos() + "] is occupied. Would you like to upgrade it?");
		        		}
		        		else{//pressed on an open ground
		        		    // TODO gui to select tower
		        		    new Tower(g);
		        			//System.out.println("You may build on g.level.GroundArray[" + g.getxPos() + "][" + g.getyPos() + "]");
		        		}
		        	}
		        	if(G.state == G.STATE_BATTLE && G.gamestate.Mode == G.MODE_OVERWATCH){//mid wave, time to pew pew the creeps
		        		System.out.println("Pew, pew at " + out[0] + ", " + out[1]);
		        	}
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
	
	    // Create our view and set it as the content of our Activity
	    mGLSurfaceView = new GameView(this);
	    
	   mGLSurfaceView.setGLWrapper(new GameView.GLWrapper(){  
	        public GL wrap(GL gl)  
	        {  
	            System.out.println("Wrapping");
	            return new MatrixTrackingGL(gl);  
	        }  
	    }); 
	    
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
	    if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
	    	G.paused = true;
	    	mGLSurfaceView.onPause();
	    	showDialog(DIALOG_PAUSE_ID);
	    	return true;
	    }
	    else if(keyCode == KeyEvent.KEYCODE_MENU) {
	    	if(!G.Waves.isEmpty() && G.Creeps.isEmpty()){//still waves to go for this level and current wave is finished
	    		G.state = G.STATE_BATTLE;
	    		G.Creeps = G.Waves.get(0);
	    		G.Waves.remove(0);
	    	}
	    	return true;
    	}
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
	
	public float[] unproject(float rx, float ry, float rz) {
		float[] xyzw = {0,0,0,0};
		
		GLU.gluUnProject(rx, ry, rz, G.mg.mModelView, 0, G.mg.mProjection, 0, G.viewport, 0, xyzw, 0);
		
		xyzw[0] /= xyzw[3];
		xyzw[1] /= xyzw[3];
		xyzw[2] /= xyzw[3];
		xyzw[3] = 1;
		
		return xyzw;
	}

	public float distanceToDepth(float distance) {
	    return ((1/G.fNear) - (1/distance))/((1/G.fNear) - (1/G.fFar));
	}
}
