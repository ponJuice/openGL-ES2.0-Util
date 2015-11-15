package com.example.opengles20util.core;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
/**
 * 2次元用画像描画クラス（なんで抽象クラスにしたのか本気で悩んでるwww）
 * @author
 * @version 1.0
 */


public abstract class abstractGLES20Util {
	/**
	 * floatのバイト数
	 */
	private static final int FSIZE = Float.SIZE / Byte.SIZE;//floatのバイト数
	/**
	 * glSurfaceViewを使っているアクティビティ
	 */
	private static Activity targetActivity;
	/**
	 * 頂点シェーダオブジェクト
	 */
	private static int vertexShader;				//頂点シェーダオブジェクト
	/**
	 * ピクセル（フラグメント）シェーダオブジェクト
	 */
	private static int fragmentShader;				//ピクセルシェーダオブジェクト
	/**
	 * プログラムオブジェクト
	 */
	private static int program;						//プログラムオブジェクト
	/**
	 * 画面の幅
	 */
	private static int Width;						//画面の幅
	/**
	 * 画面の高さ
	 */
	private static int Height;						//画面の高さ

	/**
	 * 頂点シェーダの頂点座標の格納場所
	 */
	private static int ma_Position;			//頂点シェーダの頂点座標の格納場所
	/**
	 * 頂点シェーダのワールド行列用格納変数の場所
	 */
	private static int mu_ProjMatrix;				//頂点シェーダのワールド行列用格納変数の場所
	/**
	 * テクスチャオブジェクトの格納場所
	 */
	private static int ma_texCoord;				//テクスチャオブジェクトの格納場所
	/**
	 * モデル行列格納場所
	 */
	private static int mu_modelMatrix;				//モデル行列格納場所
	/**
	 * サンプラーの場所
	 */
	private static  int u_Sampler;				//サンプラーの場所
	/**
	 * アルファ値
	 */
	private static int u_alpha;

	/**
	 * 視体積（立方体型）の近平面
	 */
	private static float mNear = 0.0f;				//視体積の立方体型の近平面
	/**
	 * 視体積（立方体型）の遠平面
	 */
	private static float mFar = 50f;				//視体積の立方体型の遠平面
	/**
	 * 画面のアスペクト比
	 */
	protected static float aspect;					//画面のアスペクト比
	/**
	 * 投影行列
	 */
	private static float[] u_ProjMatrix = new float[16];	//投影行列
	/**
	 * ビューポート行列
	 */
	private static float[] viewProjMatrix = new float[16];	//ビューポート行列
	//private static float[] modelMatrix = new float[16];		//モデル行列

	/**
	 * 頂点バッファ
	 */
	private static FloatBuffer vertexBuffer;		//頂点バッファ
	/**
	 * テクスチャバッファ
	 */
	private static FloatBuffer texBuffer;			//テクスチャバッファ

	/**
	 * 正方形の頂点座標
	 */
	private static final float[] boxVertex= {		//正方形頂点座標
		0.0f,0.0f,
		1.0f,0.0f,
		0.0f,1.0f,
		1.0f,1.0f
	};
	/**
	 * テクスチャ座標
	 */
	private static final float[] texPosition={		//テクスチャ座標
		0.0f,1.0f,
		1.0f,1.0f,
		0.0f,0.0f,
		1.0f,0.0f
	};

	public static float getWidth(){
		return Width;
	}

	public static float getHight(){
		return Height;
	}

	/**
	 * 画面のアスペクト比を取得します
	 * @return　アスペクト比
	 */
	public static float getAspect(){
		return aspect;
	}
	/**
	 * glSurfaceViewを作成します。
	 * @param glSurfaceViewを設定するアクティビティ(thisで良い)
	 * @param glSurfaceViewを設定するアクティビティのレンダラー(thisで良い)
	 * @return　作成したglSurfaceView
	 */
	public static GLSurfaceView initGLES20(Activity activity, GLSurfaceView.Renderer renderer) {
		targetActivity = activity;
		GLSurfaceView glSurfaceView = new GLSurfaceView(activity); // 描画領域の作成
		// OpenGL ES 2.0を使用する
		glSurfaceView.setEGLContextClientVersion(2);
		// 作成したGLSurfaceViewにこのアプリケーションから描画する
		glSurfaceView.setRenderer(renderer);

		Log.d("abstractGLES20Util","initGLES20");

		return glSurfaceView;
	  }

	/**
	 * GLES20を使えるようにします。onSurfaceCreatedで一番最初に呼び出してください
	 * @param 頂点シェーダのソースコード
	 * @param フラグメントシェーダのソースコード
	 */
	public static void initGLES20Util(String vertexShaderString,String fragmentShaderString){
		//シェーダの準備
		initShader(vertexShaderString,fragmentShaderString);
		Log.d("abstractGLES20Util","finished init shader");
		//バッファの準備
		initBuffer();
		Log.d("abstractGLES20Util","finished initBuffer");
		//頂点バッファオブジェクトの作成
		createAndSetOnBufferObject();
		Log.d("abstractGLES20Util","finished createAndSetOnBufferObject");
		//アルファブレンディングの有効化
		GLES20.glEnable(GLES20.GL_BLEND);
		//ブレンディングメソッドの有効化
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * シェーダ―を初期化します
	 * @param 頂点シェーダのソースコード
	 * @param フラグメントシェーダのソースコード
	 */
	private static void initShader(String vertexShaderString,String fragmentShaderString){
		Log.d("abstractGLES20Util","initShader");
		//頂点シェーダの初期化
		initVertexShader(vertexShaderString);
		Log.d("abstractGLES20Util","initVertexShader finished");

		//フラグメントシェーダの初期化
		initFragmentShader(fragmentShaderString);
		Log.d("abstractGLES20Util","initFragmentShader finished");

	    //二つのシェーダオブジェクトを設定
	    program = GLES20.glCreateProgram();
	    GLES20.glAttachShader(program, vertexShader);
	    GLES20.glAttachShader(program, fragmentShader);
	    Log.d("abstractGLES20Util","setOnProgram finished");

	    // プログラムオブジェクトをリンクする
	   	GLES20.glLinkProgram(program);
	   	Log.d("abstractGLES20Util","link Program finished");

	    // リンク結果をチェックする
	    int[] linked = new int[1];
	    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
	    if (linked[0] != GLES20.GL_TRUE) {
	    String error = GLES20.glGetProgramInfoLog(program);
	    	throw new RuntimeException("failed to link program: " + error);
	    }
		//プログラムの使用開始
		GLES20.glUseProgram(program);
		Log.d("abstractGLES20Util","use stert Program finished");

		//シェーダ内の変数場所を取得
		getShaderLocation();
		Log.d("abstractGLES20Util","getShaderLocation finished");

		Log.d("abstractGLES20Util","end of initShader");

	}
	/**
	 * 頂点シェーダの初期化
	 * @param 頂点シェーダのソースコード
	 */
	private static void initVertexShader(String vertexShaderString){
		//頂点シェーダオブジェクトを作成
		vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
	    // シェーダコードを読み込む
	    GLES20.glShaderSource( vertexShader,vertexShaderString);
	    // シェーダコードをコンパイルする
	    GLES20.glCompileShader( vertexShader );
	    // コンパイル結果を検査する
	    int[] compiled = new int[1];
	    GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
	    if (compiled[0] != GLES20.GL_TRUE) {
	     	String error = GLES20.glGetShaderInfoLog(vertexShader);
	    	throw new RuntimeException("failed to compile shader: " + error);
	    }
	}

	/**
	 * フラグメントシェーダの初期化
	 * @param フラグメントシェーダのソースコード
	 */
	private static void initFragmentShader(String fragmentShaderString){
	    // ピクセルシェーダオブジェクトを作成する
		fragmentShader = GLES20.glCreateShader( GLES20.GL_FRAGMENT_SHADER );
		// シェーダコードを読み込む
	    GLES20.glShaderSource( fragmentShader,fragmentShaderString);
	    // シェーダコードをコンパイルする
	    GLES20.glCompileShader( fragmentShader );
	    // コンパイル結果を検査する
	    int[] compiled = new int[1];
	    GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
	    if (compiled[0] != GLES20.GL_TRUE) {
	    	String error = GLES20.glGetShaderInfoLog(fragmentShader);
	        throw new RuntimeException("failed to compile shader: " + error);
	    }
	}

	/**
	 * 頂点とテクスチャのフロートバッファを作成
	 */
	private static void initBuffer(){
		vertexBuffer = makeFloatBuffer(boxVertex);
		texBuffer = makeFloatBuffer(texPosition);
	}

	/**
	 * フロートバッファを作成
	 * @param 作成するデータの入った配列
	 * @return　作成したフロートバッファ
	 */
	private static FloatBuffer makeFloatBuffer(float[] array) {
    	if (array == null) throw new IllegalArgumentException();

    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
    	byteBuffer.order(ByteOrder.nativeOrder());
    	FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
    	floatBuffer.put(array);
    	floatBuffer.position(0);
    	return floatBuffer;
  	}

	/**
	 * シェーダから各データの格納場所を取得
	 */
	private static void getShaderLocation(){
		// u_ProjMatrix変数の格納場所を取得する
	    mu_ProjMatrix = GLES20.glGetUniformLocation(program, "u_ProjMatrix");
	    if (mu_ProjMatrix == -1) {
	    	throw new RuntimeException("u_ProjMatrixの格納場所の取得に失敗");
	    }
		// a_Positionの格納場所を取得
    	ma_Position = GLES20.glGetAttribLocation(program, "a_Position");
    	if (ma_Position == -1) {
    		throw new RuntimeException("a_Positionの格納場所の取得に失敗");
    	}
		// モデル行列の格納場所を取得
		mu_modelMatrix = GLES20.glGetUniformLocation(program, "u_ModelMatrix");
		if (mu_modelMatrix == -1) {
			throw new RuntimeException("u_ModelMatrixの格納場所の取得に失敗");
		}
		//テクスチャの格納場所を取得
		ma_texCoord = GLES20.glGetAttribLocation(program,"a_TexCoord");
		if (ma_texCoord == -1) {
			throw new RuntimeException("a_texCoordの格納場所の取得に失敗");
		}
	}

	/**
	 * シェーダにビューポート行列を設定
	 */
	//シェーダにビューポート行列を設定
	private static void setShaderProjMatrix(){
		GLES20.glUniformMatrix4fv(mu_ProjMatrix, 1,false,viewProjMatrix,0);
	}

	/**
	 * シェーダにモデル行列を設定
	 * @param モデルの行列
	 */
	//シェーダにモデル行列を設定
	protected static void setShaderModelMatrix(float[] modelMatrix){
		GLES20.glUniformMatrix4fv(mu_modelMatrix, 1, false, modelMatrix, 0);
	}

	/**
	 * 正方形を単色塗りつぶして表示します
	 * @param 左上頂点x座標
	 * @param 左上頂点y座標
	 * @param x方面の辺の長さ
	 * @param y方面の辺の長さ
	 * @param 色(red要素)
	 * @param 色(green要素)
	 * @param 色(blue要素)
	 * @param 色(alpha要素)
	 */
	public static void DrawBox(float startX,float startY,float lengthX,float lengthY,
								int r,int g,int b, int a){

		float scaleX = lengthX;
		float scaleY = lengthY;

		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix,0,startX-aspect,startY-1.0f,0.0f);
		Matrix.scaleM(modelMatrix,0,scaleX,scaleY,1.0f);
		setShaderModelMatrix(modelMatrix);

		//単色塗りつぶし(単色ビットマップ作成メソッドを引数で呼び出し)
		setOnTexture(createBitmap(r,g,b,a),1.0f);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);	//描画
	}

	/**
	 * 頂点座標のバッファオブジェクトを作成し、設定する
	 */
	//頂点座標のバッファオブジェクトを作成し、設定する。
	private static void createAndSetOnBufferObject(){
	    // バッファオブジェクトを作成する
	    int[] vertexTexCoord = new int[1];
	    GLES20.glGenBuffers(1, vertexTexCoord, 0);

	    // 頂点の座標とテクスチャ座標をバッファオブジェクトに書き込む
	    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexTexCoord[0]);
	    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, FSIZE * vertexBuffer.limit(), vertexBuffer, GLES20.GL_DYNAMIC_DRAW);

   		GLES20.glVertexAttribPointer(ma_Position, 2, GLES20.GL_FLOAT, false, 0, 0);
   		GLES20.glEnableVertexAttribArray(ma_Position);  // バッファオブジェクトの割り当ての有効化
	}

	/**
	 * テクスチャオブジェクトの初期化
	 */
	  public static void initTextures() {
		    // バッファオブジェクトを作成する
		    int[] vertexTexCoord = new int[1];
		    GLES20.glGenBuffers(1, vertexTexCoord, 0);

		    // 頂点の座標とテクスチャ座標をバッファオブジェクトに書き込む
		    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexTexCoord[0]);
		    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, FSIZE * texBuffer.limit(), texBuffer, GLES20.GL_DYNAMIC_DRAW);

	   		GLES20.glVertexAttribPointer(ma_texCoord, 2, GLES20.GL_FLOAT, false, 0, 0);
	   		GLES20.glEnableVertexAttribArray(ma_texCoord);  // バッファオブジェクトの割り当ての有効化

		    // テクスチャオブジェクトを作成する
		    int[] textures = new int[1];
		    GLES20.glGenTextures(1, textures, 0);

		    // テクスチャを読み込み、サンプラに設定する
		    loadTexture(textures[0], "u_Sampler");
	  }

	  /**
	   * サンプラーの場所取得とテクスチャパラメータなどを設定
	   * @param テクスチャ
	   * @param サンプラ
	   */
	  //サンプラーの場所取得とテクスチャパラメータなどを設定
	  private static void loadTexture(int texture, String sampler) {
	    // u_Samplerの格納場所を取得する
	    u_Sampler = GLES20.glGetUniformLocation(program, sampler);
	    if (u_Sampler == -1) {
	      throw new RuntimeException("u_Samplerの格納場所の取得に失敗");
	    }
	    u_alpha = GLES20.glGetUniformLocation(program, "u_alpha");
	    if(u_alpha == -1){
	    	throw new RuntimeException("u_alphaの格納場所の取得に失敗");
	    }

	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);   // テクスチャユニット0を有効にする

	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture); // テクスチャオブジェクトをバインドする

	    // テクスチャパラメータを設定する
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	  }

	  /**
	   * テクスチャ画像を設定する
	   * @param 使用する画像のbitmapデータ
	   */
	  //テクスチャ画像を設定する
	  protected static void setOnTexture(Bitmap image,float alpha){
		    // テクスチャ画像を設定する
		    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0,image, 0);

		    GLES20.glUniform1f(u_alpha, alpha);		//サンプラにアルファを設定する
		    GLES20.glUniform1i(u_Sampler,0);     // サンプラにテクスチャユニットを設定する
	  }

	  /**
	   * 画像ファイルを読み込み、全体が対象
	   * @param 使用する画像のRのid
	   * @return bitmap化した画像
	   */
	 //画像ファイルの読み込み	全体を読み込む
	  public static Bitmap loadBitmap(int id){
		  // 画像ファイルを読み込む
		  Bitmap image = BitmapFactory.decodeResource(targetActivity.getResources(), id);
		  if (image == null) {
		      throw new RuntimeException("画像の読み込みに失敗");
		  }
		  return image;
	  }

	  /**
	   * 画像ファイルの読み込み、切り抜き
	   * @param 切り抜きの左上x座標
	   * @param 切り抜きの左上y座標
	   * @param 切り抜きの右下x座標
	   * @param 切り抜きの右下y座標
	   * @param 画像ファイルのRのid
	   * @return 作成したbitmapデータ
	   */
	  //画像ファイルの読み込み　切り抜き
	  public static Bitmap loadBitmap(int startX,int startY,int endX,int endY,int id){
		  //画像ファイルを切り抜いて読み込む
		  //targetActivity.get
		  BitmapRegionDecoder regionDecoder;
		  Resources res = targetActivity.getResources();
		  InputStream is = res.openRawResource(id);
		try{
		  regionDecoder = BitmapRegionDecoder.newInstance(is, false);
		  Rect rect = new Rect(startX,startY,endX,endY);	//切り抜く領域（矩形クラス）の用意
		  return regionDecoder.decodeRegion(rect,null).copy(Bitmap.Config.ARGB_8888,true);	//RegionDecoderで読み込んだbitmapはイミュターブル（不変）なのでもコピーメソッドを使いコピーしてミュータブル（変更可）にする
	  	} catch (IOException e) {
		    e.printStackTrace();
		}
		  return null;
	  }

	  /**
	   * 単色塗りつぶしたビットマップを作成
	   * @param 赤
	   * @param 緑
	   * @param 青
	   * @param 透明度
	   * @return　作成したビットマップデータ
	   */
	  //単色塗りつぶしbitmapの作成
	  public static Bitmap createBitmap(int r,int g,int b,int a){
		  Bitmap bitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
		  bitmap.eraseColor(Color.argb(a,r,g,b));
		  return bitmap;
	  }

	  /**
	   * 表示領域を設定
	   * @param 幅
	   * @param 高さ
	   * @param 視体積を立方体型(false)にするか四角錐型(true)にするか。四角錐型の方は未実装
	   */
	//表示領域を設定
 	public static void initDrawErea(int width,int height,boolean Perspective){
		Width = width;
		Height = height;
		Log.d("debug[abstractGLES20Util][initDrawErea]","width "+String.valueOf(width)+"\nheight "+String.valueOf(height));
		aspect = (float)width/(float)height;
		GLES20.glViewport(0,0,width,height);
		if(Perspective){
			float[] viewMatrix = new float[16];
			setPerspectiveM(u_ProjMatrix,0,40.0,(double)width/height,1.0,100.0);
		    Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, -2.0f, 0.0f, 1.0f, 0.0f);
		    Matrix.multiplyMM(viewProjMatrix, 0, u_ProjMatrix, 0, viewMatrix, 0);
		}
		else{
			Matrix.orthoM(u_ProjMatrix,0,-aspect,aspect,-1.0f,1.0f,mNear/100,mFar/100);
			viewProjMatrix = u_ProjMatrix;
		}

		//シェーダにワールド行列を設定
		setShaderProjMatrix();

		//デバッグ
		Log.d("GLES20Util:Width",String.valueOf(aspect));
	}

	//視体積の四角錐型の設定
	private static void setPerspectiveM(float[] m, int offset, double fovy, double aspect, double zNear, double zFar) {
		    Matrix.setIdentityM(m, offset);
		    double ymax = zNear * Math.tan(fovy * Math.PI / 360.0);
		    double ymin = -ymax;
		    double xmin = ymin * aspect;
		    double xmax = ymax * aspect;
		    Matrix.frustumM(m, offset, (float)xmin, (float)xmax, (float)ymin, (float)ymax, (float)zNear, (float)zFar);
		  }

}