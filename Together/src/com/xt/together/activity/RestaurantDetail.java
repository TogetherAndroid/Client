package com.xt.together.activity;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantDetail extends Activity {
	
	private Button btnBack;
	private Button btnShare;
	private Button btnStore;
	private Button btnInvite;
	private ImageView imageView;
	private TextView txtTitle;
	private TextView txtName;
	private TextView txtAddress;
	private TextView txtSpecialty;
	private TextView txtPhone;
	
	private Restaurant restaurant = null;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurantdetail);
		restaurant = (Restaurant)getIntent().getSerializableExtra("restaurant");
		btnBack = (Button)findViewById(R.id.restaurantdetail_back);
		btnShare = (Button)findViewById(R.id.restaurantdetail_share);
		btnInvite = (Button)findViewById(R.id.restaurantdetail_invite);
		btnStore = (Button)findViewById(R.id.restaurantdetail_like);
		imageView = (ImageView)findViewById(R.id.restaurantdetail_img);
		txtTitle = (TextView)findViewById(R.id.restaurantdetail_title);
		txtName = (TextView)findViewById(R.id.restaurantdetail_name);
		txtAddress = (TextView)findViewById(R.id.restaurantdetail_address);
		txtSpecialty = (TextView)findViewById(R.id.restaurantdetail_specialty);
		txtPhone = (TextView)findViewById(R.id.restaurantdetail_phone);
		
		btnBack.setOnClickListener(new BackOnClickListener());
		btnShare.setOnClickListener(new ShareOnClickListener());
		btnStore.setOnClickListener(new StoreOnClickListener());
		btnInvite.setOnClickListener(new InviteOnClickListener());
		
		txtTitle.setText(restaurant.getName());
		txtName.setText(restaurant.getName());
		txtAddress.setText(restaurant.getAddress());
		txtSpecialty.setText("招牌菜：" + restaurant.getSpecialty());
		txtPhone.setText("电话：" + restaurant.getPhone());
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(restaurant.getImage(),null,null);
	}
	
	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Object... params) {
			String url = (String)params[0];
			Bitmap bitmap = imageLoader.loadImageFromInternet(url);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result == null) {
				return;
			}
			imageView.setImageBitmap(result);
		}
		
	}
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			RestaurantDetail.this.finish();
		}
		
	}
	
	class ShareOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
		}
		
	}
	
	class StoreOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new GetDataTask().execute(constant.USERHTTPID, restaurant.getId());
		}
		
	}
	
	class InviteOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
		}
		
	}
	
	private class GetDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
        	String resurl = "http://192.168.1.106:8080/TogetherWeb/store";
        	String jsonString = new HttpData().addPostStore(resurl, params[0], params[1]);
        	Log.e(constant.DEBUG_TAG, "we have send the food url" + params[0] + " and " + params[1]);
        	boolean isSuccess = false;
        	isSuccess = new JsonAnalyze().jsonAddStoreAnalyze(jsonString);
        	if(isSuccess){
        		Log.e(constant.DEBUG_TAG, "we have send the food like");
        	}
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.

            super.onPostExecute(result);
        }
    }
}
