package com.xt.together.activity;

import com.xt.together.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InvitingActivity extends Activity {
	
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inviting);
		btnBack = (Button)findViewById(R.id.inviting_back);
		btnBack.setOnClickListener(new BackOnClickListener());
	}

	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			InvitingActivity.this.finish();
		}
		
	}
}
