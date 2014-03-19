package com.xt.together.activity;

import com.xt.together.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SendPhotoActivity extends Activity {
	
	private Button btnBack;
	private Button btnSend;
	private EditText txtDescription;
	private TextView txtAddress;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendphoto);
		btnBack = (Button)findViewById(R.id.sendphoto_back);
		btnSend = (Button)findViewById(R.id.sendphoto_send);
		txtDescription = (EditText)findViewById(R.id.sendphoto_description);
		txtAddress = (TextView)findViewById(R.id.sendphoto_address);
		imageView = (ImageView)findViewById(R.id.sendphoto_image);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSend.setOnClickListener(new SendOnClickListener());
		imageView.setImageBitmap(MainActivity.bitmap);
	}
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			SendPhotoActivity.this.finish();
		}
		
	}
	
	class SendOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			String description = txtDescription.getText().toString();
			String address = txtAddress.getText().toString();
		}
		
	}
}
