//Data container class for an individual condition within an achievement 
package data;

public class Condition {
	public String attribute, comparison;
	public float compareTo;

    public Condition(String attribute, String comparison, float compareTo){
    	this.attribute = attribute;
    	this.comparison = comparison;
    	this.compareTo = compareTo;
    }
    
    //checks if a condition is satisfied
    public boolean check(Player p){
    	//get attribute
    	float compareFrom = getAttribute(p);
    	//uses the porper comparison
    	if(comparison.equals("<")){
    	 	return compareFrom < compareTo;
    	}
    	else if(comparison.equals(">")){
    		return compareFrom > compareTo;
    	}
    	else{
    		return compareTo == compareFrom;
    	}
    }
    
    //returns statistic data for a player based on the type needed
    private float getAttribute(Player p){
    	//only usable in jdk 1.7 and greater, commented out in favor of if-else statements for compatibilities sake
		/*switch(attribute.toLowerCase()){
	    	case "gamesplayed":
				return p.gamesPlayed;
	    	case "totalkills":
				return p.totalKills;
	    	case "totalattacks":
				return p.TotalAttacks;
	    	case "totalhits":
				return p.TotalHits;
	    	case "totaldamage":
				return p.TotalDamage;
	    	case "kills":
				return p.Kills;
	    	case "oneshotkills":
				return p.OneShotKills;
	    	case "assists":
				return p.Assists;
	    	case "spellscast":
				return p.SpellsCast;
	    	case "spelldamage":
				return p.SpellDamage;
	    	case "gameswon":
				return p.wins;
	    	case "timeplayed":
				return p.TimePlayed;
	    	case "accuracy":
	    		return p.TotalAttacks > 0?(float)(1.0 * p.TotalHits / p.TotalAttacks):0;
	        case "kdr":
	        	return p.Deaths > 0?(float)(1.0 * p.Kills / p.Deaths):p.Kills;
	        default:
	        	return -1;
    	}*/
		String comp = attribute.toLowerCase();
    	if(comp.equals("gamesplayed"))
			return p.gamesPlayed;
    	else if(comp.equals("totalkills"))
			return p.totalKills;
    	else if(comp.equals("totalattacks"))
			return p.TotalAttacks;
    	else if(comp.equals("totalhits"))
			return p.TotalHits;
    	else if(comp.equals("totaldamage"))
			return p.TotalDamage;
    	else if(comp.equals("kills"))
			return p.Kills;
    	else if(comp.equals("oneshotkills"))
			return p.OneShotKills;
    	else if(comp.equals("assists"))
			return p.Assists;
    	else if(comp.equals("spellscast"))
			return p.SpellsCast;
    	else if(comp.equals("spelldamage"))
			return p.SpellDamage;
    	else if(comp.equals("gameswon"))
			return p.wins;
    	else if(comp.equals("timeplayed"))
			return p.TimePlayed;
    	else if(comp.equals("accuracy"))
    		return p.TotalAttacks > 0?(float)(1.0 * p.TotalHits / p.TotalAttacks):0;
    	else if(comp.equals("kdr"))
    		return p.Deaths > 0?(float)(1.0 * p.Kills / p.Deaths):p.Kills;
    	else
        	return -1;
    }
}
