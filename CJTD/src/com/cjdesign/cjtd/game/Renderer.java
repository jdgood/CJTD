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
		G.viewZ = -10;
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
				G.viewX, G.viewY, G.viewZ, //camera.pos.px, camera.pos.py - 20, camera.pos.pz, //eye position
				G.viewX, G.viewY, 1, //reference point
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
		//Load the texture for the cube once during Surface creation
    	/*for(GameObject go : G.objs){
    		((Grid)go).loadGLTexture(gl, G.gameContext);
    	}*/
    	
    	G.textures = new GLTextures(gl, G.gameContext);
    	Ground[][] gridArray = new Ground[5][5];
    	gridArray[0][0] = new GroundDirt();
    	gridArray[0][1] = new GroundGrass();
    	gridArray[0][2] = new GroundDirt();
    	gridArray[0][3] = new GroundGrass();
    	gridArray[0][4] = new GroundDirt();
    	gridArray[1][0] = new GroundGrass();
    	gridArray[1][1] = new GroundDirt();
    	gridArray[1][1].occupied = true;
    	gridArray[1][2] = new GroundGrass();
    	gridArray[1][3] = new GroundDirt();
    	gridArray[1][4] = new GroundGrass();
    	gridArray[2][0] = new GroundDirt();
    	gridArray[2][1] = new GroundGrass();
    	gridArray[2][2] = new GroundDirt();
    	gridArray[2][3] = new GroundGrass();
    	gridArray[2][4] = new GroundDirt();
    	gridArray[3][0] = new GroundGrass();
    	gridArray[3][1] = new GroundDirt();
    	gridArray[3][2] = new GroundGrass();
    	gridArray[3][3] = new GroundDirt();
    	gridArray[3][3].occupied = true;
    	gridArray[3][4] = new GroundGrass();
    	gridArray[4][0] = new GroundDirt();
    	gridArray[4][1] = new GroundGrass();
    	gridArray[4][2] = new GroundDirt();
    	gridArray[4][3] = new GroundGrass();
    	gridArray[4][4] = new GroundDirt();
    	G.objs.add(new Grid(gridArray, 5, 5));
    	G.objs.add(new Tower(2,2));
    	G.objs.add(new Tower(-2,-2));
    	
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