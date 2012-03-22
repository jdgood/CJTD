/**
 * 
 */
package com.cjdesign.cjtd.game.gameobjects.traps;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.game.gameobjects.GameObject;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public abstract class Trap extends GameObject {
    
    private Ground location;

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

    /**
     * @param g Ground to be summoned on
     */
    public Trap(Ground g) {
        super(G.TRAP_ID);
        
        this.location = g;
        
        this.x = g.x;
        this.y = g.y;
        z = G.gridDepth;
        
        g.setTrap(this);

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
            gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);
        
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
     * @return the location
     */
    public Ground getLocation() {
        return location;
    }

}
