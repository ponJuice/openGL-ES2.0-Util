package com.example.opengles20util.graphic.composition_mode;

import android.opengl.GLES20;

public class GLESS20COMPOSITION_ADD extends GLES20COMPOSITIONMODE {
	private static int destination = GLES20.GL_SRC_ALPHA;
	private static int source = GLES20.GL_ONE;
	@Override
	public void setBlendMode() {
		// TODO 自動生成されたメソッド・スタブ
		GLES20.glBlendFunc(GLESS20COMPOSITION_ADD.destination,GLESS20COMPOSITION_ADD.source);
	}

}
