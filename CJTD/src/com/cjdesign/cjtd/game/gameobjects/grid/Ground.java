package com.cjdesign.cjtd.game.gameobjects.grid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjtd.globals.G;

public abstract class Ground extends GameObject {
	public boolean occupied = false;
	public Tower occupiedBy = null;
	public int xPos,yPos;
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
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
	
	public Ground(int xpos, int ypos, float x, float y) {
		super(G.GRID_ID);
		
		this.x = x;
		this.y = y;
		z = G.gridDepth;
		
		this.xPos = xpos;
		this.yPos = ypos;

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
	
	public void setTower(Tower temp){
	    if(G.path.addTower(this))
	        occupiedBy = temp;
	}
	
	public void update(float dt){
		if(occupied){ 
			occupiedBy.update(dt);
		}
	}

	public void draw(GL10 gl) {
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
	
	public void drawTower(GL10 gl){
		if(occupied){
			occupiedBy.draw(gl);
		}
	}
	
	public void drawShots(GL10 gl){
		if(occupied){
			occupiedBy.drawShots(gl);
		}
	}
}
