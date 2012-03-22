package com.cjdesign.cjtd.game.gameobjects.grid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.game.gameobjects.GameObject;
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
	
	public void update(float dt){
		if(isOccupied()){ 
			getTower().update(dt);
		} else if(isTrapped()) {
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
		if(isOccupied()){
			getTower().draw(gl);
		}
	}

    public void drawTrap(GL10 gl) {
        if(isTrapped()) {
            getTrap().draw(gl);
        }
    }
	
	public void drawShots(GL10 gl){
		if(isOccupied()){
			getTower().drawShots(gl);
		}
	}

	/**
	 * Make sure this will not block before adding a tower
	 * @param tower the Tower to occupy this Ground
	 * 
	 * @return was it successful
	 */
    public boolean setTower(Tower tower){
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
        if(!isOccupied()) {
            this.trapped = true;
            this.trap = trap;
        }
        return trapped;
    }
}
