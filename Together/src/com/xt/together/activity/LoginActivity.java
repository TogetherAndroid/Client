package com.xt.together.activity;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.widget.LoginButton;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity{

//	private Button loginButton;
	private LoginButton loginButton;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken;
	private UsersAPI mUserAPI;
	private SsoHandler mSsoHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if("" != AccessTokenKeeper.readAccessToken(LoginActivity.this).getUid()){
			Log.e(constant.DEBUG_TAG, AccessTokenKeeper.readAccessToken(LoginActivity.this).toString());
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
		
		setContentView(R.layout.activity_login);
		
		mWeiboAuth = new WeiboAuth(this, constant.APP_KEY,constant.REDIRECT_URL, constant.SCOPE);
		
		loginButton = (LoginButton)findViewById(R.id.login_button_default);
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mSsoHandler = new SsoHandler(LoginActivity.this, mWeiboAuth);
				mSsoHandler.authorize(new AuthListener());
			}});
	}
	
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if(mAccessToken.isSessionValid()){
				Log.w(constant.DEBUG_TAG, "更新token");
				
				AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
				Log.e(constant.DEBUG_TAG, "the user id is" + AccessTokenKeeper.readAccessToken(LoginActivity.this).getUid());
				
				mUserAPI = new UsersAPI(mAccessToken);
				mUserAPI.show(Long.parseLong(mAccessToken.getUid()), mUserListener);
				

			} else {
				String code = values.getString("code");
				Log.e(constant.DEBUG_TAG, "收到的错误code为" + code);
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
			
	}
	
	private RequestListener mUserListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			Log.e(constant.DEBUG_TAG, "we get the weibo image" + User.parse(response).profile_image_url);
			
			SharedPreferences userInfo = getSharedPreferences("user_info", 0);
			userInfo.edit().putString("screen_name", User.parse(response).screen_name).commit();
			boolean issuccess = userInfo.edit().putString("image_head", User.parse(response).profile_image_url).commit();
			
			Log.e(constant.DEBUG_TAG, "we have store the image" + issuccess + "the image is " + userInfo.getString("image_head", ""));
			
			new GetDataTask().execute(mAccessToken.getUid(), User.parse(response).profile_image_url, User.parse(response).screen_name);
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(mSsoHandler != null){
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.e(constant.DEBUG_TAG, "what's up");
		}
		
	}
	
	private class GetDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
        	String jsonString = new HttpData().addPostWeiboIdData(constant.HTTPUSERURL, params[0], params[2], params[1]);
        	String HttpId = new JsonAnalyze().jsonIDAnalyze(jsonString);
        	if(null != HttpId && "" != HttpId){
        		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
        		userInfo.edit().putString("http_id", HttpId).commit();
        		constant.USERHTTPID = HttpId;
        	}
        	
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {

			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
            super.onPostExecute(result);
        }
    }

}
