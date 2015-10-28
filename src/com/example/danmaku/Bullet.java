package com.example.danmaku;


class Bullet extends Object{
	/**
	 * 表示する画像ID
	 */
	private int graphId;
	/**
	 * 初期位置X
	 */
	private float startPositionX;
	/**
	 * 初期位置Y
	 */
	private float startPositionY;
	/**
	 * 現在位置X
	 */
	private float positionX;
	/**
	 * 現在位置Y
	 */
	private float positionY;
	/**
	 * 一フレーム当たりの移動量
	 */
	private float speed;
	/**
	 * 0:X方向の速度
	 * 1:Y方向の速度
	 */
	private float[] vector;
	/**
	 * 大きさ
	 */
	private float sizeX;
	/**
	 * 大きさ
	 */
	private float sizeY;
	/**
	 * 弾が発射されたときからカウントされるフレーム
	 */
	private int frame;
	/**
	 * true:表示
	 * false:表示しない
	 */
	private boolean alive;
	/**
	 * 初期値を設定
	 * @param 初期位置X
	 * @param 初期位置Y
	 * @param 1フレームあたりの移動量
	 * @param 発射角度（ラジアン）
	 * @param 画像ID
	 */
	public Bullet(float x,float y,float sizeX,float sizeY,float speed,float angle,int graphId){
		startPositionX = x;
		startPositionY = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.speed = speed;
		this.graphId = graphId;
		frame = 0;
		vector = new float[2];
		vector[0] = (float)(speed*Math.cos(Math.toRadians(angle)));
		vector[1] = (float)(speed*Math.sin(Math.toRadians(angle)));
		alive = true;
	}
	/**
	 * 新たに弾を作るときに使います
	 * @param 発射位置X
	 * @param 発射位置Y
	 * @param サイズX
	 * @param サイズY
	 * @param 速度
	 * @param 発射角度
	 * @param 画像ID
	 */
	public void setBullet(float x,float y,float sizeX,float sizeY,float speed,float angle,int graphId){
		startPositionX = x;
		startPositionY = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.speed = speed;
		this.graphId = graphId;
		frame = 0;
		vector = new float[2];
		vector[0] = (float)(speed*Math.cos(Math.toRadians(angle)));
		vector[1] = (float)(speed*Math.sin(Math.toRadians(angle)));
		alive = true;
	}
	/**
	 * ゲッタ
	 * @return　aliveの値
	 */
	public boolean getAlive(){
		return alive;
	}
	/**
	 * セッタ
	 * @param true of false
	 */
	public void setAlive(boolean bool){
		alive = bool;
	}
	/**
	 * 弾の位置を更新
	 */
	public void updataPosition(){
		positionX = startPositionX+(vector[0]*frame);
		positionY = startPositionY+(vector[1]*frame);
		frame++;
	}
	/**
	 * 画像IDを取得します
	 * @return　画像ID
	 */
	public int getId(){
		return graphId;
	}
	/**
	 * 表示する
	 * GLES20Util依存なので注意
	 */
	public void Draw(){
		GLES20Util.DrawGraph(positionX, positionY, sizeX,sizeY,BitmapList.getBitmap(graphId));
	}
	/**
	 * 位置Xのゲッタ
	 * @return　位置X
	 */
	public float getPositionX(){
		return positionX;
	}
	/**
	 * 位置Yのゲッタ
	 * @return　位置Y
	 */
	public float getPositionY(){
		return positionY;
	}
	/**
	 * サイズXのゲッタ
	 * @return　サイズX
	 */
	public float getSizeX(){
		return sizeX;
	}
	/**
	 * サイズYのゲッタ
	 * @return　サイズY
	 */
	public float getSizeY(){
		return sizeY;
	}
}


