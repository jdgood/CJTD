package com.cjdesign.cjtd.game.gameobjects;

import javax.microedition.khronos.opengles.GL10;

public abstract class GameObject {
	public int Type;
	public int textureID;
	
	public float x, y, z;
	
	public GameObject(int type){
		Type = type;
	}
	public void draw(GL10 gl){}
	public void update(float dt){}
}
