package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import com.xyt.app_market.R;
import com.xyt.app.pullableview.PullToRefreshLayout;
import com.xyt.app.pullableview.PullToRefreshLayout.OnRefreshListener;
import com.xyt.app.pullableview.PullableGridView;
import com.xyt.app_market.adapters.ContentGridAdaper;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.interfacs.Impl;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class DownGridLayout extends FrameLayout implements Impl {

	public DownGridLayout(Context context, int i, boolean refreshTag) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context;
		this.postion = i;
		this.refreshTag = refreshTag;
		initView();
		initData();
		initEvent();
	}

	private Context mcontext;
	private ContentGridAdaper contentGridAdaper;
	private PullableGridView gridView;
	private int postion;
	private PullToRefreshLayout pullToRefreshLayout;
	public int page, pageCount;
	private OnRefreshListener refreshListener;
	private boolean refreshTag;

	public void setRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		pullToRefreshLayout.setOnRefreshListener(refreshListener);
	}

	public List<APPEntity> dataEntities = new ArrayList<APPEntity>();

	public List<APPEntity> getDataEntities() {
		return dataEntities;
	}

	public void setDataEntities(List<APPEntity> dataEntities) {
		this.dataEntities = dataEntities;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if (refreshTag) {
			LayoutInflater.from(mcontext).inflate(R.layout.layout_refresh_grid,
					this);
			pullToRefreshLayout = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
			pullToRefreshLayout.setTag(postion);
			gridView = (PullableGridView) findViewById(R.id.id_PullableGridView);
		} else {
			gridView = new PullableGridView(mcontext);
			addView(gridView);
		}
		gridView.setNumColumns(2);
		contentGridAdaper = new ContentGridAdaper(mcontext,
				MyApp.qmType()==1?R.layout.t2content_grid_item:R.layout.content_grid_item);
		gridView.setAdapter(contentGridAdaper);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub

	}

	public void UpdateData(List<APPEntity> list) {
		this.dataEntities = list;
		contentGridAdaper.updateDaper(dataEntities);
	}

}
