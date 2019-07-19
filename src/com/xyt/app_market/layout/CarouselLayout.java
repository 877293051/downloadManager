package com.xyt.app_market.layout;

import java.util.ArrayList;
import java.util.List;
import com.xyt.app_market.R;
import com.xyt.app_market.activity.AppDetailsActivity;
import com.xyt.app_market.app.MyApp;
import com.xyt.app_market.contants.Constants.URLConstant;
import com.xyt.app_market.dowload.DowloadDialog;
import com.xyt.app_market.entity.APPEntity;
import com.xyt.app_market.interfacs.DowloadContentValue;
import com.xyt.app_market.utitl.HttpBitmap;

import android.view.View.OnClickListener;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CarouselLayout extends FrameLayout implements OnClickListener,DowloadContentValue{

	public CarouselLayout(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public CarouselLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public CarouselLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	private Context context;
	private List<String> bitmaps = new ArrayList<String>();
	private List<ImageView> itemList = new ArrayList<ImageView>();;
	private int mWidth  ;
	private int mHeight ;
	private int mItemWidth ;
	private int mItemHeight ;
	public int CenterSelectPostion;
	private int IndexMaxSize;
	/**
	 * 能显示的View
	 */
	private int[] Centerindex;
	boolean isPre = true;

	private float[] mTargetX;
	private float[] mTargetScale;
	public List<String> getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(List<String> bitmaps) {
		this.bitmaps = bitmaps;
	}

	public void SetInit(List<APPEntity> list) {
		mItemWidth=(int) getResources().getDimension(R.dimen.gallery_item_width);
		mItemHeight=(int) getResources().getDimension(R.dimen.gallery_item_hight);
		WindowManager wm = (WindowManager)MyApp.getApp()
				.getSystemService(Context.WINDOW_SERVICE);
		mWidth = (int) (wm.getDefaultDisplay().getWidth()- getResources().getDimension(R.dimen.gallery_item_start));;
		mHeight = wm.getDefaultDisplay().getHeight();
		bitmaps.clear();
		itemList.clear();
		if (list.size() == 0) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			APPEntity aPPEntity = list.get(i);
			this.bitmaps.add(aPPEntity.getHomeimagepath());
		}
		for (int i = 0; i < list.size(); i++) {
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setId(i);
			imageView.setTag(list.get(i).getFilepath());
			imageView
					.setLayoutParams(new LayoutParams(mItemWidth, mItemHeight));
				imageView.setOnClickListener(this);
			itemList.add(imageView);
		}
		IndexMaxSize = itemList.size() - 1;
		CenterSelectPostion = itemList.size() / 2 == 0 ? itemList.size() / 2
				: itemList.size() / 2 + 1;
		if (itemList.size() > 2) {
			Centerindex = new int[3];
		} else {
			Centerindex = new int[itemList.size()];
		}
		mTargetX = new float[Centerindex.length];
		mTargetScale = new float[Centerindex.length];
		SetCenterindex();
	}

	public void SetCenterindex() {
		if (Centerindex.length == 3) {
			if (CenterSelectPostion == 0) {
				Centerindex[0] = IndexMaxSize;
				Centerindex[1] = CenterSelectPostion + 1;
				Centerindex[2] = CenterSelectPostion;
			} else if (CenterSelectPostion == IndexMaxSize) {
				Centerindex[0] = CenterSelectPostion - 1;
				Centerindex[1] = 0; 
				Centerindex[2] = CenterSelectPostion;
			} else {
				Centerindex[0] = CenterSelectPostion - 1;
				Centerindex[1] = CenterSelectPostion + 1;
				Centerindex[2] = CenterSelectPostion;
			}
		} else {
			if (Centerindex.length == 2) {
				if (CenterSelectPostion == IndexMaxSize) {
					Centerindex[1] = 0;
					Centerindex[0] = 1;
				} else {
					Centerindex[0] = 0;
					Centerindex[1] = 1;
				}
			}
		} 
		resetView();
	}

	public void nextResetIcon() {
		if (CenterSelectPostion == IndexMaxSize) {
			CenterSelectPostion = 0;
		} else {
			CenterSelectPostion++;
		}
		SetCenterindex();
	}

	public void resetView() {
		removeAllViews();
		for (int i = 0; i < Centerindex.length; i++) {
			int centX = mWidth / 2;
			float scale = 1f;
			if (i == 0) {
				centX = (int) (mWidth / 2 - 250);
				scale = 0.9f;
			} else if (i == 2) {
				centX = (int) (mWidth / 2);
				scale = 1.0f;
			} else if (i == 1) {
				centX = (int) (mWidth / 2 + 250);
				scale = 0.9f;
			}
			if (Centerindex.length == 1) {
				centX = (int) (mWidth / 2);
				scale = 1.1f;
			} else if (Centerindex.length == 2) {
				if (i == 0) {
					centX = (int) (mWidth / 2 - 250);
					scale = 0.9f;
				} else {
					centX = (int) (mWidth / 2);
					scale = 1.0f;
				}
			}
			try {
				ImageView imageView = itemList.get(Centerindex[i]);
				if (imageView != null) {
					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
							mItemWidth, mItemHeight);
					params.setMargins(centX - mItemWidth / 2, 0, 0, 0);
					mTargetX[i] = centX - mItemWidth / 2;
					mTargetScale[i] = scale;
					imageView.setScaleX(scale);
					imageView.setScaleY(scale);
					this.addView(imageView, i, params);
//					MyImageloder.getInstance().displayImage(URLConstant.URL_Image	+ bitmaps.get(Centerindex[i]), imageView, R.drawable.market_right_banner_1);
				HttpBitmap.getInstance(getContext()).display(URLConstant.URL_Image	+ bitmaps.get(Centerindex[i]), imageView, R.drawable.market_right_banner_1);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CenterSelectPostion=(Integer) v.getId();
		SetCenterindex();
		String Tag=(String)v.getTag();
		if (!TextUtils.isEmpty(Tag)) {
			Intent intent = new Intent(DowloadDialog.getInstance()
					.getActivity(), AppDetailsActivity.class);
			intent.putExtra(Contentdowaload,Tag );
			DowloadDialog.getInstance().getActivity().startActivity(intent);
		}
	}
}
