package org.zywx.wbpalmstar.plugin.uexhexagonal;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

/**
 * Implement a simple rotation control.
 * 
 */
public class TouchSurfaceView extends GLSurfaceView {

	private List<Pair<String, String>> mParam;
	
	public static interface onItemClickListener {
		void onItemClicked(int index);

		void onItemSelected(int index);
	}

	public onItemClickListener listener;

	public void setOnItemClickedListener(onItemClickListener listener) {
		this.listener = listener;
	}

	public int getCurrentIndex() {
		{
			float an = (mRenderer.mAngleX) % 360;
			if (an < 0)
				an += 360;
			int index = (int) ((an) / 60 + 0.2);
			index = (6 - index) % 6;
			return index;
		}
	}

	public TouchSurfaceView(Context context, List<Pair<String, String>> param) {
		super(context);
		mParam = param;
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mRenderer = new CubeRenderer(context);
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public TouchSurfaceView(EUExBase base, Context context,
			List<Pair<String, String>> param) {
		super(context);
		mParam = param;
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mRenderer = new CubeRenderer(base, context);
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void setCurrentIndex(int index)
	{
		mRenderer.mAngleX = ((6-index%6)%6)*60;
		requestRender();
	}
	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		mRenderer.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR;
		mRenderer.mAngleY += e.getY() * TRACKBALL_SCALE_FACTOR;
		requestRender();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		// float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			mFliRunner.startEvent(e);

		}
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;
			// float dy = y - mPreviousY;
			mRenderer.mAngleX += dx * TOUCH_SCALE_FACTOR;
			// mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR;
			requestRender();
			break;
		case MotionEvent.ACTION_UP: {
			if (mFliRunner.endEvent(e) == 1) {
				float an = (mRenderer.mAngleX) % 360;
				if (an < 0)
					an += 360;

				int index = (int) ((an) / 60 + 0.2);
				index = (6 - index) % 6;
				if (listener != null) {
					listener.onItemClicked(index);
				}
			}
		}
			break;
		}
		mPreviousX = x;
		// mPreviousY = y;
		return true;
	}

	public float cubeInertia(float angle) {
		mRenderer.mAngleX += angle;
		requestRender();
		return mRenderer.mAngleX;
	}

	public float setAngle(float angle, boolean needRefresh) {
		mRenderer.mAngleX = angle;
		if(needRefresh)
			requestRender();
		return mRenderer.mAngleX;

	}
	
	public float setTranslate(float translate)
	{
		mRenderer.mTranslate +=translate;
		requestRender();
		return mRenderer.mTranslate;
	}
	


	/**
	 * Render a cube.
	 */
	private class CubeRenderer implements GLSurfaceView.Renderer {
		public boolean mState = true;
		public float mTranslate = 0;
		public CubeRenderer(Context context) {
			mCube = new Cube(context, mParam);
		}

		public CubeRenderer(EUExBase base, Context context) {
			mCube = new Cube(base, context, mParam);
		}

		public void onDrawFrame(GL10 gl) {
			/*
			 * Usually, the first thing one might want to do is to clear the
			 * screen. The most efficient way of doing this is to use glClear().
			 */
			
			gl.glDisable(GL10.GL_DITHER);
			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
			
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

			/*
			 * Now we're ready to draw some 3D objects
			 */

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glTranslatef(0, 0, -3.0f - mTranslate);
			// gl.glScalef(1.4f, 1.4f, 1.4f);
			gl.glRotatef(mAngleX, 0, 1, 0);
			gl.glRotatef(mAngleY, 1, 0, 0);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			// gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glActiveTexture(GL10.GL_TEXTURE0);
			if(mState)
				mCube.draw(gl);

		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {

			gl.glViewport(0, 0, width, height);

			/*
			 * Set our projection matrix. This doesn't have to be done each time
			 * we draw, but usually a new projection needs to be set when the
			 * viewport is resized.
			 */

			float ratio = (float) width / height;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glFrustumf(-ratio, ratio, -1, 1, 1.9f, 10);
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			/*
			 * By default, OpenGL enables features that improve quality but
			 * reduce performance. One might want to tweak that especially on
			 * software renderer.
			 */
			gl.glDisable(GL10.GL_DITHER);

			/*
			 * Some one-time OpenGL initialization can be made here probably
			 * based on features of this particular context
			 */
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			gl.glClearColor(0, 0, 0, 0);
			//gl.glEnable(GL10.GL_CULL_FACE);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_TEXTURE_2D);

			mCube.init(gl);
		}

		private Cube mCube;
		public float mAngleX;
		public float mAngleY;
	}

	private final float TOUCH_SCALE_FACTOR = 180.0f / 360;
	private final float TRACKBALL_SCALE_FACTOR = 36.0f;
	private CubeRenderer mRenderer;
	private float mPreviousX;
	
	private class FlingRunnable implements Runnable {
		/**
		 * Tracks the decay of a fling scroll
		 */
		private TouchSurfaceView mParent;
//		boolean mShouldStopFling = false;
		private float mBeginX;
		private long mBeginTime;
		private float mEndX;
		private long mEndTime;
		private float v, a;
		private float destAngle, currentAngle;
		private int inertiaState = 0;
		private int direction = 1;
//		private double step;
		private int isRunning = 0;

		public FlingRunnable(TouchSurfaceView Context) {
			mParent = Context;
		}

		public void startEvent(MotionEvent e) {
			removeCallbacks(this);
			mBeginX = e.getX();
			mBeginTime = System.currentTimeMillis();
			inertiaState = 0;
		}

		public int endEvent(MotionEvent e) {

			mEndX = e.getX();
			mEndTime = System.currentTimeMillis();

			long decTime = mEndTime - mBeginTime + 1;
			float decRange = mEndX - mBeginX;
			
			if (decTime < 300 && Math.abs(decRange) < 16 && isRunning == 0) {
				return 1;
			}
			
			v = decRange*TOUCH_SCALE_FACTOR/decTime*1000;
			
			direction = (int) (v/Math.abs(v));
			a = 360*3;
			inertiaState = 0;
			post(this);
			isRunning = 1;
			return 0;
		}
		
		public void startScale(boolean direct)
		{
			if(!direct)
			{
				mParent.updateState(true);
				inertiaState = 2;
			}
			else if(direct)
			{
				inertiaState = 3;
			}
			post(this);
			requestRender();
		}

		public void run() {
			if(inertiaState == 2)
			{
				float mTranslate = mParent.setTranslate(0.3f);
				if(mTranslate >= 4)
				{
					removeCallbacks(this);
					mParent.updateState(false);
					mParent.requestRender();
					Activity context = (Activity) getContext();
					context.runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							mParent.onPause();
							mParent.setVisibility(View.INVISIBLE);
						}
					});
				}
				else
				{
					postDelayed(this,10);
				}
			}
			else if(inertiaState == 3)
			{
				float mTranslate = mParent.setTranslate(-0.3f);
				if(mTranslate <= 0.3f)
				{
					removeCallbacks(this);
				}
				else
				{
					postDelayed(this,10);
				}
			}
			else
				if (inertiaState == 0) {
				float t = (System.currentTimeMillis()-mEndTime);
				float decAngle = t*v/1000;
				currentAngle = mParent.cubeInertia(decAngle);
				v = v/Math.abs(v)*(Math.abs(v)-a*t/1000);
				
				if (v*direction > 0)
				{	
					mEndTime = System.currentTimeMillis();
					postDelayed(this,10);
				}
				else {
					float an = (currentAngle % 360);
					if (an < 0)
						an += 360;
					currentAngle = mParent.setAngle(an,false);
					float pass = an % 60;
					destAngle = currentAngle;
					
					if (pass < 30 && pass > 0) {
						destAngle -= pass;
						v = (float) ((pass/0.5)*2);
						a = (float) (v/0.5);
						direction = (int) ((destAngle - currentAngle)/Math.abs(destAngle - currentAngle));
						inertiaState = 1;
						mEndTime = System.currentTimeMillis();
						postDelayed(this,60);
					} else if (pass >= 30 && pass < 60) {
						destAngle += (60 - pass);
						v = (float) (((60 - pass)/0.5)*2);
						a = (float) (v/0.5);
						direction = (int) ((destAngle - currentAngle)/Math.abs(destAngle - currentAngle));
						inertiaState = 1;
						mEndTime = System.currentTimeMillis();
						postDelayed(this,10);
					} else
						removeCallbacks(this);
				}
				
			} else {
				float t = (float)(System.currentTimeMillis()-mEndTime);
				float decAngle = t*v/1000;
				v = v/Math.abs(v)*(Math.abs(v)-a*t/1000);
				currentAngle = mParent.cubeInertia(direction*decAngle);
				
				if (v <= 0) {
					mParent.setAngle((int) destAngle,true);
					removeCallbacks(this);
					{
						float an = (destAngle) % 360;
						if (an < 0)
							an += 360;

						int index = (int) ((an) / 60 + 0.2);
						index = (6 - index) % 6;
						isRunning = 0;
						if (listener != null) {
							mParent.listener.onItemSelected(index);
						}
					}
				} else
					postDelayed(this,10);
					mEndTime = System.currentTimeMillis();
			}
		}

	}

	private FlingRunnable mFliRunner = new FlingRunnable(this);

	public void setState(boolean state) {
		if(state == mRenderer.mState)
			return ;
		mRenderer.mState  = state;
		mFliRunner.startScale(state);
	}
	
	public void updateState(boolean state) {
		mRenderer.mState  = state;
	}
}