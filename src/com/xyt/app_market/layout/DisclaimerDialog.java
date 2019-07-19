package com.xyt.app_market.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.ShareKey;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.utitl.Tools;

public class DisclaimerDialog {
	public static DisclaimerDialog disclaimerDialog;
	private Activity activity;
	public DisclaimerDialog(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
		Dialog();
	}
	public static DisclaimerDialog getInstance(Activity activity) {
		if (disclaimerDialog == null) {
			synchronized (DisclaimerDialog.class) {
				if (disclaimerDialog == null) {
					disclaimerDialog = new DisclaimerDialog(activity);
				}
			}
		}
		return disclaimerDialog;
	}
	
	public void Dialog() {
		final TextView textViewSelect;
		TextView textViewOK, textViewNO;
		View view = LayoutInflater.from(activity)
				.inflate(R.layout.disclaimer_layoyt, null);
		final Dialog dialog = new android.app.Dialog(activity, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
		textViewSelect = (TextView) view.findViewById(R.id.dis_text_select);
		textViewOK = (TextView) view.findViewById(R.id.dis_text_yes);
		textViewNO = (TextView) view.findViewById(R.id.dis_text_no);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) MyApp.getApp().res.getDimension(R.dimen.dis_dialog_with);
		lp.height =  (int) MyApp.getApp().res.getDimension(R.dimen.dis_dialog_hight);
		dialog.getWindow().setAttributes(lp);
		dialog.setCancelable(false);
		textViewSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textViewSelect.setSelected(!textViewSelect.isSelected());
			}
		});
		textViewOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (textViewSelect.isSelected()) {
					MyApp.getApp().preferences.edit().putBoolean(ShareKey.Dis_select, true).commit();
				}
				dialog.dismiss();
			}
		});
		textViewNO.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss(); // 关闭dialog
				System.exit(0);
			}
		});
	}
}
