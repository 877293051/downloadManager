package com.xyt.app_market.recriver;

import com.xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetReceiver extends BroadcastReceiver {
	public static String TAG=NetReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
    	MyApp.MLog(TAG, "注册");
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isAvailable()) {
				// ///////////网络连接
				String name = netInfo.getTypeName();
				MyApp.getApp().startDownSerivce();
/*				MyApp.mToast(R.string.toast_nettitle2);*/
				if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					// ///WiFi网络
					Constants.netType = Constants.NetType.WIFI;
			    	MyApp.MLog(TAG, "WIFI");
				} else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
					// ///有线网络
					Constants.netType = Constants.NetType.FLOW;
			    	MyApp.MLog(TAG, "有限网络");
				} else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					// ///////3g网络
					Constants.netType = Constants.NetType.FLOW;
				   	MyApp.MLog(TAG, "3g网络");
				}
			} else {
				// //////网络断开
				Constants.netType = Constants.NetType.NONET;
				MyApp.getApp().stopDownSerivce();
				MyApp.mToast(R.string.toast_nettitle);
			   	MyApp.MLog(TAG, "网络断开");
			}
		}
	}
	
}
