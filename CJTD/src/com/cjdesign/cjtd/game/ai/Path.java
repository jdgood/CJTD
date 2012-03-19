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
	//ANYONE WANT TO IMPLEMENT THIS????
	 /*I actually wrote a game once which did this sort of thing perfectly. And the amount of recalculation was very little compared to A*.
	
	 In a grid base map (even if the characters don't move in a grid base way), you have a 2dimensional array, and mark the finish with 0,0.
	
	 Add the four adjacent squares to a list.
	
	 Then using a for or while loop, iterate through the list, and simply give each square a value of the minimum of the four surrounding squares + 1.
	
	 Then add the four surrounding squares to that square to the list (if their value is still not set).
	
	 basically, on an empty map, you could receive an effect like this in the array:
	
	 3,2,1,2,3,4
	 2,1,0,1,2,3
	 3,2,1,2,3,4
	 4,3,2,3,4,5
	 5,4,3,4,5,6
	 However on a map with obstacles...
	
	 3,2,1,2,3,4
	 2,1,0,X,4,5
	 3,2,1,X,5,6
	 4,X,2,X,6,X
	 5,4,3,X,7,8
	 Now this method has 3 main benefits.
	
	 If i place a block on a 4 for example. Then only the squares with a value of 5 or higher need to be updated! I simply have a loop which goes through the values of 5 upwards (make sure you have all the 5's done before you do the 6's, and all the 6's before the 7's, etc.
	
	 Or you could do it from an empty array again. It's extremely fast!
	
	 The second benefit, is that the grid is exactly the same for all enemies! Every enemy in the map follows this array. If it is on a 7, then it wants to move to a 6! Doesn't matter which 6 it takes, because they all are the same distance (6 squares) away from the end!
	
	 Thirdly, if, during your loop to work out the values of this array, if a square doesn't get reached, (if the value stays unset, and there isn't a tower there), then it means that part of the map is cut off from the base. In most tower defenses, this isn't allowed. So if you check this before confirming a towers placement, it means you know whether it is a legal or illegal build.
	
	 Hope this helped, Randomman159*/
	
	//use at beginning of round so creeps find quickest path
	private boolean updateShortestPath(){
		//return true if traversable
		//else return false
		return true;
	}
}
