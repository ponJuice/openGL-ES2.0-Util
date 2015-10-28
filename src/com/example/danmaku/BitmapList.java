package com.example.danmaku;

import android.graphics.Bitmap;

/**
 * 表示するビットマップを管理するクラス
 * @author
 *
 */
public class BitmapList {
	/**
	 * 登録できる最大数
	 */
	private static int maxElement = 128;
	/**
	 * ビットマップ配列
	 */
	private static Bitmap[] bitmap = new Bitmap[maxElement];
	/**
	 * 現在登録されている数
	 */
	private static int index = 0;
	
	/**
	 * 現在登録されている量を返します
	 * @return 現在登録されている量
	 */
	public static int getIndex(){
		return index;
	}
	/**
	 * 指定したidのビットマップを取得します
	 * @param 取得したいビットマップのid
	 * @return 成功した場合は指定したビットマップ。失敗した場合はnullを返します
	 */
	public static Bitmap getBitmap(int id){
		if(id >= maxElement || id <= 0){
			return null;	//要素の範囲外だとエラーとしてnull
		}
		if(bitmap[id] == null){
			return null;
		}
		else{
			return bitmap[id];
		}
	}
	/**
	 * ビットマップを登録します
	 * @param 登録するビットマップ
	 * @return　呼び出すときのID
	 */
	public static int setBitmap(Bitmap value){
		if(index >= maxElement){
			return -1;	//要素限界として-1を戻す
		}
		for(int n=1;n<maxElement;n++){
			if(bitmap[n]==null){
				bitmap[n] = value;
				index++;
				return n;
			}
		}
		return 0;	//デバッグ用
	}
	/**
	 * 登録したbitmapを解放します。
	 * @param 解放したいbitmapのID
	 * @return 0:正常終了 -1:IDの範囲外(1~128)
	 */
	public static int deleteBitmap(int id){
		if(id >= maxElement || id <= 0){
			return -1;	//要素の範囲外だとエラーとして-1
		}
		if(bitmap[id] == null){
			return 0;
		}
		else{
			bitmap[id] = null;
			index--;
			return 0;
		}
	}
}
