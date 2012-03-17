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
	public Renderer(Context context) {
		this.context = context;
		G.viewX = 0;
		G.viewY = 0;
		G.viewZ = -10;
		mCube = new Cube();
	}
    
    private void update(float dt){
    	if(!G.paused){
    		xrot += 0.3f;
    		yrot += 0.2f;
    		zrot += 0.4f;
    	}
	}

    public void onDrawFrame(GL10 gl) {
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		GLU.gluLookAt(gl,
				G.viewX, G.viewY, G.viewZ, //camera.pos.px, camera.pos.py - 20, camera.pos.pz, //eye position
				G.viewX, G.viewY, 0, //reference point
				0, 1, 0); //normal
		
		gl.glPushMatrix();
			//Drawing
			//gl.glTranslatef(0.0f, 0.0f, -5.0f);		//Move 5 units into the screen
			gl.glTranslatef(0.0f, 0.0f, 5.0f);		//Move 5 units into the screen
			//gl.glScalef(0.8f, 0.8f, 0.8f); 			//Scale the Cube to 80 percent, otherwise it would be too large for the screen
			gl.glScalef(10, 10, 10);
			
			//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
			//gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			//gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
			//gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);	//Z
					
			mCube.draw(gl);							//Draw the Cube	
		gl.glPopMatrix();
		update(1);
        /*
         * Now we're ready to draw some 3D objects
         */
        /*gl.glMatrixMode(GL10.GL_MODELVIEW);
        
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3.0f);
        gl.glRotatef(mAngle,        0, 1, 0);
        gl.glRotatef(mAngle*0.25f,  1, 0, 0);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glColor4f(1, 1, 1, 1);	
        
        mCube.draw(gl);

        gl.glRotatef(mAngle*2.0f, 0, 1, 1);
        gl.glTranslatef(0.5f, 0.5f, 0.5f);

        mCube.draw(gl);
        
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -40.0f);
        bg.draw(gl);

        update(1);*/
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, G.W / G.H, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
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
    private Grid bg;
    private Context context;
    private float xrot;
	private float yrot;
	private float zrot;
}