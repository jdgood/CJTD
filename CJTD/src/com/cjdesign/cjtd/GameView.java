package com.cjdesign.cjtd;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.ai.Path;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Grid;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.game.gameobjects.grid.GroundDirt;
import com.cjdesign.cjtd.game.gameobjects.grid.GroundGrass;
import com.cjdesign.cjtd.game.gameobjects.towers.AlphaObject;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.game.textures.GLTextures;
import com.cjtd.globals.G;

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
		G.Creeps = new ArrayList<Creep>();
		
		//doing this is superhacks! create texture container
		G.textures = new GLTextures(G.gameContext);
		
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
		new AlphaObject(gridArray[0][0]);
		gridArray[1][1].setTower(new Tower(gridArray[1][1]));
		new AlphaObject(gridArray[1][2]);
		new AlphaObject(gridArray[1][3]);
		
		
		new AlphaObject(gridArray[3][1]);
		new AlphaObject(gridArray[3][2]);
		gridArray[3][3].setTower(new Tower(gridArray[3][3]));
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
		
		//adding an enemy to check stuff out
		G.Creeps.add(new Creep());
		
		//create renderer
		G.renderer = new com.cjdesign.cjtd.game.Renderer();
		setRenderer(G.renderer);
		
		//create updater thread
		G.updater = new com.cjdesign.cjtd.game.Updater();//Initially paused
		G.updaterThread = new Thread(G.updater);
		G.updater.running = true;
		G.paused = false;
		G.updaterThread.start();
     }
}
