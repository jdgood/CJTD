package com.cjdesign.cjtd.game.hud;

import com.cjdesign.cjtd.globals.G;

public class BuildMenuOption extends MenuOption {
    
    private int buildId = 0;
    
    public BuildMenuOption(String label, int id) {
        super(label);
        buildId = id;
    }

    public void onSelection() {
        G.buildId = this.buildId;
    }
}
