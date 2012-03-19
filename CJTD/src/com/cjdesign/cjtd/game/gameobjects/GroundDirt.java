package com.cjdesign.cjtd.game.gameobjects;

import com.cjdesign.cjtd.R;

public class GroundDirt extends Ground {
	public GroundDirt(int xpos, int ypos, float x, float y) {
		super(xpos,ypos,x,y);
		textureResource = R.drawable.dirt;
	}
}
