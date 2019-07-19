package com.xyt.app_market.adapters;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

	public class ViewPagerAdapter extends PagerAdapter   implements OnPageChangeListener{
	    /**
	     * 子元素的集合
	     */
	    private List<View> mViewList;
	    /**
	     * 上下文对象
	     */
	    private Context mContext;
	    private OnPagerSelectListener pagerSelectListener;
	    private View view_translation;
		private int countwidth;
		private Animation animation;
		public void setView_translation(View view_translation) {
			this.view_translation = view_translation;
		}

		public ViewPagerAdapter(List<View> mImageViewList, Context context,OnPagerSelectListener pagerSelectListener) {
	        this.mViewList = mImageViewList;
	        mContext = context;
	        this.pagerSelectListener=pagerSelectListener;
			countwidth=	768/3;
	    }

	    @Override
	    public int getCount() {
	        return mViewList == null ? 0 : mViewList.size();
	    }

	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView(mViewList.get(position));
	    }

	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	        View view = mViewList.get(position);
	        container.addView(view);

	        return view;
	    }
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == object;
	    }

	/*    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	        // 该方法回调ViewPager 的滑动偏移量
	        if (mViewList.size() > 0 && position < mViewList.size()) {
	            //当前手指触摸滑动的页面,从0页滑动到1页 offset越来越大，padding越来越大
	            Log.i("info", "重新设置padding");
	            int outHeightPadding = (int) (positionOffset * sHeightPadding);
	            int outWidthPadding = (int) (positionOffset * sWidthPadding);
	            // 从0滑动到一时，此时position = 0，其应该是缩小的，符合
	            mViewList.get(position).setPadding(outWidthPadding, outHeightPadding, outWidthPadding, outHeightPadding);

	            // position+1 为即将显示的页面，越来越大
	            if (position < mViewList.size() - 1) {
	                int inWidthPadding = (int) ((1 - positionOffset) * sWidthPadding);
	                int inHeightPadding = (int) ((1 - positionOffset) * sHeightPadding);
	                mViewList.get(position + 1).setPadding(inWidthPadding, inHeightPadding, inWidthPadding, inHeightPadding);
	            }
	        }
	    }*/


		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		if(view_translation!=null)	view_translation.setTranslationX(countwidth*arg0+countwidth*arg1);
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			pagerSelectListener.onPagerSelect(arg0);
		}
		public interface OnPagerSelectListener{
			void onPagerSelect(int postion);
		}
}
