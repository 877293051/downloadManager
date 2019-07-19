package com.xyt.app_market.entity;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

import android.content.pm.ResolveInfo;

import com.xyt.app_market.contants.Constants.SQL_Constant;

@Table(name = SQL_Constant.tablename)
//用于FinalDb指定的表名
public class DownDataEntity implements Serializable{
/*	*//**
	 * 	  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 *//*
	private static final long serialVersionUID = 12L;*/
    /**
     * 主键
     */
    private int id;
	/**
	 * 是否本地存在
	 */
	private boolean nativeAppTag;
    /**
     * 是否为更新
     */
    private boolean updatestate;
	/** 
	 * 下载进度
	 */
	private int fileprogress = 0;
	/**
	 * 下载状态
	 */
	private int dowstatus = 2;
	/**
	 * 是否下载状态
	 */
	private boolean urlstatus;
	/**
	 * 是否下载完成
	 */
	private boolean finshstatus;
	
	/**
	 * 是否已经安装
	 */
	private boolean Installstatus;
	/**
	 * sd卡存储路径
	 */
	private String sdfilepath;
	/**
	 * 唯一标识 下载地址
	 */
	private String url;
	/**
	 * 应用名
	 */
	private String name;
	/**
	 * 软件包名
	 */
	private String packagename;
	/**
	 * 应用图标
	 */
	private String icon;
	/**
	 * 软件类别
	 */
	private String type;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 版本码
	 */
	private long versioncode;
	/**
	 * 大小
	 */
	private String size;
	/**
	 * 内存大小
	 */
	private long memorysize=0;
	/**
	 * 来源类型: 0 行影通产品 1 第三方互联网来源 2 第三方供应商来源
	 */
	private long fromtype;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isNativeAppTag() {
		return nativeAppTag;
	}
	public void setNativeAppTag(boolean nativeAppTag) {
		this.nativeAppTag = nativeAppTag;
	}
	public boolean isUpdatestate() {
		return updatestate;
	}
	public void setUpdatestate(boolean updatestate) {
		this.updatestate = updatestate;
	}
	public int getFileprogress() {
		return fileprogress;
	}
	public void setFileprogress(int fileprogress) {
		this.fileprogress = fileprogress;
	}
	public int getDowstatus() {
		return dowstatus;
	}
	public void setDowstatus(int dowstatus) {
		this.dowstatus = dowstatus;
	}
	public boolean isUrlstatus() {
		return urlstatus;
	}
	public void setUrlstatus(boolean urlstatus) {
		this.urlstatus = urlstatus;
	}
	public boolean isFinshstatus() {
		return finshstatus;
	}
	public void setFinshstatus(boolean finshstatus) {
		this.finshstatus = finshstatus;
	}
	public boolean isInstallstatus() {
		return Installstatus;
	}
	public void setInstallstatus(boolean installstatus) {
		Installstatus = installstatus;
	}
	public String getSdfilepath() {
		return sdfilepath;
	}
	public void setSdfilepath(String sdfilepath) {
		this.sdfilepath = sdfilepath;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public long getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(long versioncode) {
		this.versioncode = versioncode;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public long getMemorysize() {
		return memorysize;
	}
	public void setMemorysize(long memorysize) {
		this.memorysize = memorysize;
	}
	public long getFromtype() {
		return fromtype;
	}
	public void setFromtype(long fromtype) {
		this.fromtype = fromtype;
	}
	@Override
	public String toString() {
		return "DownDataEntity [id=" + id + ", nativeAppTag=" + nativeAppTag
				+ ", updatestate=" + updatestate + ", fileprogress="
				+ fileprogress + ", dowstatus=" + dowstatus + ", urlstatus="
				+ urlstatus + ", finshstatus=" + finshstatus
				+ ", Installstatus=" + Installstatus + ", sdfilepath="
				+ sdfilepath + ", url=" + url + ", name=" + name
				+ ", packagename=" + packagename + ", icon=" + icon + ", type="
				+ type + ", version=" + version + ", versioncode="
				+ versioncode + ", size=" + size + ", memorysize=" + memorysize
				+ ", fromtype=" + fromtype + "]";
	}


}
