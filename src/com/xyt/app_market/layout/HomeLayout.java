package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import com.xyt.app_market.R;
import com.xyt.app_market.adapters.ViewPagerAdapter;
import com.xyt.app_market.adapters.ViewPagerAdapter.OnPagerSelectListener;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.HandleWat_Constant;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.interfacs.Impl;
import com.xyt.app_market.utitl.HttpUtitl;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

public class HomeLayout extends FrameLayout implements Impl,
		OnItemClickListener, OnTabSelectItemListnter, OnPagerSelectListener,
		Observer {
	public static String TAG = HomeLayout.class.getSimpleName();
	private Context mcontext;
	private View layout;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private FrameLayout layout_tab;
	private TabLayout tabLayout;
	private CarouselLayout carouselLayout;
	public List<View> views = new ArrayList<View>();
	public boolean fristNetTag = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			DowloadDialog.getInstance().CloseProgress();
			switch (msg.what) {
			case HandleWat_Constant.successwhat:
				fristNetTag = true;
				DowdloadData.SyncAllData(TAG);
				UpdateData();
				break;
			case HandleWat_Constant.failwhat:
				break;
			default:
				break;
			}
		};
	};
	public HomeLayout(Context context) {
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
		if (MyApp.qmType()==1) {
			layout = LayoutInflater.from(mcontext).inflate(R.layout.t2layout_hot,
					this);
		}else {
			layout = LayoutInflater.from(mcontext).inflate(R.layout.layout_hot,
					this);
		}

		viewPager = (ViewPager) findViewById(R.id.id_hot_paper_content);
		layout_tab = (FrameLayout) findViewById(R.id.id_hot_layout_tab);
		carouselLayout = (CarouselLayout) findViewById(R.id.id_conver_viewpager);
		addTab();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		MyApp.targetObservable.addObserver(this);
	}
	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		viewPagerAdapter = new ViewPagerAdapter(views, mcontext, this);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(viewPagerAdapter);
	}
	public synchronized void GetData() {
		if (!fristNetTag) {
			DowloadDialog.getInstance().onCreateProgress(
					MyApp.res.getString(R.string.dialog_settings));
		}
		if (!DowdloadData.isDownstate() ) {
			HttpUtitl.GetHomeData(handler);
		} 
	}

	public void addTab() {
		List<String> tabnames = new ArrayList<String>();
		Resources res = mcontext.getResources();
		tabnames.add(res.getString(R.string.hot_tab_name));
		tabnames.add(res.getString(R.string.hot_tab_name2));
		tabnames.add(res.getString(R.string.hot_tab_name3));
		tabLayout = new TabLayout(mcontext, tabnames, this);
		layout_tab.addView(tabLayout);
		tabLayout.SetSelectItem(0);
		for (int i = 0; i < tabnames.size(); i++) {
			DownGridLayout tab_content = new DownGridLayout(mcontext, i, false);
			views.add(tab_content);
		}
	}

	public void UpdateData() {
		((DownGridLayout) views.get(0)).UpdateData(DataConstant.httpListEntity
				.getRecomLists());
		((DownGridLayout) views.get(1)).UpdateData(DataConstant.httpListEntity
				.getDownLists());
		((DownGridLayout) views.get(2)).UpdateData(DataConstant.httpListEntity
				.getNewsLists());
		carouselLayout.SetInit(DataConstant.httpListEntity.getScrollLists());
		StartCarouselTimer();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO Auto-generated method stub
		if (visibility == View.VISIBLE) {
			StartCarouselTimer();
		} else {
			EndCarouselTimer();
		}
		super.onWindowVisibilityChanged(visibility);
	}

	private TimerTask timerTask;
	private Timer timer;
	private int index = 2;

	private void StartCarouselTimer() {
		if (carouselLayout.getBitmaps().size() < 2) {
			EndCarouselTimer();
			return;
		}
		if (timer == null) {
			timer = new Timer();
		}
		if (timerTask == null) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (index < carouselLayout.getBitmaps().size() - 1) {
						index++;
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								carouselLayout.nextResetIcon();
							}
						});
					} else {
						index = 0;
					}
				}
			};
			timer.schedule(timerTask, 0, 3000);
		}
	}

	public void EndCarouselTimer() {
		if (timer != null) {
			timer.cancel();
		}
		if (timerTask != null) {
			timerTask.cancel();
		}
		timer = null;
		timerTask = null;
	}

	@Override
	public void onSelectPostion(int postions) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(postions);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPagerSelect(int postion) {
		// TODO Auto-generated method stub
		tabLayout.SetSelectItem(postion);
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		((DownGridLayout) views.get(0)).UpdateData(DataConstant.httpListEntity
				.getRecomLists());
		((DownGridLayout) views.get(1)).UpdateData(DataConstant.httpListEntity
				.getDownLists());
		((DownGridLayout) views.get(2)).UpdateData(DataConstant.httpListEntity
				.getNewsLists());
	}


}
