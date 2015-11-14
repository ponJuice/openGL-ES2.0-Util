package com.example.opengles20util.graphic;

import android.graphics.Bitmap;

import com.example.opengles20util.graphic.composition_mode.GLES20COMPOSITIONMODE;

public class Image {
	private int program;
	private Bitmap image;
	private GLES20COMPOSITIONMODE mode;
	public Bitmap getImage() {
		return image;
	}
	public GLES20COMPOSITIONMODE getBlend() {
		return mode;
	}
}
