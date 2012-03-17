//Data container class for an achievement
package data;

import java.util.ArrayList;

public class Achievement {
	public String name;
	public ArrayList<Condition> cl = new ArrayList<Condition>();

	public Achievement(String name, ArrayList<Condition> cl){
		this.name = name;
		this.cl = cl;
	}
	
	//this only allows 'and' comparisons between a list of conditions
	public boolean check(Player p){
		for(Condition c : cl){
			if(!c.check(p)){
				return false;
			}
		}
		return true;
	}
	
	//used for set comparison(achievement name only)
	public boolean equals(Object t) {
	    if(t == null){
	    	return false;
	    }
	    else if(!(t instanceof Achievement)){
	    	return false;
	    }
	    return name.equals(((Achievement)t).name);
	}
}
