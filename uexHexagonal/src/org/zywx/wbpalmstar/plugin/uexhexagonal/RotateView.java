package org.zywx.wbpalmstar.plugin.uexhexagonal;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

public class RotateView extends ViewGroup{

	public static final int INTENT_TAG_0 = 0;
	public static final int INTENT_TAG_1 = 1;
	public static final int INTENT_TAG_2 = 2;
	public static final int INTENT_TAG_3 = 3;
	public static final int INTENT_TAG_4 = 4;
	public static final int INTENT_TAG_5 = 5;
	
	static final int SNAP_VELOCITY = 600;
	// 旋转时的偏转角度
	static final float ANGLE = 90.0f;
	//左右侧边
	
	static final int TOUCH_STATE_REST 		= 0;
	static final int TOUCH_STATE_SCROLLING 	= 1;
	
	private int mCurScreen = 1;
	private float mLastMotionX;
	private Scroller mScroller;
	private int mTouchSlop;
	private int mTouchState = TOUCH_STATE_REST;
	private VelocityTracker mVelocityTracker;
	private int mWidth;
	private Camera mCamera;
	private Matrix mMatrix;
	private HImageView mPage0, mPage1, mPage2, mPage3, mPage4, mPage5;
	private Container mContainer;

	public RotateView(Context context, Container c) {
		super(context);
		mContainer = c;
		mScroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mCamera = new Camera();
		mMatrix = new Matrix();
		initView(context);
	}
	
	public void initView(Context context) {
		
		mPage0 = new HImageView(context);
		int id1 = EUExUtil.getResDrawableID("plugin_hexagonal_0");
		if(0 != id1){
			mPage0.setBackgroundResource(id1);
			mPage0.setOnClickListener(mContainer);
			mPage0.setTag(INTENT_TAG_0);
			mPage0.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams p1 = new ViewGroup.LayoutParams(-2, -2);
			mPage0.setLayoutParams(p1);
			addView(mPage0);
		}else{
			Toast.makeText(context, "plugin_hexagonal_0.png 丢失!", Toast.LENGTH_SHORT).show();
		}
		
		int id2 = EUExUtil.getResDrawableID("plugin_hexagonal_1");
		if(0 != id2){
			mPage1 = new HImageView(context);
			mPage1.setBackgroundResource(id2);
			mPage1.setOnClickListener(mContainer);
			mPage1.setTag(INTENT_TAG_1);
			mPage1.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams p2 = new ViewGroup.LayoutParams(-2, -2);
			mPage1.setLayoutParams(p2);
			addView(mPage1);
		}else{
			Toast.makeText(context, "plugin_hexagonal_1.png 丢失!", Toast.LENGTH_SHORT).show();
		}

		int id3 = EUExUtil.getResDrawableID("plugin_hexagonal_2");
		if(0 != id3){
			mPage2 = new HImageView(context);
			mPage2.setBackgroundResource(id3);
			mPage2.setOnClickListener(mContainer);
			mPage2.setTag(INTENT_TAG_2);
			mPage2.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams p3 = new ViewGroup.LayoutParams(-2, -2);
			mPage2.setLayoutParams(p3);
			addView(mPage2);
		}else{
			Toast.makeText(context, "plugin_hexagonal_2.png 丢失!", Toast.LENGTH_SHORT).show();
		}
		
		int id4 = EUExUtil.getResDrawableID("plugin_hexagonal_3");
		if(0 != id4){
			mPage3 = new HImageView(context);
			mPage3.setBackgroundResource(id4);
			mPage3.setOnClickListener(mContainer);
			mPage3.setTag(INTENT_TAG_3);
			mPage3.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams p4 = new ViewGroup.LayoutParams(-2, -2);
			mPage3.setLayoutParams(p4);
			addView(mPage3);
		}else{
			Toast.makeText(context, "plugin_hexagonal_3.png 丢失!", Toast.LENGTH_SHORT).show();
		}
		
		int id5 = EUExUtil.getResDrawableID("plugin_hexagonal_4");
		if(0 != id5){
			mPage4 = new HImageView(context);
			mPage4.setBackgroundResource(id5);
			mPage4.setOnClickListener(mContainer);
			mPage4.setTag(INTENT_TAG_4);
			mPage4.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams p5 = new ViewGroup.LayoutParams(-2, -2);
			mPage4.setLayoutParams(p5);
			addView(mPage4);
		}else{
			Toast.makeText(context, "plugin_hexagonal_4.png 丢失!", Toast.LENGTH_SHORT).show();
		}
		
		int id6 = EUExUtil.getResDrawableID("plugin_hexagonal_5");
		if(0 != id6){
			mPage5 = new HImageView(context);
			mPage5.setBackgroundResource(id6);
			mPage5.setOnClickListener(mContainer);
			mPage5.setTag(INTENT_TAG_5);
			mPage5.setVisibility(View.VISIBLE);
			ViewGroup.LayoutParams p6 = new ViewGroup.LayoutParams(-2, -2);
			mPage5.setLayoutParams(p6);
			addView(mPage5);
		}else{
			Toast.makeText(context, "plugin_hexagonal_5.png 丢失!", Toast.LENGTH_SHORT).show();
		}
	}

	boolean init;
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
		}
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(width, 0);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				final int childHeight = childView.getMeasuredHeight();
				childView.layout(childLeft, 0, childLeft + childWidth, childHeight);
				childLeft += childWidth;
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mBegin = System.currentTimeMillis();
			mLastMotionX = x;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	long mBegin = 0;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mBegin = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (!mScroller.isFinished()) {

				return true;
			}
			long now = System.currentTimeMillis();
			long step = now - mBegin;
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				snapToPage((mCurScreen - 1), step);
			} else if (velocityX < -SNAP_VELOCITY
			&& mCurScreen < getChildCount() - 1) {
				snapToPage((mCurScreen + 1), step);
			} else {
				snapToDestination(step);
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}
	
	boolean mInitRoate;
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}else{
		}
	}

	public void reset(){
		mInitRoate = false;
		init = false;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		final long drawingTime = getDrawingTime();
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			drawPage(canvas, i, drawingTime);
		}
	}

	public void drawPage(Canvas canvas, int which, long drawingTime) {
		
		final int width = getWidth();
		final int scrollWidth = which * width;
		final int scrollX = getScrollX();

		if (scrollWidth > scrollX + width || scrollWidth + width < scrollX) {
			return;
		}
		final HImageView child = (HImageView) getChildAt(which);
		final int faceIndex = which;
		final float currentDegree = scrollX * (ANGLE / getMeasuredWidth());
		final float faceDegree = currentDegree - faceIndex * ANGLE;
		if (faceDegree > 90 || faceDegree < -90) {
			return;
		}
		final float centerX = (scrollWidth < scrollX) ? scrollWidth + width : scrollWidth;
		final float centerY = getHeight() / 2;
		final Camera camera = mCamera;
		final Matrix matrix = mMatrix;
		final float degree = -faceDegree;
		canvas.save();
		camera.save();
		camera.rotateY(degree);
		float dcolor = degree > 0 ? degree : -degree;
		dcolor = (dcolor * 2.8f);
		child.mColor = color((int) dcolor);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
		canvas.concat(matrix);
		drawChild(canvas, child, drawingTime);
		canvas.restore();
	}
	
	private int color(int degree){
		
		return (degree << 24) | (0 << 16) | (0 << 8) | 0;
	}

	private void setMWidth() {
		if (mWidth == 0) {
			mWidth = getWidth();
		}
	}

	private void setNext() {
		int next = getChildCount() - 1;
		View view = getChildAt(next);
		removeViewAt(next);
		addView(view, 0);
	}

	private void setPre() {
		int count = getChildCount();
		View view = getChildAt(0);
		removeViewAt(0);
		addView(view, count - 1);
	}

	public void snapToDestination(long step) {
		setMWidth();
		final int destPage = (getScrollX() + mWidth / 2) / mWidth;
		snapToPage(destPage, step);
	}

	public void snapToPage(int whichPage, long step) {
		whichPage = Math.max(0, Math.min(whichPage, getChildCount() - 1));
		setMWidth();
		int scrollX = getScrollX();
		int startWidth = whichPage * mWidth;
		if (scrollX != startWidth) {
			int delta = 0;
			int startX = 0;
			if (whichPage > mCurScreen) {
				setPre();
				delta = startWidth - scrollX;
				startX = mWidth - startWidth + scrollX;
			} else if (whichPage < mCurScreen) {
				setNext();
				delta = -scrollX;
				startX = scrollX + mWidth;
			} else {
				startX = scrollX;
				delta = startWidth - scrollX;
			}
			int dur = Math.abs(delta);
			if(step > 500){
				dur = dur * 2;
			}
			mScroller.startScroll(startX, 0, delta, 0, dur);
			invalidate();
		}
		notifyViewShowing();
	}

	private void notifyViewShowing() {
		View child = getChildAt(mCurScreen);
		child.requestFocus();
		mContainer.childOnShow(child);
	}
}