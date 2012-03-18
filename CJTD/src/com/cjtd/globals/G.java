package com.cjtd.globals;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.GameObject;

public class G {
	public static boolean paused = false;
	public static float viewX = 0;
	public static float viewY = 0;
	public static float viewZ = -5;
	public static float viewXlimit = 10;
	public static float viewYlimit = 10;
	public static float viewZlimit = -10;
	public static float W = 480;
	public static float H = 240;
	public static float friction = 50f;
	public static float velX = 0;
	public static float velY = 0;
	
	public static ArrayList<GameObject> objs;
	
	public static final int GRID_ID = 0;
	public static final int TOWER_ID = 1;
	public static final int CREEP_ID = 2;
	public static final int BULLET_ID = 3;
}
