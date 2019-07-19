package com.xyt.app_market.view;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ViewDownOnTouch implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			removeFilter(v);
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			setFilter(v);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			removeFilter(v);
		}
		return false;
	}

	/**
	 * 设置滤镜
	 */
	public static void setFilter(View view) {
		Drawable drawable = null;
		// 先获取设置的src图片
		if (view instanceof ImageView) {
			drawable = ((ImageView) view).getDrawable();
		}
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = view.getBackground();
		}
		if (drawable != null) {
			// 设置滤镜
			drawable.setColorFilter(Color.GRAY, Mode.MULTIPLY);
			;
		}
	}

	/**
	 * 清除滤镜
	 */
	public static void removeFilter(View view) {
		Drawable drawable = null;
		// 先获取设置的src图片
		if (view instanceof ImageView) {
			drawable = ((ImageView) view).getDrawable();
		}
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = view.getBackground();
		}
		if (drawable != null) {
			// 清除滤镜
			drawable.clearColorFilter();
		}
	}
}
