package com.example.danmaku;

import android.util.Log;

public class BulletList {
	/**
	 *　最大弾登録数
	 */
	private static int maxBullet = 512;
	/**
	 * 現在登録されている数
	 */
	private static int nowNumber = 0;
	/**
	 * 登録用配列
	 */
	private static Bullet[] bullet = new Bullet[maxBullet];
	/**
	 * 弾を登録します
	 * @param 発射位置X
	 * @param 発射位置Y
	 * @param サイズX
	 * @param サイズY
	 * @param 速度
	 * @param 発射角度
	 * @param 画像ID
	 * @return　登録数が最大に達している場合:-1_正常登録:0
	 */
	public static int setBullet(float x,float y,float sizeX,float sizeY,float speed,float angle,int graphId){
		if(nowNumber == maxBullet){
			Log.d("debug[BulletList][setBullet]","maximam resourse");
			return -1;
		}
		for(int n=0;n<maxBullet;n++){
			if(bullet[n] == null){
				bullet[n] = new Bullet(x,y,sizeX,sizeY,speed,angle,graphId);
				nowNumber++;
				return 0;
			}
			else if(bullet[n].getAlive() == false){
				bullet[n].setBullet(x,y,sizeX,sizeY,speed,angle,graphId);
				nowNumber++;
				return 0;
			}
		}
		return -1;
	}
	/**
	 * 登録されている弾の位置を一括して更新します
	 */
	public static void updateBullet(){
		for(int n=0;n<maxBullet;n++){
			if(bullet[n] != null){
				if(bullet[n].getAlive()){
					bullet[n].updataPosition();
				}
			}
		}
	}
	/**
	 * 表示範囲に出た弾の消去を行います
	 * @param 画面のアスペクト比
	 */
	public static void eraseBullet(float aspect){
		for(int n=0;n<maxBullet;n++){
			//表示範囲外に出たら消去
			if(bullet[n] != null){
				if(bullet[n].getAlive()){
					if(bullet[n].getPositionX() < (0.0-(1.0f*bullet[n].getSizeX())) ||
							bullet[n].getPositionX() > (aspect*2.0f) ||
							bullet[n].getPositionY() < (0.0-(1.0f*bullet[n].getSizeY())) ||
							bullet[n].getPositionY() > 2.0f){
						bullet[n].setAlive(false);
						nowNumber--;
						//Log.d("debug[BulletList][eraseBullet]","bullet["+String.valueOf(n)+"] erase");
					}
				}
			}
		}
	}
	/**
	 * 登録されているすべての玉を表示します
	 * GLES20Util依存なので注意
	 */
	public static void Draw(){
		for(int n=0;n<maxBullet;n++){
			if(bullet[n] != null){
				if(bullet[n].getAlive()){
					bullet[n].Draw();
				}
			}
		}
	}
}
