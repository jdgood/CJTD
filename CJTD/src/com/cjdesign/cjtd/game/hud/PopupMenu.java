package com.cjdesign.cjtd.game.hud;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.globals.G;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public abstract class PopupMenu {
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
				1.0f, 0.0f,
				0.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f};
	
	/** The initial indices definition */	
	private byte indices[] = {
	    		0,1,3, 0,3,2};

    private boolean visible = false;
    private boolean initialized = false;
    private float left,right,top,bottom;
    private int height, width, itemHeight;
    private float scale = 2;
    
    private ArrayList<MenuOption> options;
    
    /**
     * Popup Menu must be initialized in {@code draw()} because {@code G.tf} is
     * not ready when object is constructed.
     */
    private void init() {
        G.tf.SetScale(scale);
        height = G.tf.GetTextHeight() * getOptions().size();
        
        width = 0;
        for(MenuOption s : getOptions()) {
            int w = G.tf.GetTextLength(s.getLabel());
            if(w > width)
                width = w;
        }
        itemHeight = G.tf.GetTextHeight();
        
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
        
        initialized = true;
    }
    
    public void draw(GL10 gl) {
        if(!initialized)
            init();

        if(!isVisible())
            return;
        
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glColor4f(0, 0, 0, 0);
		
		//Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Enable the vertex and texture state
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glColor4f(1, 1, 1, 1);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glEnable(GL10.GL_TEXTURE_2D);

        G.tf.SetScale(scale);
        for(int i = 0; i < getOptions().size(); i++) {
            G.tf.PrintAt(gl, getOptions().get(i).getLabel(), (int)left, (int)(top-itemHeight*(i+1)));
        }
    }

    public void setPosition(float x, float y) {
        y = G.H - y;

        left = x;
        bottom = y;
        right = left + width;
        top = bottom + height;
        
        if(top > G.H) {
            top = y;
            bottom = top - height;
        }
        if(right > G.W){
            right = x;
            left = right - width;
        }
        
        float vertices[] = {
    			right+5, G.H - bottom, 0,
        		left-5, G.H - bottom, 0,    		
        		right+5, G.H - top, 0,
        		left-5, G.H - top, 0};
        
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
    }

    public boolean hitTest(float x, float y) {
        y = G.H - y;
        if(!isVisible())
            return false;
        this.hide();
        
        if(x > left && x < right && y < top && y > bottom) {
            for(int i = 0; i < getOptions().size(); i++) {
                if(y > top - (i+1) * itemHeight) {
                    this.getOptions().get(i).onSelection();
                    return true;
                }
            }
        }
        
        return false;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public void show(float x, float y) {
        setPosition(x,y);
        visible = true;
    }
    
    public void hide() {
        visible = false;
    }

    public ArrayList<MenuOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<MenuOption> options) {
        this.options = options;
    }

}
