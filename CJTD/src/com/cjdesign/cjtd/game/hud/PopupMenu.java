/**
 * 
 */
package com.cjdesign.cjtd.game.hud;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import com.cjdesign.cjtd.globals.G;

/**
 * @author Erik Sandberg <erik@sandberg.net>
 *
 */
public abstract class PopupMenu {

    private boolean visible = false;
    private boolean initialized = false;
    private float left,right,top,bottom;
    private int height, width, itemHeight;
    private float scale = 2;
    
    private ArrayList<MenuOption> options;
    
    private void init() {
        G.tf.SetScale(scale);
        height = G.tf.GetTextHeight() * getOptions().size();
        
        width = 0;
        for(MenuOption s : getOptions()) {
            int w = G.tf.GetTextLength(s.getLabel());
            if(w > width)
                width = w;
        }
        itemHeight = G.tf.GetTextHeight();
        
        initialized = true;
    }
    
    public void draw(GL10 gl) {
        if(!initialized)
            init();

        if(!isVisible())
            return;

        G.tf.SetScale(scale);
        for(int i = 0; i < getOptions().size(); i++) {
            G.tf.PrintAt(gl, getOptions().get(i).getLabel(), (int)left, (int)(top-itemHeight*(i+1)));
        }
    }

    public void setPosition(float x, float y) {
        y = G.H - y;

        left = x;
        bottom = y;
        right = left + width;
        top = bottom + height;
        
        if(top > G.H) {
            top = y;
            bottom = top - height;
        }
        if(right > G.W){
            right = x;
            left = right - width;
        }
    }

    public boolean hitTest(float x, float y) {
        y = G.H - y;
        if(!isVisible())
            return false;
        this.hide();
        
        if(x > left && x < right && y < top && y > bottom) {
            for(int i = 0; i < getOptions().size(); i++) {
                if(y > top - (i+1) * itemHeight) {
                    this.getOptions().get(i).onSelection();
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * @return if is visible
     */
    public boolean isVisible() {
        return visible;
    }
    
    public void show(float x, float y) {
        setPosition(x,y);
        visible = true;
    }
    
    public void hide() {
        visible = false;
    }

    /**
     * @return the options
     */
    public ArrayList<MenuOption> getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(ArrayList<MenuOption> options) {
        this.options = options;
    }

}
