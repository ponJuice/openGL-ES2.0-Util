package com.example.opengles20util.graphic.blending_mode;

import android.opengl.GLES20;

public class GLES20COMPOSITION_ALPHA extends GLES20COMPOSITIONMODE {
	private static GLES20COMPOSITIONMODE instance;
	private GLES20COMPOSITION_ALPHA(){
		destination = GLES20.GL_SRC_ALPHA;
		source =GLES20.GL_ONE_MINUS_SRC_ALPHA;
	};
	public static GLES20COMPOSITIONMODE getInstance() {
		if(instance == null)
			instance = new GLES20COMPOSITION_ALPHA();
		return instance;
	}
}
