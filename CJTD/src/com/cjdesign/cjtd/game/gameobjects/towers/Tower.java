package com.cjdesign.cjtd.game.gameobjects.towers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.Shot;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.Vector2D;

public class Tower extends GameObject {
	
	private float zrot;
	protected int damage = 5;
	protected float range = 15f;
	/** time since last shot */
	protected float lastShot;
	/** seconds between shots */
	protected float frequency = 2f;
	protected float bulletSpeed = 25f;
	protected Vector2D dir;
	protected Creep target;
	protected ArrayList<Shot> shots;
	
	private Ground location;

	private float vertices[] = {
    		G.gridSize/2, -G.gridSize/2, 0,
    		-G.gridSize/2, -G.gridSize/2, 0,    		
    		G.gridSize/2, G.gridSize/2, 0,
    		-G.gridSize/2, G.gridSize/2, 0};

	public Tower(Ground g) {
		super(G.TOWER_ID);
		
		dir = new Vector2D(1,0);
		
		location = g;
		getLocation().setTower(this);
		
		this.x = g.x; 
		this.y = g.y;
		this.z = G.gridDepth;
		
		shots = new ArrayList<Shot>();
		
		textureResource = R.drawable.tower;

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	
	public void update(float dt) {
		if(target != null){
			if(!target.isAlive() || FloatMath.sqrt((float)Math.pow(target.x - x, 2) + (float)Math.pow(target.y - y, 2)) > getRange()){//if target no longer in range or dead
				target = null;
			}
		}
		if(target == null){//if no current target
			//probably optimize this to grab enemies with a currentGoal within the radius
			for(Creep c : G.Creeps){
				if(FloatMath.sqrt((float)Math.pow(c.x - x, 2) + (float)Math.pow(c.y - y, 2)) < getRange()){//checks for enemy farthest along path (probably allow to change this to closest enemy or fastest enemy)
					target = c;
					break;
				}
			}
		}
		if(target != null && lastShot - frequency > 0){
			lastShot = 0;
			float n = .25f;
			dir = new Vector2D(target.x + target.getDir().x * target.getSpeed() * n - x, target.y + target.getDir().y * target.getSpeed() * n - y);//update direction of target(adds an n second prediction by checking creeps location in n seconds)
			dir.normalize();
			//update zrot here to point towards an enemy
			/*zrot -= 1;
			if(zrot == -360){
				zrot = 0;
			}*/
			shots.add(new Shot(dir, this));
		}else{
			lastShot += dt;
		}
		
		ArrayList<Shot> temp = new ArrayList<Shot>();
		//updates a shot and checks if a shot needs to be deleted(because of a hit or out of range)
		for(Shot s : shots){
			s.update(dt);
			if(!s.hit() && !s.range()){
				temp.add(s);
			}
		}
		shots = new ArrayList<Shot>(temp);
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
			//gl.glTranslatef(0, 0, .1f);
			gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);
		
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
	
	public void drawShots(GL10 gl){
		//nullpointerexception
		ArrayList<Shot> slist = new ArrayList<Shot>(shots);//this should avoid concurrent exception
		for(Shot s : slist){
			s.draw(gl);
		}
	}

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * @return the range
     */
    public float getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(float range) {
        this.range = range;
    }

    /**
     * @return the bulletSpeed
     */
    public float getBulletSpeed() {
        return bulletSpeed;
    }

    /**
     * @param bulletSpeed the bulletSpeed to set
     */
    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    /**
     * @return the Ground the Tower is on
     */
    public Ground getLocation() {
        return location;
    }
}
