package com.xyt.app_market.layout;

import com.xyt.app.pullableview.PullToRefreshLayout;

public class RefreshData {
	PullToRefreshLayout pullToRefreshLayout;
	int type;

	public RefreshData(PullToRefreshLayout pullToRefreshLayout, int type) {
		super();
		this.pullToRefreshLayout = pullToRefreshLayout;
		this.type = type;
	}

}
