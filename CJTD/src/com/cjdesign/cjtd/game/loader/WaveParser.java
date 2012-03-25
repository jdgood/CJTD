package com.cjdesign.cjtd.game.loader;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.globals.G;

public class WaveParser {
	public static void parseWaves(int levelNum){
		//adding an enemy to check stuff out
		ArrayList<Creep> temp;
		if(G.levelName.equals("V") || G.levelName.equals("M")){
			temp = new ArrayList<Creep>();
			temp.add(new Creep(0));
			temp.add(new Creep(3));
			temp.add(new Creep(6));
			temp.add(new Creep(9));
			temp.add(new Creep(12));
			temp.add(new Creep(15));
			temp.add(new Creep(16));
			temp.add(new Creep(17));
			temp.add(new Creep(18));
			temp.add(new Creep(19));
			temp.add(new Creep(20));
			temp.add(new Creep(21));
			
			G.Waves.add(temp);
		}
		
		if(G.levelName.equals("D") || G.levelName.equals("M")){
			temp = new ArrayList<Creep>();
			temp.add(new Creep(0));
			temp.add(new Creep(.5f));
			temp.add(new Creep(1));
			temp.add(new Creep(1.5f));
			temp.add(new Creep(2));
			temp.add(new Creep(2.5f));
			temp.add(new Creep(3));
			temp.add(new Creep(3.5f));
			temp.add(new Creep(4));
			temp.add(new Creep(4.5f));
			temp.add(new Creep(5));
			temp.add(new Creep(5.5f));
			
			G.Waves.add(temp);
		}
	}

}
