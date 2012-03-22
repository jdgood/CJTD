/**
 * 
 */
package com.cjdesign.cjtd.game.gameobjects.traps;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public class SpikeTrap extends Trap {
    
    /** damage trap deals every {@code frequency} */
    private int damage = 100;
    /** seconds between shots */
    private float frequency = 10f;
    /** seconds since last shot */
    private float lastShot;

    /**
     * @param g Ground the trap is on
     */
    public SpikeTrap(Ground g) {
        super(g);
        
        this.textureResource = R.drawable.spikes;
    }

    /* (non-Javadoc)
     * @see com.cjdesign.cjtd.game.gameobjects.GameObject#update(float)
     */
    @Override
    public void update(float dt) {
        if(lastShot >= frequency) {
            boolean hit = false;
            for(Creep c : G.Creeps) {
                // TODO figure out if the creep is actually on the trap
                if(c.getCurrentGoal() == this.getLocation()) {
                    c.takeDamage(damage);
                    hit = true;
                }
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

}
