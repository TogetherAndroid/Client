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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

//	private Button logoutButton;
	private ImageView settingDoneBtn;
	private LinearLayout changeCityLayout;
	private TextView changeCityText;
	private CheckBox bindSinaCheckBox;
	private CheckBox pushCheckBox;
	private LinearLayout newLayout;
	private RelativeLayout aboutusLayout;
	private Button btnBack;
	private Oauth2AccessToken mAccessToken;
	private LogOutRequestListener mLogoutRequestListener = new LogOutRequestListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mAccessToken = AccessTokenKeeper.readAccessToken(SettingActivity.this);
		changeCityText = (TextView)findViewById(R.id.setting_current_city);
		changeCityLayout = (LinearLayout)findViewById(R.id.setting_change_city_layout);
		changeCityLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				new AlertDialog.Builder(SettingActivity.this)
				.setTitle("请选择城市")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(constant.citys, 0, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							changeCityText.setText(constant.citys[which]);
							dialog.dismiss();
						}
					}	
						
				)
				.setNegativeButton("取消", null)
				.show();
				
			}
			
		});
		
		newLayout = (LinearLayout)findViewById(R.id.setting_new_layout);
		newLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				
			}
			
		});
		
		aboutusLayout = (RelativeLayout)findViewById(R.id.setting_aboutus_layout);
		aboutusLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		bindSinaCheckBox = (CheckBox)findViewById(R.id.setting_bind_sina_checkbox);
		bindSinaCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
					if(mAccessToken != null && mAccessToken.isSessionValid()){
						
						new LogoutAPI(mAccessToken).logout(mLogoutRequestListener);
					}
				}

			}
			
		});
		
		pushCheckBox = (CheckBox)findViewById(R.id.setting_push_checkbox);
		pushCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					Toast.makeText(SettingActivity.this, "推送", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		btnBack = (Button)findViewById(R.id.setting_back);
		btnBack.setOnClickListener(new BackOnClickListener());
//		logoutButton = (Button)findViewById(R.id.logoutButton);
		
/*		logoutButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mAccessToken != null && mAccessToken.isSessionValid()){
					
					new LogoutAPI(mAccessToken).logout(mLogoutRequestListener);
				}
			}
			
		});
	*/
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
