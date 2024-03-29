package com.cjdesign.cjtd.game.gameobjects.grid;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.globals.G;

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
		for(int i = 0; i < ySize; i++){
			for(int j = 0; j < xSize; j++){
				GroundArray[j][i].draw(gl);
			}
		}
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
	
	public void drawShots(GL10 gl){
		gl.glPushMatrix();
			for(int i = 0; i < ySize; i++){
				for(int j = 0; j < xSize; j++){
					GroundArray[j][i].drawShots(gl);
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
    
    public Ground getGround(float x, float y) {
    	for(int i = 0; i < ySize; i++){
			for(int j = 0; j < xSize; j++){
				if(inBounds(x, y, getGround(j, i))){
					return getGround(j, i);
				}
			}
		}
        
        return null;
    }
    private boolean inBounds(float x, float y, Ground g){
    	return x <= g.x + G.gridSize/2 && //x less than its max x bound
    		   x >= g.x - G.gridSize/2 && //x greater than its min x bound
    		   y <= g.y + G.gridSize/2 && //y less than its max y bound
    		   y >= g.y - G.gridSize/2;   //y greater than its min y bound
    }

    public void drawTraps(GL10 gl) {
        gl.glPushMatrix();
            for(int i = 0; i < ySize; i++){
                for(int j = 0; j < xSize; j++){
                    GroundArray[j][i].drawTrap(gl);
                    gl.glTranslatef(0, 0, .01f);//to avoid z-fighting on blending
                }
            }
        gl.glPopMatrix();
    }
}
