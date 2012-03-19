package com.cjdesign.cjtd.game.textures;

import javax.microedition.khronos.opengles.*; 


import android.content.Context; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Integer; 
import java.util.HashMap;

public class GLTextures {
	
	private Context context;
	private HashMap<Integer, Integer> textureMap;

	public GLTextures(Context context) {
		this.context = context;
		this.textureMap = new java.util.HashMap<Integer, Integer>();
	}
	
	public int loadTexture(int resourceID, GL10 gl) {
		if(textureMap.containsKey(resourceID)){
			//System.out.println("Accessed old texture");
			return textureMap.get(resourceID);
		}
		//System.out.println("Created new texture");
		int newTex = loadGLTexture(resourceID, gl);
		textureMap.put(resourceID, newTex);
		return newTex;
	}
	
	/**
	 * Load the textures
	 * @param resourceid 
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	private int loadGLTexture(int resourceID, GL10 gl) {
		//Get the texture from the Android resource directory
		InputStream is = context.getResources().openRawResource(resourceID);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate one texture pointer...
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Clean up
		bitmap.recycle();
		
		return textures[0];
	}
} 