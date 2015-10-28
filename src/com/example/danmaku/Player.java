package com.example.danmaku;


/**
 * 自機制御用クラス
 * @author 
 *
 */
public class Player extends Touch{
	/**
	 * 自機の画像ID
	 */
	private int graphId;
	/**
	 * サイズX
	 */
	private float sizeX;
	/**
	 * サイズY
	 */
	private float sizeY;
	/**
	 * コンストラクタ
	 * @param 画像ID
	 * @param サイズX
	 * @param サイズY
	 */
	public Player(int id,float sizeX,float sizeY){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		graphId = id;
	}
	/**
	 * ゲッタ
	 * @return　サイズX
	 */
	public float getSizeX(){
		return sizeX;
	}
	/**
	 * ゲッタ
	 * @return　サイズY
	 */
	public float getSizeY(){
		return sizeY;
	}
	/**
	 * セッタ
	 * @param 画像ID
	 */
	public void setGraphId(int id){
		graphId = id;
	}
	/**
	 * ゲッタ
	 * @return　画像ID
	 */
	public int getGraphId(){
		return graphId;
	}
	/**
	 * 画面外に出ないようにする関数
	 */
	public void regulation(){
		if(nowPositionX < 0.0f){
			nowPositionX = 0.0f;
		}
		if(nowPositionX > (GLES20Util.getAspect()*2.0f)-(1.0f*sizeX)){
			nowPositionX = ((GLES20Util.getAspect()*2.0f)-(1.0f*sizeX));
		}
		if(nowPositionY < 0.0f){
			nowPositionY = 0.0f;
		}
		if(nowPositionY > 2.0f-(1.0f*sizeY)){
			nowPositionY = 2.0f-(1.0f*sizeY);
		}
		//Log.d("debug[Player][regulation]",String.valueOf(GLES20Util.getAspect()*2.0f));
	}
	/**
	 * 表示
	 * GLES20Util依存なので注意
	 */
	public void Draw(){//true 点有
		regulation();
		GLES20Util.DrawGraph(nowPositionX,nowPositionY,sizeX,sizeY,BitmapList.getBitmap(graphId));
	}
	@Override
	public float getPositionX(){
		regulation();
		return nowPositionX;
	}
	@Override
	public float getPositionY(){
		regulation();
		return nowPositionY;
	}
}
