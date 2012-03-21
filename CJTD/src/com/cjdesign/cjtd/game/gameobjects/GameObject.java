package com.cjdesign.cjtd.game.gameobjects;

import javax.microedition.khronos.opengles.GL10;

public abstract class GameObject {
	public int Type;
	public int textureResource = -1;
	
	public float x, y, z;
	
	public GameObject(int type){
		Type = type;
	}
	public abstract void draw(GL10 gl);
	public abstract void update(float dt);
}
