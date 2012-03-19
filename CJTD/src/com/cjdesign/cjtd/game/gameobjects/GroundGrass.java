package com.cjdesign.cjtd.game.gameobjects;

import com.cjdesign.cjtd.R;
import com.cjtd.globals.G;

public class GroundGrass extends Ground {
	public GroundGrass(int xpos, int ypos) {
		super(xpos,ypos);
		textureID = G.textures.loadTexture(R.drawable.grass);
	}
}
