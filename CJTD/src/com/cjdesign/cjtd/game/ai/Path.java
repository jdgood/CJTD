package com.cjdesign.cjtd.game.ai;

import java.util.ArrayList;
import java.util.Arrays;

import com.cjdesign.cjtd.game.gameobjects.*;

/*
 Path in demo level 
 O denotes open grid
 X denotes open area
 S denotes start
 E denotes end
 P denotes shortest path 
 
   X O P P P X P P P X
   O X P X P X P X P X
   O X P X P X P X P X
   O X P X P X P X P X
   S P P X P P P X P E
 
 */

public class Path {
	boolean[][] grid; //current path
	ArrayList<Ground> shortestPath;
	
	public Path(int ysize, int xsize){
		grid = new boolean[ysize][xsize];
		Arrays.fill(grid, false);
	}
	
	//use when adding a tower
	public boolean addTower(Ground g){
		grid[g.yPos][g.xPos] = true;
		if(!isTraversable()){
			grid[g.yPos][g.xPos] = false;
			return false;
		}
		return true;
	}
	
	public boolean isTraversable(){
		return updateShortestPath();
	}
	
	//use at beginning of round so creeps find quickest path
	private boolean updateShortestPath(){//implement Dijkstra's probably unless an easier way
		//create temporary shortest path
		//set new path to be shortest path if dist of shortest path < infinity return true
		//else return false
		return true;
	}
}
