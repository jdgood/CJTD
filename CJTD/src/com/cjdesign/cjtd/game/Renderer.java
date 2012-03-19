package com.cjdesign.cjtd.game;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.cjdesign.cjtd.game.gameobjects.*;
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
		G.objs = new ArrayList<GameObject>();
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
		
		for(GameObject go : G.objs){
			go.draw(gl);
		}
		
		if(!G.paused){
			for(GameObject go : G.objs){
				go.update(dt);
			}
			update(dt);
		}
		
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
    	//want to move the following to the constructor because this is called everytime the quit dialog is cancelled except not sure how to do get the FL10 gl thing...
    	G.textures = new GLTextures(gl, G.gameContext);
    	
    	int xSize = 5;
    	int ySize = 5;
    	
    	Ground[][] gridArray = new Ground[xSize][ySize];
    	boolean grass = true;
    	for(int i = 0; i < ySize; i++){
    		if(i%2==0){
    			grass = true;
    		}
    		else{
    			grass = false;
    		}
    		
    		for(int j = 0; j < xSize; j++){
    			if(grass){
    				gridArray[j][i] = new GroundGrass(j,i);
    			}
    			else{
    				gridArray[j][i] = new GroundDirt(j,i);
    			}
    			grass = !grass;
    		}
    	}
    	
    	G.objs.add(new Grid(gridArray, xSize, ySize));
    	
    	gridArray[1][1].setTower(new Tower(gridArray[1][1]));
    	gridArray[3][3].setTower(new Tower(gridArray[3][3]));
    	
    	G.objs.add(new AlphaObject(0,0));
    	
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
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