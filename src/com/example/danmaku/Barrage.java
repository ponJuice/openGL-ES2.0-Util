package com.example.danmaku;
/**
 * 弾幕を生成するクラスです
 * 新たな弾幕関数はここに書き込むようにしてください
 * @author
 *
 */
public class Barrage {
	/**
	 * まっすぐ飛ぶ弾を生成します
	 * @param 発射位置x
	 * @param 発射位置y
	 * @param 弾の速度
	 * @param 発射角度（右が0°）
	 * @param 画像ID
	 */
	public static void normal(float x,float y,float speed,float angle,int graphId){
		BulletList.setBullet(x,y,0.1f,0.1f,speed,angle,graphId);
	}
	/**
	 * 放射状（360度）に飛ぶ弾幕を生成します
	 * @param 発射位置X
	 * @param 発射位置Y
	 * @param 弾の速度
	 * @param 角度
	 * @param 画像ID
	 * @param 一回に飛ばす弾の数
	 */
	public static void radiation(float x,float y,float speed,float angle,int graphId,int bulletNumber){
		for(int n=0;n<bulletNumber;n++){
			BulletList.setBullet(x, y, 0.1f, 0.1f, speed,360.0f/bulletNumber*n+angle, graphId);
		}
	}
	/**
	 * 扇型に弾を飛ばします
	 * @param 発射位置X
	 * @param 発射位置Y
	 * @param 弾の速度
	 * @param 範囲指定（度）
	 * @param 発射角度（この角度を中心に飛ばします）
	 * @param 画像ID
	 * @param 一度に飛ばす弾の数
	 */
	public static void funShape(float x,float y,float speed,float range,float angle,int graphId,int bulletNumber){
		for(int n=0;n<bulletNumber;n++){
			BulletList.setBullet(x,y,0.1f,0.1f,speed,(angle-(range/2.0f)+(range/((float)(bulletNumber)+1.0f))*((float)(n)+1.0f)),graphId);
		}
	}
}
