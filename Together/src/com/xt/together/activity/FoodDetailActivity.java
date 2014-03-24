package com.xt.together.activity;

import com.xt.together.R;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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
		Food food = (Food)intent.getSerializableExtra("food");
		txtName.setText(food.getName());
		txtDescription.setText(food.getDescription());
		txtAddress.setText(food.getAddress());
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(food.getImage(),null,null);
		
		Typeface fontFace = Typeface.createFromAsset(getAssets(), "font/font.TTF");
		
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
			imgFood.setImageBitmap(result);
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
			
		}
		
	}
	
	class InviteOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
		}
		
	}
}
