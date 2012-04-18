package com.cjdesign.cjtd.game.hud;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.globals.G;

public class HUD {
    private BuildMenu buildMenu;
    private UpgradeMenu upgradeMenu;
    private ArrayList<PopupMenu> menus;
    private String message;
	
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer2;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
	private float vertices[] = {
			G.W, G.H-60, 0,
    		0, G.H-60, 0,    		
    		G.W, G.H, 0,
    		0, G.H, 0};
	
	private float vertices2[] = {
			G.W, 0, 0,
    		0, 0, 0,    		
    		G.W, G.H, 0,
    		0, G.H, 0};
	
	
	/** The initial indices definition */	
	private byte indices[] = {
	    		0,1,3, 0,3,2};
	
	public HUD(){
	    buildMenu = new BuildMenu();
	    upgradeMenu = new UpgradeMenu();
	    menus = new ArrayList<PopupMenu>();
	    menus.add(buildMenu);
	    menus.add(upgradeMenu);
	    
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

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		if(G.state == G.STATE_DEFEAT){
			gl.glColor4f(0, 0, 0, 0);
			message = "Defeat!";
		}
		else if(G.state == G.STATE_VICTORY){
			gl.glColor4f(0, 0, 0, 0);
			message = "Victory!";
		}
		else if(G.damageTimer > 0){
			System.out.println("here");
			gl.glColor4f(1, 0, 0, 0);
			message = "";
		}
		else{
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
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			
			String print = G.health + " health\t\t";
			
			print += G.Waves.size() + (G.Waves.size()==1?" wave":" waves") + " remaining\t\t";
			
			if(G.state == G.STATE_PREPARATION){
				print += ((int)G.nextWave / 60) + (G.nextWave%60 < 10?":0":":") + ((int)G.nextWave % 60) + " till next wave\t\t";
			}
			else if(G.state == G.STATE_INITIAL){
				print += "Hit the menu button to send the first wave\t\t";
			}
			else{
				print += G.playSpeed + "X speed\t\t";
			}
			
			G.tf.SetScale(2);
			G.tf.PrintAt(gl, print, 10, 10);
			
			for(PopupMenu m : menus)
			    m.draw(gl);
			
			gl.glDisable(GL10.GL_BLEND);
			return;
		}
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
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
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		
		G.tf.SetScale(10);
		G.tf.PrintAt(gl, message, (int)G.W/2-300, (int)G.H/2-150);
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	public boolean hitTest(float x, float y) {
	    boolean retval = false;
	    for(PopupMenu m : menus) {
	        if(m.hitTest(x, y))
	            retval = true;
	    }
	    return retval;
	}
	
	public void hideMenus() {
	    for(PopupMenu m : menus) {
	        m.hide();
	    }
	}

    /**
     * @return the upgradeMenu
     */
    public UpgradeMenu getUpgradeMenu() {
        return upgradeMenu;
    }

    /**
     * @return the buildMenu
     */
    public BuildMenu getBuildMenu() {
        return buildMenu;
    }
}
