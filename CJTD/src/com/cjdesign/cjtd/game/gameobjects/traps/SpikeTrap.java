/**
 * 
 */
package com.cjdesign.cjtd.game.gameobjects.traps;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public class SpikeTrap extends Trap {
    
    /** damage trap deals every {@code frequency} */
    private int damage = 1;
    /** seconds between shots */
    private float frequency = 0.5f;
    /** seconds since last shot */
    private float lastShot = frequency;
    
    private ArrayList<Creep> targets;

    /**
     * @param g Ground the trap is on
     */
    public SpikeTrap(Ground g) {
        super(g);
        
        this.textureResource = R.drawable.spikes;
        
        targets = new ArrayList<Creep>();
    }

    /* (non-Javadoc)
     * @see com.cjdesign.cjtd.game.gameobjects.GameObject#update(float)
     */
    @Override
    public void update(float dt) {
        if(lastShot >= frequency) {
            boolean hit = false;
            for(Creep c : targets) {
                c.takeDamage(damage);
                hit = true;
            }
            if(hit)
                lastShot = 0;
        } else {
            lastShot+=dt;
        }

    }
    
    @Override
    public void draw(GL10 gl) {
        super.draw(gl);
    }

    @Override
    public void onEnter(Creep c) {
        targets.add(c);        
    }

    @Override
    public void onExit(Creep c) {
        targets.remove(c);        
    }

}
