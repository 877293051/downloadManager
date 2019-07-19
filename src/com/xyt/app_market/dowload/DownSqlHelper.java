package com.xyt.app_market.dowload;

import java.io.File;

import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.SQL_Constant;
import com.xyt.app_market.entity.DownDataEntity;

import net.tsz.afinal.FinalDb;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownSqlHelper {
	public static String TAG = DownSqlHelper.class.getSimpleName();
	private static DownSqlHelper downSqlHelper;
	private Context context;
	public FinalDb finalDb;
	public String dbpath;
	public SQLiteDatabase db;
	
	public static DownSqlHelper getInstance(Context context) {
		if (downSqlHelper == null) {
			synchronized (DownSqlHelper.class) {
				if (downSqlHelper == null) {
					downSqlHelper = new DownSqlHelper(context);
				}
			}
		}
		return downSqlHelper;
	}
	public DownSqlHelper(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
//		deleteDatabase(context);
		FinalDb.DaoConfig daoConfig = new FinalDb.DaoConfig();
		daoConfig.setContext(context);
		daoConfig.setDbName(SQL_Constant.dbname);
		daoConfig.setDebug(true);
		daoConfig.setDbVersion(1);
		finalDb = FinalDb.create(daoConfig);
		dbpath = context.getDatabasePath(SQL_Constant.dbname).getAbsolutePath();
		db = SQLiteDatabase.openDatabase(dbpath, null, 0);
	}

	/**
	 * 删除数据库
	 * 
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context) {
		new File("/data/data/" + context.getPackageName() + "/databases")
				.delete();
		return context.deleteDatabase(SQL_Constant.dbname);
	}

	public boolean fistInset(DownDataEntity downDataEntity) {
		if (!isTableExists(SQL_Constant.tablename)) {
			finalDb.save(downDataEntity);
			return true;
		}
		return false;
	}

	public boolean isTableExists(String tableName) {
		Cursor cursor = db.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	public synchronized long insertSql(DownDataEntity downDataEntity) {
		long id = db.insert(SQL_Constant.tablename, null,
				SetContentValues(downDataEntity));
		MyApp.MLog(TAG, "insertSql" + id);
		return id;
	}

	public synchronized long updateSql(DownDataEntity downDataEntity) {
		long id = db.update(SQL_Constant.tablename,
				SetContentValues(downDataEntity), SQL_Constant.url_key + "= ?",
				new String[] { downDataEntity.getUrl() });
		MyApp.MLog(TAG, "updateSql" + id);
		return id;
	} 

	public synchronized long QuerySql(String url) {
		Cursor c = db.rawQuery("SELECT * FROM " + SQL_Constant.tablename
				+ " WHERE " + SQL_Constant.url_key + "= ? ",
				new String[] { url });
		long cout = c.getCount();
		c.close();
		MyApp.MLog(TAG, "QuerySql" + cout);
		return cout;
	}

	public synchronized long deleSql(String url) {
		long id = db.delete(SQL_Constant.tablename, SQL_Constant.url_key
				+ "= ?", new String[] { url });
		MyApp.MLog(TAG, "deleSql" + id + "url=" + url);
		return id;
	}

	public ContentValues SetContentValues(DownDataEntity downDataEntity) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(SQL_Constant.fileprogress_key,
				downDataEntity.getFileprogress());
		contentValues.put(SQL_Constant.dowstatus_key,
				downDataEntity.getDowstatus());
		contentValues.put(SQL_Constant.sdfilepath_key,
				downDataEntity.getSdfilepath());
		contentValues.put(SQL_Constant.url_key, downDataEntity.getUrl());
		contentValues.put(SQL_Constant.name_key, downDataEntity.getName());
		contentValues.put(SQL_Constant.packagename_key,
				downDataEntity.getPackagename());
		contentValues.put(SQL_Constant.icon_key, downDataEntity.getIcon());
		contentValues.put(SQL_Constant.type_key, downDataEntity.getType());
		contentValues
				.put(SQL_Constant.version_key, downDataEntity.getVersion());
		contentValues.put(SQL_Constant.versioncode_key,
				downDataEntity.getVersioncode());
		contentValues.put(SQL_Constant.size_key, downDataEntity.getSize());
		contentValues.put(SQL_Constant.fromtype_key,
				downDataEntity.getFromtype());
		contentValues.put(SQL_Constant.urlstatus_key,
				downDataEntity.isUrlstatus() ? 1 : 0);
		contentValues.put(SQL_Constant.finshstatus_key,
				downDataEntity.isFinshstatus() ? 1 : 0);
		contentValues.put(SQL_Constant.Installstatus_key,
				downDataEntity.isInstallstatus() ? 1 : 0);
		return contentValues;
	}
}
