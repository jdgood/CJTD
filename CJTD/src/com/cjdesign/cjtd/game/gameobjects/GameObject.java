package com.cjdesign.cjtd.game.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class GameObject {
	/** The buffer holding the vertices */
	protected FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	protected FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	protected ByteBuffer indexBuffer;
	
	/** The initial texture coordinates (u, v) */	
	protected float texture[] = {
			1.0f, 1.0f,
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f};
	
	/** The initial indices definition */	
	protected byte indices[] = {
	    		0,1,3, 0,3,2};
	
	public int Type;
	public int textureResource = -1;
	
	public float x, y, z;
	
	public GameObject(int type){
		Type = type;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	public abstract void draw(GL10 gl);
	public abstract void update(float dt);
}
