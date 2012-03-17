package com.cjdesign.cjtd.game;

import javax.microedition.khronos.opengles.*; 
import android.content.Context; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import java.lang.Integer; 
import java.nio.ByteBuffer; 
import java.nio.ByteOrder; 
import java.nio.IntBuffer; 

public class GLTextures {
	
	public GLTextures(GL10 gl,Context context) {
		this.gl = gl;
		this.context = context;
		this.textureMap = new java.util.HashMap<Integer, Integer>();
	}
	
	public void loadTextures() {
		int[] tmp_tex = new int[textureFiles.length];
		gl.glGenTextures(textureFiles.length, tmp_tex, 0);
		textures = tmp_tex;
		
		for(int i=0;i < textureFiles.length; i++) {
			Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), textureFiles[i]);
			ByteBuffer bb = extract(bmp);
			// Get a new texture name
			// Load it up
			this.textureMap.put(new Integer(textureFiles[i]),new Integer(i));
			int tex = tmp_tex[i];
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
			gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA,width, height, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		}
	}
	
	public void setTexture(int id) {
		try {
			int textureid = (Integer)this.textureMap.get(new Integer(id));
			gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textures[textureid]);
		}
		catch(Exception e) {
			return;
		}
	}
	
	private static ByteBuffer extract(Bitmap bmp) {
		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
		bb.order(ByteOrder.BIG_ENDIAN);
		IntBuffer ib = bb.asIntBuffer();
		// Convert ARGB -> RGBA
		for (int y = bmp.getHeight() - 1; y > -1; y--)
		{
			for (int x = 0; x < bmp.getWidth(); x++)
			{
				int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);
				int alpha = ((pix >> 24) & 0xFF);
				int red = ((pix >> 16) & 0xFF);
				int green = ((pix >> 8) & 0xFF);
				int blue = ((pix) & 0xFF);
				
				// Make up alpha for interesting effect
				
				//ib.put(red << 24 | green << 16 | blue << 8 | ((red + blue + green) / 3));
				ib.put(red << 24 | green << 16 | blue << 8 | alpha);
			}
		}
		bb.position(0);
		return bb;
	}
	
	
	
	public void add(int resource) {
		if(textureFiles==null) {
			textureFiles = new int[1];
			textureFiles[0]=resource;
		}
		else
		{
			int[] newarray = new int[textureFiles.length+1];
			for(int i=0;i<textureFiles.length;i++){
				newarray[i]=textureFiles[i];
			}
			newarray[textureFiles.length]=resource;
			textureFiles = newarray;
		}
	}
	
	private java.util.HashMap textureMap;
	private int[] textureFiles;
	private GL10 gl;
	private Context context;
	private int[] textures;
} 