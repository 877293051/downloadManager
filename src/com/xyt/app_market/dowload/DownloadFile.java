/**   
* @Title: DownloadFile.java
* @Package com.cloud.coupon.utils
* @Description: TODO(用一句话描述该文件做�?��)
* @author 陈红�?
* @date 2013-6-26 下午5:31:23
* @version V1.0
*/ 
package com.xyt.app_market.dowload;

import java.io.File;
import java.io.Serializable;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

/** 
 * 
 */
public class DownloadFile implements Serializable
{

	private static final long serialVersionUID = 1L;
	private boolean isStop;
	@SuppressWarnings("rawtypes")
	private HttpHandler mHttpHandler;
	public String URL;
	public  DownloadFile startDownloadFileByUrl(String url , String toPath , AjaxCallBack<File> downCallBack) {
		this.URL=url;
		if(downCallBack == null) {
			throw new RuntimeException("AjaxCallBack对象不能为null");
		}else {
			FinalHttp down = new FinalHttp();
			//支持断点续传
			mHttpHandler = down.download(url, toPath,true, downCallBack);
		}
		return this;
	}
	public  void stopDownload() {
		if(mHttpHandler != null) {
			mHttpHandler.stop();
			mHttpHandler.cancel(true);
			if(!mHttpHandler.isStop()) {
				mHttpHandler.stop();
				mHttpHandler.cancel(true);
			}
		}
	}
	public boolean isStop()
	{
		isStop = mHttpHandler.isStop();
		return isStop;
	}
	public void setStop(boolean isStop)
	{
		this.isStop = isStop;
	}
}

