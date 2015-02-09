package org.zywx.wbpalmstar.plugin.uexhexagonal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexhexagonal.TouchSurfaceView.onItemClickListener;
import org.zywx.wbpalmstar.widgetone.dataservice.WWidgetData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EUExHexagonal extends EUExBase {
	public static String[] LABLES = new String[] {"移动应用平台POC", "航班动态", "航班雷达", "员工工资", "员工手册", "员工名片"};
	public static final String function_on = "uexHexagonal.naviClicked";
	public static final String onItemClickFunName = "uexHexagonal.onItemClick";
	private String TAG = "EUExHexagonal";
	private static boolean isOpened = false;
	public static View decorView = null;
	private static int currentIndex = 0;
	private List<Pair<String, String>> mParam;

	public EUExHexagonal(Context context, EBrowserView inParent) {
		super(context, inParent);
	}
	
	public void setPrismParam(String[] parm){
		if(parm.length < 1){
			return;
		}
		try{
			JSONObject json = new JSONObject(parm[0]);
			JSONArray value = json.optJSONArray("value");
			int length = value.length();
			if(length < 6){
				return;
			}
			mParam = new ArrayList<Pair<String,String>>(length);
			WWidgetData wgtData = mBrwView.getCurrentWidget();
			for(int i = 0; i < length; ++i){
				JSONObject object = value.optJSONObject(i);
				String pic = object.optString("pic");
				pic = BUtility.makeRealPath(pic, wgtData.m_widgetPath, wgtData.m_wgtType);
				String text = object.optString("text");
				Pair<String, String> p = new Pair<String, String>(pic, text);
				mParam.add(p);
				if(0 == i){
					mParam.add(p);
					mParam.add(p);
				}
				LABLES[i] = text;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open(String[] parm) {
		BDebug.i("fzy", "open()-->" + isOpened);
		if (!isOpened) {
			RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			parms.addRule(RelativeLayout.CENTER_IN_PARENT);
			decorView = initView();
			addViewToCurrentWindow(decorView, parms);

			isOpened = true;
		}else{
			if (decorView != null) {
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						LinearLayout layout = (LinearLayout) decorView;
						TouchSurfaceView surfaceView = (TouchSurfaceView) layout.findViewById(12345678);
						surfaceView.setVisibility(View.VISIBLE);
						surfaceView.setState(true);
					}
				});
			}
		}
	}

	private View initView() {
		TouchSurfaceView touchSurfaceView = new TouchSurfaceView(this, mContext, mParam);
		touchSurfaceView.setZOrderOnTop(true);
		touchSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		LinearLayout layout = new LinearLayout(mContext);
		layout.setBackgroundColor(0x00000000);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		int length = dm.widthPixels;
		layout.addView(touchSurfaceView, new LayoutParams(length, length));
		touchSurfaceView.setId(12345678);
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(EUExUtil.getResDrawableID("plugin_hexagonal_jiantou"));
		LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(-2, -2);
		ip.topMargin = -(length / 8);
		layout.addView(imageView, ip);
		final TextView textView = new TextView(mContext);
		layout.addView(textView, new LayoutParams(-2, -2));
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(18);
		touchSurfaceView.setCurrentIndex(currentIndex);
		touchSurfaceView.setOnItemClickedListener(new onItemClickListener() {

			@Override
			public void onItemClicked(int index) {
				textView.setText(LABLES[index]);
				String js = SCRIPT_HEADER + "if(" + function_on + "){" + function_on + "(" + index + ");}";
				onCallback(js);
				js = SCRIPT_HEADER + "if(" + onItemClickFunName + "){" + onItemClickFunName + "(" + index + ");}";
				onCallback(js);
			}

			@Override
			public void onItemSelected(int index) {
				textView.setText(LABLES[index]);
				currentIndex=index;
			}
		});
		textView.setText(LABLES[touchSurfaceView.getCurrentIndex()]);
		touchSurfaceView.requestFocus();
		touchSurfaceView.setFocusableInTouchMode(true);
		return layout;
	}

	public void close(String[] parm) {
		if (isOpened) {
			if (decorView != null) {
				LinearLayout layout = (LinearLayout) decorView;
				removeViewFromCurrentWindow(layout);
				isOpened = false;
				decorView = null;
			}
		}
	}

	@Override
	public boolean clean() {
		BDebug.i("fzy", TAG + "   clean()----->" + this.toString() + isOpened);
		close(null);
		return true;
	}
}