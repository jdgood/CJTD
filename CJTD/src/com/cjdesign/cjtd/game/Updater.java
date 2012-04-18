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
	    	
	    	G.damageTimer -= dt;
	    	
	    	if(!G.paused){
	    		if(G.health <= 0){//ran out of health or whatever set somewhere within update method
	    			G.state = G.STATE_DEFEAT;//indicates death/game over screen in renderer
	    			break;//updates no longer needed at this point
	    		}
	    		else if(!G.Creeps.isEmpty()){//current wave still going. this implies state == state_battle, which is set when a wave is launched
	    			for(int i = 0; i < G.playSpeed; i++){
	    				doLogic(dt);
	    			}
	    		}
	    		else if(G.Waves.isEmpty() && G.Creeps.isEmpty()){//last wave of the level launched and current/last wave is finished
	    			G.state = G.STATE_VICTORY;//indicates victory/level over screen in renderer
	    			break;//updates no longer needed at this point
	    		}
	    		else{//waiting for next wave to launch
	    			if(G.state != G.STATE_INITIAL){//initial gives user unlimited time to do initial setup of towers
		    			G.nextWave -= dt;
		    			if(G.state == G.STATE_BATTLE){//wave just finished, clear out all bullets and enemies
		    				doLogic(5);
		    			}
		    			G.state = G.STATE_PREPARATION;
	    			}
		    		if(G.nextWave <= 0){
		    			G.state = G.STATE_BATTLE;
		    			G.playSpeed = 1;
			    		G.Creeps = G.Waves.get(0);
			    		G.Waves.remove(0);
			    		if(G.timeBetweenWaves.size() != 0){
			    			G.nextWave= G.timeBetweenWaves.get(0);
			    			G.timeBetweenWaves.remove(0);
			    		}
	    			}
	    		}
	    		updateCamera(dt);
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
		if(!G.deadCreeps.isEmpty()) {
		    G.Creeps.removeAll(G.deadCreeps);
		    G.deadCreeps.clear();
		}
		G.level.update(dt);
	}
	
	/** dt : time in sec */
    private void updateCamera(float dt){
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
