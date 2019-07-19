package com.xyt.app_market.dowload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants;
import com.xyt.app_market.contants.Constants.Action_Constant;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.Intent_Constant;
import com.xyt.app_market.contants.Constants.URLConstant;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.utitl.HttpUtitl;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.app.PendingIntent.OnFinished;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class DowloadService extends Service implements DowloadContentValue {
	public DownloadFile downloadFile = null;
	public static String TAG = DowloadService.class.getSimpleName();
	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override
	 * 
	 * @Deprecated public void onStart(Intent intent, int startId) { // TODO
	 * Auto-generated method stub super.onStart(intent, startId); }
	 */

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (intent != null) {
			int code = intent.getIntExtra(Actiondowaload, Errordowaload);
			if (Constants.netType.equals(Constants.NetType.NONET)) {
				return super.onStartCommand(intent, flags, startId);
			}
			switch (code) {
			case Startdowload:
				try {
					MyApp.MLog(TAG, "Startdowload");
					DownDataEntity downDataEntity = (DownDataEntity) intent
							.getSerializableExtra(Contentdowaload);
					addTask(downDataEntity);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				break;
			case Pausedowload:
				String pasueurl = intent.getStringExtra(Contentdowaload);
				if (!TextUtils.isEmpty(pasueurl)) {
					Pausedowload(pasueurl);
				}
				break;
			case Stopdowaload:
				String stopurl = intent.getStringExtra(Contentdowaload);
				if (!TextUtils.isEmpty(stopurl)) {
					Stopdowaload(stopurl);
				}
				break;
			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MyApp.MLog(TAG, "onDestroy");
		for (DownDataEntity mdowndataEntity : DataConstant.InDownloadThreep
				.values()) {
			StopStask(mdowndataEntity.getUrl());
			mdowndataEntity.setDowstatus(Pausestatus);
		}
		super.onDestroy();
	}

	/**
	 * @param position
	 *            加入任务队列
	 */
	public void addTask(DownDataEntity downDataEntity) {
		if (DataConstant.InDownloadThreep.containsKey(downDataEntity.getUrl())
				&& downDataEntity.getDowstatus() != Waitstatus) {
			/*
			 * if (!downDataEntity.isUrlstatus()) {
			 * Startdowload(downdownDataEntity.getUrl()); } else {
			 * MyApp.MLog(TAG, "任务已在列表"); }
			 */
			MyApp.MLog(TAG, "任务已在列表");
			return; // 任务已经在队列
		}
		DataConstant.DownloadThreep
				.put(downDataEntity.getUrl(), downDataEntity);
		DataConstant.InDownloadThreep.put(downDataEntity.getUrl(),
				downDataEntity);
		if (downloadFile != null) {
			downDataEntity.setUrlstatus(true);
			downDataEntity.setDowstatus(Waitstatus);
			DowdloadData.SyncAllData(TAG);
		} else {
			MyApp.MLog(TAG, "addTask" + downDataEntity.getUrl());
			Startdowload(downDataEntity.getUrl());
		}
	}

	/**
	 * 开始任务
	 * 
	 * @param position
	 */
	public void Startdowload(String url) {
		final DownDataEntity downDataEntity = DataConstant.DownloadThreep
				.get(url);
		/*
		 * Intent intent = new Intent(IntentConstant.NetDataupdate);
		 * sendBroadcast(intent);
		 */
		// 构建存储文件路径
		if (TextUtils.isEmpty(downDataEntity.getUrl())) {
			MyApp.MLog(TAG, "getUr为空");
			return;
		}
		String urlpath = URLConstant.URL_APP + downDataEntity.getUrl();
		String filepath = downDataEntity.getSdfilepath();
		if (TextUtils.isEmpty(filepath)) {
			filepath = downDataEntity.getName() + ".apk";
			downDataEntity.setSdfilepath(filepath);
		}
		File file = new File(Constants.DOWN_PATH, filepath);
		if (file.exists()
				&& file.length() == Long.parseLong(downDataEntity.getSize())) {
			MyApp.MLog(TAG, "已经下载" + file.getAbsolutePath());
			file.delete();
			/* return; */
		}
		downDataEntity.setDowstatus(Readystatus);
		downDataEntity.setUrlstatus(true);
		downDataEntity.setFinshstatus(false);
		boolean insettag = TaskInsetSql(downDataEntity);// 插入数据库
		DowdloadData.SyncAllData(TAG);
		MyApp.MLog(TAG, "insettag" + insettag);
		downloadFile = new DownloadFile();
		downloadFile.startDownloadFileByUrl(urlpath, file.getAbsolutePath(),
				new AjaxCallBack<File>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						MyApp.MLog(TAG, "开始任务");
						HttpUtitl.AddDownNum(downDataEntity.getPackagename());
						downDataEntity.setDowstatus(Startstatus);
						DowdloadData.SyncAllData(TAG);
						super.onStart();
					}

					@Override
					public void onLoading(long count, long current) {
						// TODO Auto-generated method stub
						int p = (int) ((current * 100) / count);
						downDataEntity.setUrlstatus(true);
						downDataEntity.setFinshstatus(false);
						downDataEntity.setFileprogress(p);
						MyApp.MLog(TAG, "下载进度" + p);
						MyApp.getApp().downSqlHelper.updateSql(downDataEntity);
						DowdloadData.SyncAllData(TAG);
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(File file) {
						// TODO Auto-generated method stub
						MyApp.MLog(TAG, "下载成功");
						downDataEntity.setUrlstatus(false);
						downDataEntity.setFinshstatus(true);
						downDataEntity.setDowstatus(Pausestatus);
						DataConstant.InDownloadThreep.remove(downDataEntity);
						MyApp.getApp().downSqlHelper.updateSql(downDataEntity);
						boolean stoptag = StopStask(downDataEntity.getUrl());
						OpenwaitTask();
						DowloadDialog.getInstance().InstallDialog(
								downDataEntity.getSdfilepath());
						super.onSuccess(file);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						MyApp.MLog(TAG, "下载失败");
						downDataEntity.setDowstatus(Errordowaload);
						MyApp.getApp().downSqlHelper.updateSql(downDataEntity);
						boolean stoptag = StopStask(downDataEntity.getUrl());
						OpenwaitTask();
						super.onFailure(t, strMsg);
					}
				});
	}

	/**
	 * 暂停任务
	 * 
	 * @param position
	 */
	public void Pausedowload(String url) {
		final DownDataEntity downDataEntity = DataConstant.DownloadThreep
				.get(url);
		if (!DataConstant.InDownloadThreep.containsKey(downDataEntity.getUrl())) {
			return; // 任务不在在队列
		}
		downDataEntity.setDowstatus(Pausestatus);
		if (downDataEntity.getDowstatus() == Waitstatus) {
			DataConstant.InDownloadThreep.remove(downDataEntity.getUrl());
			DowdloadData.SyncAllData(TAG);
		} else {
			StopStask(downDataEntity.getUrl());
		}
		MyApp.getApp().downSqlHelper.updateSql(downDataEntity);
		/*
		 * Intent intent = new Intent(IntentConstant.NetDataupdate);
		 * sendBroadcast(intent);
		 */
		OpenwaitTask();
	}

	/**
	 * 取消任务
	 * 
	 * @param position
	 */
	public void Stopdowaload(String url) {
		final DownDataEntity downDataEntity = DataConstant.DownloadThreep
				.get(url);
		if (downDataEntity == null) {
			return;
		}
		String filepath = downDataEntity.getSdfilepath();
		if (!TextUtils.isEmpty(filepath)) {
			MyApp.MLog(TAG, "deleteConstans" + filepath);
			new File(Constants.DOWN_PATH, filepath).delete();
		} 
		DataConstant.DownloadThreep.remove(url);
		long id = MyApp.getApp().downSqlHelper.deleSql(url);
		DowdloadData.InitHttpAPPEntity(url);
		StopStask(downDataEntity.getUrl());
		// OpenwaitTask();
	}

	public boolean TaskInsetSql(DownDataEntity downDataEntity) {
		MyApp.MLog(TAG, "TaskInsetSql");
		// 将这个对象插入到数据库中
		if (MyApp.getApp().downSqlHelper.fistInset(downDataEntity)) {
			MyApp.MLog(TAG, "TaskInsetSql2");
			return true;
		}
		try {
			long cout = MyApp.getApp().downSqlHelper.QuerySql(downDataEntity
					.getUrl());
			if (cout == 0) {
				long id = MyApp.getApp().downSqlHelper
						.insertSql(downDataEntity);
				if (id > 1) {
					return true;
				}
			}
			/*
			 * List<DownDataEntity> SQLApplist =
			 * MyApp.downSqlHelper.finalDb
			 * .findAllByWhere(DownDataEntity.class,
			 * downDataEntity.getUrl()+" "); if (SQLApplist == null ||
			 * SQLApplist.size() == 0) { // 将这个对象插入到数据库中 MyApp.MLog(TAG,
			 * "TaskInsetSql3"); long id = MyApp.downSqlHelper
			 * .insertSql(downDataEntity); if (id > 1) { return true; } } else {
			 * for (int i = 0; i < DataConstant.SQLApplist.size(); i++) { if
			 * (DataConstant.SQLApplist.get(i).getUrl()
			 * .equals(downDataEntity.getUrl())) { // 如果数据库中有此记录 return false; }
			 * else { // 将这个对象插入到数据库中 MyApp.MLog(TAG, "TaskInsetSql4"); long id
			 * = MyApp.downSqlHelper .insertSql(downDataEntity); if (id
			 * > 1) { return true; } } } }
			 */
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * @return获取下个任务url
	 */
	public String ObtainNetURL() {
		if (DataConstant.InDownloadThreep.size() == 0) {
			return null;
		}
		List<String> keyList = new ArrayList<String>();
		Set<String> keySet = DataConstant.InDownloadThreep.keySet();
		for (String urlkey : keySet) {
			keyList.add(urlkey);
		}
		return keyList.get(keyList.size() - 1);
	}

	/**
	 * @param URL
	 * @return查询URL对应任务
	 */
	public DownDataEntity ObtainNetTask(String URL) {
		for (DownDataEntity mdowndownDataEntity : DataConstant.InDownloadThreep
				.values()) {
			if (URL.equals(mdowndownDataEntity.getUrl())) {
				return mdowndownDataEntity;
			}
		}
		return null;
	}

	/**
	 * 开启等待任务
	 */
	public void OpenwaitTask() {
		if (DataConstant.InDownloadThreep.size() == 0) {
			return;
		}
		if (downloadFile != null) {
			return;
		}
		String OpenURL = ObtainNetURL();
		if (TextUtils.isEmpty(OpenURL)) {
			return;
		}
		DownDataEntity waitdowndownDataEntity = ObtainNetTask(OpenURL);
		if (waitdowndownDataEntity != null) {
			Startdowload(waitdowndownDataEntity.getUrl());
		}
	}

	/**
	 * @param URL
	 * @return停止任务
	 */
	public boolean StopStask(String URL) {
		String urlString =URLConstant.URL_APP +URL;
		DataConstant.InDownloadThreep.remove(URL);
		DowdloadData.SyncAllData(TAG);
		if (downloadFile != null&&!TextUtils.isEmpty(URL)&&urlString.equals(downloadFile.URL)) {
			MyApp.MLog(TAG, "StopStask");
			downloadFile.stopDownload();
			downloadFile = null;
			return true;
		}
		return false;
	}
}
