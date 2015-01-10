package org.zywx.wbpalmstar.plugin.uexhexagonal;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Container extends LinearLayout implements OnClickListener {

	public static final int INTENT_TAG_1 = 1;
	public static final int INTENT_TAG_2 = 2;
	public static final int INTENT_TAG_3 = 3;
	public static final int INTENT_TAG_4 = 4;
	public static final int INTENT_TAG_5 = 5;
	public static final int INTENT_TAG_6 = 6;
	
	private RotateView mRoot;
	private TextView mTitle;
	private ReContainer mReContainer;
	private int mDefaultFont;
	private float mDefaultRadius;
	private CircleView mCircleView;
	
	public Container(Context context, ReContainer r, int d, float rd) {
		super(context);
		mReContainer = r;
		mDefaultRadius = rd;
		mDefaultFont = d;
		setOrientation(VERTICAL);
		init(context);
	}
	
	private void init(Context context){
		mRoot = new RotateView(context, this);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -1);
        sp.weight = 1.0f;
        mRoot.setLayoutParams(sp);
        addView(mRoot); 
        
        LinearLayout tLayout = new LinearLayout(context);
        tLayout.setOrientation(LinearLayout.VERTICAL);
        tLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.topMargin = -20;
        tLayout.setLayoutParams(tp);
        
        int jId = EUExUtil.getResDrawableID("plugin_hexagonal_jiantou");
        if(0 != jId){
            ImageView jiantou = new ImageView(context);
            jiantou.setBackgroundResource(jId);
            LinearLayout.LayoutParams jl = new LinearLayout.LayoutParams(-2, -2);
            jiantou.setLayoutParams(jl);
            tLayout.addView(jiantou);
        }
        
        mCircleView = new CircleView(context, mDefaultRadius);
        LinearLayout.LayoutParams cl = new LinearLayout.LayoutParams(-1, -2);
        mCircleView.setPadding(0, 5, 0, 2);
        mCircleView.setLayoutParams(cl);
//        mCircleView.setCurrentItem(1);
        tLayout.addView(mCircleView);
        
        mTitle = new TextView(context);
        mTitle.setTextColor(0xFF000000);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setTextSize(mDefaultFont);
        mTitle.setText("东航移动应用平台POC");
        LinearLayout.LayoutParams tl = new LinearLayout.LayoutParams(-2, -2);
        tl.setMargins(0, 5, 0, 0);
        mTitle.setLayoutParams(tl);
        tLayout.addView(mTitle);
        addView(tLayout);
	}

	public void childOnShow(View child){
    	int viewTag = (Integer)child.getTag();
		String title = "";
		int index = 1;
		switch (viewTag) {
		case RotateView.INTENT_TAG_0:
			title = "员工名片";
			index = 5;
			break;
		case RotateView.INTENT_TAG_1:
			title = "东航移动应用平台POC";
			index = 0;
			break;
		case RotateView.INTENT_TAG_2:
			title = "航班动态";
			index = 1;
			break;
		case RotateView.INTENT_TAG_3:
			title = "航班雷达";
			index = 2;
			break;
		case RotateView.INTENT_TAG_4:
			title = "员工工资";
			index = 3;
			break;
		case RotateView.INTENT_TAG_5:
			title = "员工手册";
			index = 4;
			break;
		}
		mTitle.setText(title);
		mCircleView.setCurrentItem(index);
	}
	
	@Override
	public void onClick(View v) {
		int tag = (Integer) v.getTag();
		int index = 0;
		switch (tag) {
		case RotateView.INTENT_TAG_0:
			index = 5;
			break;
		case RotateView.INTENT_TAG_1:
			index = 0;
			break;
		case RotateView.INTENT_TAG_2:
			index = 1;
			break;
		case RotateView.INTENT_TAG_3:
			index = 2;
			break;
		case RotateView.INTENT_TAG_4:
			index = 3;
			break;
		case RotateView.INTENT_TAG_5:
			index = 4;
			break;
		}
		mReContainer.onClick(index);
	}
	
	public void reset(){
		mRoot.reset();
	}
}