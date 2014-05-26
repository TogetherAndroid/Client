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
import com.xt.together.model.FoodLike;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDetailActivity extends Activity {

//	private ImageView imgHead;
//	private ImageView imgFood;
	private Button btnBack;
//	private Button btnShare;
//	private Button btnLike;
//	private Button btnInvite;
//	private TextView txtName;
//	private TextView txtDescription;
//	private TextView txtAddress;
	private Food food;

	
	private ImageView foodDetailPic;
	private ImageView foodDetailShare;
	private TextView foodLocation;
	private ImageView foodLike1;
	private ImageView foodLike2;
	private ImageView foodLike3;
	private ImageView foodLike4;
	private ImageView foodLike5;
	private ImageLoader headLoader;
	private ImageView foodDetailInvite;
	private ImageView foodDetailLike;
	private PopupWindow popupwindow;
	private ImageView addPop;
	private RelativeLayout popupwindowLayout;
	
	
	private ImageLoader imageLoader;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Bitmap bitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fooddetail);
		
		Intent intent = getIntent();
/*		
		imgHead = (ImageView)findViewById(R.id.fooddetail_head);
		imgFood = (ImageView)findViewById(R.id.fooddetail_image);		
		btnShare = (Button)findViewById(R.id.fooddetail_share);
		btnLike = (Button)findViewById(R.id.fooddetail_like);
		btnInvite = (Button)findViewById(R.id.fooddetail_invite);
		txtName = (TextView)findViewById(R.id.fooddetail_name);
		txtDescription = (TextView)findViewById(R.id.fooddetail_description);
		txtAddress = (TextView)findViewById(R.id.fooddetail_address);
		*/
		btnBack = (Button)findViewById(R.id.fooddetail_back);
		
		foodDetailShare = (ImageView)findViewById(R.id.fooddetail_share);
		foodDetailShare.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new sendSinaTask().execute();
			}
			
		});
		
		foodLocation = (TextView)findViewById(R.id.fooddetail_location_text);
		food = (Food)intent.getSerializableExtra("food");
		foodLocation.setText(food.getAddress());
		
		foodDetailPic = (ImageView)findViewById(R.id.fooddetail_foodpic);
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(food.getImage(),null,null);
		
		foodLike1 = (ImageView)findViewById(R.id.fooddetail_like1);
		foodLike2 = (ImageView)findViewById(R.id.fooddetail_like2);
		foodLike3 = (ImageView)findViewById(R.id.fooddetail_like3);
		foodLike4 = (ImageView)findViewById(R.id.fooddetail_like4);
		foodLike5 = (ImageView)findViewById(R.id.fooddetail_like5);
		
		FoodLike[] foodlike = food.getFoodlike();
		headLoader = new ImageLoader();
		HeadLoadTask headLoadTask = new HeadLoadTask();
		headLoadTask.execute(foodlike);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View popview = inflater.inflate(R.layout.pupupwindow_fooddetail, null);
		popupwindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		popupwindow.setFocusable(true);
		popupwindow.setOutsideTouchable(true);
		
		popupwindowLayout = (RelativeLayout)popview.findViewById(R.id.popupwindow_layout);
		
		addPop = (ImageView)findViewById(R.id.fooddetail_add_button);
		addPop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(popupwindow.isShowing()){
					popupwindow.dismiss();
				}else{
					popupwindow.showAsDropDown(view, -addPop.getWidth() * 4 - popupwindowLayout.getWidth() * 2 - 45, -addPop.getHeight() - 21);
				}
			}
			
		});
		
		foodDetailInvite = (ImageView)popview.findViewById(R.id.fooddetail_popup_invite);
		foodDetailInvite.setOnClickListener(new InviteOnClickListener());
		foodDetailLike = (ImageView)popview.findViewById(R.id.fooddetail_popup_like);
		foodDetailLike.setOnClickListener(new LikeOnClickListener());
		
		btnBack.setOnClickListener(new BackOnClickListener());
/*		btnShare.setOnClickListener(new ShareOnClickListener());
		btnLike.setOnClickListener(new LikeOnClickListener());
		btnInvite.setOnClickListener(new InviteOnClickListener());
*/		
		
/*		image = intent.getStringExtra("image");
		if (image == null || image.length() == 0) {
			SharedPreferences userInfo = getSharedPreferences("user_info", 0);
			image = userInfo.getString("image_head", "");
		}
		txtName.setText(food.getName());
		txtDescription.setText(food.getDescription());
		txtAddress.setText(food.getAddress());
*/
		
		mAccessToken = AccessTokenKeeper.readAccessToken(FoodDetailActivity.this);
		mStatusesAPI = new StatusesAPI(mAccessToken);
		
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
			bitmap = result;
			foodDetailPic.setImageBitmap(result);
/*			if(null != result[1]){
				imgHead.setImageBitmap(result[1]);
			}
			*/
		}
		
	}
	
	class HeadLoadTask extends AsyncTask<Object, Void, Bitmap[]> {

		@Override
		protected Bitmap[] doInBackground(Object... params) {
			if(null == params){
				return null;
			}
			FoodLike[] myFoodLike = (FoodLike[]) params;
			Bitmap[] bitmapArray = new Bitmap[myFoodLike.length];
			for(int i = 0; i < myFoodLike.length; i++){
				bitmapArray[i] = headLoader.loadImageFromInternet(myFoodLike[i].getHead());
			}
			return bitmapArray;
			
		}

		@Override
		protected void onPostExecute(Bitmap[] result) {
			super.onPostExecute(result);
			if(result == null) {
				return;
			}
			
			if(result.length >= 1 && null != result[0]){
				foodLike1.setImageBitmap(result[0]);
			}
			
			if(result.length >= 2 && null != result[1]){
				foodLike2.setImageBitmap(result[1]);
			}
			
			if(result.length >= 3 && null != result[2]){
				foodLike3.setImageBitmap(result[2]);
			}
			
			if(result.length >= 4 && null != result[3]){
				foodLike4.setImageBitmap(result[3]);
			}
			
			if(result.length >= 5 && null != result[4]){
				foodLike5.setImageBitmap(result[4]);
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
			intent.putExtra("foodName", food.getName());
			intent.putExtra("foodAddress", food.getAddress());
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
			
			mStatusesAPI.upload(food.getName().toString(), bitmap, null, null, mListener);
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
