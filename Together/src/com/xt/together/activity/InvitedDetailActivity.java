package com.xt.together.activity;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.xt.together.R;
import com.xt.together.activity.InviteDetailActivity.ImageLoadTask;
import com.xt.together.activity.InviteDetailActivity.SinaOnClickListener;
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
	private ImageLoader imageLoader;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Bitmap bitmap = null;
	
	private ImageView invitedDetailPic;
	private ImageView invitedDetailHeadPic;
	private TextView invitedDetailName;
	private TextView invitedDetailTime;
	private TextView invitedDetailLocation;
	private TextView invitedDetailPhone;
	private ImageView invitedDetailShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviteddetail);
		
		Invite invite = (Invite)getIntent().getSerializableExtra("invite");
		
		invitedDetailPic = (ImageView)findViewById(R.id.inviteddetail_foodpic);
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(invite.getImage(),null,null);
		
		invitedDetailHeadPic = (ImageView)findViewById(R.id.inviteddetail_invite_head);
		
		invitedDetailName = (TextView)findViewById(R.id.inviteddetail_invite_person);
		invitedDetailName.setText(invite.getInvited());
		
		invitedDetailTime = (TextView)findViewById(R.id.inviteddetail_time_text);
		invitedDetailTime.setText(invite.getDate());
		
		invitedDetailLocation = (TextView)findViewById(R.id.inviteddetail_location_text);
		invitedDetailLocation.setText(invite.getAddress());
		
		invitedDetailPhone = (TextView)findViewById(R.id.inviteddetail_phone_text);
		invitedDetailPhone.setText(invite.getPhone());
		
		invitedDetailShare = (ImageView)findViewById(R.id.inviteddetail_share);
		invitedDetailShare.setOnClickListener(new SinaOnClickListener());
		
		btnBack = (Button)findViewById(R.id.inviteddetail_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		
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
			invitedDetailPic.setImageBitmap(result);
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
			
			mStatusesAPI.upload(invitedDetailName.getText().toString(), bitmap, null, null, mListener);
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
