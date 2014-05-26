package com.xt.together.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

public class InvitingActivity extends Activity {
	
	private Button btnBack;
	private Button btnFinish;
	private MultiAutoCompleteTextView foodNameTextView;
	private TextView dateTextView;
	private TextView addressTextView;
	private TextView nameTextView;
	private EditText phoneTextView;
	private ImageView imageView;
	private String foodName;
	private String foodImage;
	private List<String> partFriendsIds = new ArrayList<String>();
	private List<String> partFriendsNames = new ArrayList<String>();
	private List<String> partFriendsHeads = new ArrayList<String>();
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviting);
		btnBack = (Button)findViewById(R.id.inviting_back);
		btnFinish = (Button)findViewById(R.id.inviting_finish);
		foodNameTextView = (MultiAutoCompleteTextView)findViewById(R.id.inviting_menu_inviting_text);
		Typeface fontFace = Typeface.createFromAsset(getAssets(), "font/font.ttf");
		foodNameTextView.setTypeface(fontFace);
		dateTextView = (TextView)findViewById(R.id.inviting_menu_date_text);
		dateTextView.setTypeface(fontFace);
		addressTextView = (TextView)findViewById(R.id.inviting_menu_address_text);
		addressTextView.setTypeface(fontFace);
		phoneTextView = (EditText)findViewById(R.id.inviting_menu_phone_text);
		phoneTextView.setTypeface(fontFace);
		imageView = (ImageView)findViewById(R.id.inviting_img);
		nameTextView = (TextView)findViewById(R.id.inviting_name);
		nameTextView.setTypeface(fontFace);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnFinish.setOnClickListener(new finishOnClickListener());
		Intent intent = getIntent();
		foodName = intent.getStringExtra("foodName");
		foodImage = intent.getStringExtra("foodImage");
		nameTextView.setText(foodName);
		ImageLoadTask imageLoadTask = new ImageLoadTask();
		imageLoader = new ImageLoader();
		imageLoadTask.execute(foodImage,null,null);
		addressTextView.setText(intent.getStringExtra("foodAddress"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy 年 MM 月 dd 日  HH 时 mm 分");
		Date curDate = new Date(System.currentTimeMillis());
		dateTextView.setText(formatter.format(curDate));
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, constant.friendsNames);
		foodNameTextView.setAdapter(adapter);
		foodNameTextView.setThreshold(1);
		foodNameTextView.setTokenizer(new InvitingActivity.myTokenizer());
		foodNameTextView.addTextChangedListener(freindTextWatcher);
		foodNameTextView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Log.e(constant.DEBUG_TAG, constant.friendsIds[position] + "we get what we have clicked");
				if(constant.friendsIds[position] != null){
					partFriendsIds.add(constant.friendsIds[position]);
					partFriendsNames.add(constant.friendsNames[position]);
					partFriendsHeads.add(constant.friendsHeads[position]);
				}
			}
			
		});
		
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		mStatusesAPI = new StatusesAPI(mAccessToken);
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
			imageView.setImageBitmap(result);
		}
	}
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			InvitingActivity.this.finish();
		}
		
	}
	
	class finishOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			mStatusesAPI.update(foodNameTextView.getText().toString(), null, null, mstatusListener);
			new GetDataTask().execute();
		}
		
	}
	
	private  RequestListener mstatusListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			if(!TextUtils.isEmpty(response)){
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                    	Log.e(constant.DEBUG_TAG, "获得微博信息成功，条数" + statuses.statusList.size());
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                } else {
                    Log.e("com.sina.weibo.sdk.demo", response);
                }
			}
			
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
		
	};
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {

        	String url = "http://togetherxt.duapp.com/invite";
        	String jsonText = new HttpData().addPostInviting(url, foodName, addressTextView.getText().toString(),
        			dateTextView.getText().toString(), constant.USERHTTPID,
        			list2json(partFriendsIds, partFriendsNames, partFriendsHeads), "1234", foodImage);
        	Log.e(constant.DEBUG_TAG, jsonText);
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            
        }
    }
	
	public TextWatcher freindTextWatcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable text) {
			if (foodNameTextView.getSelectionEnd() > 0) {
				char lastText = text
						.charAt(foodNameTextView.getSelectionEnd() - 1);
				if ('@' == lastText) {
					if(!foodNameTextView.isPopupShowing()){
						foodNameTextView.showDropDown();
					}
				}
			}
			
			
			if(foodNameTextView.getSelectionEnd() < foodName.length()){
				text.delete(0, foodNameTextView.length());
				text.insert(0, foodName, 0, foodName.length());
				text.delete(foodName.length(), foodNameTextView.length());				
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}
		
	};
	
	public static class myTokenizer implements Tokenizer
	{

		@Override
		public int findTokenEnd(CharSequence text, int cursor) {
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
	
	public JSONArray list2json(List<String> IdList, List<String> nameList , List<String> headList){
		
		JSONArray jsonarray = new JSONArray();
		for( int i = 0; i < IdList.size() ; i ++){
			JSONObject jsonobject = new JSONObject();
			try {
				jsonobject.put("id", IdList.get(i));
				jsonobject.put("name", nameList.get(i));
				jsonobject.put("head", headList.get(i));
				jsonarray.put(i, jsonobject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return jsonarray;
	}
	
}
