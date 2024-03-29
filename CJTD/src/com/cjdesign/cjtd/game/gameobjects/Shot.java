package com.cjdesign.cjtd.game.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.Vector2D;

public class Shot extends GameObject {
	private float vertices[] = {
    		G.gridSize/4, -G.gridSize/4, 0,
    		-G.gridSize/4, -G.gridSize/4, 0,    		
    		G.gridSize/4, G.gridSize/4, 0,
    		-G.gridSize/4, G.gridSize/4, 0};
	
	protected Vector2D dir;
	protected Tower owner;
	protected float traveled;
	
	public Shot(Vector2D dir, Tower owner) {
		super(G.BULLET_ID);
		this.dir = dir;
		this.owner = owner;
		
		x = owner.x;
		y = owner.y;
		z = owner.z+.5f;
		
		textureResource = R.drawable.shot;

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	
	public void update(float dt){
		x += dir.x * owner.getBulletSpeed() * dt;
		y += dir.y * owner.getBulletSpeed() * dt;
		traveled += owner.getBulletSpeed() * dt;
	}
	
	//returns true if it hits
	public boolean hit(){
		for(Creep c : G.Creeps){
			if(FloatMath.sqrt((float)Math.pow(c.x - x, 2) + (float)Math.pow(c.y - y, 2)) < G.ANDROID_CREEP_SIZE){//checks for hits putting a hit radius of the creep size around a creep
				c.takeDamage(owner.getDamage());
				return true;
			}
		}
		return false;
	}
	
	//returns true if it reaches range
	public boolean range(){
		if(traveled > owner.getRange()){
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
