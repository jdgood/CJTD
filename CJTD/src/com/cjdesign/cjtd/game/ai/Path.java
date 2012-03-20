package com.cjdesign.cjtd.game.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Pair;

import com.cjdesign.cjtd.game.gameobjects.*;
import com.cjtd.globals.G;

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
	int[][] grid;
	int xSize, ySize;
	
	public Path(int xsize, int ysize){
	    xSize = xsize;
	    ySize = ysize;
		grid = new int[xsize][ysize];
		for(int[] g : grid)
		    Arrays.fill(g, -1);
//		    for(int i = 0; i < g.length; i++)
//		        g[i] = -1;
		System.out.println("is it traverable?");
		if(!isTraversable())
		    System.out.println("Not Traversable!");
		else
		    System.out.println("Is Traversable!");
	}
	
	//use when adding a tower
	public boolean addTower(Ground g){
		return isTraversable();
	}
	
	public boolean isTraversable(){
	    int[][] tempGrid = new int[xSize][ySize];
        for(int[] g : tempGrid)
            Arrays.fill(g, -1);
//            for(int i = 0; i < g.length; i++)
//                g[i] = -1;
	    Pair<Integer,Integer> endPoint = new Pair<Integer,Integer>(G.level.getEnd().xPos,G.level.getEnd().yPos);
	    List<Pair<Integer,Integer>> unprocessed = new ArrayList<Pair<Integer,Integer>>();
	    unprocessed.add(endPoint);
		return updateGrid(tempGrid,unprocessed);
	}
	//ANYONE WANT TO IMPLEMENT THIS????
	 /*I actually wrote a game once which did this sort of thing perfectly.
	  *  And the amount of recalculation was very little compared to A*.
	
	 In a grid base map (even if the characters don't move in a grid base 
	 way), you have a 2dimensional array, and mark the finish with 0,0.
	
	 Add the four adjacent squares to a list.
	
	 Then using a for or while loop, iterate through the list, and simply 
	 give each square a value of the minimum of the four surrounding 
	 squares + 1.
	
	 Then add the four surrounding squares to that square to the list 
	 (if their value is still not set).
	
	 basically, on an empty map, you could receive an effect like this 
	 in the array:
	
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
	
	 If i place a block on a 4 for example. Then only the squares with a 
	 value of 5 or higher need to be updated! I simply have a loop which 
	 goes through the values of 5 upwards (make sure you have all the 5's
	 done before you do the 6's, and all the 6's before the 7's, etc.
	
	 Or you could do it from an empty array again. It's extremely fast!
	
	 The second benefit, is that the grid is exactly the same for all 
	 enemies! Every enemy in the map follows this array. If it is on a 7,
	 then it wants to move to a 6! Doesn't matter which 6 it takes, 
	 because they all are the same distance (6 squares) away from the 
	 end!
	
	 Thirdly, if, during your loop to work out the values of this array, 
	 if a square doesn't get reached, (if the value stays unset, and there
	 isn't a tower there), then it means that part of the map is cut off
	 from the base. In most tower defenses, this isn't allowed. So if
	 you check this before confirming a towers placement, it means you
	 know whether it is a legal or illegal build.
	
	 Hope this helped, Randomman159*/
	
	//use at beginning of round so creeps find quickest path
	private boolean updateGrid(int[][] tempGrid, List<Pair<Integer,Integer>> unprocessed){
	    List<Pair<Integer,Integer>> nextUnprocessed = new ArrayList<Pair<Integer,Integer>>();
        for (Pair<Integer,Integer> p : unprocessed)
        {
            int minvalue = -1;
            // find minimum visited adjacent
            try {
                if(minvalue == -1 || // minvalue unset
                        (tempGrid[p.first][p.second-1] != -1 && // visited and
                        tempGrid[p.first][p.second-1] < minvalue)) { // less than minvalue
                    minvalue = tempGrid[p.first][p.second-1];
                    if (!G.level.getGround(p.first, p.second-1).occupied)
                        nextUnprocessed.add(new Pair<Integer,Integer>(p.first, p.second-1));
                }
            } catch ( ArrayIndexOutOfBoundsException e ){
                // do nothing
            }
            
            try {
                if(minvalue == -1 || // minvalue unset
                        (tempGrid[p.first][p.second+1] != -1 && // visited and
                        tempGrid[p.first][p.second+1] < minvalue)) { // less than minvalue
                    minvalue = tempGrid[p.first][p.second+1];
                    if (!G.level.getGround(p.first,p.second+1).occupied)
                        nextUnprocessed.add(new Pair<Integer,Integer>(p.first,p.second+1));
                }
            } catch ( ArrayIndexOutOfBoundsException e ){
                // do nothing
            }
            
            try {
                if(minvalue == -1 || // minvalue unset
                        (tempGrid[p.first-1][p.second] != -1 && // visited and
                        tempGrid[p.first-1][p.second] < minvalue)) { // less than minvalue
                    minvalue = tempGrid[p.first-1][p.second];
                    if (!G.level.getGround(p.first-1, p.second).occupied)
                        nextUnprocessed.add(new Pair<Integer,Integer>(p.first-1, p.second));
                }
            } catch ( ArrayIndexOutOfBoundsException e ){
                // do nothing
            }
            
            try {
                if(minvalue == -1 || // minvalue unset
                        (tempGrid[p.first+1][p.second] != -1 && // visited and
                        tempGrid[p.first+1][p.second] < minvalue)) { // less than minvalue
                    minvalue = tempGrid[p.first+1][p.second];
                    if (!G.level.getGround(p.first+1, p.second).occupied)
                        nextUnprocessed.add(new Pair<Integer,Integer>(p.first+1, p.second));
                }
            } catch ( ArrayIndexOutOfBoundsException e ){
                // do nothing
            }
            
            System.out.println("About to update tempGrid");
            System.out.println("p.first: " + Integer.toString(p.first));
            System.out.println("p.second: " + Integer.toString(p.second));
            System.out.println("tempGrid.length: " + Integer.toString(tempGrid.length));
            System.out.println("xSize: " + Integer.toString(xSize));
            System.out.println("ySize: " + Integer.toString(ySize));
            tempGrid[p.first][p.second] = minvalue + 1;
            System.out.println("Updated tempGrid");
        }
        
        if (!nextUnprocessed.isEmpty())
            return updateGrid(tempGrid,nextUnprocessed);
        else if (tempGrid[G.level.getStart().xPos][G.level.getStart().yPos] == -1)
            return false;
        else
        {
            // TODO look for cut off creeps
            grid = tempGrid;
            return true;
        }
	}
	
	public Ground getNextGoal(Ground currentGoal){
	    Pair<Integer,Integer> p = new Pair<Integer,Integer>(currentGoal.yPos,currentGoal.xPos);
	    Ground nextGoal = currentGoal;
	    int minvalue = grid[p.first][p.second];
	    
        // find minimum visited adjacent
        try {
            if(minvalue == -1 || // minvalue unset
                    (grid[p.first][p.second-1] != -1 && // not blocked and
                    grid[p.first][p.second-1] < minvalue)) { // less than minvalue
                minvalue = grid[p.first][p.second-1];
                nextGoal = G.level.getGround(p.first,p.second-1);
            }
        } catch ( ArrayIndexOutOfBoundsException e ){
            // do nothing
        }
        
        try {
            if(minvalue == -1 || // minvalue unset
                    (grid[p.first][p.second+1] != -1 && // not blocked and
                    grid[p.first][p.second+1] < minvalue)) { // less than minvalue
                minvalue = grid[p.first][p.second+1];
                nextGoal = G.level.getGround(p.first,p.second+1);
            }
        } catch ( ArrayIndexOutOfBoundsException e ){
            // do nothing
        }
        
        try {
            if(minvalue == -1 || // minvalue unset
                    (grid[p.first-1][p.second] != -1 && // not blocked and
                    grid[p.first-1][p.second] < minvalue)) { // less than minvalue
                minvalue = grid[p.first-1][p.second];
                nextGoal = G.level.getGround(p.first-1,p.second);
            }
        } catch ( ArrayIndexOutOfBoundsException e ){
            // do nothing
        }
        
        try {
            if(minvalue == -1 || // minvalue unset
                    (grid[p.first+1][p.second] != -1 && // not blocked and
                    grid[p.first+1][p.second] < minvalue)) { // less than minvalue
                minvalue = grid[p.first+1][p.second];
                nextGoal = G.level.getGround(p.first+1,p.second);
            }
        } catch ( ArrayIndexOutOfBoundsException e ){
            // do nothing
        }
	    return nextGoal;
	}
}
