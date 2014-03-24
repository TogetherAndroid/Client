package com.xt.together.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.xt.together.R;
import com.xt.together.activity.FriendCircleActivity;
import com.xt.together.activity.InviteActivity;
import com.xt.together.activity.InvitedActivity;
import com.xt.together.activity.LikeActivity;
import com.xt.together.activity.MyTrendsActivity;
import com.xt.together.activity.NearbyFoodActivity;
import com.xt.together.activity.NearbyRestaurant;
import com.xt.together.activity.StoreActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.BaseAdapter;


public class ImageLoader {
	private static final int MAX_CAPACITY = 1000;// 一级缓存的最大空间
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// 定时清理缓存

	// 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// 当超过一级缓存阈值的时候，将老的值从一级缓存搬到二级缓存
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};
	// 二级缓存，采用的是软应用，只有在内存吃紧的时候软应用才会被回收，有效的避免了oom
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			MAX_CAPACITY / 2);

	// 定时清理缓存
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};
	private Handler mPurgeHandler = new Handler();

	// 重置缓存清理的timer
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * 清理缓存
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	/**
	 * 返回缓存，如果没有则返回null
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// 从一级缓存中拿
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getFromSecondLevelCache(url);// 从二级缓存中拿
		return bitmap;
	}

	/**
	 * 从二级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// 由于内存吃紧，软引用已经被gc回收了
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * 从一级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}


	/**
	 * 普通图片加载
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap bloadImage(String url) {
		// resetPurgeTimer();
		Bitmap bitmap = loadImageFromInternet(url);// 获取网络图片
		return bitmap;

	}

	

	/**
	 * 放入缓存
	 * 
	 * @param url
	 * @param value
	 */
	public void addImage2Cache(String url, Bitmap value) {
		if (value == null || url == null) {
			return;
		}
		synchronized (mFirstLevelCache) {
			mFirstLevelCache.put(url, value);
		}
	}

	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		String url;
		BaseAdapter adapter;

		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[0];
			adapter = (BaseAdapter) params[1];
			Bitmap drawable = loadImageFromInternet(url);// 获取网络图片
			return drawable;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				return;
			}
			addImage2Cache(url, result);// 放入缓存
			adapter.notifyDataSetChanged();// 触发getView方法执行，这个时候getView实际上会拿到刚刚缓存好的图片
		}
	}

	public Bitmap loadImageFromInternet(String url) {
		Bitmap bitmap = null;
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 50000);
		HttpConnectionParams.setSocketBufferSize(params, 50000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return bitmap;
	}
	
	/*
	 * 附近餐厅
	 */
	public void nearybyRestaurantLoadImage(String url, BaseAdapter adapter, NearbyRestaurant.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 附近美食
	 */
	public void nearbyFoodLoadImage(String url, BaseAdapter adapter, NearbyFoodActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 喜欢的美食
	 */
	public void likeLoadImage(String url, BaseAdapter adapter, LikeActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 收藏的餐厅
	 */
	public void storeLoadImage(String url, BaseAdapter adapter, StoreActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 朋友圈
	 */
	public void FriendCircleLoadImage(String url, BaseAdapter adapter, FriendCircleActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 收到的邀请
	 */
	public void InvitedLoadImage(String url, BaseAdapter adapter, InvitedActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 收到的邀请
	 */
	public void InviteLoadImage(String url, BaseAdapter adapter, InviteActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
	/*
	 * 我的动态
	 */
	public void MyTrendsLoadImage(String url, BaseAdapter adapter, MyTrendsActivity.ViewHolder viewHolder) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);// 缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, viewHolder);
		} else {
			viewHolder.imageView.setImageBitmap(bitmap);// 设为缓存图片
		}
	}
	
}
