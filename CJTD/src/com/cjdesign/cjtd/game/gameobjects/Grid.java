package com.cjdesign.cjtd.game.gameobjects;

import javax.microedition.khronos.opengles.GL10;
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
		for(int i = 0; i < ySize; i++){
			for(int j = 0; j < xSize; j++){
				GroundArray[i][j].update(dt);
			}
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
			//gl.glScalef(15, 15, 1);
			
			//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
			//gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
			//gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
			//gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);	//Z
			
			drawGrid(gl);
			drawTowers(gl);//called separately for alpha purposes
			
		gl.glPopMatrix();
	}
	
	private void drawGrid(GL10 gl){
		gl.glPushMatrix();
			for(int i = 0; i < ySize; i++){
				for(int j = 0; j < xSize; j++){
					GroundArray[i][j].draw(gl);
				}
			}
		gl.glPopMatrix();
	}
	
	private void drawTowers(GL10 gl){
		gl.glPushMatrix();
			for(int i = 0; i < ySize; i++){
				for(int j = 0; j < xSize; j++){
					GroundArray[i][j].drawTower(gl);
					gl.glTranslatef(0, 0, .01f);//to avoid z-fighting on blending
				}
			}
		gl.glPopMatrix();
	}
}
