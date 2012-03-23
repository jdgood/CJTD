/**
 * 
 */
package com.cjdesign.cjtd.game.gameobjects.traps;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.game.gameobjects.grid.Ground;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public class MudTrap extends Trap {
    
    private float effect = 0.5f;

    /**
     * @param g
     */
    public MudTrap(Ground g) {
        super(g);
        
        this.textureResource = R.drawable.mud;
    }

    /* (non-Javadoc)
     * @see com.cjdesign.cjtd.game.gameobjects.GameObject#update(float)
     */
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnter(Creep c) {
        c.setSpeed(c.getSpeed() * getEffect());
    }

    @Override
    public void onExit(Creep c) {
        c.setSpeed(c.getNormalSpeed());
    }

    /**
     * @return the effect
     */
    public float getEffect() {
        return effect;
    }

    /**
     * @param effect the effect to set
     */
    public void setEffect(float effect) {
        this.effect = effect;
    }

}
