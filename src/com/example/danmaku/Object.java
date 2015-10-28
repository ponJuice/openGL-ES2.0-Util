package com.example.danmaku;

import android.graphics.Bitmap;

/**
 * 敵や弾、自機に共通する処理を搭載したクラス
 * 継承して使うこと
 * @author テツヤ
 *
 */

public class Object {
	/**
	 * ビットマップリスト
	 */
	private static BitmapList bitmap = new BitmapList();
	/**
	 * 画像ID。自動生成
	 */
	private static int id = -1;
	/**
	 * 画像を取得。画像をセットする前に行うとnullを返す。
	 * @return	ビットマップを返します。無い場合はnullを返します。
	 */
	public Bitmap getGraph(){
		return bitmap.getBitmap(id);
	}
	/**
	 * 画像をセットします
	 * @param セットしたい画像
	 * @return　成功した場合:0。失敗した場合:-1
	 */
	public int setGraph(Bitmap image){
		int result = bitmap.setBitmap(image);
		if(result == -1){
			return -1;
		}
		else{
			id = result;
			return 0;
		}
	}
}
