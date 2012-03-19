package com.cjdesign.cjtd.game.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjtd.globals.G;

public class Grid extends GameObject {
	//private float xrot;
	//private float yrot;
	private float zrot;
	
	private Ground GroundArray[][];
	private int xSize, ySize;  


	/**
	 * The Grid constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Grid(Ground[][] groundArray, int xSize, int ySize) {
		super(G.GRID_ID);
		
		this.GroundArray = groundArray;
		this.xSize = xSize;
		this.ySize = ySize;
		
		x = y = 0;
		z = G.gridDepth;
	}
	
	public void update(float dt){
		zrot += 1;
		if(zrot == 360){
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
			gl.glTranslatef(x-xSize+1, y+ySize-1, z);
			//gl.glScalef(15, 15, 1);
			
			//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
			//gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			//gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
			//gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);	//Z
			
			for(int i = 0; i < ySize; i++){
				gl.glPushMatrix();
					for(int j = 0; j < xSize; j++){
						gl.glPushMatrix();
							GroundArray[j][i].draw(gl);
						gl.glPopMatrix();
						gl.glTranslatef(xSize/2, 0, 0);
					}
				gl.glPopMatrix();
				gl.glTranslatef(0, -ySize/2, 0);
			}
		gl.glPopMatrix();
	}
}
