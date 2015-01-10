/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zywx.wbpalmstar.plugin.uexhexagonal;

import org.zywx.wbpalmstar.plugin.uexhexagonal.TouchSurfaceView.onItemClickListener;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view that
 * uses OpenGL drawing into a dedicated surface.
 * 
 * Shows: + How to redraw in response to user input.
 */
public class TouchRotateActivity extends Activity {

	public static final String[] LABLES = new String[] { "东航移动应用平台POC", "航班动态", "航班雷达", "员工工资", "员工手册", "员工名片" };
	private OnPageClickedCallback callback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(initView());
		touchSurfaceView.requestFocus();
		touchSurfaceView.setFocusableInTouchMode(true);
	}

	private View initView() {
		touchSurfaceView = new TouchSurfaceView(this, null);
		touchSurfaceView.setZOrderOnTop(true);
		touchSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int length = dm.widthPixels;
		layout.addView(touchSurfaceView, new LayoutParams(length, length));
		ImageView imageView = new ImageView(this);
		//imageView.setImageResource(R.drawable.plugin_hexagonal_jiantou);
		layout.addView(imageView, new LayoutParams(-2, -2));
		final TextView textView = new TextView(this);
		layout.addView(textView, new LayoutParams(-2, -2));
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(18);
		touchSurfaceView.setOnItemClickedListener(new onItemClickListener() {

			@Override
			public void onItemClicked(int index) {
				textView.setText(LABLES[index]);
				if (callback != null) {
					callback.onPageClicked(index);
				}
			}

			@Override
			public void onItemSelected(int index) {
				textView.setText(LABLES[index]);
			}
		});
		textView.setText(LABLES[touchSurfaceView.getCurrentIndex()]);
		return layout;
	}

	public void setSurfaceViewTop(boolean toped) {
		touchSurfaceView.setZOrderOnTop(toped);
	}

	public void setAlpha(float alpha) {
		touchSurfaceView.setAlpha(alpha);
	}

	@Override
	protected void onResume() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();
		touchSurfaceView.onResume();
		// touchSurfaceView.setZOrderOnTop(true);
		// BDebug.i("fzy", "onResume()-->");
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		// touchSurfaceView.setZOrderOnTop(false);
		touchSurfaceView.onPause();
		// BDebug.i("fzy", "onPause()-->");

	}

	public void setCallback(OnPageClickedCallback callback) {
		this.callback = callback;
	}

	private TouchSurfaceView touchSurfaceView;

	public interface OnPageClickedCallback {

		void onPageClicked(int index);
	}
}