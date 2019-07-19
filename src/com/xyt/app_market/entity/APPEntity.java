package com.xyt.app_market.entity;

import java.io.Serializable;

import android.content.pm.ResolveInfo;

/**
 * 应用
 * 
 * @author sfli
 *
 */
public class APPEntity  {
	private DownDataEntity dataEntity=new DownDataEntity();
	/**
	 * ID
	 */
	private long apid;
	/**
	 * 应用名
	 */
	private String name;
	/**
	 * 应用图标
	 */
	private String icon;

	/**
	 * 作者
	 */
	private String author;
	/**
	 * 公司
	 */
	private String company;
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
	 * 使用平台
	 */
	private String platform;
	/**
	 * 上传时间
	 */
	private long uploaddate;
	/**
	 * 星级评分
	 */
	private int star;
	/**
	 * 介绍
	 */
	private String introduction;
	/**
	 * 更新信息
	 */
	private String updateinfo;
	/**
	 * 软件包名
	 */
	private String packagename;
	/**
	 * 截图介绍
	 */
	private String review1;
	private String review2;
	private String review3;
	private String review4;
	private String review5;
	/**
	 * apk文件路径
	 */
	private String filepath;
	/**
	 * 下载计数
	 */
	private long downloadcount;
	/**
	 * 来源类型: 0 行影通产品 1 第三方互联网来源 2 第三方供应商来源
	 */
	private long fromtype;
	/**
	 * 推荐位置: 0 普通列表 1 APP首页滚动 3 类别首页
	 */
	private long recommend;
	
	private String homeimagepath;

	public DownDataEntity getDataEntity() {
		return dataEntity;
	}

	public void setDataEntity(DownDataEntity dataEntity) {
		this.dataEntity = dataEntity;
	}

	public long getApid() {
		return apid;
	}

	public void setApid(long apid) {
		this.apid = apid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public long getUploaddate() {
		return uploaddate;
	}

	public void setUploaddate(long uploaddate) {
		this.uploaddate = uploaddate;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getUpdateinfo() {
		return updateinfo;
	}

	public void setUpdateinfo(String updateinfo) {
		this.updateinfo = updateinfo;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getReview1() {
		return review1;
	}

	public void setReview1(String review1) {
		this.review1 = review1;
	}

	public String getReview2() {
		return review2;
	}

	public void setReview2(String review2) {
		this.review2 = review2;
	}

	public String getReview3() {
		return review3;
	}

	public void setReview3(String review3) {
		this.review3 = review3;
	}

	public String getReview4() {
		return review4;
	}

	public void setReview4(String review4) {
		this.review4 = review4;
	}

	public String getReview5() {
		return review5;
	}

	public void setReview5(String review5) {
		this.review5 = review5;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public long getDownloadcount() {
		return downloadcount;
	}

	public void setDownloadcount(long downloadcount) {
		this.downloadcount = downloadcount;
	}

	public long getFromtype() {
		return fromtype;
	}

	public void setFromtype(long fromtype) {
		this.fromtype = fromtype;
	}

	public long getRecommend() {
		return recommend;
	}

	public void setRecommend(long recommend) {
		this.recommend = recommend;
	}

	public String getHomeimagepath() {
		return homeimagepath;
	}

	public void setHomeimagepath(String homeimagepath) {
		this.homeimagepath = homeimagepath;
	}

	@Override
	public String toString() {
		return "APPEntity [dataEntity=" + dataEntity + ", apid=" + apid
				+ ", name=" + name + ", icon=" + icon + ", author=" + author
				+ ", company=" + company + ", type=" + type + ", version="
				+ version + ", versioncode=" + versioncode + ", size=" + size
				+ ", platform=" + platform + ", uploaddate=" + uploaddate
				+ ", star=" + star + ", introduction=" + introduction
				+ ", updateinfo=" + updateinfo + ", packagename=" + packagename
				+ ", review1=" + review1 + ", review2=" + review2
				+ ", review3=" + review3 + ", review4=" + review4
				+ ", review5=" + review5 + ", filepath=" + filepath
				+ ", downloadcount=" + downloadcount + ", fromtype=" + fromtype
				+ ", recommend=" + recommend + ", homeimagepath="
				+ homeimagepath + "]";
	}


}
