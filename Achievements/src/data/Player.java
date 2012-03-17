//Data container for a player. Contains a single game's stats along with the players global stats
package data;

public class Player {
	public String username;
	public int gid, gamesPlayed, totalKills, TotalAttacks, TotalHits, TotalDamage, Kills, OneShotKills, Assists, SpellsCast, SpellDamage, wins, Deaths;
	public float TimePlayed;
	
	public Player(int gid, String username, int gamesPlayed, int totalKills, int TotalAttacks,
		int TotalHits, int TotalDamage, int Kills, int OneShotKills, int Assists, int SpellsCast, int SpellDamage, 
		float TimePlayed, int wins, int Deaths){
		this.username = username;
		this.gid = gid;
		this.gamesPlayed = gamesPlayed;
		this.totalKills = totalKills;
		this.TotalAttacks = TotalAttacks;
		this.TotalHits = TotalHits;
		this.TotalDamage = TotalDamage;
		this.Kills = Kills;
		this.OneShotKills = OneShotKills;
		this.Assists = Assists;
		this.SpellsCast = SpellsCast;
		this.SpellDamage = SpellDamage;
		this.wins = wins;
		this.TimePlayed = TimePlayed;
		this.Deaths = Deaths;
	}
}
