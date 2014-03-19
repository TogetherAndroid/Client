package com.xt.together.activity;

import com.xt.together.R;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private TextView txtAverage;
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
		btnStore = (Button)findViewById(R.id.restaurantdetail_store);
		btnInvite = (Button)findViewById(R.id.restaurantdetail_invite);
		imageView = (ImageView)findViewById(R.id.restaurantdetail_img);
		txtTitle = (TextView)findViewById(R.id.restaurantdetail_title);
		txtName = (TextView)findViewById(R.id.restaurantdetail_name);
		txtAddress = (TextView)findViewById(R.id.restaurantdetail_address);
		txtAverage = (TextView)findViewById(R.id.restaurantdetail_average);
		txtSpecialty = (TextView)findViewById(R.id.restaurantdetail_specialty);
		txtPhone = (TextView)findViewById(R.id.restaurantdetail_phone);
		
		btnBack.setOnClickListener(new BackOnClickListener());
		btnShare.setOnClickListener(new ShareOnClickListener());
		btnStore.setOnClickListener(new StoreOnClickListener());
		btnInvite.setOnClickListener(new InviteOnClickListener());
		
		txtTitle.setText(restaurant.getName());
		txtName.setText(restaurant.getName());
		txtAddress.setText(restaurant.getAddress());
		txtAverage.setText("人均" + restaurant.getAverage());
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
			
		}
		
	}
	
	class InviteOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
		}
		
	}
}
