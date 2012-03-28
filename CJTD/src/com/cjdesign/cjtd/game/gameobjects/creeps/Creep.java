package com.cjdesign.cjtd.game.gameobjects.creeps;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.Vector2D;

public class Creep extends GameObject {
    private Ground previousGoal = null;
    private Ground currentGoal;
    private Vector2D dir;
	private float speed;
	private float normalSpeed;
	
	private int health;
	private int maxHealth;
	
	/** spawn delay in seconds after the creep is added to the G.Creeps array(current wave) */
	private float delay;
	
	private float vertices[] = {
			G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE, 0,
    		-G.ANDROID_CREEP_SIZE, -G.ANDROID_CREEP_SIZE, 0,    		
    		G.ANDROID_CREEP_SIZE, G.ANDROID_CREEP_SIZE, 0,
    		-G.ANDROID_CREEP_SIZE, G.ANDROID_CREEP_SIZE, 0};
	
	protected FloatBuffer vertexBuffer2;
	
	private float vertices2[] = {
			1, -.1f, 0,
    		-1, -.1f, 0,    		
    		1, .1f, 0,
    		-1, .1f, 0};
	
	public Creep(float delay) {
		super(G.CREEP_ID);
		
		this.delay = delay;
		
		textureResource = R.drawable.android_sh;
		
		setCurrentGoal(G.level.getStart());
		
		x = getCurrentGoal().x - 2;
		y = getCurrentGoal().y - 2;
		z = G.gridDepth+1f;
		
		setSpeed(3);
		setNormalSpeed(3);
		maxHealth = health = 100;
		
		setDir(new Vector2D(getCurrentGoal().x - x, getCurrentGoal().y - y));
		getDir().normalize();//makes it so direction always implies a magnitude of 1
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(vertices2.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer2 = byteBuf.asFloatBuffer();
		vertexBuffer2.put(vertices2);
		vertexBuffer2.position(0);
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
    
        float halfGridSize = G.gridSize/2;
        if(previousGoal != null &&
                x < previousGoal.x + halfGridSize && x > previousGoal.x - halfGridSize &&
                y < previousGoal.y + halfGridSize && y > previousGoal.y - halfGridSize &&
                x+dx < currentGoal.x + halfGridSize && x+dx > currentGoal.x - halfGridSize &&
                y+dy < currentGoal.y + halfGridSize && y+dy > currentGoal.y - halfGridSize) {
            previousGoal.onExit(this);
            currentGoal.onEnter(this);
            this.onExit(previousGoal);
            this.onEnter(currentGoal);
        }
        
		x+=dx;
		y+=dy;
	}
	
	public void draw(GL10 gl){
		if(delay > 0){//delay
			return;
		}
		gl.glPushMatrix();
			//draw creep
		
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		
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
			
			gl.glDisable(GL10.GL_BLEND);

			
			//draw health bar
			gl.glDisable(GL10.GL_TEXTURE_2D);
	        gl.glColor4f(1, 0, 0, 0);
			
	        gl.glTranslatef(0, 1, 0);
	        gl.glScalef((float)health/maxHealth, 1, 1);
	        
			//Point to our buffers
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CCW);
			
			//Enable the vertex and texture state
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer2);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

			gl.glColor4f(1, 1, 1, 1);
			gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}
	
    /* 
     * I just thought maybe we can have creeps that do things like lay "traps" 
     * that help other creeps or something that requires them to be aware of the Ground.
     */
	public void onEnter(Ground g) {}
	public void onExit(Ground g) {}

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
     * @param newGoal the currentGoal to set
     */
    public void setCurrentGoal(Ground newGoal) {
    	if(this.currentGoal == newGoal){
    		G.health--;
    		die();
    	}
    	this.previousGoal = this.currentGoal;
        this.currentGoal = newGoal;
    }

    /**
     * @return the normalSpeed
     */
    public float getNormalSpeed() {
        return normalSpeed;
    }

    /**
     * @param normalSpeed the normalSpeed to set
     */
    protected void setNormalSpeed(float normalSpeed) {
        this.normalSpeed = normalSpeed;
    }

}
