package com.cjdesign.cjtd.game.gameobjects;

import com.cjdesign.cjtd.R;
import com.cjtd.globals.G;

public class GroundDirt extends Ground {
	public GroundDirt(int xpos, int ypos) {
		super(xpos,ypos);
		textureID = G.textures.loadTexture(R.drawable.dirt);
	}
}
