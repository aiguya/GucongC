
package com.smartware.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.RejectedExecutionException;

public class ImageDownloader {

	private static final boolean					DEBUG = false;
	private static final String TAG = "ImageDownloader";
	
	public static final int								SHAPE_RECTANGLE = 0;
	public static final int								SHAPE_CIRCLE = 1;
	
	public static final int 								IMGAE_CACHE_LIMIT_SIZE = 200;
	
	public static HashMap<String, Bitmap> sImageCache = new HashMap<String, Bitmap>();
	private static String sCacheFilePath = "";
	private static Context sContext = null;
	
	public static void setCacheFilePath(String val) {
		sCacheFilePath = val;
	}
	
	public static String getCacheFilePath() {
		return sCacheFilePath;
	}
	
	public static void setContext(Context context) {
		sContext = context;
	}
	
	public static Context getContext() {
		return sContext;
	}

	public static void clearCache() {		
		sImageCache.clear();
		System.gc();
	}
	
	public static void download(String url, ImageView imageView, int sx, int sy) {
		Utils.getInstance().printLog(  DEBUG, TAG, "[download] url : " + url + ", sx : " + sx + ", sy : " + sy);
		Bitmap cachedImage = sImageCache.get(url);
		if (cachedImage != null) {
			int			tag = (Integer) imageView.getTag();
			int			id = imageView.getId();
			Utils.getInstance().printLog( DEBUG, TAG, "[download] tag : " + tag + ", id : " + id);
			switch (tag) {
			case ImageDownloader.SHAPE_CIRCLE:
			{
				Bitmap squareBitmap;
				if (cachedImage.getWidth() >= cachedImage.getHeight()) {
					squareBitmap = Bitmap.createBitmap(
							cachedImage, 
//							cachedImage.getWidth()/2 - cachedImage.getHeight()/2, 
							( ( cachedImage.getWidth() - cachedImage.getHeight() ) / 2 ),
							0, 
							cachedImage.getHeight(), 
							cachedImage.getHeight()
							);
				} else {
					squareBitmap = Bitmap.createBitmap(
							cachedImage, 
							0, 
//							cachedImage.getHeight()/2 - cachedImage.getWidth()/2, 
							( ( cachedImage.getHeight() - cachedImage.getWidth() ) / 2 ),
							cachedImage.getWidth(), 
							cachedImage.getWidth()
							);
				}
				imageView.setImageDrawable(new CircleDrawable(squareBitmap));
			}
				break;
			case ImageDownloader.SHAPE_RECTANGLE:
			default:
				imageView.setImageBitmap(cachedImage);
				break;
			}
			imageView.setVisibility(View.VISIBLE);
		} else if (cancelPotentialDownload(url, imageView)) {
			if (sImageCache.size() > IMGAE_CACHE_LIMIT_SIZE) {
				clearCache();
			}
			while (true) {
				ImageDownloaderTask 		task = new ImageDownloaderTask(url, imageView);
				DownloadedDrawable 		downloadedDrawable = new DownloadedDrawable(task);
				imageView.setImageDrawable(downloadedDrawable);
				try {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url, String.valueOf(sx), String.valueOf(sy));
//					task.execute(url, String.valueOf(sx), String.valueOf(sy));
					break;
				} catch (RejectedExecutionException e) {
					Utils.getInstance().printLog(  DEBUG, TAG, "[download] RejectedExecutionException : " + e.getLocalizedMessage() );
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		ImageDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.mUrl;
			if ( ( bitmapUrl == null ) || ( ! bitmapUrl.equals(url) ) ) {
				bitmapDownloaderTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static ImageDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if ( imageView != null ) {
			Drawable drawable = imageView.getDrawable();
			if ( drawable instanceof DownloadedDrawable ) {
				DownloadedDrawable 		downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	public static class DownloadedDrawable extends ColorDrawable {
		
		private final WeakReference<ImageDownloaderTask> mBitmapDownloaderTaskReference;
		
		public DownloadedDrawable(ImageDownloaderTask bitmapDownloaderTask) {
			super(Color.TRANSPARENT);
			mBitmapDownloaderTaskReference = new WeakReference<ImageDownloaderTask>(bitmapDownloaderTask);
		}
		
		public ImageDownloaderTask getBitmapDownloaderTask() {
			return mBitmapDownloaderTaskReference.get();
		}
	}
}

























