package com.xyt.app_market.utitl;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import com.xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.DataConstant;
import com.xyt.app_market.contants.Constants.HandleWat_Constant;
import com.xyt.app_market.contants.Constants.TypeConstant;
import com.xyt.app_market.contants.Constants.URLConstant;
import com.xyt.app_market.dowload.DowdloadData;
import com.xyt.app_market.dowload.DowloadDialog;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class HttpUtitl {
	public static String TAG=HttpUtitl.class.getSimpleName();
	public static void GetHomeData(final Handler handler) {
		StringBuffer URLbuffer = new StringBuffer();
		URLbuffer.append(URLConstant.HomeURL);
		URLbuffer.append("platform=" + 0);
		MyApp.finalHttp.get(URLbuffer.toString(), new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject((String) t);
//			MyApp.MLog(TAG, "GetHomeData"+t);
					int status = jsonObject.getInt("status");
					if (status==1) {
						DowdloadData.HomeJsonHelper(jsonObject, DataConstant.httpListEntity);
						handler.sendEmptyMessage(HandleWat_Constant.successwhat);
						MyApp.MLog(TAG, DataConstant.httpListEntity.getScrollLists().size()+DataConstant.httpListEntity.getScrollLists().toString());
/*						MyApp.MLog(TAG, "httpListEntity"+httpListEntity.toString());*/
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(HandleWat_Constant.failwhat);
				super.onFailure(t, strMsg);
			}
		});
	}
	public static void GetTypeData(final boolean clearTag, final Handler handler,int platform,String type,int page,int pageCount) {
		if (DataConstant.InDownloadThreep.size()!=0) {
			return; 
		}
		if (page>0) {
			page=page*pageCount;
		}
		StringBuffer URLbuffer = new StringBuffer();
		URLbuffer.append(URLConstant.TypeURL);
		URLbuffer.append("platform=" + platform);
		URLbuffer.append("&type="+ type);
		URLbuffer.append("&page=" + page);
		URLbuffer.append("&pageCount=" +pageCount );
		MyApp.MLog(TAG, "GetTypeData"+URLbuffer.toString());
		MyApp.finalHttp.get(URLbuffer.toString(), new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject((String) t);
//				MyApp.MLog(TAG, "GetTypeData"+t);
					int status = jsonObject.getInt("status");
					if (status==1) {
						DowdloadData.TypeJsonHelper(clearTag,jsonObject, DataConstant.httpListEntity);
						handler.sendEmptyMessage(HandleWat_Constant.successwhat);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				handler.sendEmptyMessage(HandleWat_Constant.failwhat);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static void AddDownNum(String packagename) {
		MyApp.MLog(TAG, "AddDownNum");
		StringBuffer URLbuffer = new StringBuffer();
		URLbuffer.append(URLConstant.Add_num);
		URLbuffer.append("packagename=" + packagename);
		FinalHttp finalHttp = new FinalHttp();
		finalHttp.get(URLbuffer.toString(), new AjaxCallBack() {
			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject((String) t);
					int status = jsonObject.getInt("status");
					if (status == 1) {
						MyApp.MLog(TAG, "AddDownNum下载量添加");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(t);
			}
		});
	}
}
