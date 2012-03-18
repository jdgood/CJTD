package com.cjdesign.cjtd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class CJTDActivity extends Activity {
	private static final int DIALOG_NEW_EASY_GAME_ID = 0;
	private static final int DIALOG_NEW_NORMAL_GAME_ID = 1;
	private static final int DIALOG_NEW_HARD_GAME_ID = 2;
	private static final int DIALOG_QUIT_ID = 3;
	
	View view;
	MediaPlayer mpMenu;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mpMenu = MediaPlayer.create(this, R.raw.menu);
        mpMenu.setLooping(true);
        mpMenu.start();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	mpMenu.release();
		mpMenu = null;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if(mpMenu!=null)
    		mpMenu.start();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if(mpMenu!=null)
        	mpMenu.pause();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	//mpMenu.start();
    	final String difficulty;
    	AlertDialog.Builder builder;
        
        switch(id) {
	        case DIALOG_QUIT_ID:
	        	builder = new AlertDialog.Builder(this);
	        	builder.setMessage("Are you sure you want to quit?")
	        	       .setCancelable(false)
	        	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   CJTDActivity.this.finish();
	        	           }
	        	       })
	        	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	           }
	        	       });
	        	return builder.create();
	        case DIALOG_NEW_EASY_GAME_ID:
	        	difficulty = "Easy";
	        	break;
	        case DIALOG_NEW_NORMAL_GAME_ID:
	        	difficulty = "Normal";
	        	break;
	        case DIALOG_NEW_HARD_GAME_ID:
	        	difficulty = "Hard";
	            break;
	        default:
	            return null;
        }
        
        builder = new AlertDialog.Builder(this);
        
    	builder.setMessage("Are you sure you want to erase old game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   CJTDActivity.this.createNewGame(difficulty);
    	           }
    	       })
    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	
    	return builder.create();
        
        
    }
    
    public void quit(View view) {
    	this.view = view;
    	showDialog(DIALOG_QUIT_ID);
    }
    
    public void newEasy(View view) {
    	this.view = view;
    	showDialog(DIALOG_NEW_EASY_GAME_ID);
    }
    
    
    public void newNormal(View view) {
    	this.view = view;
    	showDialog(DIALOG_NEW_NORMAL_GAME_ID);
    }

	public void newHard(View view) {
		this.view = view;
		showDialog(DIALOG_NEW_HARD_GAME_ID);
	}
	
	public void createNewGame(String mode){
		if(mode.equals("Easy")){
			System.out.println("Creating new easy game save");
			//set difficulty to easy
		}
		else if(mode.equals("Normal")){
			System.out.println("Creating new normal game save");
			//set difficulty to normal
		}
		else if(mode.equals("Hard")){
			System.out.println("Creating new hard game save");
			//set difficulty to hard
		}
		//load game using current gamestate
		runGame();
	}
	
	public void continueEasy(View view) {
		System.out.println("Continuing Easy Game");
		this.view = view;
		//load game using current gamestate
		runGame();
    }
    
    public void continueNormal(View view) {
    	System.out.println("Continuing Normal Game");
    	this.view = view;
    	//load game using current gamestate
    	runGame();
    }

	public void continueHard(View view) {
		System.out.println("Continuing Hard Game");
		this.view = view;
		//load game using current gamestate
		runGame();
	}
	
	private void runGame(){
		mpMenu.pause();
		Intent myIntent = new Intent(view.getContext(), MainGame.class);
        startActivityForResult(myIntent, 0);
	}
}