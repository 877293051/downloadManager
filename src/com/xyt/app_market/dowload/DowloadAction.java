package com.xyt.app_market.dowload;

import java.io.File;

import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.utitl.Tools;

public class DowloadAction implements DowloadContentValue{
	public static String TAG=DowloadAction.class.getSimpleName();
	public static  final int yes_insatll = 0, yes_use = 1, yes_uninstall = 2,
			yes_delete = 3;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case yes_insatll:
				installAPP((String) msg.obj);
				DowdloadData.SyncAllData(TAG);
				break;
			case yes_uninstall:
				unintsallAPP((String) msg.obj);
				DowdloadData.SyncAllData(TAG);
				break;
			case yes_use:
				openAPP((String) msg.obj); 
				break;
			case yes_delete:
				String url=(String) msg.obj;
				if (!TextUtils.isEmpty(url)) {
					Intent intent =new Intent(MyApp.getApp(),DowloadService.class);
					intent.putExtra(Actiondowaload, Stopdowaload);
					intent.putExtra(Contentdowaload, url);
					MyApp.getApp().startService(intent);
				}
				break;
			default:
				break;
			}
		};
	};
	public void installAPP(String sdpath){
		MyApp.MLog(TAG, new File(Constants.DOWN_PATH,sdpath).getAbsolutePath());
		Tools.installApk(MyApp.getApp(), new File(Constants.DOWN_PATH,sdpath));
	}
	public void openAPP(String packageName){
		Tools.RunApp(MyApp.getApp(), packageName);
	}
	public void unintsallAPP(String packageName){
		PackageManager pkgManager =  MyApp.getApp().getPackageManager(); // 需要system权限
		pkgManager.deletePackage(packageName,
				new IPackageDeleteObserver() {

					@Override
					public IBinder asBinder() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void packageDeleted(String arg0, int arg1)
							throws RemoteException {
						// TODO Auto-generated method stub
						MyApp.mToast(arg0 + "--" + arg1);
					}
				}, 0);
	}
}
