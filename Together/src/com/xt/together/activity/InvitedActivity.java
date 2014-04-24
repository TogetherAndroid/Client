package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Invite;
import com.xt.together.utils.ImageLoader;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InvitedActivity extends ListActivity {
	
	private Button btnBack;
	private ImageView btnSetting;
	private static List<Invite> listInvited = new ArrayList<Invite>();
	private InvitedAdapter adapter;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(InvitedActivity.this,InvitedDetailActivity.class);
		intent.putExtra("invite", listInvited.get(position - 1));
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invited);
		btnBack = (Button)findViewById(R.id.invited_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSetting = (ImageView)findViewById(R.id.invited_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		((PullToRefreshListView) getListView()).setOnRefreshListener(new com.xt.together.control.PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
		adapter = new InvitedAdapter(this, listInvited);
        setListAdapter(adapter);
	}
	
	private class InvitedAdapter extends BaseAdapter {
		
		private List<Invite> list;
		private LayoutInflater inflater;
		private ImageLoader imageLoader;
		
		public InvitedAdapter(Context context, List<Invite> list) {
			this.list = list;
			this.inflater = LayoutInflater.from(context);
			this.imageLoader = new ImageLoader();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.item_invite, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView)convertView.findViewById(R.id.invite_item_img);
				viewHolder.txtName = (TextView)convertView.findViewById(R.id.invite_item_name);
				viewHolder.txtAddress = (TextView)convertView.findViewById(R.id.invite_item_position);
				viewHolder.txtInvited = (TextView)convertView.findViewById(R.id.invite_item_invited);
				viewHolder.txtDate = (TextView)convertView.findViewById(R.id.invite_item_date);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Invite invite = list.get(position);
			imageLoader.InvitedLoadImage(invite.getImage(), this, viewHolder);
			viewHolder.txtName.setText(invite.getName());
			viewHolder.txtDate.setText(invite.getDate());
			viewHolder.txtInvited.setText(invite.getInvited());
			viewHolder.txtAddress.setText(invite.getAddress());
			return convertView;
		}
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
        	String url = "http://togetherxt.duapp.com/invite";
        	Log.e(constant.DEBUG_TAG, "the send id is " +  AccessTokenKeeper.readAccessToken(InvitedActivity.this).getUid());
        	String jsonString = new HttpData().getPostInvitedData(url, AccessTokenKeeper.readAccessToken(InvitedActivity.this).getUid());
        	Invite[] newInvited = new JsonAnalyze().jsonInviteAnalyze(jsonString);
        	if( null != newInvited){
        		listInvited.removeAll(listInvited);
        		for(int i = 0; i < newInvited.length; i ++){
        			listInvited.add(newInvited[i]);
        		}
        	}
        	Log.e(constant.DEBUG_TAG, jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
        	adapter.notifyDataSetChanged();
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
	
	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			InvitedActivity.this.finish();
		}
		
	}
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(InvitedActivity.this,SettingActivity.class);
			startActivity(intent);
		}
		
	}
	
	public final class ViewHolder {
		public ImageView imageView;
		TextView txtName;
		TextView txtAddress;
		TextView txtDate;
		TextView txtInvited;
	}
}
