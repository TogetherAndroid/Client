package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

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
import com.xt.together.model.Invite;
import com.xt.together.utils.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class InvitedActivity extends ListFragment {
	
	private static List<Invite> listInvited = new ArrayList<Invite>();
	private InvitedAdapter adapter;
	private Oauth2AccessToken mAccessToken;
	private StatusesAPI mStatusesAPI;
	private Status status = null;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_invited, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((PullToRefreshListView) getListView()).setOnRefreshListener(new com.xt.together.control.PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
		adapter = new InvitedAdapter(getActivity(), listInvited);
        setListAdapter(adapter);
        
		mAccessToken = AccessTokenKeeper.readAccessToken(this.getActivity());
		mStatusesAPI = new StatusesAPI(mAccessToken);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(),InvitedDetailActivity.class);
		intent.putExtra("invite", listInvited.get(position - 1));
		startActivity(intent);
	}
	
	private class InvitedAdapter extends BaseAdapter {
		
		private List<Invite> list;
		private LayoutInflater inflater;
		private ImageLoader imageLoader;
		private Typeface fontFace;
		
		public InvitedAdapter(Context context, List<Invite> list) {
			this.list = list;
			this.inflater = LayoutInflater.from(context);
			this.imageLoader = new ImageLoader();
			this.fontFace = Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.item_invite, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView)convertView.findViewById(R.id.invite_item_img);
				viewHolder.txtName = (TextView)convertView.findViewById(R.id.invite_item_name);
				viewHolder.txtName.setTypeface(fontFace);
				viewHolder.txtAddress = (TextView)convertView.findViewById(R.id.invite_item_position);
				viewHolder.txtAddress.setTypeface(fontFace);
				viewHolder.txtInvited = (TextView)convertView.findViewById(R.id.invite_item_invited);
				viewHolder.txtInvited.setTypeface(fontFace);
				viewHolder.txtDate = (TextView)convertView.findViewById(R.id.invite_item_date);
				viewHolder.txtDate.setTypeface(fontFace);
				viewHolder.imgMore = (ImageView)convertView.findViewById(R.id.invite_item_more);
				viewHolder.menuLayout = (LinearLayout)convertView.findViewById(R.id.invite_item_menu);
				viewHolder.imgDelete = (ImageView)convertView.findViewById(R.id.invite_item_delete);
				viewHolder.imgShare = (ImageView)convertView.findViewById(R.id.invite_item_share);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Invite invite = list.get(position);
			imageLoader.InvitedLoadImage(invite.getImage(), this, viewHolder);
			viewHolder.id = invite.getId();
			viewHolder.txtName.setText(invite.getName());
			viewHolder.txtDate.setText(invite.getDate());
			viewHolder.txtInvited.setText(invite.getInvited());
			viewHolder.txtAddress.setText(invite.getAddress());
			viewHolder.menuLayout.setVisibility(View.GONE);
			viewHolder.imgMore.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					viewHolder.menuLayout.setVisibility(viewHolder.menuLayout.isShown()?View.GONE:View.VISIBLE);
				}
			});
			viewHolder.imgDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					SharedPreferences invitedPreferences = getActivity().getSharedPreferences("invited_list", 0);
	        			String invitedList = invitedPreferences.getString("invited_list", " ");
	        			invitedList = invitedList + "\"" + viewHolder.id + "\"";
	        			invitedPreferences.edit().putString("invited_list", invitedList).commit();
	        			list.remove(position);
	        			adapter.notifyDataSetChanged();
				}
			});
			viewHolder.imgShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mStatusesAPI.update(viewHolder.txtName.getText().toString() + viewHolder.txtDate.getText() + 
							viewHolder.txtAddress.getText() + "@" + viewHolder.txtInvited.getText() + "", null, null, mstatusListener);
				}
			});
			return convertView;
		}
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
        	String url = "http://togetherxt.duapp.com/invite";
        	Log.e(constant.DEBUG_TAG, "the send id is " +  AccessTokenKeeper.readAccessToken(getActivity()).getUid());
        	String jsonString = new HttpData().getPostInvitedData(url, AccessTokenKeeper.readAccessToken(getActivity()).getUid());
        	Invite[] newInvited = new JsonAnalyze().jsonInviteAnalyze(jsonString);
        	if( null != newInvited){
        		listInvited.removeAll(listInvited);
        		SharedPreferences invitedPreferences = getActivity().getSharedPreferences("invited_list", 0);
        		String invitedList = invitedPreferences.getString("invited_list", " ");
        		for(int i = 0; i < newInvited.length; i ++){
        			if (!invitedList.contains("\"" + newInvited[i].getId() + "\"")) {
        				listInvited.add(newInvited[i]);
        			}
        		}
        	}
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
   //             new postStatusTask().execute();
			}
			
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
		
	};
	
	public final class ViewHolder {
		public ImageView imageView;
		TextView txtName;
		TextView txtAddress;
		TextView txtDate;
		TextView txtInvited;
		ImageView imgMore;
		LinearLayout menuLayout;
		ImageView imgDelete;
		ImageView imgShare;
		String id;
	}
}
