package com.example.danmaku;

import android.R;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;


public class GLES20Util extends abstractGLES20Util{
	public GLES20Util(){
		Log.d("GLES20Util","Constract");
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
	public static void DrawGraph(float startX,float startY,float lengthX,float lengthY,Bitmap image){

		float scaleX = lengthX;
		float scaleY = lengthY;

		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix,0,startX-aspect,startY-1.0f,0.0f);
		Matrix.scaleM(modelMatrix,0,scaleX,scaleY,1.0f);
		setShaderModelMatrix(modelMatrix);

		setOnTexture(image);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);	//描画
		}

	/**
	 * FPS表示。三桁。FPSに限らず数値であれば表示できる
	 * @param 表示する左上x座標
	 * @param 表示する左上y座標
	 * @param 表示したいFPS数値
	 * @param 十進数の数字が描かれているbitmapの配列
	 */
	//FPS表示	三桁表示
	public static void DrawFPS(float x,float y,int FPS,Bitmap[] digitBitmap){
		int place100,place10,place1;
		FPS %= 1000;
		place100 = FPS/100;
		place10 = (FPS-place100*100)/10;
		place1 = (FPS - place100*100-place10*10);
		DrawGraph(x,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place100]);
		DrawGraph(x+62.0f/1000.0f,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place10]);
		DrawGraph(x+62.0f/1000.0f*2.0f,y,62.0f/1000.0f,110.0f/1000.0f,digitBitmap[place1]);

	}

	/**
	 * FPS用の十進数画像の作成。現在は用意されたdegital2.pngのみを対象。
	 * @param ビットマップの配列。0～9の十個必要
	 * @param trueで黒を透過色、falseで白を透過色
	 */
	//flagはtrueで黒を透過色、falseで白を透過色
	public static void initFpsBitmap(Bitmap[] bitmap,boolean flag){
		int count=0;
		int rgb = 0;
		for(int n=0;n<2;n++){
			for(int m=0;m<5;m++){
				/*if(n==0&&m==0)
					bitmap[count] = loadBitmap(62*m+1,110*n+1,62*(m+1),110*(n+1),R.drawable.degital);
				else if(m==0)
					bitmap[count] = loadBitmap(62*m+1,110*n,62*(m+1),110*(n+1),R.drawable.degital);
				else*/
					//bitmap[count].setConfig(Bitmap.Config.ARGB_8888);
					bitmap[count] = loadBitmap(62*m,110*n,62*(m+1),110*(n+1),R.drawable.degital2);
					//bitmap[count].setConfig(Bitmap.Config.ARGB_8888);
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

