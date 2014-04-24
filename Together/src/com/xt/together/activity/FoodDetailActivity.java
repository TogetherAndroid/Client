package com.xt.together.activity;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.xt.together.R;
import com.xt.together.activity.InvitedDetailActivity.sendSinaTask;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Bitmap bitmap = null;

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
		if (image == null || image.length() == 0) {
			SharedPreferences userInfo = getSharedPreferences("user_info", 0);
			image = userInfo.getString("image_head", "");
		}
		txtName.setText(food.getName());
		txtDescription.setText(food.getDescription());
		txtAddress.setText(food.getAddress());
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(food.getImage(), image, null,null);
		
		mAccessToken = AccessTokenKeeper.readAccessToken(FoodDetailActivity.this);
		mStatusesAPI = new StatusesAPI(mAccessToken);
		
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
				bitmap = result[0];
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
			new sendSinaTask().execute();
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
			Intent intent = new Intent(FoodDetailActivity.this,InvitingActivity.class);
			intent.putExtra("foodName", txtName.getText());
			intent.putExtra("foodAddress", txtAddress.getText());
			intent.putExtra("foodImage", food.getImage());
			startActivity(intent);
		}
		
	}
	
	private class GetDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
        		String jsonString = new HttpData().addPostFoodLike(constant.HTTPLIKEURL, params[0], params[1]);
        		new JsonAnalyze().jsonAddFoodLikeAnalyze(jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }
	
	
	class sendSinaTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			while(null == bitmap){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			mStatusesAPI.upload(txtName.getText().toString(), bitmap, null, null, mListener);
			return null;
		}
				
	}
	
	
	private RequestListener mListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                    Toast.makeText(FoodDetailActivity.this, 
                            "发送一送微博成功, id = " + status.id, 
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FoodDetailActivity.this, response, Toast.LENGTH_LONG).show();
                    Log.e(constant.DEBUG_TAG, response);
                }
            }
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
		
	};
}
