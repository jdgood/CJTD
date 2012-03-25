package com.cjdesign.cjtd.gamestate;

import com.cjdesign.cjtd.globals.G;

public class UpdateState {
	public static void continueGame(int diff, int mode){
		//set G.gamestate from a db, xml, or some kind of save file
		G.gamestate = new GameState(G.DIFFICULTY_HARD, G.MODE_OVERWATCH);
	}
	
	public static void saveGame(){
		//use G.gamestate to overwrite a db, xml, or some kind of save file
	}
	
	//add stuff for CJ account later
}
