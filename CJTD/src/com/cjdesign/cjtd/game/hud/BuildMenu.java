package com.cjdesign.cjtd.game.hud;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.gameobjects.grid.Ground;
import com.cjdesign.cjtd.game.gameobjects.towers.AlphaObject;
import com.cjdesign.cjtd.game.gameobjects.towers.Tower;
import com.cjdesign.cjtd.game.gameobjects.traps.MudTrap;
import com.cjdesign.cjtd.game.gameobjects.traps.SpikeTrap;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public class BuildMenu extends PopupMenu {
    
    private Ground g;

    public BuildMenu() {
        ArrayList<MenuOption> options = new ArrayList<MenuOption>();
        options.add(new MenuOption("Normal Tower"){
            @Override
            public void onSelection() {
                new Tower(g);
            }
        });
        options.add(new MenuOption("Alpha Tower"){
            @Override
            public void onSelection() {
                new AlphaObject(g);
            }
        });
        options.add(new MenuOption("Spike Trap"){
            @Override
            public void onSelection() {
                new SpikeTrap(g);
            }
        });
        options.add(new MenuOption("Mud Trap"){
            @Override
            public void onSelection() {
                new MudTrap(g);
            }
        });
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
