package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import com.xyt.app_market.R;
import com.xyt.app.pullableview.PullToRefreshLayout;
import com.xyt.app.pullableview.PullToRefreshLayout.OnRefreshListener;
import com.xyt.app_market.adapters.ContentListAdaper;
import com.xyt.app_market.adapters.ViewPagerAdapter;
import com.xyt.app_market.adapters.ViewPagerAdapter.OnPagerSelectListener;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.HandleWat_Constant;
import com.xyt.app_market.contants.Constants.Intent_Constant;
import com.xyt.app_market.contants.Constants.TypeConstant;
import com.xyt.app_market.dowload.DowloadService;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.interfacs.Impl;
import com.xyt.app_market.utitl.HttpUtitl;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OtherAppLayout extends FrameLayout implements Impl,
		OnItemClickListener, OnTabSelectItemListnter,OnPagerSelectListener,Observer,OnRefreshListener{
	public static String TAG=OtherAppLayout.class.getSimpleName();
	private Context mcontext;
	private View layout;
	private DownGridLayout Layout_content;
	private FrameLayout layout_tab;
	public List<APPEntity> dataEntities = new ArrayList<APPEntity>();
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	public List<View> views = new ArrayList<View>();
	private TabLayout tabLayout;
	private RefreshData refreshData;
	private 		List<String> tabnames = new ArrayList<String>();
	public boolean NetTag=false;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			DowloadDialog.getInstance().CloseProgress();
			switch (msg.what) {
			case HandleWat_Constant.successwhat:
					NetTag=true;
				 UpdateData();
				 DowdloadData.SyncAllData(TAG);
				 SetRefreshState(true);
				break;
			case HandleWat_Constant.failwhat:
				 SetRefreshState(false);
				break;
			default:
				break;
			}
			
		};
	};
	public void SetRefreshState(boolean state){
			if (refreshData!=null) {
				if (refreshData.type==0) {
					if (state) {
						refreshData.pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
					}else {
						refreshData.pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
					}
				}else if(refreshData.type==1){
					if (state) {
						refreshData.pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}else {
						refreshData.pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
					}
				}
				refreshData=null;
			}
	}
	public OtherAppLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context;
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		layout = LayoutInflater.from(mcontext).inflate(R.layout.layout_other,
				this);
		viewPager = (ViewPager) findViewById(R.id.id_other_pager_content);
		layout_tab = (FrameLayout) findViewById(R.id.id_other_layout_tab);
		addTab();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		MyApp.targetObservable.addObserver(this);
		viewPagerAdapter=new ViewPagerAdapter(views, mcontext,this);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(viewPagerAdapter);
	}
	public void addTab() {
		Resources res = mcontext.getResources();
		tabnames.add(res.getString(R.string.other_tab_name));
		tabnames.add(res.getString(R.string.other_tab_name2));
		tabnames.add(res.getString(R.string.other_tab_name3));
		 tabLayout = new TabLayout(mcontext, tabnames, this);
		layout_tab.addView(tabLayout);
		for (int i = 0; i < tabnames.size(); i++) {
			DownGridLayout tab_content = new DownGridLayout(mcontext,i,true);
			tab_content.setRefreshListener(this);
			views.add(tab_content);
		}
		tabLayout.SetSelectItem(0);
//		GetData();
	}
	public void GetData( ){
		DowloadDialog.getInstance().onCreateProgress(MyApp.res.getString(R.string.dialog_settings));
		for (int i = 0; i < tabnames.size(); i++) {
			((DownGridLayout)views.get(i)).page=0;
			((DownGridLayout)views.get(i)).pageCount=10;
			GetHttpData(i,true);
		}
	}
	public void UpdateData() {
		((DownGridLayout)views.get(0)).UpdateData( DataConstant.httpListEntity.getMapLists());
		((DownGridLayout)views.get(1)).UpdateData(DataConstant.httpListEntity.getMusicLists());
		((DownGridLayout)views.get(2)).UpdateData(	 DataConstant.httpListEntity.getOtherLists());
	}
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		super.onWindowVisibilityChanged(visibility);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onSelectPostion(int postions) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(postions);
	}

	@Override
	public void onPagerSelect(int postion) {
		// TODO Auto-generated method stub
		tabLayout.SetSelectItem(postion);
	}
	public void GetHttpData(int postion,boolean clearTag){
			switch (postion) {
			case 0:
					HttpUtitl.GetTypeData(clearTag,handler, 0, TypeConstant.Map_Type, 	((DownGridLayout)views.get(0)).page, 	((DownGridLayout)views.get(0)).pageCount);
				break;
			case 1:
				HttpUtitl.GetTypeData(clearTag,handler, 0, TypeConstant.Music_Type, 	((DownGridLayout)views.get(1)).page, 	((DownGridLayout)views.get(1)).pageCount);
				break;
			case 2:
				HttpUtitl.GetTypeData(clearTag,handler, 0, TypeConstant.Other_Type,((DownGridLayout)views.get(2)).page, 	((DownGridLayout)views.get(2)).pageCount);
				break;
			default:
				break;
			}
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
				((DownGridLayout)views.get(0)).UpdateData(DataConstant.httpListEntity.getMapLists());
				((DownGridLayout)views.get(1)).UpdateData(DataConstant.httpListEntity.getMusicLists());
				((DownGridLayout)views.get(2)).UpdateData(DataConstant.httpListEntity.getOtherLists());
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		int postion=(Integer) pullToRefreshLayout.getTag();
		((DownGridLayout)views.get(postion)).page=0;
		((DownGridLayout)views.get(postion)).pageCount=10;
		refreshData=new RefreshData(pullToRefreshLayout,0);
		GetHttpData(postion,true);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		int postion=(Integer) pullToRefreshLayout.getTag();
		((DownGridLayout)views.get(postion)).page++;
		refreshData=new RefreshData(pullToRefreshLayout,1);
		GetHttpData(postion,false);
	}
}
