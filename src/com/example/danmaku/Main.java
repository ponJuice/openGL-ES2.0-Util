package com.example.danmaku;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;

public class Main extends Activity implements GLSurfaceView.Renderer {
  // メンバー変数
  private boolean touch = false;
  private ScaleGestureDetector gesDetect = null;
  private int bulletImage,plane;
  private Bitmap[] fpsImage = new Bitmap[10];
  private Enemy enemy;
  private Player player;
  private String vertexShader;
  private String fragmentShader;

  public boolean onTouchEvent(MotionEvent event){
	  if(event.getPointerCount()>1){
		  //マルチタッチを検出
		  gesDetect.onTouchEvent(event);
		  touch = true;
		  return true;
	  }
	  switch(event.getAction()){
	  case MotionEvent.ACTION_DOWN:
		  player.setTouchDown(event.getX(),event.getY());
		  break;
	  case MotionEvent.ACTION_MOVE:
		  player.move(event.getX(),event.getY());
		  break;
	  case MotionEvent.ACTION_UP:
		  break;
	  }
	return true;
  }

//スケールジェスチャーイベントを取得
private final SimpleOnScaleGestureListener onScaleGestureListener = new SimpleOnScaleGestureListener(){
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		Log.v("ScaleGesture", "onScaleBegin");
		return super.onScaleBegin(detector);
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		//moveZ = positionZ;
		//positionZ = 0;
		Log.v("ScaleGesture", "onScaleEnd");
		super.onScaleEnd(detector);
	}
};

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);

     // OpenGL ES 2.0が使用できるように初期化する
     GLSurfaceView glSurfaceView = GLES20Util.initGLES20(this, this);

     // GLSurfaceViewをこのアプリケーションの画面として使用する
     setContentView(glSurfaceView);

     // ScaleGestureDetecotorクラスのインスタンス生成
     gesDetect = new ScaleGestureDetector(this, onScaleGestureListener);

     plane = BitmapList.setBitmap(GLES20Util.loadBitmap(R.drawable.plane));
     bulletImage = BitmapList.setBitmap(GLES20Util.loadBitmap(R.drawable.bomd2));
     player = new Player(plane,0.1f,0.1f);
     enemy = new Enemy(60,180,0.0f,0.0f,1.0f,1.5f,1.0f,0.0f,0,0,plane,0,0,0);


     Log.d("onCreate","onCreate finished");
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	Log.d("onSurfaceCreated","initShader");
	vertexShader = new String(FileManager.readShaderFile(this,"VSHADER.txt"));
	fragmentShader = new String(FileManager.readShaderFile(this,"FSHADER.txt"));
	GLES20Util.initGLES20Util(vertexShader,fragmentShader);
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // 画面をクリアする色を設定する
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    // 表示領域を設定する
    GLES20Util.initDrawErea(width, height,false);
    //テクスチャの再読み込み
    GLES20Util.initTextures();
    GLES20Util.initFpsBitmap(fpsImage,true);

  }
  private int totalFrame=0;
  private FpsController fpsControll = new FpsController((short)60);

  @Override
  public void onDrawFrame(GL10 gl) {
	  process();
	  draw();
  }
  private void process(){
	fpsControll.updateFps();
	Barrage.normal((0.65f*(float)Math.sin(totalFrame/10.0f)+0.65f),
			0.3f*(float)Math.sin(totalFrame/30.0f)+1.3f,
			0.01f,
			totalFrame*10,
			bulletImage);
	//if(totalFrame >=60){
		enemy.update();
	//}
	BulletList.updateBullet();
	BulletList.eraseBullet(GLES20Util.getAspect());
	totalFrame++;
  }
  private void draw(){
	// 描画領域をクリアする
	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	//自機の表示
	player.Draw();
	//敵の表示
	enemy.Draw();
    //弾の表示
    BulletList.Draw();
    //FPSの表示
    GLES20Util.DrawFPS(1.1f,1.8f,fpsControll.getFps(),fpsImage);
  }
}