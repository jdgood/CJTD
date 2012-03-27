package com.cjdesign.cjtd.game.hud;

import java.util.ArrayList;

import com.cjdesign.cjtd.game.gameobjects.grid.Ground;

public class UpgradeMenu extends PopupMenu {

    private Ground g;

    public UpgradeMenu() {
        ArrayList<MenuOption> options = new ArrayList<MenuOption>();
        options.add(new MenuOption("Upgrade"){
            @Override
            public void onSelection() {
                // TODO Upgrade Tower
            }
        });
        options.add(new MenuOption("Sell"){
            @Override
            public void onSelection() {
                // TODO something with money
                g.removeTower();
                g.removeTrap();
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
