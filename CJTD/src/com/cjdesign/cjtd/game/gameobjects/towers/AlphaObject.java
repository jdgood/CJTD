package com.cjdesign.cjtd.game.gameobjects.towers;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;

public class AlphaObject extends Tower{	
	public AlphaObject(Ground g) {
		super(g);
		textureResource = R.drawable.lightsource;
		
		setRange(5);
		frequency = 1;
		setBulletSpeed(15);
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
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			super.draw(gl);
			gl.glDisable(GL10.GL_BLEND);
		gl.glPopMatrix();
	}
}
