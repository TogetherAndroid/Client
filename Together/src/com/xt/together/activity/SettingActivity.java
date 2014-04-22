package com.xt.together.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.xt.together.R;
import com.xt.together.constant.constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity {

	private Button logoutButton;
	private Button btnBack;
	private Oauth2AccessToken mAccessToken;
	private LogOutRequestListener mLogoutRequestListener = new LogOutRequestListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		btnBack = (Button)findViewById(R.id.setting_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		logoutButton = (Button)findViewById(R.id.logoutButton);
		mAccessToken = AccessTokenKeeper.readAccessToken(SettingActivity.this);
		logoutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mAccessToken != null && mAccessToken.isSessionValid()){
					
					new LogoutAPI(mAccessToken).logout(mLogoutRequestListener);
				}
			}
			
		});
	}
	
	private class LogOutRequestListener implements RequestListener{

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");
                    
                    if ("true".equalsIgnoreCase(value)) {
                    	AccessTokenKeeper.clear(SettingActivity.this);
                        mAccessToken = null;
                    }
                    Log.e(constant.DEBUG_TAG, "logout sucess");
                    
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                	Log.e(constant.DEBUG_TAG, response);
                    e.printStackTrace();
                }
            }
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			 Log.e(constant.DEBUG_TAG, "logout failuer");
		}
		
	}
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			SettingActivity.this.finish();
		}
		
	}
}
