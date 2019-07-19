package com.xyt.app_market.interfacs;

/**
 * @author tjy
 *下载dowload参数
 */
public interface DowloadContentValue {
	public static final String DOWNLOADTASK_TABLE = "user_file";
	public static final int GetNETData=0x11;
	public static final int GetLoactData=0x22;
	/**
	 * 开始下载行为
	 */
	public static final int Startdowload=0x123;

	/**
	 * 暂停下载行为
	 */
	public static final int Pausedowload=0x124;
	/**
	 * 取消下载行为
	 */
	public static final int Stopdowaload=0x125;

	/**
	 * 下载错误行为
	 */
	public static final int Errordowaload=-1;
	/**
	 * 准备状态
	 */
	public static final int Readystatus=0x111;
	/**
	 * 正在状态
	 */
	public static final int Startstatus=0x222;
	/**
	 * 暂停状态
	 */
	public static final int Pausestatus=0x333;
	/**
	 * 等待状态
	 */
	public static final int Waitstatus=0x444;

	/**
	 *下载行为 key值
	 */
	public static final String Actiondowaload="doewloadaction";
	public static final String Contentdowaload="doewloadcontent";
	/**
	 * 安装任务文件行为
	 */
	public static final String Installdowaload="install";
}
