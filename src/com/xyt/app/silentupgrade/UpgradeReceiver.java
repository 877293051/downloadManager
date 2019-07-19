package com.xyt.app.silentupgrade;

import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.http.AjaxParams;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class UpgradeReceiver extends BroadcastReceiver {
	public static final String TAG = UpgradeReceiver.class.getSimpleName();
	private static final String BootACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(BootACTION)) {
			Log.d(TAG, "开机启动");
	/*		context.startService(new Intent(context,UpgradeService.class));*/
		}
	}

}
