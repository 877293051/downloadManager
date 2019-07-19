package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import com.xyt.app_market.R;
import com.xyt.app_market.adapters.ContentListAdaper;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.interfacs.Impl;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;

public class DownListLayout extends FrameLayout implements Impl {

	public DownListLayout(Context context,boolean isDowMangerTag ) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context;
		this.isDowMangerTag=isDowMangerTag;
		initView();
		initData();
		initEvent();
	}
	private boolean isDowMangerTag;
	private Context mcontext;
	private ContentListAdaper contentListAdaper;
	private ListView  listView;
	private List<DownDataEntity> dataEntitiest=new ArrayList<DownDataEntity>();;

	public List<DownDataEntity> getDataEntitiest() {
		return dataEntitiest;
	}

	public void setDataEntitiest(List<DownDataEntity> dataEntitiest) {
		this.dataEntitiest = dataEntitiest;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		listView=new ListView(mcontext);
		listView.setDivider(null);
		addView(listView);
		contentListAdaper = new ContentListAdaper(mcontext,
				R.layout.content_list_item,isDowMangerTag);
		listView.setAdapter(contentListAdaper);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub

	}

	public void UpdateData(List<DownDataEntity> dataEntitiest) {
		this.dataEntitiest=dataEntitiest;
		contentListAdaper.updateDaper(dataEntitiest);
	}

}
