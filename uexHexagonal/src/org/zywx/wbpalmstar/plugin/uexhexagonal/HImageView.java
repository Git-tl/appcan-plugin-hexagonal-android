package org.zywx.wbpalmstar.plugin.uexhexagonal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;

import android.widget.ImageView;

public class HImageView extends ImageView {

	public int DefaultColor = 0x00000000;
	public boolean mDrawFlow = true;
	public int mColor = 0x00000000;
	private Paint mPaint;
	private int mHeight;
	
	public HImageView(Context context) {
		super(context);
		mPaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		LinearGradient shader = new LinearGradient(0, 0, 0, mHeight, mColor, 0x00000000, TileMode.CLAMP);  
		mPaint.setShader(shader);
		canvas.drawPaint(mPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mHeight = h;
	}
	
	public void setDefault(){
		mColor = DefaultColor;
		invalidate();
	}

}