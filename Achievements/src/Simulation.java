//This class simulates games being played and calls on the achievement system at the end of every game
import java.util.*;

import database.Database;

public class Simulation {
	//simulates (args[1]) games 
	public static void main(String[] args){
		if(args.length == 1)
			simulate(new Integer(args[0]));
		else
			simulate(2000);
	}
	
	//simulates n games with random values and players then calls achievement utility with gameid
	private static void simulate(int n){
		for(int i = 0; i < n; i++){
			System.out.println("Simulating game " + (i+1) + " of " + n);
			simulate();
		}
	}
	
	//simulates a single game with random values
	private static void simulate(){
		Random r = new Random(System.currentTimeMillis());
		
		String[] userset = {"UserA", "UserB", "UserC", "UserD", "UserE", "UserF", "UserG", "UserH", "UserI", "UserJ", "UserK", "UserL"};
		ArrayList<String> users = new ArrayList<String>(Arrays.asList(userset));
		
		//insert new game into db
		float GameTime = r.nextInt(80) + 60 * r.nextFloat();
		int gid = Database.addGame(GameTime);
		boolean win = r.nextBoolean();
		
		//either 3v3, 4v4 or 5v5
		int players = r.nextInt(2)*2 + 6;
		
		//add players with stats one at a time to a game
		for(int i = 0; i < players; i++){
			//get and remove a random user from the list
			String username = users.remove(r.nextInt(users.size()));
			
			//randomize stats
			int TotalAttacks = r.nextInt(1000);
			int TotalHits = r.nextInt(TotalAttacks + 1);
			int TotalDamage = r.nextInt(10 * TotalHits + 1);
			int Kills = r.nextInt(TotalDamage/50 + 1);
			int OneShotKills = r.nextInt(Kills + 1);
			int Assists = r.nextInt(TotalHits/2 + 1);
			int SpellsCast = r.nextInt(TotalAttacks + 1);
			int SpellDamage = r.nextInt((TotalAttacks - SpellsCast) * 10 + 1);
			float TimePlayed = r.nextFloat() * GameTime;
			int Deaths = r.nextInt(50);
			
			//add to db
			Database.addPlayerToGame(gid, username, win, TotalAttacks,
					TotalHits, TotalDamage, Kills, OneShotKills, Assists,
					SpellsCast, SpellDamage, TimePlayed, Deaths);
			
			//next player randomized will be other team
			win = !win;
		}
		//game with all players created, send to achievements.update(gameid)
		Achievements.update(gid);
	}
}
