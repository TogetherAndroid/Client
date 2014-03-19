package com.xt.together.activity;

import com.xt.together.R;
import com.xt.together.model.Invite;
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

public class InvitedDetailActivity extends Activity {
	
	private Button btnBack;
	private Button btnSina;
	private TextView txtName;
	private TextView txtPosition;
	private TextView txtInvite;
	private TextView txtDate;
	private TextView txtAddress;
	private TextView txtPhone;
	private ImageView imageView;
	
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviteddetail);
		btnBack = (Button)findViewById(R.id.inviteddetail_back);
		txtName = (TextView)findViewById(R.id.inviteddetail_name);
		txtPosition = (TextView)findViewById(R.id.inviteddetail_position);
		txtInvite = (TextView)findViewById(R.id.inviteddetail_invite);
		txtDate = (TextView)findViewById(R.id.inviteddetail_date);
		txtAddress = (TextView)findViewById(R.id.inviteddetail_address);
		txtPhone = (TextView)findViewById(R.id.inviteddetail_phone);
		imageView = (ImageView)findViewById(R.id.inviteddetail_img);
		btnBack.setOnClickListener(new BackOnClickListener());
		Invite invite = (Invite)getIntent().getSerializableExtra("invite");
		txtName.setText(invite.getName());
		txtPosition.setText(invite.getAddress());
		txtInvite.setText(invite.getInvited());
		txtDate.setText(invite.getDate());
		txtAddress.setText(invite.getAddress());
		txtPhone.setText(invite.getPhone());
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(invite.getImage(),null,null);
		
	}

	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			InvitedDetailActivity.this.finish();
		}
		
	}
	
	class SinaOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
		}
		
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
}
