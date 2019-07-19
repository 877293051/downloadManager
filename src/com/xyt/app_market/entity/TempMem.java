package com.xyt.app_market.entity;

import com.xyt.app_market.app.MyApp;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Debug.MemoryInfo;

public 	class TempMem
{
	public TempMem(RunningAppProcessInfo t) {
		rinfo = t;
		
	}
	public void makeInfo() {
		new MemoryInfo();
		ii = MyApp.activityManager.getProcessMemoryInfo(new int[]{rinfo.pid})[0];
	}
	public android.os.Debug.MemoryInfo getInfo()
	{
		return ii;
	}
	
	public  MemoryInfo ii ;
	public RunningAppProcessInfo rinfo;
}
