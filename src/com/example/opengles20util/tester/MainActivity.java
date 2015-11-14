package com.example.opengles20util.tester;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import com.example.opengles20util.R;
import com.example.opengles20util.Util.FileManager;
import com.example.opengles20util.Util.FpsController;
import com.example.opengles20util.core.GLES20Util;

public class MainActivity extends Activity implements GLSurfaceView.Renderer{
	private FpsController fpsController = new FpsController((short)60);
	private Bitmap[] fpsImage = new Bitmap[10];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);

	     // OpenGL ES 2.0が使用できるように初期化する
	     GLSurfaceView glSurfaceView = GLES20Util.initGLES20(this, this);

	     // GLSurfaceViewをこのアプリケーションの画面として使用する
	     setContentView(glSurfaceView);
	     Log.d("onCreate","onCreate finished");
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO 自動生成されたメソッド・スタブ
		  process();
		  draw();
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		Log.d("MainActivity","onSurfaceChanged");
	    // 表示領域を設定する
	    GLES20Util.initDrawErea(width, height,false);
	    //テクスチャの再読み込み
	    GLES20Util.initTextures();
	    GLES20Util.initFpsBitmap(fpsImage,true,R.drawable.degital2);
		Log.d("onSurfaceCreated","initShader");
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		String vertexShader = new String(FileManager.readShaderFile(this,"VSHADER.txt"));
		String fragmentShader = new String(FileManager.readShaderFile(this,"FSHADER.txt"));
		GLES20Util.initGLES20Util(vertexShader,fragmentShader);
	    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // 画面をクリアする色を設定する
	}

	private void process(){
			fpsController.updateFps();
	}
	private void draw(){
			// 描画領域をクリアする
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
			//文字の描画
			GLES20Util.DrawString("Hello OpenGLES2.0!!", 1, 255, 255, 255, 0, 0);

		    //FPSの表示
		    GLES20Util.DrawFPS(1.1f,1.8f,fpsController.getFps(),fpsImage);
	}
}
