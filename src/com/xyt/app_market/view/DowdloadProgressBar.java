package com.xyt.app_market.view;

import com.xyt.app_market.R;
import com.xyt.app_market.R.string;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.interfacs.DowloadContentValue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

public class DowdloadProgressBar extends ProgressBar implements
		DowloadContentValue {
	private String text;
	private Paint mPaint;

	public DowdloadProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initText();
	}

	public DowdloadProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initText();
	}

	public DowdloadProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		if (progress == Pausestatus) {
			setMText(getResources().getString(R.string.down_state_zan));
		} else if (progress == Errordowaload) {
			setMText(getResources().getString(R.string.down_state_isstart));
			super.setProgress(0);
		} else if (progress == Waitstatus) {
			setMText(getResources().getString(R.string.down_state_wait));
		} else if (progress == Readystatus) {
			setMText(getResources().getString(R.string.down_state_ready));
			super.setProgress(0);
		} else {
			setText(progress);
			super.setProgress(progress);
		}
	}

	public synchronized void setStateText(String state) {
		setMText(state);
		super.setProgress(100);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// this.setText();
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	// 初始化，画笔
	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);
		this.mPaint.setAntiAlias(true);
		this.mPaint.setTextSize(20);

	}

	private void setText() {
		setText(this.getProgress());
	}

	// 设置文字内容
	private void setText(int progress) {
		int i = (progress * 100) / this.getMax();
		this.text = String.valueOf(i) + "%";
	}

	// 设置文字内容
	private void setMText(String string) {
		MyApp.MLog("setMText", "setMText"+string);
		this.text = string;
	}
}
