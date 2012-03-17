//This is the entry point for the achievements system.
import java.util.ArrayList;

import data.*;
import database.Database;

public class Achievements {
	//Updates achievements in the database using given game id
   public static void update(int gameid){
	   //pull game data from db then call update
	   update(Database.getGame(gameid));
   }
	
   //Updates achievements in the database using given game data
   public static void update(Game g){
	   //grab a list of all achievements
	   ArrayList<Achievement> asbase = new ArrayList<Achievement>(Database.getAchievements()); 
	   for(Player p : g.pl){
		   ArrayList<Achievement> as = new ArrayList<Achievement>(asbase); 
		   //grab list of achievement names player has and take difference from all achievements
		   as.removeAll(Database.getAchievements(p.username));
		   //call check on list of achievements player does not have
		   for(Achievement a : as){
			   if(a.check(p)){
				   System.out.println("\t" + a.name + " awarded to " + p.username);
				   Database.achievementUnlocked(a.name, p.username);
			   }
		   }
	   }
   }
   
}
