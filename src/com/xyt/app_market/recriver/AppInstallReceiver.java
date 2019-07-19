package com.xyt.app_market.recriver;
import java.io.File;

import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadAction;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.interfacs.DowloadContentValue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
public class AppInstallReceiver extends BroadcastReceiver implements DowloadContentValue{
	public static String TAG=AppInstallReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {//安装
            String packageName = intent.getData().getSchemeSpecificPart();
            if (!TextUtils.isEmpty(packageName)) {
                MyApp.MLog(TAG, "安装");
                chanageAPP(packageName);
			} 
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {//卸载
            String packageName = intent.getData().getSchemeSpecificPart();
            if (!TextUtils.isEmpty(packageName)) {
                MyApp.MLog(TAG, "卸载");
             	chanageAPP(packageName);}
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {//更新
            String packageName = intent.getData().getSchemeSpecificPart();
            if (!TextUtils.isEmpty(packageName)) {
            	MyApp.MLog(TAG, "更新");
            	chanageAPP(packageName);}
        	}
    }
    private void chanageAPP(String packageName){
    	  DownDataEntity delectdataEntity=   DowdloadData.deletDownDataEntity(packageName);
          if (delectdataEntity!=null) {
        	  	long id = MyApp.getApp().downSqlHelper.deleSql(delectdataEntity.getUrl());
            	new File(Constants.DOWN_PATH,delectdataEntity.getSdfilepath()).delete();
			}
          APPEntity appEntity=DowdloadData.NameAPPEntity(packageName);
          if (appEntity!=null) {
          	MyApp.MLog(TAG, "InitHttpAPPEntity");
            	DowdloadData.InitHttpAPPEntity(appEntity.getFilepath());
          }
		DataConstant.nativeEntity.getAppMap().remove(packageName);
		DataConstant.nativeEntity.getSystemMap().remove(packageName);
		DowdloadData.SyncAllData(TAG);
    }
}