package com.xyt.app_market.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.xyt.app_market.R;
import com.xyt.app_market.activity.AppDetailsActivity;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.DataConstant;
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
import com.xyt.app_market.view.ViewDownOnTouch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ContentListAdaper extends CommonAdapter<DownDataEntity> implements
		DowloadContentValue, OnClickListener {
	private long mb = 1048576;
	private Context context;
	public List<DownDataEntity> dataEntities = new ArrayList<DownDataEntity>();
	private Resources res;
	private boolean isDowMangerTag;
	private java.text.DecimalFormat df;
	private 		PackageManager pkgManager;

	public ContentListAdaper(Context context, int itemLayoutResId,
			boolean isDowMangerTag) { 
		super(context, itemLayoutResId);
		super.SetData(dataEntities);
		this.context = context;
		this.isDowMangerTag = isDowMangerTag;
		res = context.getResources();
		df = new java.text.DecimalFormat("#.##");
		pkgManager=  MyApp.getApp().getPackageManager(); // 需要system权限
		// TODO Auto-generated constructor stub
	}

	public void updateDaper(List<DownDataEntity> dataEntities) {
		this.dataEntities = dataEntities;
		super.SetData(dataEntities);
		notifyDataSetChanged();
	}

	@Override
	public void convert(ViewHolder viewHolder, DownDataEntity item) {
		// TODO Auto-generated method stub
		View layout = viewHolder.getView(R.id.id_list_app_layout);
		Button bth_state = viewHolder.getView(R.id.id_app_down);
		Button bth_action = viewHolder.getView(R.id.id_app_down2);
		ImageView icon_image = viewHolder.getView(R.id.id_app_icon);
		TextView app_name = viewHolder.getView(R.id.id_app_name);
		TextView app_nversion = viewHolder.getView(R.id.id_app_version);
		TextView app_size = viewHolder.getView(R.id.id_app_size);
		TextView progress_text = viewHolder.getView(R.id.id_app_down_prosstext);
		TextView netprogress_text = viewHolder
				.getView(R.id.id_app_down_netprosstext);
		ProgressBar progressBar = viewHolder.getView(R.id.id_app_down_bar);
		float mbszie = Tools.isInteger(item.getSize()) ? Integer.valueOf(item
				.getSize()) / mb : 0;
		bth_state.setEnabled(!TextUtils.isEmpty(item.getUrl())); 
		bth_action.setBackgroundResource(R.drawable.tab_bth_bg);
		if (isDowMangerTag) {
			int lastpross = progressBar.getProgress();
			int pross = item.getFileprogress();
			float netpross = (pross - lastpross) / mbszie;
			if (netpross < 0) {
				netpross = 0;
			}
			netprogress_text.setText(df.format(netpross) + "M/S");
			progress_text.setText(pross + "%");
			progressBar.setProgress(pross);
			bth_action.setVisibility(View.VISIBLE);
			bth_action.setTag(item);
			Tools.SetViewText(bth_action, res.getString(R.string.down_state_shan));
			HttpBitmap.getInstance(context).display(
					URLConstant.URL_Image + item.getIcon(), icon_image,
					R.drawable.market_right_tubiao_moren);
			Tools.SetViewText(app_name, item.getName());
			Tools.SetViewText(app_nversion, "V "+item.getVersion());
			Tools.SetViewText(app_size, Tools.isInteger(item.getSize()) ? Tools
					.convertFileSize(Integer.valueOf(item.getSize())) : "");
		} else {
			bth_action.setEnabled(!item.isFinshstatus());
			bth_state.setEnabled(item.isUpdatestate());
			progressBar.setProgress(0);
			progress_text.setVisibility(View.INVISIBLE);
			netprogress_text.setVisibility(View.INVISIBLE);
			ResolveInfo resolveInfo =DowdloadData.getResolveInfo(item.getPackagename());
			if (resolveInfo != null) {// 如果是本地安装应用
				String packageName = resolveInfo.activityInfo.packageName;
				PackageInfo pkg = Tools.getPackageInfo(packageName);
				if (pkg != null) {
					Tools.SetViewText(app_nversion,"V "+pkg.versionName);
				}
				try {
					float memsize = item.getMemorysize() / (float) 1024;
					if (memsize > 0) {
						app_size.setText(res.getString(R.string.default_title6)
								+ df.format(memsize) + "MB");
					} else {
						app_size.setText(res.getString(R.string.default_title7));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				app_name.setText(resolveInfo.loadLabel(pkgManager));
				Drawable drawable=resolveInfo
						.loadIcon(pkgManager);
				if (drawable!=null&&drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					Bitmap icon = bitmapDrawable.getBitmap();
					icon_image.setImageBitmap(icon);
				}
				if (!resolveInfo.system) {// 如果安装的不是系统应用
					item.setPackagename(packageName);
					bth_action.setVisibility(View.VISIBLE);
					bth_action.setTag(item);
					Tools.SetViewText(bth_action, res.getString(R.string.down_state_xie));
					bth_action.setBackgroundResource(R.drawable.uninstall_bg);
				}else {
					bth_action.setBackgroundResource(R.drawable.tab_bth_bg);
				}
			}
		}
		SetBthTextState(bth_state, item);
		bth_action.setOnClickListener(this);
		bth_state.setOnClickListener(this);
		layout.setTag(item);
		layout.setOnClickListener(this);
		layout.setOnTouchListener(new ViewDownOnTouch());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_app_down:
			Log.d("id_app_down2", "id_app_down2");
			SetBthService(v);
			break;
		case R.id.id_app_down2:
			DownDataEntity downDataEntity = (DownDataEntity) v.getTag();
			if (isDowMangerTag) {
				DowloadDialog.getInstance().deleteDialog(downDataEntity.getUrl());
			} else {
				DowloadDialog.getInstance().unInstallDialog(
						downDataEntity.getPackagename());
			}
			break;
		case R.id.id_list_app_layout:
			DownDataEntity downDataEntity2 = (DownDataEntity) v.getTag();
			if (!downDataEntity2.isUpdatestate()&&!downDataEntity2.isUrlstatus()) {
				if (!TextUtils.isEmpty(downDataEntity2.getPackagename())) {
					DowloadDialog.getInstance().OpenCurrentDialog(
							downDataEntity2.getPackagename());
				}
				return;
			}
			if (!TextUtils.isEmpty(downDataEntity2.getUrl())) {
				Intent intent = new Intent(DowloadDialog.getInstance()
						.getActivity(), AppDetailsActivity.class);
				intent.putExtra(Contentdowaload, downDataEntity2.getUrl());
				DowloadDialog.getInstance().getActivity().startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	public void SetBthTextState(Button bth_state, DownDataEntity downDataEntity) {
		bth_state.setTag(downDataEntity);
		if (downDataEntity.isUrlstatus()){
			setProgressText(downDataEntity.getDowstatus(), bth_state);
		} else if (downDataEntity.isFinshstatus()) {
			bth_state.setText(res.getString(R.string.down_state_an));
		} else {
			if(isDowMangerTag) {
				bth_state.setText(downDataEntity.isUpdatestate()?res
						.getString(R.string.down_state_update):res
						.getString(R.string.down_state_xia));
			}else {
				bth_state.setText(res
						.getString(R.string.down_state_update));
			}
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
		else if (downDataEntity.isFinshstatus()) {
			DowloadDialog.getInstance().InstallDialog(
					downDataEntity.getSdfilepath());
		} 
		else if(downDataEntity.isInstallstatus()&&!downDataEntity.isUpdatestate()){
			DowloadDialog.getInstance().OpenCurrentDialog(
					downDataEntity.getPackagename());
		}
		else	{
			if (isDowMangerTag||(!isDowMangerTag&&downDataEntity.isUpdatestate())) {
				intent.putExtra(Actiondowaload, Startdowload);
				intent.putExtra(Contentdowaload, downDataEntity);
				context.startService(intent);
			}
		}
	}

	public synchronized void setProgressText(int progress, Button bth_state) {
		// TODO Auto-generated method stub
		if (progress == Pausestatus) {
			bth_state.setText(res.getString(R.string.down_state_zan));
		} else if (progress == Errordowaload) {
			bth_state.setText(res.getString(R.string.down_state_isstart));
		} else if (progress == Waitstatus) {
			bth_state.setText(res.getString(R.string.down_state_wait));
		} else if (progress == Readystatus) {
			bth_state.setText(res.getString(R.string.down_state_ready));
		} else if (progress == Startstatus) {
			bth_state.setText(res.getString(R.string.down_state_start));
		}
	}

}
