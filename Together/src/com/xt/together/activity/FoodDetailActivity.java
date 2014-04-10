package com.xt.together.activity;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodDetailActivity extends Activity {
	
	private ImageView imgHead;
	private ImageView imgFood;
	private Button btnBack;
	private Button btnShare;
	private Button btnLike;
	private Button btnInvite;
	private TextView txtName;
	private TextView txtDescription;
	private TextView txtAddress;
	private Food food;
	private String image;
	
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fooddetail);
		imgHead = (ImageView)findViewById(R.id.fooddetail_head);
		imgFood = (ImageView)findViewById(R.id.fooddetail_image);
		btnBack = (Button)findViewById(R.id.fooddetail_back);
		btnShare = (Button)findViewById(R.id.fooddetail_share);
		btnLike = (Button)findViewById(R.id.fooddetail_like);
		btnInvite = (Button)findViewById(R.id.fooddetail_invite);
		txtName = (TextView)findViewById(R.id.fooddetail_name);
		txtDescription = (TextView)findViewById(R.id.fooddetail_description);
		txtAddress = (TextView)findViewById(R.id.fooddetail_address);
		
		btnBack.setOnClickListener(new BackOnClickListener());
		btnShare.setOnClickListener(new ShareOnClickListener());
		btnLike.setOnClickListener(new LikeOnClickListener());
		btnInvite.setOnClickListener(new InviteOnClickListener());
		
		Intent intent = getIntent();
		food = (Food)intent.getSerializableExtra("food");
		image = intent.getStringExtra("image");
		txtName.setText(food.getName());
		txtDescription.setText(food.getDescription());
		txtAddress.setText(food.getAddress());
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(food.getImage(), image, null,null);
		
		
		//Typeface fontFace = Typeface.createFromAsset(getAssets(), "font/font.TTF");
		
	}
	
	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap[]> {

		@Override
		protected Bitmap[] doInBackground(Object... params) {
			String url = (String)params[0];
			Bitmap[] bitmapArray = new Bitmap[2];
			bitmapArray[0] = imageLoader.loadImageFromInternet(url);
			bitmapArray[1] = imageLoader.loadImageFromInternet((String)params[1]);
			return bitmapArray;
		}

		@Override
		protected void onPostExecute(Bitmap[] result) {
			super.onPostExecute(result);
			if(result == null) {
				return;
			}
			if(null != result[0]){
				imgFood.setImageBitmap(result[0]);
			}
			if(null != result[1]){
				imgHead.setImageBitmap(result[1]);
			}
		}
		
	}

	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			FoodDetailActivity.this.finish();
		}
		
	}
	
	class ShareOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
		}
		
	}
	
	class LikeOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new GetDataTask().execute(food.getId(), constant.USERHTTPID);
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
        	String resurl = "http://192.168.1.106:8080/TogetherWeb/like";
        	String jsonString = new HttpData().addPostFoodLike(resurl, params[0], params[1]);
        	Log.e(constant.DEBUG_TAG, "we have send the food url" + params[0] + " and " + params[1]);
        	boolean isSuccess = false;
        	isSuccess = new JsonAnalyze().jsonAddFoodLikeAnalyze(jsonString);
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
