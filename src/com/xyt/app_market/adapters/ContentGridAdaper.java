package com.xyt.app_market.adapters;

import java.util.ArrayList;
import java.util.List;
import com.xyt.app_market.R;
import com.xyt.app_market.activity.AppDetailsActivity;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.URLConstant;
import com.xyt.app_market.dowload.DowloadService;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.holder.CommonAdapter;
import com.xyt.app_market.holder.ViewHolder;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.utitl.HttpBitmap;
import com.xyt.app_market.utitl.Tools;
import com.xyt.app_market.view.DowdloadProgressBar;
import com.xyt.app_market.view.ViewDownOnTouch;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentGridAdaper extends CommonAdapter<APPEntity> implements
		OnClickListener, DowloadContentValue, OnItemClickListener {
	public List<APPEntity> dataEntities = new ArrayList<APPEntity>();
	private Resources res;

	public ContentGridAdaper(Context context, int itemLayoutResId) {
		super(context, itemLayoutResId);
		super.SetData(dataEntities);
		// TODO Auto-generated constructor stub
		res = context.getResources();
	}
	public void updateDaper(List<APPEntity> dataEntities) {
		this.dataEntities = dataEntities;
		super.SetData(dataEntities);
		notifyDataSetChanged();
	}
	@Override
	public void convert(ViewHolder viewHolder, APPEntity item) {
		// TODO Auto-generated method stub
		View layout = viewHolder.getView(R.id.id_list_app_layout);
		TextView app_name = viewHolder.getView(R.id.id_app_name);
		TextView app_size = viewHolder.getView(R.id.id_app_size);
		ImageView icom_image = viewHolder.getView(R.id.id_app_icon);
		DowdloadProgressBar dowdloadProgressBar = viewHolder
				.getView(R.id.id_app_down);
		HttpBitmap.getInstance(context).display(
				URLConstant.URL_Image + item.getIcon(), icom_image,
				R.drawable.market_right_tubiao_moren);
		Tools.SetViewText(app_name, item.getName());
		Tools.SetViewText(app_size, Tools.convertFileSize(Integer.valueOf(item.getSize())));
		SetBthTextState(dowdloadProgressBar, item);
		dowdloadProgressBar.setOnClickListener(this);
		layout.setTag(item.getFilepath());
		layout.setOnClickListener(this);
		layout.setOnTouchListener(new ViewDownOnTouch());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_app_down:
			SetBthService(v);
			break;
		case R.id.id_list_app_layout:
			String url = (String) v.getTag();
			if (TextUtils.isEmpty(url)) {
				return;
			}
			Intent intent = new Intent(DowloadDialog.getInstance()
					.getActivity(), AppDetailsActivity.class);
			intent.putExtra(Contentdowaload, url);
			DowloadDialog.getInstance().getActivity().startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String url = dataEntities.get(position).getFilepath();
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Intent intent = new Intent(DowloadDialog.getInstance().getActivity(),
				AppDetailsActivity.class);
		intent.putExtra(Contentdowaload, url);
		DowloadDialog.getInstance().getActivity().startActivity(intent);
	}

	public void SetBthTextState(DowdloadProgressBar dowdloadProgressBar,
			APPEntity aPPEntity) {
		DownDataEntity downDataEntity =aPPEntity.getDataEntity();
		dowdloadProgressBar.setTag(downDataEntity);
		if (downDataEntity.isUrlstatus()) {
			if (downDataEntity.getDowstatus() == Startstatus) {
				dowdloadProgressBar.setProgress(downDataEntity
						.getFileprogress());
			} else {
				if (downDataEntity.getDowstatus() ==Pausestatus) {
					dowdloadProgressBar.setProgress(downDataEntity
							.getFileprogress());
				}
				dowdloadProgressBar.setProgress(downDataEntity.getDowstatus());
			}
		}else if (downDataEntity.isFinshstatus()) {
			dowdloadProgressBar.setStateText(res
					.getString(R.string.down_state_an)); 
		} 
		else if (downDataEntity.isInstallstatus()&&!downDataEntity.isUpdatestate()) {
			dowdloadProgressBar.setStateText(res
					.getString(R.string.down_state_ying));
		}  else {
			dowdloadProgressBar.setStateText(downDataEntity.isUpdatestate()?res
					.getString(R.string.down_state_update):res
					.getString(R.string.down_state_xia));
		}
	}

	public void SetBthService(View v) {
		Intent intent = new Intent(context, DowloadService.class);
		DownDataEntity downDataEntity = (DownDataEntity) v.getTag();
		if (downDataEntity.isUrlstatus()) {
			if (downDataEntity.getDowstatus() == Startstatus
					|| downDataEntity.getDowstatus() == Waitstatus
					|| downDataEntity.getDowstatus() == Readystatus) {
				intent.putExtra(Actiondowaload, Pausedowload);
				intent.putExtra(Contentdowaload, downDataEntity.getUrl());
				context.startService(intent);
			}else if(downDataEntity.getDowstatus() ==Pausestatus||downDataEntity.getDowstatus() ==Errordowaload){
				intent.putExtra(Actiondowaload, Startdowload);
				intent.putExtra(Contentdowaload, downDataEntity);
				context.startService(intent);
			}
		}
		else if(downDataEntity.isFinshstatus()){
				DowloadDialog.getInstance().InstallDialog(
						downDataEntity.getSdfilepath());
		}
		else if (downDataEntity.isInstallstatus()&&!downDataEntity.isUpdatestate()) {
			DowloadDialog.getInstance().OpenCurrentDialog(
					downDataEntity.getPackagename());
		}
		else {
			intent.putExtra(Actiondowaload, Startdowload);
			intent.putExtra(Contentdowaload, downDataEntity);
			context.startService(intent);
		}
	}
}
