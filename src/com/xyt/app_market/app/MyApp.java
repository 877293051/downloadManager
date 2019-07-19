package com.xyt.app_market.app;

import java.io.File;
import java.util.Observable;
import net.tsz.afinal.FinalHttp;

import com.fourtech.platform.XytPlatform;
import com.fourtech.platform.entry.EPlatform;
import com.google.gson.Gson;
import com.xyt.app_market.contants.Constants;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.dowload.DowloadService;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DownSqlHelper;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.recriver.NetReceiver;
import com.xyt.app_market.utitl.CrashHandler;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class MyApp extends Application implements DowloadContentValue{
	public static String TAG = MyApp.class.getSimpleName();
	private static MyApp myApp;
	private static boolean DebugTag = true, ToastTag = true;
	public static FinalHttp finalHttp;
	public static Gson gson;
	public static TargetObservable targetObservable;
	public DownSqlHelper downSqlHelper;
	public static ActivityManager activityManager;
	public static Resources res;
	public Intent downservice;
	public IntentFilter netreciver;
	public SharedPreferences preferences;
	public static EPlatform mPlatform = EPlatform.UNKNOWN;
	public static MyApp getApp() {
		return myApp;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mPlatform=XytPlatform.getPlatform();
		if (preferences == null) {
			preferences = getSharedPreferences(
					Constants.name,
					Context.MODE_PRIVATE);
		} 
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		File filefolder = new File(Constants.DOWN_PATH);
		if (!filefolder.isFile()) {
			filefolder.mkdirs();
		}
		res = getResources();
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		targetObservable = new TargetObservable();
		myApp = this;
		finalHttp = new FinalHttp();
//		MyImageloder.getInstance().setContext(this).setCache(new ImageloderCache().getCache(2));
		finalHttp.configTimeout(10000);
		gson = new Gson();
		downservice = new Intent();
		startDownSerivce();
		recrvier();
		downSqlHelper = DownSqlHelper.getInstance(this);
		DowdloadData.getSQLDownList(downSqlHelper);
//		startService(new Intent(this,UpgradeService.class));
	}
	public static Integer qmType(){
		if (MyApp.mPlatform.equals(EPlatform.QCOM4_B_T2)) {
			return 1;
		}
		return 0;
	}
	public static void MLog(String tag, String msg) {
		if (DebugTag)
			Log.d(tag, msg);
	}

	public static void mToast(String text) {
		if (ToastTag)
			Toast.makeText(myApp, text, Toast.LENGTH_SHORT).show();
	}

	public static void mToast(int id) {
		if (ToastTag)
			Toast.makeText(myApp, res.getString(id), Toast.LENGTH_SHORT).show();
	}

	public void recrvier() {
		netreciver = new IntentFilter();
		netreciver.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(new NetReceiver(), netreciver);
	}

	public void startDownSerivce() {
		downservice = new Intent(this, DowloadService.class);
		startService(downservice);
	}

	public void stopDownSerivce() {
		stopService(downservice);
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		DataConstant.DataDestroy();
		super.onTerminate();
	}

	// Observable是被观察者对象接口，实现该接口就是：目标（被观察者）的具体实现
	public static class TargetObservable extends Observable {
		// 要观察的数据：消息发生改变时，所有被添加的观察者都能收到通知
		private String message;

		public String getConent() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
			// 被观察者数据发生变化时，通过以下两行代码通知所有的观察者
			this.setChanged();
			this.notifyObservers(message);
		}
	}
}
