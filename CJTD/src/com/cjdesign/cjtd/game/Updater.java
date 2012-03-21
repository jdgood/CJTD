package com.cjdesign.cjtd.game;

import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.globals.G;

public class Updater implements Runnable {
	private long startTime, endTime;
	public boolean running = false;
	private final long ForceFPS = 1000/60;

	public void run() {
		startTime = System.currentTimeMillis();
		
		while(running){
	    	endTime = startTime;
	    	startTime = System.currentTimeMillis();
	    	float dt = (float)(startTime - endTime)/1000f;
	    	
	    	if(!G.paused){
    			doLogic(dt);
	    	}
	    	try{//forces fps
				Thread.currentThread();
				Thread.sleep((long) (ForceFPS - dt));
			}
			catch(InterruptedException ie){
				//If this thread was interrupted by another thread 
			}
		}
	}
	
	public void doLogic(float dt){
		for(Creep c : G.Creeps){
			c.update(dt);
		}
		update(dt);
		G.level.update(dt);
	}
	
	/** dt : time in sec */
    private void update(float dt){
        if(G.viewX + G.velX * dt < G.viewXlimit && G.viewX + G.velX * dt > -G.viewXlimit){
            G.viewX += G.velX * dt;
        } else {
            G.velX = 0;
        }
        if(G.viewY + G.velY * dt < G.viewYlimit && G.viewY + G.velY * dt > -G.viewYlimit){
            G.viewY += G.velY * dt;
        } else {
            G.velY = 0;
        }

        if(G.velX < G.friction * dt && G.velX > -G.friction * dt)
            G.velX = 0;
        else if(G.velX > 0)
            G.velX -= G.friction * dt;
        else if(G.velX < 0)
            G.velX += G.friction * dt;

        if(G.velY < G.friction * dt && G.velY > -G.friction * dt)
            G.velY = 0;
        else if (G.velY > 0)
            G.velY -= G.friction * dt;
        else if(G.velY < 0)
            G.velY += G.friction * dt;
        
        
    }

}
