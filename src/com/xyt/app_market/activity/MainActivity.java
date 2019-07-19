package com.xyt.app_market.activity;

import com.fourtech.platform.entry.EPlatform;
import com. xyt.app_market.R;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.ShareKey;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.interfacs.Impl;
import com.xyt.app_market.layout.ContentLayout;
import com.xyt.app_market.layout.DisclaimerDialog;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class MainActivity extends Activity implements Impl, OnCheckedChangeListener{
	private ContentLayout Layout_content;
	private RadioGroup tab_radioGroup;
	private View translation_view;
	private TextView  t2_tab_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		DowloadDialog.getInstance().setActivity(this);
		if(MyApp.qmType()==1){
		       setTheme(R.style.t2AppTheme);
		       setContentView(R.layout.t2activity_main); 
		}else {
		       setContentView(R.layout.activity_main); 
		}
		initView(); 
		initData();
		initEvent();
    }
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		Layout_content=(ContentLayout) findViewById(R.id.id_market_layout_content);
		tab_radioGroup=(RadioGroup) findViewById(R.id.id_tab_group);
		translation_view=findViewById(R.id.id_layout_translation);
		t2_tab_name=(TextView) findViewById(R.id.id_t2_tab_name);
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		tab_radioGroup.setOnCheckedChangeListener(this);
		Layout_content.SelectLayout(0,false);
		boolean selecttag = MyApp.getApp().preferences.getBoolean(ShareKey.Dis_select, false);
		if (!selecttag) {
			DisclaimerDialog.getInstance(this);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		DowloadDialog.getInstance().setActivity(this);
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.id_radio_hot:
			Layout_content.SelectLayout(0,false);
			if(translation_view!=null)translation_view.setTranslationX(0);
			if (t2_tab_name!=null) t2_tab_name.setText(MyApp.res.getString(R.string.tab_name));
			break;
		case R.id.id_radio_other:
			Layout_content.SelectLayout(1,false);
			if(translation_view!=null)translation_view.setTranslationX(256);
			if (t2_tab_name!=null) t2_tab_name.setText(MyApp.res.getString(R.string.tab_name2));
			break;
		case R.id.id_radio_set:
			Layout_content.SelectLayout(2,false);
			if(translation_view!=null)translation_view.setTranslationX(512);
			if (t2_tab_name!=null) t2_tab_name.setText(MyApp.res.getString(R.string.tab_name3));
			break;
		default:
			break;
		}
	}

	private long firstTime = 0;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, MyApp.res.getString(R.string.toast_title), Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else { // 两次按键小于2秒时，退出应用
				System.exit(0);
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
