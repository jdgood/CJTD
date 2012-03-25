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
			
			//Change to orthogonal projection
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glViewport(0,0,(int)G.W,(int)G.H);

			gl.glOrthof(-G.W/2, G.W/2, -G.H/2, G.H/2, G.fNear, G.fFar);

			//Draw the HUD
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			//gl.glScalef(G.W/(Preferences.Get().mapWidth*3f), G.W/(Preferences.Get().mapWidth*3f), 1f);
			//gl.glTranslatef((Preferences.Get().mapWidth*3f/G.W)*G.W/2f-Preferences.Get().mapWidth,(Preferences.Get().mapWidth*3f/G.W)*G.H/2f-Preferences.Get().mapHeight,-minz-2f);
			
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

			//Return to a perspective projection
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glViewport(0,0,(int)G.W,(int)G.H);

			GLU.gluPerspective(gl, 45.0f, G.W/G.H, G.fNear, G.fFar);
		gl.glPopMatrix();
	}

}
