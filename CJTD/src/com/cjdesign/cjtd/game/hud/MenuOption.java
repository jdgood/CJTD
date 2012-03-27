package com.cjdesign.cjtd.game.hud;

public abstract class MenuOption {

    private String label;
    
    public MenuOption() {
        label = new String();
    }
    
    public MenuOption(String label) {
        this.label = label;
    }
    
    public abstract void onSelection();

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
}
