package com.xt.together.activity;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.utils.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends BaseActivity{
	
	private FriendshipsAPI mFriendshipsAPI;
	private Oauth2AccessToken mAccessToken;
	private ImageView imgNearbyfood = null;
	private ImageView imgNearbyrestaurant = null;
	private ImageView imgCamera = null;
	private ImageView imgFriendcircle = null;
	private ImageView imgMyrecipe = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		String userWeiboScreenName = userInfo.getString("screen_name", "");
		String userHttpId = userInfo.getString("http_id", "");
		constant.USERHTTPID = userHttpId;
		constant.userScreenName = userWeiboScreenName;
		mFriendshipsAPI = new FriendshipsAPI(mAccessToken);
		mFriendshipsAPI.friends(constant.userScreenName, 50, 0, false, friendsListener);
		imgNearbyfood = (ImageView)findViewById(R.id.main_nearbyfood);
		imgNearbyfood.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, NearbyFoodActivity.class);
				startActivity(intent);
			}
		});
		imgNearbyrestaurant = (ImageView)findViewById(R.id.main_nearbyrestaurant);
		imgNearbyrestaurant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NearbyRestaurant.class);
				startActivity(intent);
			}
		});
		imgCamera = (ImageView)findViewById(R.id.main_camera);
		imgCamera.setOnClickListener(new CameraOnClickListener());
		imgFriendcircle = (ImageView)findViewById(R.id.main_friendcircle);
		imgFriendcircle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, FriendCircleActivity.class);
				startActivity(intent);
			}
		});
		imgMyrecipe = (ImageView)findViewById(R.id.main_myrecipe);
		imgMyrecipe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MyRecipe.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		if(requestCode == CAPTURE_CODE) {
			bitmap = (Bitmap)data.getExtras().get("data");
		} else if(requestCode == ALBUM_CODE) {
			Uri originalUri = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), originalUri);
			} catch (Exception e) {
				return;
			}
		}
		
		Intent intent = new Intent(MainActivity.this,SendPhotoActivity.class);
		startActivity(intent);
	}
	
	private RequestListener friendsListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			constant.friendsNames = new JsonAnalyze().jsonFriendsAnalyze(response);
			constant.friendsIds = new JsonAnalyze().jsonFriendsIdAnalyze(response);
			constant.friendsHeads = new JsonAnalyze().jsonFriendsHeadAnalyze(response);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			
		}
		
	};
}
