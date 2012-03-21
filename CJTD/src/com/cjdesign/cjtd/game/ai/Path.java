package com.cjdesign.cjtd.game.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.util.Pair;

import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;

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
   
 This is what grid looks like. 
 The number is the distance to the end.
 -1 means it's blocked by a tower.
   
   -1 20 19 18 17 -1 07 06 05 -1
   28 -1 20 -1 16 -1 08 -1 04 -1
   27 -1 21 -1 15 -1 09 -1 03 -1
   26 -1 22 -1 14 -1 10 -1 02 -1
   25 24 23 -1 13 12 11 -1 01 00
 
 */

public class Path {
	int[][] grid;
	int xSize, ySize;
	
	class PairComparator implements Comparator<Pair<Integer,Integer>>{

        public int compare(Pair<Integer, Integer> lhs,
                Pair<Integer, Integer> rhs) {
            if(lhs.first < rhs.first || lhs.second < rhs.second)
                return -1;
            if(lhs.first > rhs.first || lhs.second > rhs.second)
                return 1;
            else
                return 0;
        }
        
	}
	
	public Path(int xsize, int ysize){
	    xSize = xsize;
	    ySize = ysize;
		grid = new int[xsize][ysize];
		for(int[] g : grid)
		    Arrays.fill(g, -1);
		isTraversable();
	}
	
	/**
	 * Use when adding a tower to make sure it's a legal move
	 * @param g Ground that is receiving the new tower.
	 * @return true if it's OK to add the tower, false otherwise.
	 */
	public boolean addTower(Ground g){
	    g.occupied = true;
	    g.occupied = isTraversable();
	    return g.occupied;
	}

    //use at beginning of round so creeps find quickest path
	public boolean isTraversable(){
	    int[][] tempGrid = new int[xSize][ySize];
        for(int[] g : tempGrid)
            Arrays.fill(g, -1);
	    Pair<Integer,Integer> endPoint = new Pair<Integer,Integer>(G.level.getEnd().xPos,G.level.getEnd().yPos);
	    Set<Pair<Integer,Integer>> unprocessed = new TreeSet<Pair<Integer,Integer>>(new PairComparator());
	    unprocessed.add(endPoint);
		return updateGrid(tempGrid,unprocessed);
	}
	
	 /*Description of path algorithm from some forum
	  * 
	  * I actually wrote a game once which did this sort of thing perfectly.
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
	private boolean updateGrid(int[][] tempGrid, Set<Pair<Integer,Integer>> unprocessed){
	    Set<Pair<Integer,Integer>> nextUnprocessed = new TreeSet<Pair<Integer,Integer>>(new PairComparator());
        for (Pair<Integer,Integer> p : unprocessed)
        {
            int minvalue = -1;
            
            // If the current ground is occupied, then make sure it's -1 and continue.
            if(G.level.getGround(p.first, p.second).occupied){
                tempGrid[p.first][p.second] = minvalue;
                continue;
            }

            // vectors to add to current to find neighbors
            List<Pair<Integer,Integer>> neighbors = new ArrayList<Pair<Integer,Integer>>();
            neighbors.add(new Pair<Integer,Integer>(0,1));
            neighbors.add(new Pair<Integer,Integer>(0,-1));
            neighbors.add(new Pair<Integer,Integer>(1,0));
            neighbors.add(new Pair<Integer,Integer>(-1,0));
            
            for(Pair<Integer,Integer> offset : neighbors)
            {
                Pair<Integer,Integer> n = Path.addPair(p, offset);
                // find minimum visited adjacent
                try {
                    if(minvalue == -1 || // minvalue not set OR
                            (tempGrid[n.first][n.second] != -1 && // neighbor visited AND
                            tempGrid[n.first][n.second] < minvalue)) { // less than minvalue
                        minvalue = tempGrid[n.first][n.second];
                    }
                    if (tempGrid[n.first][n.second] == -1 && // neighbor unvisited AND
                            !G.level.getGround(n.first, n.second).occupied) // unoccupied
                        nextUnprocessed.add(new Pair<Integer,Integer>(n.first, n.second));
                } catch ( ArrayIndexOutOfBoundsException e ){
                    // do nothing
                }
            }
            
            tempGrid[p.first][p.second] = minvalue + 1;
        }
        
        if (!nextUnprocessed.isEmpty()) // recursive base case
            return updateGrid(tempGrid,nextUnprocessed);
        else if (tempGrid[G.level.getStart().xPos][G.level.getStart().yPos] == -1)
            return false;
        else
        {
            // Look for cut off creeps
            for(Creep c : G.Creeps)
                if(tempGrid[c.currentGoal.xPos][c.currentGoal.yPos] == -1)
                    return false;
            
            grid = tempGrid;
            return true;
        }
	}
	
	public Ground getNextGoal(Ground currentGoal){
	    Pair<Integer,Integer> p = new Pair<Integer,Integer>(currentGoal.xPos,currentGoal.yPos);
	    
	    // Initialize to current goal.
	    Ground nextGoal = currentGoal;
	    int minvalue = grid[p.first][p.second];
	    
	    // vectors to add to current to find neighbors
	    List<Pair<Integer,Integer>> neighbors = new ArrayList<Pair<Integer,Integer>>();
	    neighbors.add(new Pair<Integer,Integer>(0,1));
	    neighbors.add(new Pair<Integer,Integer>(0,-1));
	    neighbors.add(new Pair<Integer,Integer>(1,0));
	    neighbors.add(new Pair<Integer,Integer>(-1,0));
	    
	    for(Pair<Integer,Integer> offset : neighbors)
	    {
	        Pair<Integer,Integer> n = Path.addPair(p, offset);
	        try {
	            if(grid[n.first][n.second] != -1 && // not blocked/occupied and
	                    grid[n.first][n.second] < minvalue) { // less than minvalue
	                minvalue = grid[n.first][n.second];
	                nextGoal = G.level.getGround(n.first,n.second);
	            }
	        } catch ( ArrayIndexOutOfBoundsException e ) {
	            // do nothing
	        }
	    }
        
	    return nextGoal;
	}

    public static Pair<Integer,Integer> addPair(Pair<Integer, Integer> lhs, Pair<Integer,Integer> rhs) {
        return new Pair<Integer,Integer>(lhs.first + rhs.first, lhs.second + rhs.second);
    }
    
}
