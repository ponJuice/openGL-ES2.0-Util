package com.example.danmaku;


/**
 * タッチ管理クラス
 * @author
 *
 */

class Touch{
	/**
	 * 今の場所X
	 */
	protected float nowPositionX = 0.0f;
	/**
	 * 今の場所Y
	 */
	protected float nowPositionY = 0.0f;

	/**
	 * 一時変数
	 */
	private float onePositionX = 0.0f;
	/**
	 * 一時変数
	 */
	private float onePositionY = 0.0f;

	/**
	 * タッチされた場所X
	 */
	private float touchDownPositionX = 0.0f;
	/**
	 * タッチされた場所Y
	 */
	private float touchDownPositionY = 0.0f;

	/**
	 * タッチされた場所をセットする
	 * @param タッチされた場所X
	 * @param タッチされた場所Y
	 */
	public void setTouchDown(float downPosX,float downPosY){
		touchDownPositionX = downPosX;
		touchDownPositionY = downPosY;
		onePositionX = nowPositionX;
		onePositionY = nowPositionY;
	}
	/**
	 * 移動
	 * @param タッチ移動量X
	 * @param タッチ移動量Y
	 */
	public void move(float PosX,float PosY){
		nowPositionX = onePositionX + (PosX-touchDownPositionX)*(2.0f/1824.0f);
		nowPositionY = onePositionY - (PosY-touchDownPositionY)*(2.0f/1824.0f);
	}
	/**
	 * ゲッタ
	 * @return　現在位置X
	 */
	public float getPositionX(){
		return nowPositionX;
	}
	/**
	 * ゲッタ
	 * @return　現在位置Y
	 */
	public float getPositionY(){
		return nowPositionY;
	}


}