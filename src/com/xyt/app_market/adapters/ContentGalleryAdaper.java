/*package com.xyt.app_market.activity.adapters;

import java.util.ArrayList;
import java.util.List;
import com.example.app_market.R;
import com.xyt.app_market.activity.entity.GralleryDataEntity;
import com.xyt.app_market.activity.holder.CommonAdapter;
import com.xyt.app_market.activity.holder.ViewHolder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentGalleryAdaper extends CommonAdapter<GralleryDataEntity> {
	private Context context;
	public List<GralleryDataEntity> dataEntities = new ArrayList<GralleryDataEntity>();
	private int selectItem = -1;
	private int width = 440, height = 240;

	public ContentGalleryAdaper(Context context, int itemLayoutResId) {
		super(context, itemLayoutResId);
		super.SetData(dataEntities);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public void setSelectItem(int selectItem,boolean updatetag ) {
		if (this.selectItem != selectItem) {
			this.selectItem = selectItem;
			if (updatetag) {
				notifyDataSetChanged();
			}
		}
	}

	public void updateDaper(List<GralleryDataEntity> dataEntities) {
		this.dataEntities = dataEntities;
		super.SetData(dataEntities);
		notifyDataSetChanged();
	}

	@Override
	public void convert(ViewHolder viewHolder, GralleryDataEntity item) {
		// TODO Auto-generated method stub
		ImageView imageView = viewHolder.getView(R.id.id_gallery_image);
		imageView.setImageBitmap(BitmapFactory.decodeResource(
				context.getResources(), item.drawId));
		if (selectItem != -1 && selectItem == viewHolder.getPosition()) {
			ScaleAnimRun(imageView);
		}else {
			BackScaleAnimRun(imageView);
		}
	}
	ObjectAnimator anim;
	public void ScaleAnimRun(final View view) {
		ObjectAnimator anim= ObjectAnimator//
				.ofFloat(view, "zhy", 1.f, 1.1f);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				view.setAlpha(cVal);
				view.setScaleX(cVal);
				view.setScaleY(cVal);
			}
		});
		anim.setDuration(500);
		anim.start();
	}

	public void BackScaleAnimRun(final View view) {
		if (view.getScaleX()==1f||view.getScaleY()==1f) {
			return;
		}
		ObjectAnimator anim = ObjectAnimator//
				.ofFloat(view, "zhy", 1.0f, 1.0f);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				view.setAlpha(cVal);
				view.setScaleX(cVal);
				view.setScaleY(cVal);
			}
		});
		anim.setDuration(100);
		anim.start();
	}
}
*/