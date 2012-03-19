package com.cjdesign.cjtd.game.gameobjects;

import com.cjdesign.cjtd.R;
import com.cjtd.globals.G;

public class GroundGrass extends Ground {
	public GroundGrass(){
		super();
		textureID = G.textures.loadTexture(R.drawable.grass);
	}
}
