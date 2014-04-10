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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.TextView;
import android.widget.Toast;

public class SendPhotoActivity extends Activity {
	
	private Button btnBack;
	private Button btnSend;
	private MultiAutoCompleteTextView txtDescription;
	private TextView txtAddress;
	private ImageView imageView;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private FriendshipsAPI mFriendshipsAPI;
	private Status status = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendphoto);
		btnBack = (Button)findViewById(R.id.sendphoto_back);
		btnSend = (Button)findViewById(R.id.sendphoto_send);
		txtDescription = (MultiAutoCompleteTextView)findViewById(R.id.sendphoto_description);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, constant.friendsNames);
		txtDescription.setAdapter(adapter);
		txtDescription.setThreshold(1);
		txtDescription.setTokenizer(new SendPhotoActivity.myTokenizer());   
		
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
			new GetDataTask().execute();
//			mStatusesAPI.upload(description + address, MainActivity.bitmap, null, null, mstatusListener);
//			new postStatusTask().execute();
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
                    status = Status.parse(response);
                    Log.e(constant.DEBUG_TAG, "获得微博信息成功，id" + status.id);
                } else {
                    Log.e("com.sina.weibo.sdk.demo", response);
                }
                new postStatusTask().execute();
			}
			
		}

		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
		
	};
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
        	//String aa = new HttpData().sendpicture(constant.HTTPMYRECIPEURL, MainActivity.bitmap);
        	String picAddress = new HttpData().sendPicUseUpload(MainActivity.bitmap);
  //      	Log.e(constant.DEBUG_TAG,"aa is" + aa);
        	
            return picAddress;
        }

        @Override
        protected void onPostExecute(String result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
        	new postStatusTask().execute(result);
 //           super.onPostExecute(result);
        }
    }
	
	private class postStatusTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
 //       	 Log.e(constant.DEBUG_TAG, "the status" +constant.HTTPMYRECIPEURL + constant.userScreenName + "wuhan" + txtDescription.getText().toString() + status.id ); 
 //           String aa = new HttpData().addPostStatusData(constant.HTTPMYRECIPEURL, constant.userScreenName, "wuhan", txtDescription.getText().toString(), status.id);
        	
        	String myurl = "http://192.168.1.106:8080/TogetherWeb/dynamic";
        	String aa = new HttpData().addPostStatusData(myurl, constant.userScreenName, "wuhan", txtDescription.getText().toString(), params[0], constant.USERHTTPID);
            Log.e(constant.DEBUG_TAG, "we get the status response" + aa);        	
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.

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
				if ('@' == lastText) {
					if(!txtDescription.isPopupShowing()){
						txtDescription.showDropDown();
					}
					Log.e(constant.DEBUG_TAG, "i have found @");
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
	
	public static class myTokenizer implements Tokenizer
	{

		@Override
		public int findTokenEnd(CharSequence text, int cursor) {
			// TODO Auto-generated method stub
			int i = cursor;
			int len = text.length();
			
			while(i < len){
				if(text.charAt(i) == '.'){
					return i;
				}else {
					i++;
				}
			}
			return len;
		}

		@Override
		public int findTokenStart(CharSequence text, int cursor) {
			// TODO Auto-generated method stub
			int i = cursor;
			int len = text.length();
			
			while(i< len){
				if(text.charAt(i) == '.'){
					if(i > 1){
						return i-1;
					}else{
						return 0;
					}
				}else {
					i++;
				}
			}
			if(len > 1){
				return len -1 ;
			}else{
				return len;
			}
			
			
		}

		@Override
		public CharSequence terminateToken(CharSequence text) {
			// TODO Auto-generated method stub
			int i = text.length();
			
			while(i > 0 && text.charAt(i-1) == ' '){
				i --;
			}
			
			if( i > 0 && text.charAt( i -1 ) == '.'){
				return text;
			}else {
				if(text instanceof Spanned){
					SpannableString sp = new SpannableString(text + String.valueOf('.'));
					TextUtils.copySpansFrom((Spanned)text, 0, text.length(), Object.class, sp, 0);
					return sp;
				} else {
					return text + String.valueOf('.');
				}
			}
		}
		
	}
}
