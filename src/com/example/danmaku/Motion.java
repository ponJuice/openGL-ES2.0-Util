package com.example.danmaku;

import android.util.Log;

public class Motion {
	protected float positionX;	//位置
	protected float positionY;	//位置
	private float startPositionX;
	private float startPositionY;
	private float endPositionX;
	private float endPositionY;
	private float directionX;
	private float directionY;
	private int startFrame;	//移動開始フレーム
	private int endFrame;	//移動終了フレーム
	private int frame=0;
	public Motion(int sFrame,
			int eFrame,
			float sPosX,
			float sPosY,
			float ePosX,
			float ePosY,
			float dPosX,
			float dPosY){
		startFrame = sFrame;
		endFrame = eFrame;
		startPositionX = sPosX;
		startPositionY = sPosY;
		endPositionX = ePosX;
		endPositionY = ePosY;
		directionX = dPosX;
		directionY = dPosY;
	}
	// (1-t)^2*x1+2*(1-t)*t*x2+t^2*x3
	void oneDimentionBezier(){
		double b = 1.0/(double)(endFrame-startFrame);
		double t = b*(double)frame;
		double a = (1.0-(b*(double)frame));
		positionX = (float) (a*a*startPositionX+2*a*t*directionX+t*t*endPositionX);
		positionY = (float) (a*a*startPositionY+2*a*t*directionY+t*t*endPositionY);
		Log.d("Motion(oneDimentionBezier)","positionX:"+String.valueOf(positionX)+"\npositionY:"+String.valueOf(positionY));
		frame++;
	}
	public float getPositionX(){
		return positionX;
	}
	public float getPositionY(){
		return positionY;
	}
}
