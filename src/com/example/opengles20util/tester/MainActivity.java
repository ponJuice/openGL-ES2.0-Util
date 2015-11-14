package com.example.opengles20util.tester;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.opengles20util.R;
import com.example.opengles20util.Util.FileManager;
import com.example.opengles20util.Util.FpsController;
import com.example.opengles20util.core.GLES20Util;

public class MainActivity extends ActionBarActivity implements GLSurfaceView.Renderer{
	private FpsController fpsController = new FpsController((short)60);
	private Bitmap[] fpsImage;
	private int totalFrame = 0;

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		// TODO 自動生成されたメソッド・スタブ
		  process();
		  draw();
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
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
			totalFrame++;
		  }
		  private void draw(){
			// 描画領域をクリアする
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		    //FPSの表示
		    GLES20Util.DrawFPS(1.1f,1.8f,fpsController.getFps(),fpsImage);
		  }
}
