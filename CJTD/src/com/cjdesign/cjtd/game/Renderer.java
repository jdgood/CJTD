package com.cjdesign.cjtd.game;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.cjdesign.cjtd.game.ai.Path;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Grid;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.game.gameobjects.grid.GroundDirt;
import com.cjdesign.cjtd.game.gameobjects.grid.GroundGrass;
import com.cjdesign.cjtd.game.gameobjects.towers.AlphaObject;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.game.textures.GLTextures;
import com.cjtd.globals.*;

import android.opengl.GLU;

/**
 * Render a pair of tumbling cubes.
 */

public class Renderer implements GLSurfaceView.Renderer {
    private long startTime, endTime;
    
	public Renderer(Context context) {
		G.gameContext = context;
		G.viewX = 0;
		G.viewY = 0;
		G.viewZ = 10;
		G.Creeps = new ArrayList<Creep>();
		
    	//doing this is superhacks! create texture container
    	G.textures = new GLTextures(G.gameContext);
    	
    	//create demo gameworld(create a level importer later)
    	int xSize = 10;
    	int ySize = 5;
    	Ground[][] gridArray = new Ground[xSize][ySize];
    	boolean grass = true;
    	
    	float startx = -xSize*G.gridSize/2+G.gridSize/2;
    	float starty = ySize*G.gridSize/2-G.gridSize/2;
    	
    	G.viewXlimit = xSize*G.gridSize/2+G.gridSize;
    	G.viewYlimit = ySize*G.gridSize/2-G.gridSize;
    	
    	for(int i = 0; i < ySize; i++){
    		if(i%2==0){
    			grass = true;
    		}
    		else{
    			grass = false;
    		}
    		
    		for(int j = 0; j < xSize; j++){
    			if(grass){
    				gridArray[j][i] = new GroundGrass(j,i,startx+G.gridSize*j,starty-G.gridSize*i);
    			}
    			else{
    				gridArray[j][i] = new GroundDirt(j,i,startx+G.gridSize*j,starty-G.gridSize*i);
    			}
    			grass = !grass;
    		}
    	}
    	
    	G.level = new Grid(gridArray, xSize, ySize, 0, 4, 9, 4);
    	G.path = new Path(xSize, ySize);
    	
    	//adding towers for sanity checks and to test ai in a bit
    	new AlphaObject(gridArray[0][0]);
    	gridArray[1][1].setTower(new Tower(gridArray[1][1]));
    	new AlphaObject(gridArray[1][2]);
    	new AlphaObject(gridArray[1][3]);
    	
    	
    	new AlphaObject(gridArray[3][1]);
    	new AlphaObject(gridArray[3][2]);
    	gridArray[3][3].setTower(new Tower(gridArray[3][3]));
    	new AlphaObject(gridArray[3][4]);
    	
    	new AlphaObject(gridArray[5][0]);
    	new AlphaObject(gridArray[5][1]);
    	new AlphaObject(gridArray[5][2]);
    	new AlphaObject(gridArray[5][3]);
    	
    	new AlphaObject(gridArray[7][1]);
    	new AlphaObject(gridArray[7][2]);
    	new AlphaObject(gridArray[7][3]);
    	new AlphaObject(gridArray[7][4]);
    	
    	new AlphaObject(gridArray[9][0]);
    	new AlphaObject(gridArray[9][1]);
    	new AlphaObject(gridArray[9][2]);
    	new AlphaObject(gridArray[9][3]);
    	
    	G.Creeps.add(new Creep());
    	
    	//setup game start time
	    startTime = System.currentTimeMillis();
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

    public void onDrawFrame(GL10 gl) {
        endTime = System.currentTimeMillis();
        float dt = (float)(endTime - startTime)/1000f;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		
		GLU.gluLookAt(gl,
				G.viewX, G.viewY, G.viewZ, //camera.pos.px, camera.pos.py, camera.pos.pz, //eye position
				G.viewX, G.viewY, -1, //reference point
				0, 1, 0); //normal
		
		G.level.draw(gl); //draw grid first
		
		G.level.drawTowers(gl); //draw towers
		
		G.level.drawShots(gl); //draw bullets
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		for(Creep c : G.Creeps){
			c.draw(gl); //draw creeps last
		}
		gl.glDisable(GL10.GL_BLEND);
		
		if(!G.paused){
			for(Creep c : G.Creeps){
				c.update(dt);
			}
			update(dt);
		}
		
		G.level.update(dt);
		
        startTime = System.currentTimeMillis();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) {
			height = 1;
		}

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 45.0f, G.W / G.H, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
    
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {		    	
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.53f, 0.81f, 0.92f, 1.0f); //Sky Blue Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}
    
    public void onPause(){
    	G.paused = true;
    }
    
    public void onResume(){
    	G.paused = false;
    }
}