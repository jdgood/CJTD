package com.cjdesign.cjtd.game.gameobjects.towers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.Shot;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.utils.Vector2D;
import com.cjtd.globals.G;

public class Tower extends GameObject {
	
	private float zrot;
	public float damage;
	public float range;
	public float lastShot;//time since last shot
	public float frequency;//seconds between shots
	public float bulletSpeed;
	public Vector2D dir;
	public Creep target;
	public ArrayList<Shot> shots;
	
	public Ground location;

	/** The buffer holding the vertices */
	protected FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	protected FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	protected ByteBuffer indexBuffer;

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

	public Tower(Ground g) {
		super(G.TOWER_ID);
		
		dir = new Vector2D(1,0);
		
		location = g;
		location.setTower(this);
		
		this.x = g.x; 
		this.y = g.y;
		this.z = G.gridDepth+.1f;
		
		bulletSpeed = 50;
		frequency = 2;
		range = 15;
		
		shots = new ArrayList<Shot>();
		
		textureResource = R.drawable.tower;

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
	
	public void update(float dt) {
		if(target != null && (!target.isAlive() || FloatMath.sqrt((float)Math.pow(target.x - x, 2) + (float)Math.pow(target.y - y, 2)) > range)){//if target no longer in range
			target = null;
		}
		if(target == null){//if no current target
			//probably optimize this to grab enemies with a currentGoal within the radius
			for(Creep c : G.Creeps){
				if(FloatMath.sqrt((float)Math.pow(c.x - x, 2) + (float)Math.pow(c.y - y, 2)) < range){//checks for enemy farthest along path (probably allow to change this to closest enemy or fastest enemy)
					target = c;
					break;
				}
			}
		}
		if(target != null && lastShot - frequency > 0){
			lastShot = 0;
			float n = .25f;
			dir = new Vector2D(target.x + target.dir.x * target.speed * n - x, target.y + target.dir.y * target.speed * n - y);//update direction of target(adds an n second prediction by checking creeps location in n seconds)
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
		
		ArrayList<Shot> delThese = new ArrayList<Shot>();
		//updates a shot and checks if a shot needs to be deleted(because of a hit or out of range
		for(Shot s : shots){
			s.update(dt);
			if(s.hit() || s.range()){
				delThese.add(s);
			}
		}
		for(Shot s : delThese){
			shots.remove(s);
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
		for(Shot s : shots){
			s.draw(gl);
		}
	}
}
