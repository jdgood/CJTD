package com.cjdesign.cjtd.game.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjtd.globals.G;

public class Tower extends GameObject {
	
	private float zrot;

	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;

	/** 
	 * The initial vertex definition
	 * 
	 * Note that each face is defined, even
	 * if indices are available, because
	 * of the texturing we want to achieve 
	 */	
    private float vertices[] = {
    					//Vertices according to faces
			    		-1.0f, -1.0f, 1.0f, //Vertex 0
			    		1.0f, -1.0f, 1.0f,  //v1
			    		-1.0f, 1.0f, 1.0f,  //v2
			    		1.0f, 1.0f, 1.0f,   //v3
			    		
			    		1.0f, -1.0f, 1.0f,	//...
			    		1.0f, -1.0f, -1.0f,    		
			    		1.0f, 1.0f, 1.0f,
			    		1.0f, 1.0f, -1.0f,
			    		
			    		1.0f, -1.0f, -1.0f,
			    		-1.0f, -1.0f, -1.0f,    		
			    		1.0f, 1.0f, -1.0f,
			    		-1.0f, 1.0f, -1.0f,
			    		
			    		-1.0f, -1.0f, -1.0f,
			    		-1.0f, -1.0f, 1.0f,    		
			    		-1.0f, 1.0f, -1.0f,
			    		-1.0f, 1.0f, 1.0f,
			    		
			    		-1.0f, -1.0f, -1.0f,
			    		1.0f, -1.0f, -1.0f,    		
			    		-1.0f, -1.0f, 1.0f,
			    		1.0f, -1.0f, 1.0f,
			    		
			    		-1.0f, 1.0f, 1.0f,
			    		1.0f, 1.0f, 1.0f,    		
			    		-1.0f, 1.0f, -1.0f,
			    		1.0f, 1.0f, -1.0f,
											};
    
    /** The initial texture coordinates (u, v) */	
    private float texture[] = {    		
			    		//Mapping coordinates for the vertices
			    		0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f, 
			    		
			    		0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f,
			    		
			    		0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f,
			    		
			    		0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f,
			    		
			    		0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f,
			    		
			    		0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f,

			    							};
        
    /** The initial indices definition */	
    private byte indices[] = {
    					//Faces definition
			    		0,1,3, 0,3,2, 			//Face front
			    		4,5,7, 4,7,6, 			//Face right
			    		8,9,11, 8,11,10, 		//... 
			    		12,13,15, 12,15,14, 	
			    		16,17,19, 16,19,18, 	
			    		20,21,23, 20,23,22, 	
    										};

	public Tower(float x, float y) {
		super(G.TOWER_ID);
		
		this.x = x; 
		this.y = y;
		this.z = G.gridDepth-.1f;
		
		textureID = G.textures.loadTexture(R.drawable.tower);

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
		zrot -= 1;
		if(zrot == -360){
			zrot = 0;
		}
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
			gl.glTranslatef(x, y, z);
			gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);
		
			//Bind our only previously generated texture in this case
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			
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
