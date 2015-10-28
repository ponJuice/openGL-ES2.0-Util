package com.example.danmaku;

public class Enemy extends Motion{
	private int bulletType;		//弾幕種類
	private int bulletColor;	//弾色
	private int graphId;		//敵画像ID
	private int animationId;	//敵アニメーション
	private float speed;		//移動スピード
	private float HP;			//HP
	
	public Enemy(int sFrame,
			int eFrame,
			float sPosX,
			float sPosY,
			float ePosX,
			float ePosY,
			float dPosX,
			float dPosY,
			int bulletType,
			int bulletColor,
			int graphId,
			int animetionId,
			float speed,
			float HP){
		super(sFrame,eFrame,sPosX,sPosY,ePosX,ePosY,dPosX,dPosY);
		this.bulletType = bulletType;
		this.bulletColor = bulletColor;
		this.graphId = graphId;
		this.animationId = animetionId;
		this.speed = speed;
		this.HP = HP;
	}
	public void update(){
		oneDimentionBezier();
	}
	public void Draw(){
		GLES20Util.DrawGraph(positionX,positionY,0.1f,0.1f,BitmapList.getBitmap(graphId));
	}	
}
