package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import com.xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.interfacs.Impl;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class TabLayout extends LinearLayout implements Impl, OnClickListener {
	private Context mcontext;
	private List<String> tabnames = new ArrayList<String>();
	private View Item;
	private TextView textname;
	private OnTabSelectItemListnter tabSelectItemListnter;
	private LinearLayout.LayoutParams params;

	public TabLayout(Context context, List<String> tabnames,
			OnTabSelectItemListnter tabSelectItemListnter) {
		super(context);
		this.tabnames = tabnames;
		// TODO Auto-generated constructor stub
		mcontext = context;
		this.tabSelectItemListnter = tabSelectItemListnter;
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		params=new LayoutParams(LayoutParams.MATCH_PARENT,50);
		params.weight=1;
		for (int i = 0; i < tabnames.size(); i++) {
			Item = LayoutInflater.from(mcontext).inflate(MyApp.qmType()==1?R.layout.t2item_tab:R.layout.item_tab,
					null);
			textname = (TextView) Item.findViewById(R.id.id_tab_name);
			Item.setId(i);
			textname.setText(tabnames.get(i));
			Item.setOnClickListener(this);
			addView(Item, i,params);
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SetSelectItem(v.getId());
	}

	public void SetSelectItem(int postion) {
		if (getChildCount() == 0) {
			return;
		}
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i).getId() == postion) {
				getChildAt(i).setSelected(true);
			} else {
				getChildAt(i).setSelected(false);
			}
		}
		tabSelectItemListnter.onSelectPostion(postion);
	}
}
