package com.xyt.app_market.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NativeEntity {
	private Map<String,DownDataEntity> systemMap=new HashMap<String, DownDataEntity>();
	private Map<String,DownDataEntity> appMap=new HashMap<String, DownDataEntity>();
	private Map<String,DownDataEntity> DownMap=new HashMap<String, DownDataEntity>();
	public Map<String, DownDataEntity> getSystemMap() {
		return systemMap;
	}
	public void setSystemMap(Map<String, DownDataEntity> systemMap) {
		this.systemMap = systemMap;
	}
	public Map<String, DownDataEntity> getAppMap() {
		return appMap;
	}
	public void setAppMap(Map<String, DownDataEntity> appMap) {
		this.appMap = appMap;
	}
	@Override
	public String toString() {
		return "NativeEntity [systemMap=" + systemMap + ", appMap=" + appMap
				+ "]";
	}
}
