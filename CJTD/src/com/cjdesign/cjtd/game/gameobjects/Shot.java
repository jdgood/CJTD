package com.cjdesign.cjtd.game.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.Vector2D;

public class Shot extends GameObject {
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
	private float vertices[] = {
    		G.gridSize/4, -G.gridSize/4, -G.gridSize/4f,
    		-G.gridSize/4, -G.gridSize/4, -G.gridSize/4,    		
    		G.gridSize/4, G.gridSize/4, -G.gridSize/4,
    		-G.gridSize/4, G.gridSize/4, -G.gridSize/4};

	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
	    		0.0f, 0.0f,
	    		0.0f, 1.0f,
	    		1.0f, 0.0f,
	    		1.0f, 1.0f};
	
	/** The initial indices definition */	
	private byte indices[] = {
	    		0,1,3, 0,3,2};
	
	public Vector2D dir;
	public Tower owner;
	public float traveled;
	
	private int damage;
	
	public Shot(Vector2D dir, Tower owner) {
		super(G.BULLET_ID);
		this.dir = dir;
		this.owner = owner;
		
		damage = 5;
		
		x = owner.x;
		y = owner.y;
		z = owner.z;
		
		textureResource = R.drawable.shot;

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
	
	public void update(float dt){
		x += dir.x * owner.bulletSpeed * dt;
		y += dir.y * owner.bulletSpeed * dt;
		traveled += owner.bulletSpeed * dt;
	}
	
	//returns true if it hits
	public boolean hit(){
		for(Creep c : G.Creeps){
			if(FloatMath.sqrt((float)Math.pow(c.x - x, 2) + (float)Math.pow(c.y - y, 2)) < G.ANDROID_CREEP_SIZE){//checks for hits putting a hit radius of the creep size around a creep
				c.takeDamage(damage);
				return true;
			}
		}
		return false;
	}
	
	//returns true if it reaches range
	public boolean range(){
		if(traveled > owner.range){
			return true;
		}
		return false;
	}
	
	public void draw(GL10 gl){
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glPushMatrix();
			gl.glTranslatef(x, y, z);
		
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
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_BLEND);
	}
}
