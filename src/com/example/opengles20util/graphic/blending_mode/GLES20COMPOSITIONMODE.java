package com.example.opengles20util.graphic.blending_mode;

import android.opengl.GLES20;


public abstract class GLES20COMPOSITIONMODE {
	protected int destination;
	protected int source;
	public void setBlendMode(){
		GLES20.glBlendFunc(destination,source);
	};
}
