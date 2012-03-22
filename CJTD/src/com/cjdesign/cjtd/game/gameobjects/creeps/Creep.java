package com.cjdesign.cjtd.game.gameobjects.creeps;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.Vector2D;

public class Creep extends GameObject {
    private Ground currentGoal;
    private Vector2D dir;
	private float speed;
	
	private int health;
	
	/** spawn delay in seconds after the creep is added to the G.Creeps array(current wave) */
	private float delay;
	
	private float vertices[] = {
			G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE,
    		-G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE,    		
    		G.ANDROID_CREEP_SIZE, G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE,
    		-G.ANDROID_CREEP_SIZE, G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE};
	
	public Creep(float delay) {
		super(G.CREEP_ID);
		
		this.delay = delay;
		
		textureResource = R.drawable.android_sh;
		
		setCurrentGoal(G.level.getStart());
		
		x = getCurrentGoal().x - 2;
		y = getCurrentGoal().y - 2;
		z = G.gridDepth+.1f;
		
		setSpeed(3);
		health = 100;
		
		setDir(new Vector2D(getCurrentGoal().x - x, getCurrentGoal().y - y));
		getDir().normalize();//makes it so direction always implies a magnitude of 1
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	
	private void die(){
	    G.deadCreeps.add(this);
	}
	
	public void takeDamage(int damage){
	    health -= damage;
        if(health <= 0)
            die();
	}
	
	public boolean isAlive(){
	    return (health > 0);
	}
	
	public void update(float dt){
		if(delay > 0){//delay
			delay-=dt;
			return;
		}
	    float dx = getDir().x * dt * getSpeed();
	    float dy = getDir().y * dt * getSpeed();

	    // Change targets when the Manhattan distance will increase with the next delta.
        if(Math.abs(x-getCurrentGoal().x)+Math.abs(y-getCurrentGoal().y) < Math.abs(x+dx-getCurrentGoal().x)+Math.abs(y+dy-getCurrentGoal().y))
        //if(G.ANDROID_CREEP_SIZE >= FloatMath.sqrt((x-currentGoal.x)*(x-currentGoal.x)+(y-currentGoal.y)*(y-currentGoal.y)))
        {
            setCurrentGoal(G.path.getNextGoal(getCurrentGoal()));
        
            setDir(new Vector2D(getCurrentGoal().x - x, getCurrentGoal().y - y));
            getDir().normalize();//makes it so direction always implies a magnitude of 1

            dx = getDir().x * dt * getSpeed();
            dy = getDir().y * dt * getSpeed();
        }
        
		x+=dx;
		y+=dy;
        
	}
	
	public void draw(GL10 gl){
		if(delay > 0){//delay
			return;
		}
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

    /**
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * @return the dir
     */
    public Vector2D getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(Vector2D dir) {
        this.dir = dir;
    }

    /**
     * @return the currentGoal
     */
    public Ground getCurrentGoal() {
        return currentGoal;
    }

    /**
     * @param currentGoal the currentGoal to set
     */
    public void setCurrentGoal(Ground currentGoal) {
    	if(this.currentGoal == currentGoal){
    		G.health--;
    		die();
    	}
        this.currentGoal = currentGoal;
    }

}
