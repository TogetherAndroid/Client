package com.xt.together.activity;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.xt.together.R;
import com.xt.together.activity.InviteDetailActivity.sendSinaTask;
import com.xt.together.constant.constant;
import com.xt.together.model.Invite;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
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
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Bitmap bitmap = null;

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
		
		mAccessToken = AccessTokenKeeper.readAccessToken(InvitedDetailActivity.this);
		mStatusesAPI = new StatusesAPI(mAccessToken);
		
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
			new sendSinaTask().execute();
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
			bitmap = result;
			imageView.setImageBitmap(result);
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
                    Toast.makeText(InvitedDetailActivity.this, 
                            "发送一送微博成功, id = " + status.id, 
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InvitedDetailActivity.this, response, Toast.LENGTH_LONG).show();
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
