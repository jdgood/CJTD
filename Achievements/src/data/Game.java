//Data container class for a game played
package data;

import java.util.ArrayList;

//Data container for game data
public class Game {
	int gid;
	public ArrayList<Player> pl;
	public Game(int gid, ArrayList<Player> pl){
		this.gid = gid;
		this.pl = pl;
	}
}
