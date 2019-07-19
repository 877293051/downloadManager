package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import com.xyt.app_market.R;
import com.xyt.app_market.adapters.ContentListAdaper;
import com.xyt.app_market.adapters.ViewPagerAdapter;
import com.xyt.app_market.adapters.ViewPagerAdapter.OnPagerSelectListener;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.Action_Constant;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.Intent_Constant;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.entity.TempMem;
import com.xyt.app_market.interfacs.Impl;
import com.xyt.app_market.utitl.Tools;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Debug.MemoryInfo;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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

public class AppDownMangeLayout extends FrameLayout implements Impl,
		OnTabSelectItemListnter, OnPagerSelectListener,
		Observer {
	public static String TAG=AppDownMangeLayout.class.getSimpleName();
	private Context mcontext;
	private View layout;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private FrameLayout layout_tab;
	private Handler handler = new Handler();
	public List<View> views = new ArrayList<View>();
	private TabLayout tabLayout;
	private boolean memMonitorTag = true,showDowTag=false;;
	public AppDownMangeLayout(Context context) {
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
		layout = LayoutInflater.from(mcontext).inflate(R.layout.layout_set,
				this);
		viewPager = (ViewPager) findViewById(R.id.id_set_paper_content);
		layout_tab = (FrameLayout) findViewById(R.id.id_set_layout_tab);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		addTab();
		UpdateData();
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		MyApp.targetObservable.addObserver(this);
		viewPagerAdapter = new ViewPagerAdapter(views, mcontext, this);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(viewPagerAdapter);
	}

	public void addTab() {
		List<String> tabnames = new ArrayList<String>();
		Resources res = mcontext.getResources();
		tabnames.add(res.getString(R.string.set_tab_name));
		tabnames.add(res.getString(R.string.set_tab_name2));
		tabnames.add(res.getString(R.string.set_tab_name3));
		tabLayout = new TabLayout(mcontext, tabnames, this);
		layout_tab.addView(tabLayout);
		tabLayout.SetSelectItem(0);
		boolean isDowMangerTag;
		for (int i = 0; i < tabnames.size(); i++) {
			if (i == 2) {
				isDowMangerTag = true;
			} else {
				isDowMangerTag = false;
			}
			DownListLayout tab_content = new DownListLayout(mcontext,
					isDowMangerTag);
			views.add(tab_content);
		}
	}

	public void UpdateData() {
		List<DownDataEntity> mdataEntities = new ArrayList<DownDataEntity>();
		for (DownDataEntity mdowndataEntity : DataConstant.nativeEntity
				.getSystemMap().values()) {
			mdataEntities.add(mdowndataEntity);
		}
		((DownListLayout) views.get(0)).UpdateData(mdataEntities);
		List<DownDataEntity> mdataEntities2 = new ArrayList<DownDataEntity>();
		for (DownDataEntity mdowndataEntity2 : DataConstant.nativeEntity
				.getAppMap().values()) {
			MyApp.MLog("ContentListAdaper", "mdowndataEntity2"
					+ mdowndataEntity2.toString());
			mdataEntities2.add(mdowndataEntity2);
			
		}
		((DownListLayout) views.get(1)).UpdateData(mdataEntities2);
		List<DownDataEntity> mdataEntities3 = new ArrayList<DownDataEntity>();
		for (DownDataEntity mdowndataEntity3 : DataConstant.DownloadThreep
				.values()) {
				mdataEntities3.add(mdowndataEntity3);
		}
		((DownListLayout) views.get(2)).UpdateData(mdataEntities3);
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		if (visibility == View.VISIBLE) {
			memMonitorTag = true;
			showDowTag=true;
			startMemThread();
		} else {
			memMonitorTag = false;
		}
		super.onWindowVisibilityChanged(visibility);
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

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		UpdateData();
	}
	public void startMemThread() {
		Thread thread = new Thread() {
			public void run() {
				while (memMonitorTag) {
					final ArrayList<TempMem> tempMems = Tools.getTempMems();
					if (tempMems.size() != 0) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								if (showDowTag) {
									showDownManger();
									showDowTag=false;
								}
								for (int i = 0; i < tempMems.size(); i++) {
									TempMem tm = tempMems.get(i);
									MemoryInfo info = tm.getInfo();
									String processName = tm.rinfo.processName;
									if (!TextUtils.isEmpty(processName)) {
										macthMemorySzie(processName,
												info.getTotalPss());
									}
								}
							}
						});
					}
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};
		if (!thread.isAlive() && memMonitorTag) {
			thread.start();
		}
	}

	public void showDownManger(){
		List<DownDataEntity> mdataEntities3 = new ArrayList<DownDataEntity>();
		for (DownDataEntity mdowndataEntity3 : DataConstant.DownloadThreep
				.values()) {
				mdataEntities3.add(mdowndataEntity3);
		}
		((DownListLayout) views.get(2)).UpdateData(mdataEntities3);
	}

	public void macthMemorySzie(String processName, long mesize) {
		List<DownDataEntity> mdataEntities = new ArrayList<DownDataEntity>();
		for (DownDataEntity mdowndataEntity : DataConstant.nativeEntity
				.getSystemMap().values()) {
			if (processName.equals(mdowndataEntity.getPackagename())) {
				mdowndataEntity.setMemorysize(mesize);
			}
			mdataEntities.add(mdowndataEntity);
		}
		((DownListLayout) views.get(0)).UpdateData(mdataEntities);
		List<DownDataEntity> mdataEntities2 = new ArrayList<DownDataEntity>();
		for (DownDataEntity mdowndataEntity2 : DataConstant.nativeEntity
				.getAppMap().values()) {
			if (processName.equals(mdowndataEntity2.getPackagename())) {
				mdowndataEntity2.setMemorysize(mesize);
			}
			mdataEntities2.add(mdowndataEntity2);
		}
		((DownListLayout) views.get(1)).UpdateData(mdataEntities2);
	}
}
