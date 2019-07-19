package com.xyt.app_market.dowload;

import java.io.File;

import com.xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.Action_Constant;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.utitl.Tools;
import com.xyt.app_market.view.MyProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class DowloadDialog  {
	public static String TAG=DowloadDialog.class.getSimpleName();
	private static DowloadDialog dowloadDialog;
	private  Activity activity;
	private DowloadAction dowloadAction;
	public MyProgressDialog progressDialog;
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		if (activity==null) {
			return;
		}
		this.activity = activity;
	}
	public static DowloadDialog getInstance() {
		if (dowloadDialog == null) {
			synchronized (DowloadDialog.class) {
				if (dowloadDialog == null) {
					dowloadDialog = new DowloadDialog();
				}
			}
		}
		return dowloadDialog;
	}
	public DowloadDialog() {
		// TODO Auto-generated constructor stub
		dowloadAction=new DowloadAction();
	}
	public void onCreateProgress(String title) {
		if (activity == null) {
			return;
		}
		if (progressDialog!=null) {
			CloseProgress();
		}
		progressDialog = progressdialog(activity, title);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		}, 10000); 
	}

	public void CloseProgress() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	public static MyProgressDialog progressdialog(Activity activity,String title){
		MyProgressDialog myProgressDialog =new MyProgressDialog(activity, title, R.anim.loading);
		myProgressDialog.show();
		return myProgressDialog;
	}
	public void deleteDialog(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Message message = new Message();
		message.obj = url;
		message.what = dowloadAction.yes_delete;
		Dialog(message);
	}

	public void unInstallDialog(String packageName) {
		if (!Tools.isSysttemApp(packageName)) {
			Message message = new Message();
			message.obj = packageName;
			message.what = dowloadAction.yes_uninstall;
			Dialog(message);
		}
	}

	/**
	 * @param insatllpath安装应用提示框
	 */
	public void InstallDialog(final String sdpath) {
		Message message = new Message();
		message.obj = sdpath;
		message.what = dowloadAction.yes_insatll;
		Dialog(message);
	}
	/**
	 * @param 应用应用提示框
	 */
	public void OpenCurrentDialog(final String usepath) {
		Message message = new Message();
		message.obj = usepath;
		message.what = dowloadAction.yes_use;
		Dialog(message);
	}
	public void Dialog(final Message message) {
		TextView textViewTilte, textViewOK, textViewNO;
		View view = LayoutInflater.from(activity)
				.inflate(R.layout.dialog, null);
		final Dialog dialog = new android.app.Dialog(activity, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
		textViewTilte = (TextView) view.findViewById(R.id.delete_title);
		textViewOK = (TextView) view.findViewById(R.id.delete_ok);
		textViewNO = (TextView) view.findViewById(R.id.delete_no);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = Tools.Changedp(380, activity);
		lp.height = Tools.Changedp(200, activity);
		dialog.getWindow().setAttributes(lp);
		dialog.setCancelable(false);
		SetTiTle(textViewTilte, message.what);
		textViewOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dowloadAction.handler.sendMessage(message);
				dialog.dismiss();
			}
		});
		textViewNO.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss(); // 关闭dialog
			}
		});
	}

	public void SetTiTle(TextView textView, int wat) {
		switch (wat) {
		case DowloadAction.yes_insatll:
			textView.setText(activity.getResources().getString(
					R.string.dialog_title));
			break;
		case DowloadAction. yes_uninstall:
			textView.setText(activity.getResources().getString(
					R.string.dialog_title3));
			break;
		case DowloadAction.yes_use:
			textView.setText(activity.getResources().getString(
					R.string.dialog_title2));
			break;
		case DowloadAction.yes_delete:
			textView.setText(activity.getResources().getString(
					R.string.dialog_title4));
			break;
		default:
			break;
		}
	}
	
}
