package com.xt.together.activity;

import com.xt.together.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRecipe extends Fragment {
	
	private TextView txtName;
	private TextView txtCity;
	private ImageView imgHead;
	private ImageView btnTrends;
	private ImageView btnLike;
	private ImageView btnStore;
	private ImageView btnInvite;
	private ImageView btnInvited;
	private Button btnSetting;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_myrecipe, null);		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		txtName = (TextView)getView().findViewById(R.id.myrecipe_name);
		txtCity = (TextView)getView().findViewById(R.id.myrecipe_city);
		btnTrends  = (ImageView)getView().findViewById(R.id.myrecipe_mytrends);
		btnLike    = (ImageView)getView().findViewById(R.id.myrecipe_like);
		btnStore   = (ImageView)getView().findViewById(R.id.myrecipe_store);
		btnInvite  = (ImageView)getView().findViewById(R.id.myrecipe_invite);
		btnInvited = (ImageView)getView().findViewById(R.id.myrecipe_invited);
		btnTrends.setOnClickListener(new TrendsOnClickListener());
		btnLike.setOnClickListener(new LikeOnClickListener());
		btnStore.setOnClickListener(new StoreOnClickListener());
		btnInvite.setOnClickListener(new InviteOnClickListener());
		btnInvited.setOnClickListener(new InvitedOnClickListener());
		btnSetting = (Button)getView().findViewById(R.id.invite_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
	}	
	
	class LikeOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),LikeActivity.class);
			startActivity(intent); 
		}
		
	}
	
	class StoreOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),StoreActivity.class);
			startActivity(intent);
		}
		
	}
	
	class InviteOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this.getActivity(),InviteActivity.class);
			startActivity(intent);
		}
		
	}
	
	class InvitedOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),InvitedActivity.class);
			startActivity(intent); 
		}
		
	}
	
	class TrendsOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this.getActivity(),MyTrendsActivity.class);
			startActivity(intent);
		}
		
	}
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyRecipe.this.getActivity(),SettingActivity.class);
			startActivity(intent);
		}
		
	}
}
