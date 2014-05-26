package com.xt.together.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.xt.together.R;
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

public class InviteDetailActivity extends Activity {
	
	private Button btnBack;
	private ImageLoader imageLoader;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Bitmap bitmap = null;
	
	private ImageView inviteDetailPic;
	private ImageLoader headLoader;
	private ImageView inviteDetailHeadPic;
	private TextView inviteDetailName;
	private TextView inviteDetailTime;
	private TextView inviteDetailLocation;
	private TextView inviteDetailPhone;
	private ImageView inviteDetailShare;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitedetail);
		
		Invite invite = (Invite)getIntent().getSerializableExtra("invite");
		
		inviteDetailPic = (ImageView)findViewById(R.id.invitedetail_foodpic);
		imageLoader = new ImageLoader();
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoadTask.execute(invite.getImage(),null,null);
		
		inviteDetailHeadPic = (ImageView)findViewById(R.id.invitedetail_invite_head);
		
		inviteDetailName = (TextView)findViewById(R.id.invitedetail_invite_person);
		inviteDetailName.setText(invite.getInvited());
		
		inviteDetailTime = (TextView)findViewById(R.id.invitedetail_time_text);
		inviteDetailTime.setText(invite.getDate());
		
		inviteDetailLocation = (TextView)findViewById(R.id.invitedetail_location_text);
		inviteDetailLocation.setText(invite.getAddress());
		
		inviteDetailPhone = (TextView)findViewById(R.id.invitedetail_phone_text);
		inviteDetailPhone.setText(invite.getPhone());
		
		inviteDetailShare = (ImageView)findViewById(R.id.invitedetail_share);
		inviteDetailShare.setOnClickListener(new SinaOnClickListener());
		
		btnBack = (Button)findViewById(R.id.invitedetail_back);

		btnBack.setOnClickListener(new BackOnClickListener());

				
		mAccessToken = AccessTokenKeeper.readAccessToken(InviteDetailActivity.this);
		mStatusesAPI = new StatusesAPI(mAccessToken);
	}
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			InviteDetailActivity.this.finish();
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
			inviteDetailPic.setImageBitmap(result);
		}
		
	}
	
	class HeadLoadTask extends AsyncTask<Object, Void, Bitmap> {

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
			inviteDetailHeadPic.setImageBitmap(result);
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
			
			mStatusesAPI.upload(inviteDetailName.getText().toString(), bitmap, null, null, mListener);
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
                    Toast.makeText(InviteDetailActivity.this, 
                            "发送一送微博成功, id = " + status.id, 
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InviteDetailActivity.this, response, Toast.LENGTH_LONG).show();
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
