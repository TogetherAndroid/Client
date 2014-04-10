package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.model.FriendsCircle;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendCircleActivity extends Fragment implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<FriendsCircle> listFriendCircle;
	private ImageView btnSetting;
	private Oauth2AccessToken mAccessToken;
	private FriendshipsAPI mFriendshipsAPI;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.activity_friendcircle, null);		
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btnSetting = (ImageView)getView().findViewById(R.id.friendcircle_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		listView = (XListView)getView().findViewById(R.id.friendcircle_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		if(null == listFriendCircle){
			listFriendCircle = new ArrayList<FriendsCircle>();
		}
		adapter = new StaggeredAdapter(listFriendCircle);
		adapter.notifyDataSetChanged();
		mAccessToken = AccessTokenKeeper.readAccessToken(this.getActivity());

	}

	@Override
	public void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		Log.e(constant.DEBUG_TAG, "we start to send ids" + System.currentTimeMillis());
		mFriendshipsAPI = new FriendshipsAPI(mAccessToken);
		mFriendshipsAPI.followersIds(constant.userScreenName, 50, 0, mListener);
		listView.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		listView.stopLoadMore();
	}
	
	private  RequestListener mListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			if(!TextUtils.isEmpty(response)){
				
				JSONArray friendids = new JsonAnalyze().jsonWeiboFansAnalyze(response);
				Log.e(constant.DEBUG_TAG, friendids.toString() + System.currentTimeMillis());				
				new GetDataTask().execute(friendids);
			}
			
		}
		
		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
	};
	
	private class GetDataTask extends AsyncTask<JSONArray, Void, String[]> {

        @Override
        protected String[] doInBackground(JSONArray... params) {
        	String resurl = "http://192.168.1.106:8080/TogetherWeb/friendcircle";
        	String jsonString = new HttpData().sendFriendids(resurl, params[0]);
        	Log.e(constant.DEBUG_TAG, "we start to get json" + System.currentTimeMillis());
        	FriendsCircle[] newFriendsCircle = new JsonAnalyze().jsonFriendsCircleAnalyze(jsonString);
        	if(null != newFriendsCircle){
        		listFriendCircle.remove(listFriendCircle);
        		for(int i = 0; i < newFriendsCircle.length ; i ++){
        			listFriendCircle.add(newFriendsCircle[i]);
        		}
        	}
        	Log.e(constant.DEBUG_TAG, jsonString.toString() + System.currentTimeMillis());
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
        	adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }
	

	private class StaggeredAdapter extends BaseAdapter {
		
		private ImageLoader imageLoader;
        private List<FriendsCircle> list;

        public StaggeredAdapter(List<FriendsCircle> list) {
            this.list = list;
            this.imageLoader = new ImageLoader();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            FriendsCircle friendsCircle = list.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.item_friendcircle, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScaleImageView) convertView.findViewById(R.id.item_circle_img);
                viewHolder.txtShop = (TextView) convertView.findViewById(R.id.item_circle_shop);
                viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.item_circle_price);
                viewHolder.txtShare = (TextView) convertView.findViewById(R.id.item_circle_share);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.FriendCircleLoadImage(friendsCircle.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtPrice.setText(friendsCircle.getName());
            viewHolder.txtShare.setText(friendsCircle.getAddress());
            viewHolder.txtShop.setText(friendsCircle.getDescription());
            convertView.setOnClickListener(new ViewOnClickListener(friendsCircle));
            
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }
        
        class ViewOnClickListener implements OnClickListener {
        	
    			private FriendsCircle friendsCircle;
    	
    			public ViewOnClickListener (FriendsCircle friendsCircle){
    				this.friendsCircle = friendsCircle;
    			}

    			@Override
    			public void onClick(View v) {
    				Food food = new Food(null, friendsCircle.getName(), null, null, friendsCircle.getImage(),
    						friendsCircle.getAddress(), friendsCircle.getDescription(), friendsCircle.getFoodLike());
    				Intent intent = new Intent(FriendCircleActivity.this.getActivity(), FoodDetailActivity.class);
    				intent.putExtra("food", food);
    				startActivity(intent);
    			}
        }
    }
	
	public final class ViewHolder {
        public ScaleImageView imageView;
        TextView txtShop;
        TextView txtPrice;
        TextView txtShare;
    }
	
	private class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(FriendCircleActivity.this.getActivity(),SettingActivity.class);
			startActivity(intent);
		}
		
	}
	

	
}
