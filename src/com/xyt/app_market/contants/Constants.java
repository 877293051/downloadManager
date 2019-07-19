package com.xyt.app_market.contants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.pm.ResolveInfo;

import com.xyt.app_market.entity.DownDataEntity;
import com.xyt.app_market.entity.HttpListEntity;
import com.xyt.app_market.entity.NativeEntity;

public class Constants {
	public static final  String name ="app_market";
	public static final String DOWN_PATH="/sdcard/appstore";
	public static NetType netType=NetType.WIFI;
	public static enum NetType{WIFI,FLOW,NONET }
	public static class DataConstant{
		public volatile static List<DownDataEntity>   SQLApplist=new ArrayList<DownDataEntity>();
		public volatile static NativeEntity nativeEntity =new NativeEntity();
		public volatile static HttpListEntity httpListEntity=new HttpListEntity();
		public volatile static Map<String, DownDataEntity> DownloadThreep = Collections.synchronizedMap(new HashMap<String, DownDataEntity>());// 下载队列
		public volatile static Map<String, DownDataEntity> InDownloadThreep = Collections.synchronizedMap(new HashMap<String, DownDataEntity>());// 正在下载队列
		public static  void DataDestroy(){//退出应用销毁数据　避免内存泄露
			SQLApplist.clear();
			DownloadThreep.clear();
			InDownloadThreep.clear(); 
			SQLApplist=null;
			nativeEntity=null;
			httpListEntity=null;
			DownloadThreep=null;
			InDownloadThreep=null;
		}
	}
	public static class URLConstant {
		public static final String HomeURL = "http://m.metur.cn/ali/server/apk/get_app_market_home.do?";
		public static final String TypeURL = "http://m.metur.cn/ali/server/apk/get_app_by_type.do?";
		public static final String Add_num = "http://m.metur.cn/ali/server/apk/update_app_downloadcount.do?";
		public static final String URL_Image = "http://m.metur.cn:88/images";
		public static final String URL_APP = "http://m.metur.cn:88/apps";
	}
	public static class TypeConstant {
		public static final String Music_Type = "music";
		public static final String Map_Type = "navi";
		public static final String Other_Type = "other";
		public static final String HIDE_Type = "hide";
	}
	public static class Action_Constant {
		public static final String Progress_Update="progress_update";
		public static final String nativieApp_Update="nativie_update";
	}
	public static class HandleWat_Constant {
		public static final int successwhat = 0x11;
		public static final int failwhat = 0x22;
	} 
	public static class SQL_Constant {
		public static final String dbname = "appdows.db";
		public static final String tablename = "appdownloadtask";
		public static final String fileprogress_key = "fileprogress";
		public static final String dowstatus_key = "dowstatus";
		public static final String sdfilepath_key = "sdfilepath";
		public static final String url_key = "url";
		public static final String name_key = "name";
		public static final String packagename_key = "packagename";
		public static final String icon_key = "icon";
		public static final String author_key = "author";
		public static final String company_key = "company";
		public static final String type_key = "type";
		public static final String version_key = "version";
		public static final String versioncode_key = "versioncode";
		public static final String size_key = "size";
		public static final String fromtype_key = "fromtype";
		public static final String urlstatus_key = "urlstatus";
		public static final String finshstatus_key = "finshstatus";
		public static final String Installstatus_key= "Installstatus";
	}
	public static class ShareKey{
		public static final String Dis_select="dis_select";
	}
	public static class Intent_Constant {
		public static final String ServiceAction="com.xyt.app_market.startdown";
		public static final String DownListUpdate = "downlistupdate";
	}
}
