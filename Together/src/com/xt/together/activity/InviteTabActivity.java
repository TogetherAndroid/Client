package com.xt.together.activity;

import com.xt.together.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;

public class InviteTabActivity extends FragmentActivity {

	private FragmentTabHost tabHost;
	private LayoutInflater layoutInflater;
	private Button btnBack;
	private ImageView btnSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitetab);
		layoutInflater = LayoutInflater.from(this);
		tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		tabHost.setup(this, getSupportFragmentManager(),R.id.invitetab_content);
		
		View invitedView = getTabItemView(R.drawable.tab_item_invited);
		TabSpec invitedSpec = tabHost.newTabSpec("invited").setIndicator(invitedView);
		tabHost.addTab(invitedSpec, InvitedActivity.class, null);
		
		View invitingView = getTabItemView(R.drawable.tab_item_inviting);
		TabSpec invitingSpec = tabHost.newTabSpec("inviting").setIndicator(invitingView);
		tabHost.addTab(invitingSpec, InviteActivity.class, null);
		
		tabHost.setCurrentTab(0);
		
		btnBack = (Button)findViewById(R.id.invitetab_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSetting = (ImageView)findViewById(R.id.invitetab_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
	}
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			InviteTabActivity.this.finish();
		}
		
	}
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(InviteTabActivity.this, SettingActivity.class);
			startActivity(intent);
		}
		
	}
	
	private View getTabItemView(int imageResource){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.tab_item_img);
		imageView.setBackgroundResource(imageResource);
		
		return view;
	}
	
}
