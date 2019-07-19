package com.xyt.app_market.layout;

import com.xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants;
import com.xyt.app_market.interfacs.Impl;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ContentLayout extends FrameLayout implements Impl ,OnClickListener{
	private Context mcontext;
	private FrameLayout hot_layout,appmange_layout, other_layout;
	private View netview;
	private int index;
	private TextView wg_image;
	public ContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mcontext = context;
		initView();
		initData();
		initEvent();
	}

	public ContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mcontext = context;
		initView();
		initData();
		initEvent();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		hot_layout = new HomeLayout(mcontext);
		appmange_layout = new AppDownMangeLayout(mcontext);
		other_layout = new OtherAppLayout(mcontext);
		netview=LayoutInflater.from(mcontext).inflate(R.layout.net_link, null);
		wg_image = (TextView)netview. findViewById(R.id.id_image_wg_link);
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		wg_image.setOnClickListener(this);
	}
	public void SelectLayout(int index,boolean refreshtag) {
		this.index=index;
		if (Constants.netType.equals(Constants.NetType.NONET)&&index!=2) {
			removeAllViews();
			addView(netview);
			if (refreshtag) {
				MyApp.mToast(R.string.toast_nettitle3);
			}
			return;
		}
		switch (index) {
		case 0:
			removeAllViews();
			addView(hot_layout);
			HomeLayout homeLayout=((HomeLayout)hot_layout);
			homeLayout.GetData();
			break;
		case 1:
			removeAllViews();
			addView(other_layout);
			OtherAppLayout otherAppLayout=(OtherAppLayout)other_layout;
			if(refreshtag||!otherAppLayout.NetTag)otherAppLayout.GetData();
			break;
		case 2:
			removeAllViews();
			addView(appmange_layout);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SelectLayout(index,true);
	}

}
