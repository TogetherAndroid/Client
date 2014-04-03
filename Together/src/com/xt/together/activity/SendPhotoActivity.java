package com.xt.together.activity;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Restaurant;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SendPhotoActivity extends Activity {
	
	private Button btnBack;
	private Button btnSend;
	private EditText txtDescription;
	private TextView txtAddress;
	private ImageView imageView;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private FriendshipsAPI mFriendshipsAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendphoto);
		btnBack = (Button)findViewById(R.id.sendphoto_back);
		btnSend = (Button)findViewById(R.id.sendphoto_send);
		txtDescription = (EditText)findViewById(R.id.sendphoto_description);
		txtDescription.addTextChangedListener(freindTextWatcher);
		txtAddress = (TextView)findViewById(R.id.sendphoto_address);
		imageView = (ImageView)findViewById(R.id.sendphoto_image);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSend.setOnClickListener(new SendOnClickListener());
		imageView.setImageBitmap(MainActivity.bitmap);
		
		mAccessToken = AccessTokenKeeper.readAccessToken(SendPhotoActivity.this);
		mStatusesAPI = new StatusesAPI(mAccessToken);
		mFriendshipsAPI = new FriendshipsAPI(mAccessToken);
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
//			new GetDataTask().execute();
			mStatusesAPI.upload(description + address, MainActivity.bitmap, null, null, mstatusListener);
//			new HttpData().sendpicture(constant.HTTPLIKEURL, MainActivity.bitmap);
		}
		
	}
	
	private RequestListener mFriendshipsListner = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			if(!TextUtils.isEmpty(response)){
//				JSONArray friendlist = new JsonAnalyze().jsonWeiboFansAnalyze(response);
				Log.e(constant.DEBUG_TAG, response);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
		
	};
	
	private  RequestListener mstatusListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			if(!TextUtils.isEmpty(response)){
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                    	Log.e(constant.DEBUG_TAG, "获得微博信息成功，条数" + statuses.statusList.size());
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                    Log.e(constant.DEBUG_TAG, "获得微博信息成功，id" + status.id);
                } else {
                    Log.e("com.sina.weibo.sdk.demo", response);
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
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	String aa = new HttpData().sendpicture(constant.HTTPMYRECIPEURL, MainActivity.bitmap);
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            Toast.makeText(SendPhotoActivity.this,
                    "上传服务器", 
                    Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }
	
	public TextWatcher freindTextWatcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable text) {
			// TODO Auto-generated method stub
//			Log.e(constant.DEBUG_TAG, "after input the chars is " + text);
			if (txtDescription.getSelectionEnd() > 0) {
				char lastText = text
						.charAt(txtDescription.getSelectionEnd() - 1);
				// Log.e(constant.DEBUG_TAG, "after input the last is " +
				// text.charAt(txtDescription.getSelectionEnd() -1));
				if ('@' == lastText) {
					Log.e(constant.DEBUG_TAG, mAccessToken.getUid());
					mFriendshipsAPI.friends(mAccessToken.getUid(), 500, 0,
							false, mFriendshipsListner);
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}
		
	};
}
