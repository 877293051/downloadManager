package com.xyt.app_market.dowload;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.xyt.app.silentupgrade.UpgradeService;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.Action_Constant;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.SQL_Constant;
import com.xyt.app_market.contants.Constants.TypeConstant;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.entity.HttpListEntity;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.utitl.Tools;
import com.xyt.app_market.utitl.VersionManagementUtil;

/**
 * @author tjy
 * @d2016-11-29DowdloadData
 */
public class DowdloadData implements DowloadContentValue {
	public static String TAG = DowdloadData.class.getSimpleName();

	public static synchronized void SyncAllData(String Tag) {
		MyApp.MLog(TAG, "SyncAllData" + Tag);
		DownloadThreepMatchhttplist();
		SyncNativeApp();
		MyApp.targetObservable.setMessage(Action_Constant.nativieApp_Update);
	}

	/**
	 * 应用管理核对本地app;
	 */
	public static void SyncNativeApp() {
		List<ResolveInfo> resolveInfos = Tools.loadApps(MyApp.getApp());
		for (int i = 0; i < resolveInfos.size(); i++) {
			ResolveInfo resolveInfo = resolveInfos.get(i);
			String packname = resolveInfo.activityInfo.packageName;
			DownDataEntity maDataEntity = new DownDataEntity();
			DownDataEntity machAppEntity = nativeMatchNetApp(packname);
			if (machAppEntity != null) {
				maDataEntity = machAppEntity;
			} else {
				maDataEntity.setUpdatestate(false);
			}
			maDataEntity.setPackagename(packname);
			maDataEntity.setNativeAppTag(true);
			maDataEntity.setInstallstatus(true);
			if (resolveInfo.system) {
				DataConstant.nativeEntity.getSystemMap().put(packname,
						maDataEntity);
			} else {
				DataConstant.nativeEntity.getAppMap().put(packname,
						maDataEntity);
			}
		}
		/*
		 * MyApp.MLog(TAG, "nativeAppCheck"+DataConstant.nativeEntity.toString()
		 * ); MyApp.MLog(TAG,
		 * "getSystemMap"+DataConstant.nativeEntity.getSystemMap().size());
		 */
	}

	public static ResolveInfo getResolveInfo(String packageName) {
		List<ResolveInfo> resolveInfos = Tools.loadApps(MyApp.getApp());
		for (int i = 0; i < resolveInfos.size(); i++) {
			ResolveInfo resolveInfo = resolveInfos.get(i);
			String packname = resolveInfo.activityInfo.packageName;
			if (packname.equals(packageName)) {
				return resolveInfo;
			}
		}
		return null;
	}

	public static boolean isDownstate() {
		if (DataConstant.InDownloadThreep.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取数据库下载任务
	 * 
	 * @param downSqlHelper
	 */
	public static void getSQLDownList(DownSqlHelper downSqlHelper) {
		if (!downSqlHelper.isTableExists(SQL_Constant.tablename)) {
			return;
		}
		DataConstant.SQLApplist = downSqlHelper.finalDb
				.findAll(DownDataEntity.class);
		if (DataConstant.SQLApplist != null
				|| DataConstant.SQLApplist.size() > 0) {
			for (int i = 0; i < DataConstant.SQLApplist.size(); i++) {
				DownDataEntity downDataEntity = DataConstant.SQLApplist.get(i);
				downDataEntity.setDowstatus(Pausestatus);
				if (downDataEntity.isUrlstatus()
						|| downDataEntity.isFinshstatus()) {
					DataConstant.DownloadThreep.put(downDataEntity.getUrl(),
							downDataEntity);
				}
			}
		}
		MyApp.MLog(TAG,
				"getSQLDownList" + DataConstant.DownloadThreep.toString());
		SyncNativeApp();
		MyApp.targetObservable.setMessage(Action_Constant.nativieApp_Update);
	}

	/**
	 * 解析HTTPTypejson数据
	 * 
	 * @param clearTag
	 * 
	 * @param jsonObject
	 * @param httpListEntity
	 */
	public static void TypeJsonHelper(boolean clearTag, JSONObject jsonObject,
			HttpListEntity httpListEntity) {
		try {
			String postion = jsonObject.getString("position");
			String type = jsonObject.getString("type");
			JSONArray jsonArray = jsonObject.getJSONArray("datas");
			httpListEntity.setPostion(postion);
			if (clearTag) {
				if (type.equals(TypeConstant.Map_Type)) {
					httpListEntity.getMapLists().clear();
				} else if (type.equals(TypeConstant.Music_Type)) {
					httpListEntity.getMusicLists().clear();
				} else if (type.equals(TypeConstant.Other_Type)) {
					httpListEntity.getOtherLists().clear();
				} else if (type.equals(TypeConstant.HIDE_Type)) {
					UpgradeService.hadiLists.clear();
				}
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject rJsonObject = (JSONObject) jsonArray.opt(i);
				APPEntity aPPEntity = MyApp.gson.fromJson(
						rJsonObject.toString(), APPEntity.class);
				if (type.equals(TypeConstant.Map_Type)) {
					httpListEntity.getMapLists().add(SetAPPEntity(aPPEntity));
				} else if (type.equals(TypeConstant.Music_Type)) {
					httpListEntity.getMusicLists().add(SetAPPEntity(aPPEntity));
				} else if (type.equals(TypeConstant.Other_Type)) {
					httpListEntity.getOtherLists().add(SetAPPEntity(aPPEntity));
				}	else if (type.equals(TypeConstant.HIDE_Type)) {
					UpgradeService.hadiLists.add(aPPEntity);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			new RuntimeException(TAG + "HomeJsonHelperException");
		}
	}

	/**
	 * 解析主页json数据
	 * 
	 * @param jsonObject
	 * @param httpListEntity
	 */
	public static void HomeJsonHelper(JSONObject jsonObject,
			HttpListEntity httpListEntity) {
		try {
			if (isDownstate()) {
				return;
			}
			DataConstant.httpListEntity.getRecomLists().clear();
			DataConstant.httpListEntity.getDownLists().clear();
			DataConstant.httpListEntity.getScrollLists().clear();
			DataConstant.httpListEntity.getNewsLists().clear();
			String postion = jsonObject.getString("position");
			JSONArray recomjsonArray = jsonObject
					.getJSONArray("recommend_apps");
			JSONArray downjsonArray = jsonObject.getJSONArray("download_apps");
			JSONArray scolljsonArray = jsonObject.getJSONArray("scroll_apps");
			JSONArray newsjsonArray = jsonObject.getJSONArray("newest_apps");
			httpListEntity.setPostion(postion);
			for (int i = recomjsonArray.length()-1; i >=0; i--) {
				JSONObject rJsonObject = (JSONObject) recomjsonArray.opt(i);
				APPEntity aPPEntity = MyApp.gson.fromJson(
						rJsonObject.toString(), APPEntity.class);
				httpListEntity.getRecomLists().add(SetAPPEntity(aPPEntity));
			}
			for (int i = downjsonArray.length()-1; i >=0; i--) {
				JSONObject dJsonObject = (JSONObject) downjsonArray.opt(i);
				APPEntity aPPEntity = MyApp.gson.fromJson(
						dJsonObject.toString(), APPEntity.class);
				httpListEntity.getDownLists().add(SetAPPEntity(aPPEntity));
			}
			for (int i = scolljsonArray.length()-1; i >=0; i--) {
				JSONObject sJsonObject = (JSONObject) scolljsonArray.opt(i);
				APPEntity aPPEntity = MyApp.gson.fromJson(
						sJsonObject.toString(), APPEntity.class);
				httpListEntity.getScrollLists().add(SetAPPEntity(aPPEntity));
			}
			for (int i = newsjsonArray.length()-1; i >=0; i--) {
				JSONObject nJsonObject = (JSONObject) newsjsonArray.opt(i);
				APPEntity aPPEntity = MyApp.gson.fromJson(
						nJsonObject.toString(), APPEntity.class);
				httpListEntity.getNewsLists().add(SetAPPEntity(aPPEntity));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			new RuntimeException(TAG + "HomeJsonHelperException");
		}
	}

	/**
	 * 匹配APPEntity
	 * 
	 * @param aPPEntity
	 * @return
	 */
	public static APPEntity SetAPPEntity(APPEntity aPPEntity) {
		DownDataEntity downDataEntity = null;
		boolean setDowTag=false;
		if (DataConstant.DownloadThreep != null
				|| DataConstant.DownloadThreep.size() > 0) {
			for (DownDataEntity mdowndataEntity : DataConstant.DownloadThreep
					.values()) {
				if (mdowndataEntity.getUrl().equals(aPPEntity.getFilepath())) {
					setDowTag=true;
					aPPEntity.setDataEntity(SetDownData(aPPEntity,
							mdowndataEntity));
				}
			}
		}
		if (!setDowTag) {
			DowdloadData.SetDownData(aPPEntity,
					null);
		}
		return aPPEntity;
	}

	/**
	 * @param packageName
	 * @return根据包名删除DownDataEntit
	 */
	public static DownDataEntity deletDownDataEntity(String packageName) {
		DownDataEntity delectdataEntity = null;
		for (DownDataEntity mdowndataEntity : DataConstant.DownloadThreep
				.values()) {
			if (mdowndataEntity.getPackagename().endsWith(packageName)) {
				delectdataEntity = mdowndataEntity;
			}
		}
		if (delectdataEntity != null) {
			DataConstant.DownloadThreep.remove(delectdataEntity.getUrl());
		}
		return delectdataEntity;

	}

	/**
	 * 设置APPEntity　DownDataEntity参数
	 * 
	 * @param aPPEntity
	 * @param downDataEntity
	 * @return
	 */
	public static DownDataEntity SetDownData(APPEntity aPPEntity,
			DownDataEntity downDataEntity) {
		DownDataEntity mdownDataEntity = null;
		if (downDataEntity == null) {
			mdownDataEntity = aPPEntity.getDataEntity();
		} else {
			mdownDataEntity = downDataEntity;
		}
		mdownDataEntity.setUrl(aPPEntity.getFilepath());
		mdownDataEntity.setName(aPPEntity.getName());
		mdownDataEntity.setPackagename(aPPEntity.getPackagename());
		mdownDataEntity.setIcon(aPPEntity.getIcon());
		mdownDataEntity.setSize(aPPEntity.getSize());
		mdownDataEntity.setType(aPPEntity.getType());
		mdownDataEntity.setVersion(aPPEntity.getVersion());
		mdownDataEntity.setVersioncode(aPPEntity.getFromtype());
		return mdownDataEntity;
	}

	public static void InitHttpAPPEntity(String url) {
		for (int i = 0; i < DataConstant.httpListEntity.getDownLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getDownLists()
					.get(i);
			InitAPPEntity(appEntity, url);
		}
		for (int i = 0; i < DataConstant.httpListEntity.getRecomLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getRecomLists()
					.get(i);
			InitAPPEntity(appEntity, url);
		}
		for (int i = 0; i < DataConstant.httpListEntity.getNewsLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getNewsLists()
					.get(i);
			InitAPPEntity(appEntity, url);
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMapLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMapLists()
					.get(i);
			InitAPPEntity(appEntity, url);
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMusicLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMusicLists()
					.get(i);
			InitAPPEntity(appEntity, url);
		}
		for (int i = 0; i < DataConstant.httpListEntity.getOtherLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getOtherLists()
					.get(i);
			InitAPPEntity(appEntity, url);
		}
	}

	public static void InitAPPEntity(APPEntity appEntity, String url) {
		DownDataEntity downDataEntity = appEntity.getDataEntity();
		if (url.equals(downDataEntity.getUrl())) {
			appEntity
					.setDataEntity(SetDownData(appEntity, new DownDataEntity()));
		}
	}

	public static APPEntity URLmathAPPEntity(String url) {
		for (int i = 0; i < DataConstant.httpListEntity.getScrollLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getScrollLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getDownLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getDownLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getRecomLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getRecomLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getNewsLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getNewsLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMapLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMapLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMusicLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMusicLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getOtherLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getOtherLists()
					.get(i);
			if (appEntity.getFilepath().equals(url)) {
				return appEntity;
			}
		}
		return null;
	}

	public static APPEntity NameAPPEntity(String packname) {
		for (int i = 0; i < DataConstant.httpListEntity.getDownLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getDownLists()
					.get(i);
			if (appEntity.getPackagename().equals(packname)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getRecomLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getRecomLists()
					.get(i);
			if (appEntity.getPackagename().equals(packname)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getNewsLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getNewsLists()
					.get(i);
			if (appEntity.getPackagename().equals(packname)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMapLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMapLists()
					.get(i);
			if (appEntity.getPackagename().equals(packname)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMusicLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMusicLists()
					.get(i);
			if (appEntity.getPackagename().equals(packname)) {
				return appEntity;
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getOtherLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getOtherLists()
					.get(i);
			if (appEntity.getPackagename().equals(packname)) {
				return appEntity;
			}
		}
		return null;
	}

	/**
	 * @param resolveInfo
	 * @return本地应用核对服务器数据获取更新:
	 */
	public static DownDataEntity nativeMatchNetApp(String packname) {
		DownDataEntity machAppEntity = null;
		boolean getDataTag = true;
		for (int i = 0; i < DataConstant.httpListEntity.getDownLists().size(); i++) {
			APPEntity entity = DataConstant.httpListEntity.getDownLists()
					.get(i);
			if (entity.getPackagename().endsWith(packname)) {
				entity.getDataEntity().setInstallstatus(true);
				SetAPPUpdate(entity.getDataEntity(), packname,
						entity.getVersion());
				if (getDataTag) {
					machAppEntity = entity.getDataEntity();
					getDataTag = false;
				}
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getRecomLists().size(); i++) {
			APPEntity entity = DataConstant.httpListEntity.getRecomLists().get(
					i);
			if (entity.getPackagename().endsWith(packname)) {
				entity.getDataEntity().setInstallstatus(true);
				SetAPPUpdate(entity.getDataEntity(), packname,
						entity.getVersion());
				if (getDataTag) {
					machAppEntity = entity.getDataEntity();
					getDataTag = false;
				}
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getNewsLists().size(); i++) {
			APPEntity entity = DataConstant.httpListEntity.getNewsLists()
					.get(i);
			if (entity.getPackagename().endsWith(packname)) {
				entity.getDataEntity().setInstallstatus(true);
				SetAPPUpdate(entity.getDataEntity(), packname,
						entity.getVersion());
				if (getDataTag) {
					machAppEntity = entity.getDataEntity();
					getDataTag = false;
				}
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMusicLists().size(); i++) {
			APPEntity entity = DataConstant.httpListEntity.getMusicLists().get(
					i);
			if (entity.getPackagename().endsWith(packname)) {
				entity.getDataEntity().setInstallstatus(true);
				SetAPPUpdate(entity.getDataEntity(), packname,
						entity.getVersion());
				if (getDataTag) {
					machAppEntity = entity.getDataEntity();
					getDataTag = false;
				}
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMapLists().size(); i++) {
			APPEntity entity = DataConstant.httpListEntity.getMapLists().get(i);
			if (entity.getPackagename().endsWith(packname)) {
				entity.getDataEntity().setInstallstatus(true);
				SetAPPUpdate(entity.getDataEntity(), packname,
						entity.getVersion());
				if (getDataTag) {
					machAppEntity = entity.getDataEntity();
					getDataTag = false;
				}
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getOtherLists().size(); i++) {
			APPEntity entity = DataConstant.httpListEntity.getOtherLists().get(
					i);
			if (entity.getPackagename().endsWith(packname)) {
				entity.getDataEntity().setInstallstatus(true);
				SetAPPUpdate(entity.getDataEntity(), packname,
						entity.getVersion());
				if (getDataTag) {
					machAppEntity = entity.getDataEntity();
					getDataTag = false;
				}
			}
		}
		return machAppEntity;
	}
  
	public static void SetAPPUpdate(DownDataEntity dataEntity, String packname,
			String version) {
		PackageInfo pkg = Tools.getPackageInfo(packname);
/*		MyApp.MLog(TAG, "SetAPPUpdate" + dataEntity.getPackagename()
				+ "versionname=" + pkg.versionName+ "netcode="
				+ version);*/
		if (pkg != null && dataEntity != null) {
			if (Tools.isString(version) && Tools.isString(pkg.versionName)) {
				int ContrastVersion=VersionManagementUtil.VersionComparison(version, pkg.versionName);
				MyApp.MLog(TAG, "SetAPPUpdate" + dataEntity.getPackagename()
						+ "versionname=" + pkg.versionName+ "netcode="
						+ version+"ContrastVersion"+ContrastVersion);
			dataEntity.setUpdatestate(ContrastVersion == 1);
			}
		}
	}

	/**
	 * 下载列表DownloadThreep数据匹配对应APPEntity数据
	 * 
	 * @return
	 */
	public static void DownloadThreepMatchhttplist() {
		for (DownDataEntity mdownDataEntity : DataConstant.DownloadThreep
				.values()) {
			APPEntity appEntity = URLmathAPPEntity(mdownDataEntity.getUrl());
			if (appEntity != null) {
				DownSyncAPPEntity(mdownDataEntity);
			}
		}
	}

	/**
	 * 下载列表DownloadThreep数据同步APPEntity数据
	 * 
	 * @param mdownDataEntity
	 */
	public static void DownSyncAPPEntity(DownDataEntity mdownDataEntity) {
		for (int i = 0; i < DataConstant.httpListEntity.getDownLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getDownLists()
					.get(i);
			if (appEntity.getFilepath().equals(mdownDataEntity.getUrl())) {
				appEntity.setDataEntity(mdownDataEntity);
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getRecomLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getRecomLists()
					.get(i);
			if (appEntity.getFilepath().equals(mdownDataEntity.getUrl())) {
				appEntity.setDataEntity(mdownDataEntity);
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getNewsLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getNewsLists()
					.get(i);
			if (appEntity.getFilepath().equals(mdownDataEntity.getUrl())) {
				appEntity.setDataEntity(mdownDataEntity);
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMapLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMapLists()
					.get(i);
			if (appEntity.getFilepath().equals(mdownDataEntity.getUrl())) {
				appEntity.setDataEntity(mdownDataEntity);
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getMusicLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getMusicLists()
					.get(i);
			if (appEntity.getFilepath().equals(mdownDataEntity.getUrl())) {
				appEntity.setDataEntity(mdownDataEntity);
			}
		}
		for (int i = 0; i < DataConstant.httpListEntity.getOtherLists().size(); i++) {
			APPEntity appEntity = DataConstant.httpListEntity.getOtherLists()
					.get(i);
			if (appEntity.getFilepath().equals(mdownDataEntity.getUrl())) {
				appEntity.setDataEntity(mdownDataEntity);
			}
		}
	}
}
