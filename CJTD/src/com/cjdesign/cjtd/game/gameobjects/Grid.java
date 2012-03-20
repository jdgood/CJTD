package com.cjdesign.cjtd.game.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import com.cjtd.globals.G;

public class Grid extends GameObject {
	//private float xrot;
	//private float yrot;
	private float zrot;
	
	private Ground GroundArray[][];
	private int xSize, ySize;  
	int startX, startY, endX, endY;

	/**
	 * The Grid constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Grid(Ground[][] groundArray, int xSize, int ySize, int startX, int startY, int endX, int endY) {
		super(G.GRID_ID);
		
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		
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
				GroundArray[j][i].update(dt);
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
			gl.glPushMatrix();
			for(int i = 0; i < ySize; i++){
				for(int j = 0; j < xSize; j++){
					GroundArray[j][i].draw(gl);
				}
			}
		gl.glPopMatrix();
	}
	
	public void drawTowers(GL10 gl){
		gl.glPushMatrix();
			for(int i = 0; i < ySize; i++){
				for(int j = 0; j < xSize; j++){
					GroundArray[j][i].drawTower(gl);
					gl.glTranslatef(0, 0, .01f);//to avoid z-fighting on blending
				}
			}
		gl.glPopMatrix();
	}
    
    public Ground getStart(){
        return GroundArray[startX][startY];
    }
    
    public Ground getEnd(){
        return GroundArray[endX][endY];
    }
    
    public Ground getGround(int x, int y) throws IndexOutOfBoundsException {
        return GroundArray[x][y];
    }
}
