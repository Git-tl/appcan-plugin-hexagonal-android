package org.zywx.wbpalmstar.plugin.uexhexagonal;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ReContainer extends RelativeLayout {

	private static ReContainer instance;
	private Container mContainer;
	private ItemListener mItemListener;
	
	private ReContainer(Context context) {
		super(context);
		init(getContext());
	}
	
	public void setItemListener(ItemListener lis){
		mItemListener = lis;
	}
	
	public static ReContainer get(Context context){
		if(null == instance){
			instance = new ReContainer(context);
		}
		ViewGroup p = (ViewGroup) instance.getParent();
		if(null != p){
			p.removeView(instance);
		}
		return instance;
	}
	
	private void init(Context context){
		int pl = 50;
        int pt = 120;
        int font = 14;
        float radius = 2;
        DisplayMetrics dispm = getResources().getDisplayMetrics();
        switch (dispm.densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			pl = 20;
			pt = 30;
			font = 11;
			radius = 2;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			pl = 30;
			pt = 80;
			font = 13;
			radius = 3;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			pl = 98;
			pt = 156;
			font = 16;
			radius = 4;
			break;
		case 320: //DisplayMetrics.DENSITY_XHIGH from 9
			pl = 115;
			pt = 170;
			font = 22;
			radius = 5;
			break;
		default:
			break;
        }
        int pr = pl;
        int pb = pt;
        setPadding(pl, pt, pr, pb);
        LayoutParams lp = new LayoutParams(-1, -1);
        setLayoutParams(lp);
        
        mContainer = new Container(context, this, font, radius);
        RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(-1, -1);
        mContainer.setLayoutParams(ll);
        addView(mContainer);
	}

	public void onClick(int index) {
		if(null != mItemListener){
			mItemListener.onItemClick(index);
		}
	}
	
	public void reset(){
		if(null != mContainer){
			mContainer.reset();
		}
	}
}