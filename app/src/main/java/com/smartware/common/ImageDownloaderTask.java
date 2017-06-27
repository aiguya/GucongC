
package com.smartware.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

	private static final boolean				DEBUG = true;
	private static final String TAG = "ImageDownloaderTask";

	public String mUrl;
	public String mTargetUrl;
	private WeakReference<ImageView> mImageViewReference;

	public ImageDownloaderTask(String url, ImageView imageView) {
		this.mTargetUrl = url;
		this.mImageViewReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		int		sx = Integer.valueOf(params[1]);
		int 		sy = Integer.valueOf(params[2]);
		return downloadBitmap(params[0], sx, sy);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		Utils.getInstance().printLog( ! DEBUG, TAG, "[onPostExecute]");
		
		if (isCancelled()) {
			bitmap = null;
		}
		if (mImageViewReference != null) {			
			ImageView imageView = mImageViewReference.get();
			ImageDownloaderTask 	bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
			if ( this == bitmapDownloaderTask) {
				int			tag = (Integer) imageView.getTag();
				int			id = imageView.getId();
				Utils.getInstance().printLog( ! DEBUG, TAG, "[onPostExecute] tag : " + tag + ", id : " + id);
				switch (tag) {
				case ImageDownloader.SHAPE_CIRCLE:
				{
					if (bitmap != null) {
						ImageDownloader.sImageCache.put(mTargetUrl, bitmap);
						Bitmap squareBitmap;
						if (bitmap.getWidth() >= bitmap.getHeight()) {
							squareBitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/2 - bitmap.getHeight()/2, 0, bitmap.getHeight(), bitmap.getHeight());
						} else {
							squareBitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2 - bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth());
						}
						imageView.setImageDrawable(new CircleDrawable(squareBitmap));
					} else {
						imageView.setImageBitmap(BitmapFactory.decodeResource(ImageDownloader.getContext().getResources(), id));
					}
				}
					break;
				case ImageDownloader.SHAPE_RECTANGLE:
				default:
				{
					if (bitmap != null) {
						ImageDownloader.sImageCache.put(mTargetUrl, bitmap);
						imageView.setImageBitmap(bitmap);
					} else {
						imageView.setImageBitmap(BitmapFactory.decodeResource(ImageDownloader.getContext().getResources(), id));
					}
				}
					break;
				}
				imageView.setVisibility(View.VISIBLE);
			}
		}
	}

	private ImageDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof ImageDownloader.DownloadedDrawable) {
				ImageDownloader.DownloadedDrawable downloadedDrawable = (ImageDownloader.DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	public static Bitmap downloadBitmap(String url, int sx, int sy) {
		Utils.getInstance().printLog(DEBUG, TAG, "[downloadBitmap] url : " + url + ", sx : " + sx + ", sy : " + sy);
		final HttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);
		String strImageFilePath;
		try {
			strImageFilePath = ImageDownloader.getCacheFilePath() + File.separator + URLEncoder.encode(url, "utf-8");
			HttpResponse response = client.execute(getRequest);
			final int 			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Utils.getInstance().printLog(DEBUG, TAG, "[downloadBitmap] statusCode != HttpStatus.SC_OK");
				return null;
			}
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					if (Utils.getInstance().isExistedFile(strImageFilePath)) {
						Bitmap bitmap = safeDecodeBitmapFile(strImageFilePath, sx, sy);
						if (bitmap != null) {
							return bitmap;
						}
					}
					manageFileCache(ImageDownloader.getCacheFilePath());
					File file = new File(strImageFilePath);
					FileOutputStream fileOutput = new FileOutputStream(file);
					FlushedInputStream 	fis = new FlushedInputStream(inputStream);
					byte[] 						buffer = new byte[1024];   
					int 							bufferLength = 0;
					try {
						while ((bufferLength = fis.read(buffer)) > 0) {   
							fileOutput.write(buffer, 0, bufferLength);
						}
						fileOutput.flush();
						fileOutput.close();
						fis.close();
						return safeDecodeBitmapFile(strImageFilePath, sx, sy);
					} catch(IOException e) {
						e.printStackTrace();
						Utils.getInstance().printLog(DEBUG, TAG, "[downloadBitmap] IOException : " + e.getLocalizedMessage());
					}
				} finally {
					if(inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			} else {
				Utils.getInstance().printLog(DEBUG, TAG, "[downloadBitmap] HttpEntity is null");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Utils.getInstance().printLog(DEBUG, TAG, "[downloadBitmap] UnsupportedEncodingException : " + e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Utils.getInstance().printLog(DEBUG, TAG, "[downloadBitmap] Exception : " + e.getLocalizedMessage());
			getRequest.abort();
		}
		return null;
	}

	public static Bitmap safeDecodeBitmapFile(String strFilePath, int sx, int sy) {

		File file = new File(strFilePath);

		if (file.exists() == false) {
			return null;
		}

		for (int i = 0; i < 5; i++) {
			try {
				final int 						image_max_size 	= sx > sy ? sx : sy ;
				BitmapFactory.Options 	bfo 	= new BitmapFactory.Options();
				bfo.inJustDecodeBounds 		= true;
				BitmapFactory.decodeFile(strFilePath, bfo);
				if(bfo.outHeight * bfo.outWidth >= image_max_size * image_max_size) {
					bfo.inSampleSize = (int) Math.pow(2, (int) Math.round(Math.log(image_max_size / (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
				}
				bfo.inJustDecodeBounds = false;
				bfo.inPurgeable = true;
				bfo.inDither = true;
				final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);
				return bitmap;
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				ImageDownloader.clearCache();
				System.gc();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static class FlushedInputStream extends FilterInputStream {

		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long 		totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long 	bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int 	bytes = read();
					if (bytes < 0) {
						/**
						 * we reached EOF
						 * **/
						break; 
					} else {
						/**
						 * we read one byte
						 * **/
						bytesSkipped = 1;
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public static void manageFileCache(String strFolderPath) {

		long 	nLastModifiedDate = 0;
		File targetFile = null;

		while (true) {
			File[] 	arrFiles = new File(strFolderPath).listFiles();

			if (arrFiles.length < ImageDownloader.IMGAE_CACHE_LIMIT_SIZE) {
				break;
			}

			for (File file : arrFiles) {
				if (nLastModifiedDate < file.lastModified()) {
					nLastModifiedDate = file.lastModified();
					targetFile = file;
				}
			}
			if (targetFile != null) {
				targetFile.delete();
			}
		}
	}
}











