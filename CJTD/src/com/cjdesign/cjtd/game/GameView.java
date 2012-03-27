package com.cjdesign.cjtd.game;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.ai.Path;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Grid;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.game.gameobjects.grid.GroundDirt;
import com.cjdesign.cjtd.game.gameobjects.grid.GroundGrass;
import com.cjdesign.cjtd.game.gameobjects.towers.AlphaObject;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.game.gameobjects.traps.MudTrap;
import com.cjdesign.cjtd.game.gameobjects.traps.SpikeTrap;
import com.cjdesign.cjtd.game.hud.HUD;
import com.cjdesign.cjtd.game.loader.WaveParser;
import com.cjdesign.cjtd.globals.G;
import com.cjdesign.cjtd.utils.MatrixGrabber;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GameView extends GLSurfaceView {
     public GameView(Context context)
     {
    	super(context);
       
        //initialize globals
        G.gameContext = context;
		G.viewX = 0;
		G.viewY = 0;
		G.viewZ = 10;
		G.hud = new HUD();
		G.Waves = new ArrayList<ArrayList<Creep>>();
		G.Creeps = new ArrayList<Creep>();
		G.deadCreeps = new ArrayList<Creep>();
		G.state = G.STATE_PREPARATION;
		G.health = 2;
		
		G.mg = new MatrixGrabber();
		
		//create demo gameworld(create a level importer later)
		int xSize = 10;
		int ySize = 5;
		Ground[][] gridArray = new Ground[xSize][ySize];
		boolean grass = true;
		
		float startx = -xSize*G.gridSize/2+G.gridSize/2;
		float starty = ySize*G.gridSize/2-G.gridSize/2;
		
		G.viewXlimit = xSize*G.gridSize/2+G.gridSize;
		G.viewYlimit = ySize*G.gridSize/2-G.gridSize;
		
		for(int i = 0; i < ySize; i++){
			if(i%2==0){
				grass = true;
			}
			else{
				grass = false;
			}
			
			for(int j = 0; j < xSize; j++){
				if(grass){
					gridArray[j][i] = new GroundGrass(j,i,startx+G.gridSize*j,starty-G.gridSize*i);
				}
				else{
					gridArray[j][i] = new GroundDirt(j,i,startx+G.gridSize*j,starty-G.gridSize*i);
				}
				grass = !grass;
			}
		}
		
		G.level = new Grid(gridArray, xSize, ySize, 0, 4, 9, 4);
		G.path = new Path(xSize, ySize);
		
		//adding towers for sanity checks and to test ai in a bit
		if(!(G.levelNum == 3)){
			new AlphaObject(gridArray[0][0]);
			new Tower(gridArray[1][1]);
			new AlphaObject(gridArray[1][2]);
			new AlphaObject(gridArray[1][3]);
		
			new SpikeTrap(gridArray[2][2]);
			new MudTrap(gridArray[2][1]);
		
			new AlphaObject(gridArray[3][1]);
			new AlphaObject(gridArray[3][2]);
			new Tower(gridArray[3][3]);
			new AlphaObject(gridArray[3][4]);
			
			new AlphaObject(gridArray[5][0]);
			new AlphaObject(gridArray[5][1]);
			new AlphaObject(gridArray[5][2]);
			new AlphaObject(gridArray[5][3]);
			
			new AlphaObject(gridArray[7][1]);
			new AlphaObject(gridArray[7][2]);
			new AlphaObject(gridArray[7][3]);
			new AlphaObject(gridArray[7][4]);
			
			new AlphaObject(gridArray[9][0]);
			new AlphaObject(gridArray[9][1]);
			new AlphaObject(gridArray[9][2]);
			new AlphaObject(gridArray[9][3]);
		}
		
		//parse levels.xml using the selected level number
		WaveParser.parseWaves(G.levelNum);
		
		//create renderer
		G.renderer = new com.cjdesign.cjtd.game.Renderer();
		setRenderer(G.renderer);
		
		//create updater thread
		if(G.updater==null){
			G.updater = new com.cjdesign.cjtd.game.Updater();//Initially paused
			G.updaterThread = new Thread(G.updater);
			G.updater.running = true;
			G.paused = false;
			G.updaterThread.start();
		}
     }
}
