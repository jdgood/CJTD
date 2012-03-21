package com.cjdesign.cjtd.game.gameobjects.creeps;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.utils.Vector2D;
import com.cjtd.globals.G;

public class Creep extends GameObject {
	public Ground currentGoal;
	public Vector2D dir;
	public float speed;
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
	private float vertices[] = {
			G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE,
    		-G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE,    		
    		G.ANDROID_CREEP_SIZE, G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE,
    		-G.ANDROID_CREEP_SIZE, G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE};

	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
	    		0.0f, 0.0f,
	    		0.0f, 1.0f,
	    		1.0f, 0.0f,
	    		1.0f, 1.0f};
	
	/** The initial indices definition */	
	private byte indices[] = {
	    		0,1,3, 0,3,2};
	
	public Creep() {
		super(G.CREEP_ID);
		
		textureResource = R.drawable.android_sh;
		
		currentGoal = G.level.getStart();
		
		x = currentGoal.x - 2;
		y = currentGoal.y - 2;
		z = G.gridDepth+.1f;
		
		speed = 5;
		
		dir = new Vector2D(currentGoal.x - x, currentGoal.y - y);
		dir.normalize();//makes it so direction always implies a magnitude of 1
		
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
		//update based on shortest path and current position on path 
		//until implemented android will go off to space!!
	    float dx = dir.x * dt * speed;
	    float dy = dir.y * dt * speed;
        
		x+=dx;
		y+=dy;
		
		//if(within a certain radius of currentGoal){
		//update next goal(adjacent path node with lowest number)
		//update velocity vector
        
        if(1f >= FloatMath.sqrt((x-currentGoal.x)*(x-currentGoal.x)+(y-currentGoal.y)*(y-currentGoal.y)))
        {
            currentGoal = G.path.getNextGoal(currentGoal);
        
            dir = new Vector2D(currentGoal.x - x, currentGoal.y - y);
            dir.normalize();//makes it so direction always implies a magnitude of 1
        }
	}
	
	public void draw(GL10 gl){
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
	}

}
