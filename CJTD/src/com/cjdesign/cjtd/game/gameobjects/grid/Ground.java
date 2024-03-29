package com.cjdesign.cjtd.game.gameobjects.grid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.game.gameobjects.traps.Trap;
import com.cjdesign.cjtd.globals.G;

public abstract class Ground extends GameObject {
    protected boolean occupied = false;
	protected Tower occupiedBy = null;
	
	protected boolean trapped = false;
	protected Trap trap = null;
	
	protected int xPos;
	protected int yPos;
	
	/** 
	 * Count number of creeps on tile. Could be used to check validity of 
	 * placement if building while creeps are attacking. 
	 * */
	private int creepCount = 0;
	
	private float vertices[] = {
    		G.gridSize/2, -G.gridSize/2, 0,
    		-G.gridSize/2, -G.gridSize/2, 0,    		
    		G.gridSize/2, G.gridSize/2, 0,
    		-G.gridSize/2, G.gridSize/2, 0};
	
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
	}
	
	public void update(float dt){
	    // Must check for null in case of race condition where pathfinding is
	    // testing validity of placement in other thread and has set occupied but
	    // not created tower yet.
		if(isOccupied() && getTower() != null){ 
			getTower().update(dt);
		} else if(isTrapped() && getTrap() != null) {
		    getTrap().update(dt);
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
        // Must check for null in case of race condition where pathfinding is
        // testing validity of placement in other thread and has set occupied but
        // not created tower yet.
		if(isOccupied() && getTower() != null){
			getTower().draw(gl);
		}
	}

    public void drawTrap(GL10 gl) {
        // Must check for null in case of race condition where pathfinding is
        // testing validity of placement in other thread and has set occupied but
        // not created tower yet.
        if(isTrapped() && getTrap() != null) {
            getTrap().draw(gl);
        }
    }
	
	public void drawShots(GL10 gl){
        // Must check for null in case of race condition where pathfinding is
        // testing validity of placement in other thread and has set occupied but
        // not created tower yet.
		if(isOccupied() && getTower() != null){
			getTower().drawShots(gl);
		}
	}
	
	public void onEnter(Creep c) {
	    creepCount++;
	    if(isTrapped() && getTrap() != null)
	        getTrap().onEnter(c);
	}
	
	public void onExit(Creep c) {
	    creepCount--;
	    if(isTrapped() && getTrap() != null)
	        getTrap().onExit(c);
	}

	/**
	 * Make sure this will not block before adding a tower
	 * @param tower the Tower to occupy this Ground
	 * 
	 * @return was it successful
	 */
    public boolean setTower(Tower tower){
        if(tower == null) {
            removeTower();
            return true;
        }
        if(isOccupied() || isTrapped() || 
                creepCount > 0 || G.level.getStart() == this)
            return false;
        if(G.path.addTower(this))
            this.occupiedBy = tower;
        return isOccupied();
    }
    
    /**
     * @return the Tower occupying this
     */
    public Tower getTower() {
        return occupiedBy;
    }

    /**
     * @return if this is occupied by a tower
     */
    public boolean isOccupied() {
        return occupied;
    }
    
    /**
     * remove tower from this Ground
     */
    public void removeTower() {
        this.occupied = false;
        this.occupiedBy = null;
    }
    
    /**
     * @param occupied
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * @return the xPos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * @return the yPos
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * @return if ground is trapped
     */
    public boolean isTrapped() {
        return trapped;
    }

    /**
     * @return the trap
     */
    public Trap getTrap() {
        return trap;
    }

    /**
     * @param trap the trap to set
     * 
     * @return {@code trapped}
     */
    public boolean setTrap(Trap trap) {
        if(trap == null) {
            removeTrap();
            return true;
        }
        if(!isOccupied()) {
            this.trapped = true;
            this.trap = trap;
        }
        return trapped;
    }
    
    public void removeTrap() {
        this.trapped = false;
        this.trap = null;
    }
}
