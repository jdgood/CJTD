package com.cjdesign.cjtd.game;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.textures.GLTextures;
import com.cjdesign.cjtd.globals.*;
import com.cjdesign.cjtd.utils.MatrixTrackingGL;

import android.opengl.GLU;

public class Renderer implements GLSurfaceView.Renderer {
    public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		
		/*GLU.gluLookAt(gl,
				G.viewX, G.viewY, G.viewZ, //camera.pos.px, camera.pos.py, camera.pos.pz, //eye position
				G.viewX, G.viewY, -1, //reference point
				0, 1, 0); //normal*/
		gl.glTranslatef(-G.viewX,-G.viewY,-G.viewZ);
		
		G.level.draw(gl); //draw grid first
		
		G.level.drawTowers(gl); //draw towers
		
		G.level.drawTraps(gl); // draw traps
		
		G.level.drawShots(gl); //draw bullets
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		
		ArrayList<Creep> clist = new ArrayList<Creep>(G.Creeps);//this should avoid concurrent exception
		for(Creep c : clist){
			c.draw(gl); //draw creeps last
		}
		gl.glDisable(GL10.GL_BLEND);
		
		getCurrentProjection(gl);
		getCurrentModelView(gl);
		
		G.hud.draw(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	G.W = width;
		if(height == 0) {
			height = 1;
		}
		G.H = height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 45.0f, G.W / G.H, 0.1f, 100.0f);
		//gluPerspective(gl, 45.0f, G.W / G.H, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
    
    /*public void gluPerspective(GL10 gl, float fovy, float aspect, float zNear, float zFar)
    {
       float xmin, xmax, ymin, ymax;

       ymax = G.viewZ - G.gridDepth * (float)Math.tan(fovy * Math.PI / 360.0);
       ymin = -ymax;
       xmin = ymin * aspect;
       xmax = ymax * aspect;
       xmin+=G.viewX;
       xmax+=G.viewX;
       ymin+=G.viewY;
       ymin+=G.viewY;
       
       System.out.println("Distance from camera " + (G.viewZ - G.gridDepth));
       System.out.println("xmin: " + xmin + " xmax: " + xmax + " ymin: " + ymin + " ymax: " + ymax);
       
       ymax = zNear * (float)Math.tan(fovy * Math.PI / 360.0);
       ymin = -ymax;
       xmin = ymin * aspect;
       xmax = ymax * aspect;


       gl.glFrustumf(xmin, xmax, ymin, ymax, zNear, zFar);
    }*/
    
    public void getCurrentModelView(GL10 gl)
    {
 		getMatrix(gl, GL10.GL_MODELVIEW, G.lastModelViewMat);
    }

    public void getCurrentProjection(GL10 gl)
    {
        getMatrix(gl, GL10.GL_PROJECTION, G.lastProjectionMat);
    }

    private void getMatrix(GL10 gl, int mode, float[] mat)
    {
        MatrixTrackingGL gl2 = new MatrixTrackingGL(gl);
        gl2.glMatrixMode(mode);
        gl2.getMatrix(mat, 0);
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