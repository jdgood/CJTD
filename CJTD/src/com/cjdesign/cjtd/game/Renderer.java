package com.cjdesign.cjtd.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.cjtd.globals.*;

import android.opengl.GLU;

/**
 * Render a pair of tumbling cubes.
 */

public class Renderer implements GLSurfaceView.Renderer {
    private long startTime, endTime;
    
	public Renderer(Context context) {
		this.context = context;
		G.viewX = 0;
		G.viewY = 0;
		G.viewZ = -10;
		mCube = new Cube();
	    startTime = System.currentTimeMillis();
	}
    
	/** dt : time in sec */
    private void update(float dt){
    	if(!G.paused){
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

    public void onDrawFrame(GL10 gl) {
        endTime = System.currentTimeMillis();
        float dt = (float)(endTime - startTime)/1000f;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		
		GLU.gluLookAt(gl,
				G.viewX, G.viewY, G.viewZ, //camera.pos.px, camera.pos.py - 20, camera.pos.pz, //eye position
				G.viewX, G.viewY, 1, //reference point
				0, 1, 0); //normal
		
		gl.glPushMatrix();
			gl.glTranslatef(0.0f, 0.0f, 15.0f);
			gl.glScalef(15, 15, 1);
			
			//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
			//gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			//gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
			//gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);	//Z
					
			mCube.draw(gl);
		gl.glPopMatrix();
        update(dt);
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
		mCube.loadGLTexture(gl, this.context);
		
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
    
    private Cube mCube;
    //private Grid bg;
    private Context context;
    //private float xrot;
	//private float yrot;
	//private float zrot;
}