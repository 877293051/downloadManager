package com.xyt.app_market.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import com.xyt.app_market.R;
import com.xyt.app_market.R.string;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.URLConstant;
import com.xyt.app_market.dowload.DowloadService;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.interfacs.Impl;
import com.xyt.app_market.utitl.HttpBitmap;
import com.xyt.app_market.utitl.Tools;
import com.xyt.app_market.view.DowdloadProgressBar;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppDetailsActivity extends Activity implements Impl,
		OnClickListener, DowloadContentValue, Observer {
	public static String TAG = AppDetailsActivity.class.getSimpleName();
	private DowdloadProgressBar progress_details;
	private LinearLayout linear_details_huadong, linear_details_progress;
	private String url;
	private APPEntity appEntity;
	private Button button_details_download;
	private TextView text_details_name, text_details_present,
			text_details_update, text_down_num, text_details_size;
	private List<String> previewlist = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DowloadDialog.getInstance().setActivity(this);
		if (MyApp.qmType() == 1) {
			setTheme(R.style.t2AppTheme);
		}
		setContentView(R.layout.activity_app_detail);
		if (MyApp.qmType() != 1) {
			findViewById(R.id.id_detail_layout).setBackground(
					MyApp.res.getDrawable(R.drawable.market_bg));
		} else {
			FrameLayout.LayoutParams layoutParams = (LayoutParams) findViewById(
					R.id.id_detail_layout).getLayoutParams();
			layoutParams.topMargin = 80;
		}
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		text_details_name = (TextView) findViewById(R.id.text_details_name);
		text_down_num = (TextView) findViewById(R.id.text_down_num);
		text_details_size = (TextView) findViewById(R.id.text_details_size);
		text_details_present = (TextView) findViewById(R.id.text_details_present);
		text_details_update = (TextView) findViewById(R.id.text_details_update);
		progress_details = (DowdloadProgressBar) findViewById(R.id.progress_details);
		linear_details_huadong = (LinearLayout) findViewById(R.id.linear_details_huadong);
		linear_details_progress = (LinearLayout) findViewById(R.id.linear_details_progress);
		button_details_download = (Button) findViewById(R.id.button_details_download);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		url = getIntent().getStringExtra(Contentdowaload);
		appEntity = DowdloadData.URLmathAPPEntity(url);
		DownDataEntity downDataEntity = appEntity.getDataEntity();
		Tools.SetViewText(text_details_name, downDataEntity.getName());
		Tools.SetViewText(text_details_size, Tools.convertFileSize(Integer
				.valueOf(downDataEntity.getSize())));
		Tools.SetViewText(
				text_down_num,
				getResources().getString(R.string.detail_content)
						+ appEntity.getDownloadcount()
						+ getResources().getString(R.string.detail_content2));
		Tools.SetViewText(text_details_present, appEntity.getIntroduction());
		Tools.SetViewText(text_details_update, appEntity.getUpdateinfo());
		Addpreview(appEntity.getReview1());
		Addpreview(appEntity.getReview2());
		Addpreview(appEntity.getReview3());
		Addpreview(appEntity.getReview4());
		Addpreview(appEntity.getReview5());
		for (int i = 0; i < previewlist.size(); i++) {
			ImageView imageView = new ImageView(getApplicationContext());
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					580, 367);
			layoutParams.bottomMargin = 20;
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.topMargin = 20;
			imageView.setScaleType(ScaleType.FIT_XY);
			HttpBitmap.getInstance(getApplicationContext()).display(
					URLConstant.URL_Image + previewlist.get(i), imageView,
					R.drawable.market_xiangqing_loading);
			linear_details_huadong.addView(imageView, layoutParams);
		}
	}

	public void Addpreview(String path) {
		if (!TextUtils.isEmpty(path)) {
			previewlist.add(path);
		}
	}

	public void initEvent() {
		// TODO Auto-generated method stub
		changeView();
		MyApp.targetObservable.addObserver(this);
		progress_details.setOnClickListener(this);
		button_details_download.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	public void changeView() {
		appEntity = DowdloadData.URLmathAPPEntity(url);
		SetBthTextState(progress_details);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.progress_details:
			SetBthService(v);
			break;
		default:
			break;
		}
	}

	public void SetBthTextState(DowdloadProgressBar dowdloadProgressBar) {
		DownDataEntity downDataEntity = appEntity.getDataEntity();
		MyApp.MLog(TAG, "SetBthTextState" + downDataEntity.toString());
		dowdloadProgressBar.setTag(downDataEntity);
		if (downDataEntity.isUrlstatus()) {
			if (downDataEntity.getDowstatus() == Startstatus) {
				dowdloadProgressBar.setProgress(downDataEntity
						.getFileprogress());
			} else {
				if (downDataEntity.getDowstatus() == Pausestatus) {
					dowdloadProgressBar.setProgress(downDataEntity
							.getFileprogress());
				}
				dowdloadProgressBar.setProgress(downDataEntity.getDowstatus());
			}
		} else if (downDataEntity.isFinshstatus()) {
			dowdloadProgressBar.setStateText(MyApp.res
					.getString(R.string.down_state_an));
		} else if (downDataEntity.isInstallstatus()
				&& !downDataEntity.isUpdatestate()) {
			dowdloadProgressBar.setStateText(MyApp.res
					.getString(R.string.down_state_ying));
		} else {
			dowdloadProgressBar
					.setStateText(downDataEntity.isUpdatestate() ? MyApp.res
							.getString(R.string.down_state_update) : MyApp.res
							.getString(R.string.down_state_xia));
		}
	}

	public void SetBthService(View v) {
		Intent intent = new Intent(this, DowloadService.class);
		DownDataEntity downDataEntity = (DownDataEntity) v.getTag();
		if (downDataEntity.isUrlstatus()) {
			if (downDataEntity.getDowstatus() == Startstatus
					|| downDataEntity.getDowstatus() == Waitstatus
					|| downDataEntity.getDowstatus() == Readystatus) {
				intent.putExtra(Actiondowaload, Pausedowload);
				intent.putExtra(Contentdowaload, downDataEntity.getUrl());
				startService(intent);
			} else if (downDataEntity.getDowstatus() == Pausestatus) {
				intent.putExtra(Actiondowaload, Startdowload);
				intent.putExtra(Contentdowaload, downDataEntity);
				startService(intent);
			}
		} else if (downDataEntity.isFinshstatus()) {
			DowloadDialog.getInstance().InstallDialog(
					downDataEntity.getPackagename());
		} else if (downDataEntity.isInstallstatus()
				&& !downDataEntity.isUpdatestate()) {
			DowloadDialog.getInstance().OpenCurrentDialog(
					downDataEntity.getPackagename());
		} else {
			intent.putExtra(Actiondowaload, Startdowload);
			intent.putExtra(Contentdowaload, downDataEntity);
			startService(intent);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		DowloadDialog.getInstance().setActivity(this);
		super.onResume();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		DowloadDialog.getInstance().CloseProgress();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		changeView();
	}
}
