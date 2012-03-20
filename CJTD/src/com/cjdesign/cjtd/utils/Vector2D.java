package com.cjdesign.cjtd.utils;

import android.util.FloatMath;

public class Vector2D {
		  public float x;
		  public float y;
		  
		  public Vector2D(float x,float y){
			  this.x = x;
			  this.y = y;
		  }
		  
		  public float mag(){
			  return FloatMath.sqrt(x*x + y*y);
		  }
		  
		  public void normalize(){
			  float mag = mag();
			  x/=mag;
			  y/=mag;
		  }
}
