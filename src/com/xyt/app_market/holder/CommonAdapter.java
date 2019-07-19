package com.xyt.app_market.holder;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public  abstract  class CommonAdapter<T> extends BaseAdapter implements
		OnScrollListener {
	/**
	 * 数据源
	 */
	protected List<T> datas = null;

	/**
	 * 上下文对象
	 */
	protected Context context = null;

	/**
	 * item布局文件的资源ID
	 */
	protected int itemLayoutResId = 0;

	public int getItemLayoutResId() {
		return itemLayoutResId;
	}

	public void setItemLayoutResId(int itemLayoutResId) {
		this.itemLayoutResId = itemLayoutResId;
	}

	int start, end;

	public CommonAdapter(Context context, int itemLayoutResId) {
		this.context = context;
		this.itemLayoutResId = itemLayoutResId;
	}
	public CommonAdapter(Context context, int itemLayoutResId,List<T> datas) {
		this.context = context;
		this.itemLayoutResId = itemLayoutResId;
		this.datas=datas;
	}
	public void SetData(List<T> datas) {
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	/**
	 * 注意，返回值也要为泛型
	 */
	@Override
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.getViewHolder(context,
				itemLayoutResId, position, convertView, parent);
		convert(viewHolder, getItem(position));

		return viewHolder.getConvertView();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		start = firstVisibleItem;
		end = firstVisibleItem + visibleItemCount;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {

		}
	}

	/**
	 * 开发者实现该方法，进行业务处理
	 */
	public abstract void convert(ViewHolder viewHolder, T item);
}
