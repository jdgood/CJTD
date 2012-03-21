package com.cjdesign.cjtd;

import com.cjdesign.cjtd.globals.G;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameMenu extends Activity {

	//private View view;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if(G.mpMenu!=null)
    		G.mpMenu.start();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if(G.mpMenu!=null)
    		G.mpMenu.pause();
    }
    
    public void playGame(View view){
    	//this.view = view;
    	if(G.mpMenu!=null)
    		G.mpMenu.pause();
		Intent myIntent = new Intent(view.getContext(), MainGame.class);
        startActivityForResult(myIntent, 0);
	}
}
