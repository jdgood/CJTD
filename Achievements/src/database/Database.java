//Database wrapper class. Creates and populates database with base content such as users and achievements that can be earned. All selects, inserts, and updates are also in this class
package database;

import java.sql.*;
import java.util.ArrayList;

import data.*;

public class Database {
	//cleans and populates database with basic necessities
	public static void main(String[] args){
		clean();
		populate();
	}

	 //creates a fresh db
	 public static void clean(){
	    try {
	        Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
	        Statement stat = conn.createStatement();
	        
	        stat.executeUpdate("drop table if exists participates;");
	        stat.executeUpdate("drop table if exists earned;");
	        stat.executeUpdate("drop table if exists conditions;");
	        stat.executeUpdate("drop table if exists achievements;");
	        stat.executeUpdate("drop table if exists users;");
	        stat.executeUpdate("drop table if exists games;");
	        
	        //contains game data
	        stat.executeUpdate("create table Games(" +
	            "GID integer not null," +
	            "GameLength number not null," +
	            "PRIMARY KEY (GID asc)" +
	            ");");
	
	        //contains user/player data including global statistics
	        stat.executeUpdate("create table Users(" +
	            "Username varchar2(40) not null," +
	            "GamesPlayed integer not null," +
	            "TotalTime number not null," +
	            "TotalKills integer not null," +
	            "PRIMARY KEY (username)" +
	            ");");
	
	        //contains name and description of all earnable achievements in the game
	        stat.executeUpdate("create table Achievements(" +
	            "Name varchar2(40) not null," +
	            "Description varchar2(255) not null," +
	            "PRIMARY KEY (name)" +
	            ");");
	
	        //contains the conditionals for every achievement based on a player stat
	        stat.executeUpdate("create table Conditions(" +
	            "Name varchar2(40) not null," +
	      		"Attribute varchar2(40) not null," +
	            "Comparison char(1) not null," +
	            "CompareTo number not null," +
	            "FOREIGN KEY (name) references Achievements," +
	            "PRIMARY KEY (name, attribute)" +
	            ");");
	
	        //contains achievements earned by players and the date they earned it
	        stat.executeUpdate("create table Earned(" +
	            "Name varchar2(40) not null," +
	            "Username varchar2(40) not null," +
	            "DateEarned date not null," + 
	            "FOREIGN KEY (name) references Achievements," +
	            "FOREIGN KEY (username) references Users," +
	            "PRIMARY KEY (name, username)" +
	            ");");
	
	        //contains individual player statistics for a game
	        stat.executeUpdate("create table Participates(" +
	            "GID integer not null," +
	            "Username varchar2(40) not null," +
	            "Win integer not null," + 
	            "TotalAttacks integer not null," +
	            "TotalHits integer not null," +
	            "TotalDamage integer not null," +
	            "Kills integer not null," +
	            "OneShotKills integer not null," +
	            "Assists integer not null," +
	            "SpellsCast integer not null," +
	            "SpellDamage integer not null," +
	            "TimePlayed number not null," +
	            "Deaths integer not null," +
	            "FOREIGN KEY (username) references Users," +
	            "FOREIGN KEY (gid) references Games," +
	            "PRIMARY KEY (username, gid)" +
	            ");");
	        
	        stat.close();            
	        conn.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//populates db with only necessary data
	public static void populate(){
		try {
	        Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
	        Statement stat = conn.createStatement();
	        
	        System.out.println("Users:");
	        
	        PreparedStatement prep = conn.prepareStatement("insert into users values (?,0,0,0);");
	        prep.setString(1, "UserA");
	        prep.addBatch();
	        prep.setString(1, "UserB");
	        prep.addBatch();
	        prep.setString(1, "UserC");
	        prep.addBatch();
	        prep.setString(1, "UserD");
	        prep.addBatch();
	        prep.setString(1, "UserE");
	        prep.addBatch();
	        prep.setString(1, "UserF");
	        prep.addBatch();
	        prep.setString(1, "UserG");
	        prep.addBatch();
	        prep.setString(1, "UserH");
	        prep.addBatch();
	        prep.setString(1, "UserI");
	        prep.addBatch();
	        prep.setString(1, "UserJ");
	        prep.addBatch();
	        prep.setString(1, "UserK");
	        prep.addBatch();
	        prep.setString(1, "UserL");
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        prep.close();
	        
	        stat = conn.createStatement();
	        ResultSet rs = stat.executeQuery("select * from users;");
	        while (rs.next()) {
	            System.out.println("Username = " + rs.getString("username"));
	        }
	        rs.close();
	        
	        System.out.println("\nAchievements:");
	        
	        prep = conn.prepareStatement("insert into achievements values (?,?);");
	        prep.setString(1, "Sharpshooter Award");
	        prep.setString(2, "Land 75% of attacks in a single game (you must attack at least once)");
	        prep.addBatch();
	        prep.setString(1, "Bruiser");
	        prep.setString(2, "Deal 500 points of damage in a single game");
	        prep.addBatch();
	        prep.setString(1, "Veteran");
	        prep.setString(2, "Play 1000 games total");
	        prep.addBatch();
	        prep.setString(1, "Big Winner");
	        prep.setString(2, "Win 200 games total");
	        prep.addBatch();
	        prep.setString(1, "No Mercy");
	        prep.setString(2, "Achieve a Kill to Death Ratio greater than 2.5");
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        prep.close();
	        
	        prep = conn.prepareStatement("insert into conditions values (?,?,?,?);");
	        prep.setString(1, "Sharpshooter Award");
	        prep.setString(2, "TotalAttacks");
	        prep.setString(3, ">");
	        prep.setFloat(4, 0);
	        prep.addBatch();
	        prep.setString(1, "Sharpshooter Award");
	        prep.setString(2, "Accuracy");
	        prep.setString(3, ">");
	        prep.setDouble(4, .75);
	        prep.addBatch();
	        prep.setString(1, "Bruiser");
	        prep.setString(2, "TotalDamage");
	        prep.setString(3, ">");
	        prep.setFloat(4, 500);
	        prep.addBatch();
	        prep.setString(1, "Veteran");
	        prep.setString(2, "GamesPlayed");
	        prep.setString(3, ">");
	        prep.setFloat(4, 1000);
	        prep.addBatch();
	        prep.setString(1, "Big Winner");
	        prep.setString(2, "GamesWon");
	        prep.setString(3, "=");
	        prep.setFloat(4, 200);
	        prep.addBatch();
	        prep.setString(1, "No Mercy");
	        prep.setString(2, "KDR");
	        prep.setString(3, ">");
	        prep.setDouble(4, 2.5);
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        prep.close();
	        
	        stat = conn.createStatement();
	        rs = stat.executeQuery("select * from achievements;");
	        while (rs.next()) {
	        	System.out.println(rs.getString("name") + ": " + rs.getString("description"));
	        	
	        	prep = conn.prepareStatement("select * from conditions where name=?;");
	        	prep.setString(1, rs.getString("name"));
	            ResultSet rsprime = prep.executeQuery();
	            while(rsprime.next()){
	            	System.out.println("\t" + rsprime.getString("attribute") + " " + rsprime.getString("comparison") + " " + rsprime.getFloat("compareto"));
	            }
	            rsprime.close();
	            prep.close();
	        }
	        rs.close();
	        
	        stat.close();
	        conn.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//Adds a new game to db and returns its unique id
	public static int addGame(double time){
		try {
	        Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
	
	        PreparedStatement prep = conn.prepareStatement("insert into games values (null,?);");
	        prep.setDouble(1, time);
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        
	        prep.close();
	
	        Statement stat = conn.createStatement();
	        ResultSet rs = stat.executeQuery("select * from games order by gid desc;");
	        int r = rs.getInt("gid");
	        rs.close();
	        stat.close();
	        conn.close();
	        
	        return r;
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
		
		return -1;
	}
	
	//Adds a player to the game using statistics from a game(denotes a players participation in a game)
	public static void addPlayerToGame(int gid, String username, boolean win, int TotalAttacks,
			int TotalHits, int TotalDamage, int Kills, int OneShotKills, int Assists,
			int SpellsCast, int SpellDamage, float TimePlayed, int Deaths){
		try {
	        Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
	        PreparedStatement prep = conn.prepareStatement("insert into participates values (?,?,?,?,?,?,?,?,?,?,?,?,?);");
	        prep.setInt(1, gid);
	        prep.setString(2, username);
	        prep.setInt(3, win?1:0);
	        prep.setInt(4, TotalAttacks);
	        prep.setInt(5, TotalHits);
	        prep.setInt(6, TotalDamage);
	        prep.setInt(7, Kills);
	        prep.setInt(8, OneShotKills);
	        prep.setInt(9, Assists);
	        prep.setInt(10, SpellsCast);
	        prep.setInt(11, SpellDamage);
	        prep.setFloat(12, TimePlayed);
	        prep.setInt(13, Deaths);
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        
	        prep.close();
	        
	        updatePlayer(conn, username, Kills, TimePlayed);
	        conn.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
	}
	
	//updates global stats using data from a game
	private static void updatePlayer(Connection conn, String username, int Kills, float TimePlayed){
		try {
	        PreparedStatement prep = conn.prepareStatement("update users set gamesplayed=gamesplayed+1, totaltime=totaltime+?, totalkills=totalkills+? where username=?;");
	        prep.setFloat(1, TimePlayed);
	        prep.setInt(2, Kills);
	        prep.setString(3, username);
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        
	        prep.close();
	        conn.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
	}
	
	//Creates a game container from db using unique id
	public static Game getGame(int gid){
		ArrayList<Player> pl = new ArrayList<Player>();
		
		try{
			Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
			
			PreparedStatement prep = conn.prepareStatement("select * from users natural join participates where gid=?;");
	        prep.setInt(1, gid);
	        ResultSet rs = prep.executeQuery();
	        
	        while(rs.next()){
	        	//selects the amount of wins for each player
	        	//select username, sum(win) as wins from participates where win=1 group by username;
		        PreparedStatement prepprime = conn.prepareStatement("select count(*) as wins from participates where win=1 and username=?;");
		        prepprime.setString(1, rs.getString("username"));
		        ResultSet rsprime = prepprime.executeQuery();
	        	pl.add(new Player(gid, rs.getString("username"), rs.getInt("gamesPlayed"), rs.getInt("totalKills"), rs.getInt("TotalAttacks"),
	        			rs.getInt("TotalHits"), rs.getInt("TotalDamage"), rs.getInt("Kills"), rs.getInt("OneShotKills"), rs.getInt("Assists"), rs.getInt("SpellsCast"), rs.getInt("SpellDamage"), 
	        			rs.getFloat("TimePlayed"), rsprime.getInt("wins"), rs.getInt("deaths")));
	        	rsprime.close();
	        	prepprime.close();
	        }
	        rs.close();
	        prep.close();
	        conn.close();
		}
		catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
		
		return new Game(gid, pl);
	}
	
	//Returns a list of all possible achievements in the db
	public static ArrayList<Achievement> getAchievements(){
		ArrayList<Achievement> al = new ArrayList<Achievement>();
		
		try{
			Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
			
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select name from achievements;");
	        
	        while(rs.next()){
		        ArrayList<Condition> cl = new ArrayList<Condition>();
	        	
		        PreparedStatement prep = conn.prepareStatement("select * from conditions where name=?;");
		        prep.setString(1, rs.getString("name"));
		        ResultSet rsprime = prep.executeQuery();
		        
		        while(rsprime.next()){
		        	cl.add(new Condition(rsprime.getString("attribute"), rsprime.getString("comparison"), rsprime.getFloat("compareTo")));
		        }
		        
	        	al.add(new Achievement(rs.getString("name"), cl));
	        	
	        	rsprime.close();
	        	prep.close();
	        }
	        rs.close();
	        stat.close();
	        conn.close();
		}
		catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
		
		return al;
	}
	
	//Returns a list of all achievements(only names) earned by a user
	public static ArrayList<Achievement> getAchievements(String username){
		ArrayList<Achievement> al = new ArrayList<Achievement>();
		
		try{
			Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
			
			PreparedStatement prep = conn.prepareStatement("select name from earned where username=?;");
			prep.setString(1, username);
			ResultSet rs = prep.executeQuery();
	        
	        while(rs.next()){
	        	al.add(new Achievement(rs.getString("name"), new ArrayList<Condition>()));
	        }
	        rs.close();
	        prep.close();
	        conn.close();
		}
		catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }
		
		return al;
	}
	
	//Adds an achievement to a users collection
	public static void achievementUnlocked(String name, String username) {
		try {
	        Class.forName("org.sqlite.JDBC");
	        Connection conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
	
	        PreparedStatement prep = conn.prepareStatement("insert into earned values (?,?,date('now'));");
	        prep.setString(1, name);
	        prep.setString(2, username);
	        prep.addBatch();
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	        
	        prep.close();
	        conn.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	        System.exit(0);
	    }		
	}
}