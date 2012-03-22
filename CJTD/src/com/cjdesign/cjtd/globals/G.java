package com.cjdesign.cjtd.globals;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;

import com.cjdesign.cjtd.game.Renderer;
import com.cjdesign.cjtd.game.Updater;
import com.cjdesign.cjtd.game.ai.Path;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Grid;
import com.cjdesign.cjtd.game.textures.GLTextures;
import com.cjdesign.cjtd.gamestate.CJAccount;
import com.cjdesign.cjtd.gamestate.GameState;

public class G {
	public static boolean paused = false;
	public static float viewX = 0;
	public static float viewY = 0;
	public static float viewZ = 5;
	public static float viewXlimit = 10;
	public static float viewYlimit = 10;
	public static float viewZlimit = 10;
	public static float W = 480;
	public static float H = 240;
	public static float friction = 50f;
	public static float velX = 0;
	public static float velY = 0;
	public static float gridDepth = -15;
	public static float gridSize = 4;
	
	public static GameState gamestate;
	public static CJAccount acc;
	
	public static ArrayList<ArrayList<Creep>> Waves;
	public static ArrayList<Creep> Creeps;
    public static ArrayList<Creep> deadCreeps;
	public static Grid level;
	public static Path path;
	public static int health;
	
	public static Context gameContext;
	public static GLTextures textures;
	
	public static Updater updater;
	public static Thread updaterThread;
	public static Renderer renderer;
	public static MediaPlayer mpMenu;
	public static MediaPlayer mpGame;
	
	public static int state;
	
	public static final int GRID_ID = 0;
	public static final int TOWER_ID = 1;
	public static final int CREEP_ID = 2;
	public static final int BULLET_ID = 3;
	public static final int TRAP_ID = 4;
	
	public static final float ANDROID_CREEP_SIZE = .5f;
	
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_NORMAL = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	public static final int MODE_OVERWATCH = 0;
	public static final int MODE_SIEGE = 1;
	
	public static final int STATE_PREPARATION = 0;
	public static final int STATE_BATTLE = 1;
	public static final int STATE_VICTORY = 2;
	public static final int STATE_DEFEAT = 3;
}
