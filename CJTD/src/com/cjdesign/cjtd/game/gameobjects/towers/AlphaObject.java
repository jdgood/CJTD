package com.cjdesign.cjtd.game.gameobjects.towers;

import java.nio.*;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjtd.globals.G;

public class AlphaObject extends Tower{	
	private float vertices[] = {
    		G.gridSize/2, -G.gridSize/2, -G.gridSize/2f,
    		-G.gridSize/2, -G.gridSize/2, -G.gridSize/2,    		
    		G.gridSize/2, G.gridSize/2, -G.gridSize/2,
    		-G.gridSize/2, G.gridSize/2, -G.gridSize/2};

	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
	    		0.0f, 0.0f,
	    		0.0f, 1.0f,
	    		1.0f, 0.0f,
	    		1.0f, 1.0f};
	
	/** The initial indices definition */	
	private byte indices[] = {
	    		0,1,3, 0,3,2};
	
	public AlphaObject(Ground g) {
		super(g);
		textureResource = R.drawable.lightsource;
		
		range = 5;
		frequency = .25f;
		bulletSpeed = 25;
		
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

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 */
	public void draw(GL10 gl) {
		gl.glPushMatrix();
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			super.draw(gl);
			gl.glDisable(GL10.GL_BLEND);
		gl.glPopMatrix();
	}
}
