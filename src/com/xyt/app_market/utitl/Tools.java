package com.xyt.app_market.utitl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.entity.TempMem;

public class Tools {
	public static void SetViewText(View view,String content){
		if (!TextUtils.isEmpty(content)) {
			if (view instanceof TextView) {
				((TextView)view).setText(content);
			}
			if (view instanceof Button) {
				((Button)view).setText(content);
			}
		}
	}
	 /**启动一个app 
	 * @param appPackageName
	 */
	public static void StartAPP(Context context,String appPackageName){  
	     try{  
	         Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);  
	         context. startActivity(intent);  
	     }catch(Exception e){  
//	         Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();  
	     }  
	 } 
	@SuppressWarnings("unused")
	public static ArrayList<TempMem> getTempMems() {
		ArrayList<TempMem> list = null;
		if (list == null) {
			list = new ArrayList<TempMem>();
			;
			List<RunningAppProcessInfo> l = MyApp.activityManager
					.getRunningAppProcesses();
			list.clear();
			for (RunningAppProcessInfo t : l) {
				list.add(new TempMem(t));
			}
		}
		for (TempMem tm : list) {
			tm.makeInfo();
		}
		return list;

	}

	public static String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	  /** 
     * 执行具体的静默安装逻辑，需要手机ROOT。 
     * @param apkPath 
     *          要安装的apk文件的路径 
     * @return 安装成功返回true，安装失败返回false。 
     */  
    public static  boolean install(String apkPath) {  
        boolean result = false;  
        DataOutputStream dataOutputStream = null;  
        BufferedReader errorStream = null;  
        Log.d("install", "install msg is " + apkPath);  
        try {  
            // 申请su权限  
            Process process = Runtime.getRuntime().exec("su");  
            dataOutputStream = new DataOutputStream(process.getOutputStream());  
            // 执行pm install命令  
            String command = "pm install -r " + apkPath + " --com\n";  
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));  
            dataOutputStream.flush();  
            dataOutputStream.writeBytes("exit\n");  
            dataOutputStream.flush();  
            process.waitFor();  
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));  
            String msg = "";  
            String line;  
            // 读取命令的执行结果  
            while ((line = errorStream.readLine()) != null) {  
                msg += line;  
            }  
            Log.d("install", "install msg is " + msg);  
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功  
            if (!msg.contains("Failure")) {  
                result = true;  
            }  
        } catch (Exception e) {  
            Log.e("install", e.getMessage(), e);  
        } finally {  
            try {  
                if (dataOutputStream != null) {  
                    dataOutputStream.close();  
                }  
                if (errorStream != null) {  
                    errorStream.close();  
                }  
            } catch (IOException e) {  
                Log.e("TAG", e.getMessage(), e);  
            }  
        }  
        return result;  
    }  
	public static String BackstageinstallApk(Context context, String apkAbsolutePath, String packname) {
		PackageManager pm = context.getPackageManager();
		pm.installPackage(Uri.parse(apkAbsolutePath),new IPackageInstallObserver() {
			@Override
			public IBinder asBinder() {
				return null;
			}
			@Override
			public void packageInstalled(String arg0, int arg1) throws RemoteException {
				Log.e("install", "arg0:" + arg0 + "  " + arg1);
			}
		}, PackageManager.INSTALL_ALL_USERS,packname);
		if(true)
			return "";
	    String[] args = { "pm", "install", "-r", apkAbsolutePath,"--com\n" };  // 
	    String result = "";  
	    ProcessBuilder processBuilder = new ProcessBuilder(args);  
	    Process process = null;  
	    InputStream errIs = null;  
	    InputStream inIs = null;  
	    try {  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        int read = -1;  
	        process.waitFor();
	        process = processBuilder.start();  
	        errIs = process.getErrorStream();  
	        while ((read = errIs.read()) != -1) {  
	            baos.write(read);  
	        }  
//	        baos.write(new Byte("/n"));
	        inIs = process.getInputStream();  
	        while ((read = inIs.read()) != -1) {  
	            baos.write(read);  
	        }  
	        byte[] data = baos.toByteArray();  
	        result = new String(data);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (errIs != null) {  
	                errIs.close();  
	            }  
	            if (inIs != null) {  
	                inIs.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        if (process != null) {  
	            process.destroy();  
	        }  
	    }  
	    return result;  
	}
	/**
	 * 描述：打开并安装文件.
	 * 
	 * @param context
	 *            the context
	 * @param file
	 *            apk文件路径
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isString(String str) {
		Pattern p = Pattern.compile("[a-zA-z]");
		if (p.matcher(str).find()) {
			return false;
		}
		return true;
	}

	public static PackageInfo getPackageInfo(String packageName) {
		PackageInfo pkg = null;
		try {
			PackageManager pkgManager = MyApp.getApp().getPackageManager(); // 需要system权限
			pkg = pkgManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pkg;
	}

	public static boolean isSysttemApp(String packageName) {
		android.content.pm.ApplicationInfo info;
		try {
			info = MyApp.getApp().getPackageManager()
					.getApplicationInfo(packageName, 0);
			int flags = info.flags;
			if ((flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
				/*
				 * int messageId = R.string.uninstall_system_app_text;
				 * Toast.makeText(MyApp.getApp(), messageId,
				 * Toast.LENGTH_SHORT).show();
				 */
				// TJYLauncher 2016-3-26
				/*
				 * Intent intent =new Intent("data.broadcast.action");
				 * sendBroadcast(intent);
				 */
				return true;
			} else {
				return false;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * 判断是否安装 check the app is installed
	 */
	public static boolean isAppInstalled(Context context, String packagename) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packagename, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			// System.out.println("没有安装");
			return false;
		} else {
			// System.out.println("已经安装");
			return true;
		}
	}

	/**
	 * 加载app
	 */
	public static List<ResolveInfo> loadApps(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> mApps = context.getPackageManager()
				.queryIntentActivities(intent, 0);
		return mApps;
	}

	/**
	 * @param context
	 * @param packagename根据包名运行应用
	 */
	public static void RunApp(Context context, String packagename) {
		List<ResolveInfo> mApps = loadApps(context);
		ResolveInfo Info = null;
		for (int i = 0; i < mApps.size(); i++) {
			ResolveInfo info = mApps.get(i);
			if (packagename.equals(info.activityInfo.packageName)) {
				Info = info;
			}
		}
		if (Info == null) {
			return;
		}
		// 应用的包名
		String pkg = Info.activityInfo.packageName;
		// 应用的主Activity
		String cls = Info.activityInfo.name;

		PackageManager pm = context.getPackageManager();
		String appLabel = (String) Info.loadLabel(pm); // 获取应用的名称
		Drawable icon = Info.loadIcon(pm); // 获取应用的图标icon

		ComponentName componentName = new ComponentName(pkg, cls);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setComponent(componentName);
		context.startActivity(intent);
	}

	public static int Changedp(int pageMargin, Context context) {
		int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				pageMargin, context.getResources().getDisplayMetrics());
		return dp;
	}

}
