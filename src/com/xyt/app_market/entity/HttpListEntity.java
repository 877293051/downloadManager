package com.xyt.app_market.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HttpListEntity {
private String postion ;
private List<APPEntity> recomLists=Collections.synchronizedList(new ArrayList<APPEntity>());
private List<APPEntity> downLists=Collections.synchronizedList(new ArrayList<APPEntity>());
private List<APPEntity> scrollLists=Collections.synchronizedList(new ArrayList<APPEntity>());
private List<APPEntity> newsLists=Collections.synchronizedList(new ArrayList<APPEntity>());
private List<APPEntity> musicLists=Collections.synchronizedList(new ArrayList<APPEntity>());
private List<APPEntity> mapLists=Collections.synchronizedList(new ArrayList<APPEntity>());
private List<APPEntity> otherLists=Collections.synchronizedList(new ArrayList<APPEntity>());
public String getPostion() {
	return postion;
}
public void setPostion(String postion) {
	this.postion = postion;
}
public List<APPEntity> getRecomLists() {
	return recomLists;
}
public void setRecomLists(List<APPEntity> recomLists) {
	this.recomLists = recomLists;
}
public List<APPEntity> getDownLists() {
	return downLists;
}
public void setDownLists(List<APPEntity> downLists) {
	this.downLists = downLists;
}
public List<APPEntity> getScrollLists() {
	return scrollLists;
}
public void setScrollLists(List<APPEntity> scrollLists) {
	this.scrollLists = scrollLists;
}
public List<APPEntity> getNewsLists() {
	return newsLists;
}
public void setNewsLists(List<APPEntity> newsLists) {
	this.newsLists = newsLists;
}
public List<APPEntity> getMusicLists() {
	return musicLists;
}
public void setMusicLists(List<APPEntity> musicLists) {
	this.musicLists = musicLists;
}
public List<APPEntity> getMapLists() {
	return mapLists;
}
public void setMapLists(List<APPEntity> mapLists) {
	this.mapLists = mapLists;
}
public List<APPEntity> getOtherLists() {
	return otherLists;
}
public void setOtherLists(List<APPEntity> otherLists) {
	this.otherLists = otherLists;
}
@Override
public String toString() {
	return "HttpListEntity [postion=" + postion + ", recomLists=" + recomLists
			+ ", downLists=" + downLists + ", scrollLists=" + scrollLists
			+ ", newsLists=" + newsLists + ", musicLists=" + musicLists
			+ ", mapLists=" + mapLists + ", otherLists=" + otherLists + "]";
}



}
