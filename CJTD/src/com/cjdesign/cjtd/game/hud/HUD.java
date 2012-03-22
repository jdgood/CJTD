package com.cjdesign.cjtd.game.hud;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.globals.G;

public class HUD {
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
	private float vertices[] = {
			10, -5, 0,
    		-10, -5, 0,    		
    		10, 5, 0,
    		-10, 5, 0};

	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
				1.0f, 1.0f,
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f};
	
	/** The initial indices definition */	
	private byte indices[] = {
	    		0,1,3, 0,3,2};
	
	public HUD(){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		//gl.glDisable(GL10.GL_DEPTH_TEST);
		//gl.glDepthMask(false);
		
			gl.glLoadIdentity();
			GLU.gluLookAt(gl,
					0, 0, 10, //camera.pos.px, camera.pos.py, camera.pos.pz, //eye position
					0, 0, 0, //reference point
					0, 1, 0); //normal
			int textureResource = 0;
			if(G.state == G.STATE_DEFEAT){
				textureResource = R.drawable.defeat;
			}
			else if(G.state == G.STATE_VICTORY){
				textureResource = R.drawable.victory;
			}
			else{
				return;
			}
			//Bind our only previously generated texture in this case
			gl.glBindTexture(GL10.GL_TEXTURE_2D, G.textures.loadTexture(textureResource, gl));
			
			//Point to our buffers
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CCW);
			
			//Enable the vertex and texture state
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		//gl.glEnable(GL10.GL_DEPTH_TEST);
		//gl.glDepthMask(true);
		gl.glPopMatrix();
	}

}
