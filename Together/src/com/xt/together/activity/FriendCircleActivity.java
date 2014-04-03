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
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.content.Intent;
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
	private List<Food> list;
	private ImageView btnSetting;
	private Oauth2AccessToken mAccessToken;
	private FriendshipsAPI mFriendshipsAPI;
	private UsersAPI mUserAPI;
	
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
		list = new ArrayList<Food>();
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		list.add(Food.getFood());
		adapter = new StaggeredAdapter(list);
		adapter.notifyDataSetChanged();
		mAccessToken = AccessTokenKeeper.readAccessToken(this.getActivity());
		getWeiboFansList();
	}

	@Override
	public void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	private void getWeiboFansList(){
		
		mFriendshipsAPI = new FriendshipsAPI(mAccessToken);
		mFriendshipsAPI.followersIds(constant.userScreenName, 50, 0, mListener);
		//mFriendshipsAPI.followers(mAccessToken.getUid(), 500, 0, true,mListener);
	}
	
	private  RequestListener mListener = new RequestListener(){

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			if(!TextUtils.isEmpty(response)){
				
				Log.e(constant.DEBUG_TAG, response);
				
//				JSONArray friendids = new JsonAnalyze().jsonWeiboFansAnalyze(response);
//				Log.e(constant.DEBUG_TAG, friendids.toString());
			}
			
		}
		
		@Override
		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Log.e(constant.DEBUG_TAG, "获得微博信息成功，错误" +  info.toString());
		}
	};
	

	private class StaggeredAdapter extends BaseAdapter {
		
		private ImageLoader imageLoader;
        private List<Food> list;

        public StaggeredAdapter(List<Food> list) {
            this.list = list;
            this.imageLoader = new ImageLoader();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            Food food = list.get(position);

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
            
            imageLoader.FriendCircleLoadImage(food.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtPrice.setText(food.getPrice());
            viewHolder.txtShare.setText(food.getShare());
            viewHolder.txtShop.setText(food.getShop());
            convertView.setOnClickListener(new ViewOnClickListener(food));
            
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
        	
    			private Food food;
    	
    			public ViewOnClickListener (Food food){
    				this.food = food;
    			}

    			@Override
    			public void onClick(View v) {
    				Intent intent = new Intent(FriendCircleActivity.this.getActivity(), FoodDetailActivity.class);
    				intent.putExtra("food", this.food);
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
