package com.xt.together.activity;

import com.xt.together.R;
import com.xt.together.utils.BaseActivity;
import com.xt.together.utils.ImageLoader;
import com.xt.together.utils.SlideMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyRecipe extends BaseActivity {
	
	private TextView txtName;
	private TextView txtCity;
	private ImageView imgHead;
	private RelativeLayout btnTrends;
	private RelativeLayout btnLike;
	private RelativeLayout btnMessage;
	private ImageView btnSetting;
	private ImageLoader imageLoader;
	private Button btnMenu;
	private SlideMenu slideMenu;
	private RelativeLayout cameraLayout;
	private RelativeLayout nearbyfoodLayout;
	private RelativeLayout nearbyrestaurantLayout;
	private RelativeLayout friendcircleLayout;
	private RelativeLayout myrecipeLayout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myrecipe);
		imgHead = (ImageView)findViewById(R.id.myrecipe_head);
		Typeface fontFace = Typeface.createFromAsset(getAssets(), "font/font.ttf");
		txtName = (TextView)findViewById(R.id.myrecipe_name);
		txtName.setTypeface(fontFace);
		txtCity = (TextView)findViewById(R.id.myrecipe_city);
		txtCity.setTypeface(fontFace);
		btnTrends  = (RelativeLayout)findViewById(R.id.myrecipe_item_dynamic);
		btnLike    = (RelativeLayout)findViewById(R.id.myrecipe_item_like);
		btnMessage  = (RelativeLayout)findViewById(R.id.myrecipe_item_message);
		btnTrends.setOnClickListener(new TrendsOnClickListener());
		btnLike.setOnClickListener(new LikeOnClickListener());
		btnMessage.setOnClickListener(new MessageOnClickListener());
		btnSetting = (ImageView)findViewById(R.id.myrecipe_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		String userWeiboScreenName = userInfo.getString("screen_name", "");
		String img_head = userInfo.getString("image_head", "");
		txtName.setText(userWeiboScreenName);
		txtCity.setText("武汉");
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoader = new ImageLoader();
		imageLoadTask.execute(img_head,null,null);
		
		btnMenu = (Button)findViewById(R.id.myrecipe_menu);
		slideMenu = (SlideMenu)findViewById(R.id.myrecipe_slide);
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (slideMenu.isMainScreenShowing()) {
					slideMenu.openMenu();
				} else {
					slideMenu.closeMenu();
				}
			}
		});
		cameraLayout = (RelativeLayout)findViewById(R.id.menu_camera);
		cameraLayout.setOnClickListener(new CameraOnClickListener());
		nearbyfoodLayout = (RelativeLayout)findViewById(R.id.menu_nearbyfood);
		nearbyfoodLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyRecipe.this, NearbyFoodActivity.class);
				startActivity(intent);
			}
		});
		nearbyrestaurantLayout = (RelativeLayout)findViewById(R.id.menu_nearbyrestaurant);
		nearbyrestaurantLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyRecipe.this, NearbyRestaurant.class);
				startActivity(intent);
			}
		});
		friendcircleLayout = (RelativeLayout)findViewById(R.id.menu_friendcircle);
		friendcircleLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MyRecipe.this, FriendCircleActivity.class);
				startActivity(intent);
			}
		});
		myrecipeLayout = (RelativeLayout)findViewById(R.id.menu_myrecipe);
		myrecipeLayout.setBackgroundColor(Color.argb(255, 159, 219, 174));
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
			imgHead.setImageBitmap(result);
		}
	}
	
	class LikeOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this,LikeTabActivity.class);
			startActivity(intent); 
		}
		
	}
	
	class MessageOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this,InviteTabActivity.class);
			startActivity(intent);
		}
		
	}
	
	class TrendsOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this,MyTrendsActivity.class);
			startActivity(intent);
		}
		
	}
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this,SettingActivity.class);
			startActivity(intent);
		}
		
	}
}
