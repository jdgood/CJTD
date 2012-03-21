package com.cjdesign.cjtd.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

import com.cjdesign.cjtd.game.ai.Path;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.textures.GLTextures;
import com.cjtd.globals.*;

import android.opengl.GLU;

/**
 * Render a pair of tumbling cubes.
 */

public class Renderer implements GLSurfaceView.Renderer {
    public void onDrawFrame(GL10 gl) {
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
    	G.textures = new GLTextures(G.gameContext);
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.53f, 0.81f, 0.92f, 1.0f); //Sky Blue Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}
}