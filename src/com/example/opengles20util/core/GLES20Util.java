package com.example.opengles20util.core;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.opengles20util.graphic.Image;


public class GLES20Util extends abstractGLES20Util{
	private static Paint paint;
	private static Canvas canvas;
	private static Rect rect = new Rect(0,0,0,0);

	public GLES20Util(){
		Log.d("GLES20Util","Constract");
	}

	//文字列描画
	public static Bitmap stringToBitmap(String text,float size,int r,int g,int b){
	    //描画するテキスト
		paint = new Paint();

		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(r, g, b));
		paint.setTextSize(size*100);
		paint.getTextBounds(text, 0, text.length(), new Rect());
		FontMetrics fm = paint.getFontMetrics();
		//テキストの表示範囲を設定

		int textWidth = (int) paint.measureText(text);
		int textHeight = (int) (Math.abs(fm.top) + fm.bottom);
		//Log.d("stringToBitmap",String.valueOf(textWidth)+" : "+String.valueOf(textHeight));
		Bitmap bitmap = Bitmap.createBitmap(textWidth, textHeight, Bitmap.Config.ARGB_8888);

		//キャンバスからビットマップを取得
		canvas = new Canvas(bitmap);
		canvas.drawText(text, 0, Math.abs(fm.top), paint);

		return bitmap;
	}

	public static void DrawString(String string,int size,int r,int g,int b,float alpha,float x,float y){
		Bitmap bitmap = stringToBitmap(string,size,r,g,b);
		//Log.d("DrawString",String.valueOf(bitmap.getWidth()));
		DrawGraph(x,y,bitmap.getWidth()/1000f,bitmap.getHeight()/1000f,bitmap,alpha);
	}

	/**
	 * 画像表示
	 * @param 表示場所の左上x座標
	 * @param 表示場所の左上y座標
	 * @param x方面の長さ
	 * @param y方面の長さ
	 * @param 表示する画像データ
	 */
	//画像表示
	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,Bitmap image,float alpha){

		float scaleX = lengthX;
		float scaleY = lengthY;

		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix,0,startX-aspect,startY-1.0f,0.0f);
		Matrix.scaleM(modelMatrix,0,scaleX,scaleY,1.0f);
		setShaderModelMatrix(modelMatrix);

		setOnTexture(image,alpha);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);	//描画
		}

	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,Image img){
		float scaleX = lengthX;
		float scaleY = lengthY;

		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix,0,startX-aspect,startY-1.0f,0.0f);
		Matrix.scaleM(modelMatrix,0,scaleX,scaleY,1.0f);
		setShaderModelMatrix(modelMatrix);

		img.getBlend().setBlendMode();
		setOnTexture(img.getImage(),1.0f);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);	//描画
	}

	public static void DrawString(String text,int textSize,Color color,float x,float y){

	}

	/**
	 * FPS表示。三桁。FPSに限らず数値であれば表示できる
	 * @param 表示する左上x座標
	 * @param 表示する左上y座標
	 * @param 表示したいFPS数値
	 * @param 十進数の数字が描かれているbitmapの配列
	 */
	//FPS表示	三桁表示
	public static void DrawFPS(float x,float y,int FPS,Bitmap[] digitBitmap,float alpha){
		int place100,place10,place1;
		FPS %= 1000;
		place100 = FPS/100;
		place10 = (FPS-place100*100)/10;
		place1 = (FPS - place100*100-place10*10);
		DrawGraph(x,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place100],alpha);
		DrawGraph(x+62.0f/1000.0f,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place10],alpha);
		DrawGraph(x+62.0f/1000.0f*2.0f,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place1],alpha);

	}

	/**
	 * FPS用の十進数画像の作成。現在は用意されたdegital2.pngのみを対象。
	 * @param ビットマップの配列。0～9の十個必要
	 * @param trueで黒を透過色、falseで白を透過色
	 */
	//flagはtrueで黒を透過色、falseで白を透過色
	public static void initFpsBitmap(Bitmap[] bitmap,boolean flag,int resource){
		int count=0;
		int rgb = 0;
		for(int n=0;n<2;n++){
			for(int m=0;m<5;m++){
					bitmap[count] = loadBitmap(62*m,110*n,62*(m+1),110*(n+1),resource);
					if(flag){
						for(int a =0;a<62;a++){
							for(int b=0;b<110;b++){
								rgb = bitmap[count].getPixel(a, b);
								bitmap[count].setPixel(a, b,Color.argb(
										(Color.red(rgb)+Color.red(rgb)+Color.blue(rgb))/3
										,Color.red(rgb)
										,Color.red(rgb)
										,Color.blue(rgb)
										));
							}
						}
					}
					else{
						for(int a =0;a<62;a++){
							for(int b=0;b<110;b++){
								rgb = bitmap[count].getPixel(a, b);

								bitmap[count].setPixel(a, b,Color.argb(
										(255-Color.red(rgb)+255-Color.red(rgb)+255-Color.blue(rgb))/3
										,Color.red(rgb)
										,Color.red(rgb)
										,Color.blue(rgb)
										));
							}
						}
					}
				count++;
			}
		}
	}
}

