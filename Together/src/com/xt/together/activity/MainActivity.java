package com.xt.together.activity;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.json.JsonAnalyze;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity{
	
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
	
	private FragmentTabHost mTabHost;
	private LayoutInflater layoutInflater;
	private Button btnCamera;
	
	private String mTextviewArray[] = {"附近美食","附近餐厅","相机","朋友圈","我的美食"};
	private int mImageViewArray[] = {R.drawable.tab_nearbyfood,R.drawable.tab_nearbyrestaurant,R.drawable.tab_nearbyfood,
			 R.drawable.tab_friendcircle,R.drawable.tab_myrecipe};
	
	private final int CAPTURE_CODE = 1001;
	private final int ALBUM_CODE   = 1002;
	public static Bitmap bitmap;
	private FriendshipsAPI mFriendshipsAPI;
	private Oauth2AccessToken mAccessToken;
	private UsersAPI mUserAPI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		mUserAPI = new UsersAPI(mAccessToken);
		mUserAPI.show(Long.parseLong(mAccessToken.getUid()), mListener);
		initView();
//		mController.getPlatformInfo(MainActivity.this, SHARE_MEDIA.SINA, new UMDataListener() {
//			
//			@Override
//			public void onStart() {
//				
//			}
//			
//			@Override
//			public void onComplete(int status, Map<String, Object> info) {
//				if(status == 200 && info != null) {
//					StringBuilder sb = new StringBuilder();
//	                 Set<String> keys = info.keySet();
//	                 for(String key : keys){
//	                     sb.append(key+"="+info.get(key).toString()+"\r\n");
//	                 }
//				}
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void initView() {
		layoutInflater = LayoutInflater.from(this);
		
		TabSpec tabSpec0 = mTabHost.newTabSpec(mTextviewArray[0]).setIndicator(getTabItemView(0));
		mTabHost.addTab(tabSpec0, NearbyFoodActivity.class, null);
		
		TabSpec tabSpec1 = mTabHost.newTabSpec(mTextviewArray[1]).setIndicator(getTabItemView(1));
		mTabHost.addTab(tabSpec1, NearbyRestaurant.class, null);
		
		TabSpec tabSpec2 = mTabHost.newTabSpec(mTextviewArray[2]).setIndicator(getTabItemView(2));
		mTabHost.addTab(tabSpec2, FriendCircleActivity.class, null);
		
		TabSpec tabSpec3 = mTabHost.newTabSpec(mTextviewArray[3]).setIndicator(getTabItemView(3));
		mTabHost.addTab(tabSpec3, FriendCircleActivity.class, null);

		TabSpec tabSpec4 = mTabHost.newTabSpec(mTextviewArray[4]).setIndicator(getTabItemView(4));
		mTabHost.addTab(tabSpec4, MyRecipe.class, null);
	
		btnCamera = (Button)findViewById(R.id.main_camera);
		btnCamera.setOnClickListener(new CameraOnClickListener());
	}
	
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
	
		return view;
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


	private RequestListener mListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			constant.userScreenName = User.parse(response).screen_name;
			mFriendshipsAPI = new FriendshipsAPI(mAccessToken);
			mFriendshipsAPI.friends(constant.userScreenName, 50, 0, false, friendsListener);
			Log.e(constant.DEBUG_TAG, constant.userScreenName);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private RequestListener friendsListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			constant.friendsNames = new JsonAnalyze().jsonFriendsAnalyze(response);
			Log.e(constant.DEBUG_TAG, "friends" + constant.friendsNames[0]);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};

	class CameraOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(MainActivity.this)
				.setTitle("选择图片")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[]{"相机","来自相册"}, 0, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(which == 0) {
									Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(intent, CAPTURE_CODE);
								} else if(which == 1) {
									Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
									intent.setType("image/");
									startActivityForResult(intent, ALBUM_CODE);
								}
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
		}
		
	}
}
