package com.xyt.app_market.utitl;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class HttpBitmap {
	public ImageLoader imageLoader = null;
	private static HttpBitmap httpBitmap;

	public HttpBitmap(Context context) {
		// TODO Auto-generated constructor stub
		if (imageLoader==null) {
			imageLoader = ImageLoader.getInstance();
		}

		initImageLoader(context);
	}
	/**
	 * @param context
	 * @return实例化HttpBitmap类
	 */
	public static HttpBitmap getInstance(Context context) {
		if (httpBitmap == null) {
			synchronized (HttpBitmap.class) {
				if (httpBitmap == null) {
					httpBitmap = new HttpBitmap(context);
				}
			}
		}
		return httpBitmap;
	}
	public void initImageLoader(Context context) {

		// Initialize ImageLoader with configuration.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// Not necessary in common
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 加载图片
	 * 
	 * @param imgurl
	 * @param imageView
	 * @param defaultPicId
	 */
	public void display(String imgurl, ImageView imageView, int defaultPicId) {
		imageLoader.displayImage(imgurl, imageView,
				displayImageOptions(defaultPicId));
	}

	DisplayImageOptions options = null;
	int defaultid = 0;

	public DisplayImageOptions displayImageOptions(int resId) {
		if (null != options && defaultid == resId) {
			return options;
		}
		options = new DisplayImageOptions.Builder().showStubImage(resId)
				.showImageForEmptyUri(resId).showImageOnFail(resId)
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		defaultid = resId;
		return options;
	}
}
