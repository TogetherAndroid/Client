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
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class RestaurantDetail extends Activity {
	
	private Button btnBack;
//	private Button btnShare;
//	private Button btnStore;
//	private Button btnInvite;
//	private ImageView imageView;
//	private TextView txtName;
//	private TextView txtAddress;
//	private TextView txtSpecialty;
//	private TextView txtPhone;
	
	private Restaurant restaurant = null;
	private ImageLoader imageLoader;
	
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Bitmap bitmap = null;
	
	private ImageView restaurantDetailPic;
	private ImageView restaurantDetailShare;
	private ImageView restaurantDetailAddpop;
	private TextView restaurantDetailLocation;
	private TextView restaurantDetailShopSign;
	private TextView restaurantDetailPhone;
	private PopupWindow popupwindow;
	private RelativeLayout popupwindowLayout;
	private ImageView restaurantDetailInvite;
	private ImageView restaurantDetailLike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurantdetail);
		restaurant = (Restaurant)getIntent().getSerializableExtra("restaurant");
		btnBack = (Button)findViewById(R.id.restaurantdetail_back);
		
		
/*		btnShare = (Button)findViewById(R.id.restaurantdetail_share);
		btnInvite = (Button)findViewById(R.id.restaurantdetail_invite);
		btnStore = (Button)findViewById(R.id.restaurantdetail_like);
		imageView = (ImageView)findViewById(R.id.restaurantdetail_img);
		txtName = (TextView)findViewById(R.id.restaurantdetail_name);
		txtAddress = (TextView)findViewById(R.id.restaurantdetail_address);
		txtSpecialty = (TextView)findViewById(R.id.restaurantdetail_specialty);
		txtPhone = (TextView)findViewById(R.id.restaurantdetail_phone);
*/		
		btnBack.setOnClickListener(new BackOnClickListener());
/*		btnShare.setOnClickListener(new ShareOnClickListener());
		btnStore.setOnClickListener(new StoreOnClickListener());
		btnInvite.setOnClickListener(new InviteOnClickListener());
		
		txtName.setText(restaurant.getName());
		txtAddress.setText(restaurant.getAddress());
		txtSpecialty.setText("招牌菜：" + restaurant.getSpecialty());
		txtPhone.setText("电话：" + restaurant.getPhone());
		*/
		restaurantDetailShare = (ImageView)findViewById(R.id.restaurantdetail_share);
		restaurantDetailShare.setOnClickListener(new ShareOnClickListener());
		
		restaurantDetailPic = (ImageView)findViewById(R.id.restaurantdetail_foodpic);
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(restaurant.getImage(),null,null);
		
		restaurantDetailLocation = (TextView)findViewById(R.id.restaurantdetail_location_text);
		restaurantDetailLocation.setText(restaurant.getAddress());
		
		restaurantDetailPhone = (TextView)findViewById(R.id.restaurantdetail_phone_text);
		restaurantDetailPhone.setText(restaurant.getPhone());
		
		restaurantDetailShopSign = (TextView)findViewById(R.id.restaurantdetail_shopsign_text);
		restaurantDetailShopSign.setText(restaurant.getSpecialty());
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View popview = inflater.inflate(R.layout.pupupwindow_fooddetail, null);
		popupwindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		popupwindow.setFocusable(true);
		popupwindow.setOutsideTouchable(true);
		
		popupwindowLayout = (RelativeLayout)popview.findViewById(R.id.popupwindow_layout);
		
		restaurantDetailAddpop = (ImageView)findViewById(R.id.restaurantdetail_add_button);
		restaurantDetailAddpop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(popupwindow.isShowing()){
					popupwindow.dismiss();
				}else{
					popupwindow.showAsDropDown(view,  -restaurantDetailAddpop.getWidth() * 5, -restaurantDetailAddpop.getHeight() - 21);
				}
			}
			
		});
		
		restaurantDetailInvite = (ImageView)popview.findViewById(R.id.fooddetail_popup_invite);
		restaurantDetailInvite.setOnClickListener(new InviteOnClickListener());
		restaurantDetailLike = (ImageView)popview.findViewById(R.id.fooddetail_popup_like);
		restaurantDetailLike.setOnClickListener(new StoreOnClickListener());
		
		mAccessToken = AccessTokenKeeper.readAccessToken(RestaurantDetail.this);
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
			restaurantDetailPic.setImageBitmap(result);
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
			new sendSinaTask().execute();
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
			Intent intent = new Intent(RestaurantDetail.this,InvitingActivity.class);
			startActivity(intent);
		}
		
	}
	
	private class GetDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
        		String jsonString = new HttpData().addPostStore(constant.HTTPSTOREURL, params[0], params[1]);
        		new JsonAnalyze().jsonAddStoreAnalyze(jsonString);
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
			
			mStatusesAPI.upload(restaurant.getName().toString(), bitmap, null, null, mListener);
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
                    Toast.makeText(RestaurantDetail.this, 
                            "发送一送微博成功, id = " + status.id, 
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RestaurantDetail.this, response, Toast.LENGTH_LONG).show();
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
