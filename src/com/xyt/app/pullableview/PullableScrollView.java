package com.xyt.app.pullableview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
public class PullableScrollView extends ScrollView implements Pullable
{
	private int downy;
	public PullableScrollView(Context context)
	{
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0&&downy<270)
			return true;
		else
			return false;
	}
	/*public boolean isViewPaper(){
		ViewGroup viewGroup =(ViewGroup) getChildAt(0);
		if (viewGroup!=null) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			if (viewGroup.getChildAt(i)   instanceof ViewPager) {
				return true;
			}
		}
		}
		return false;
	}*/
	@Override
	public boolean canPullUp()
	{
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downy=(int) ev.getY();
			Log.d("onTouch", "downy"+downy);
		}

		return super.dispatchTouchEvent(ev);
	}


}
