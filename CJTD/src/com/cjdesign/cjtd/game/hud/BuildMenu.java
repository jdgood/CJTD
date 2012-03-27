package com.cjdesign.cjtd.game.hud;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.globals.G;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public class BuildMenu extends PopupMenu {
    
    private Ground g;

    public BuildMenu() {
        ArrayList<MenuOption> options = new ArrayList<MenuOption>();
        options.add(new BuildMenuOption("Normal Tower", G.NORMAL_TOWER_ID));
        options.add(new BuildMenuOption("Alpha Tower", G.ALPHA_TOWER_ID));
        options.add(new BuildMenuOption("Spike Trap", G.SPIKE_TRAP_ID));
        options.add(new BuildMenuOption("Mud Trap", G.MUD_TRAP_ID));
        setOptions(options);
    }
    
    public Ground getGround() {
        return g;
    }
    
    public void show(float x, float y, Ground g) {
        super.show(x, y);
        this.g = g;
    }

}
