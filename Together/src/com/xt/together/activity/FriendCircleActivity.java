package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.model.FriendsCircle;
import com.xt.together.utils.BaseActivity;
import com.xt.together.utils.ImageLoader;
import com.xt.together.utils.SlideMenu;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendCircleActivity extends BaseActivity implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<FriendsCircle> listFriendCircle = new ArrayList<FriendsCircle>();
	private ImageView btnSetting;
	private Oauth2AccessToken mAccessToken;
	private FriendshipsAPI mFriendshipsAPI;
	private Button btnMenu;
	private SlideMenu slideMenu;
	private RelativeLayout cameraLayout;
	private RelativeLayout nearbyfoodLayout;
	private RelativeLayout nearbyrestaurantLayout;
	private RelativeLayout friendcircleLayout;
	private RelativeLayout myrecipeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendcircle);
		btnMenu = (Button)findViewById(R.id.friendcircle_menu);
		slideMenu = (SlideMenu)findViewById(R.id.friendcircle_slide);
		btnSetting = (ImageView)findViewById(R.id.friendcircle_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		listView = (XListView)findViewById(R.id.friendcircle_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		adapter = new StaggeredAdapter(listFriendCircle);
		adapter.notifyDataSetChanged();
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (slideMenu.isMainScreenShowing()) {
					slideMenu.openMenu();
				} else {
					slideMenu.closeMenu();
				}
			}
		});
		cameraLayout = (RelativeLayout)findViewById(R.id.menu_camera);
		cameraLayout.setOnClickListener(new CameraOnClickListener());
		nearbyfoodLayout = (RelativeLayout)findViewById(R.id.menu_nearbyfood);
		nearbyfoodLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FriendCircleActivity.this, NearbyFoodActivity.class);
				startActivity(intent);
			}
		});
		nearbyrestaurantLayout = (RelativeLayout)findViewById(R.id.menu_nearbyrestaurant);
		nearbyrestaurantLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FriendCircleActivity.this, NearbyRestaurant.class);
				startActivity(intent);
			}
		});
		friendcircleLayout = (RelativeLayout)findViewById(R.id.menu_friendcircle);
		friendcircleLayout.setBackgroundColor(Color.argb(255, 159, 219, 174));
		myrecipeLayout = (RelativeLayout)findViewById(R.id.menu_myrecipe);
		myrecipeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FriendCircleActivity.this, MyRecipe.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
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
			if(!TextUtils.isEmpty(response)){
				JSONArray friendids = new JsonAnalyze().jsonWeiboFansAnalyze(response);
				new GetDataTask().execute(friendids);
			}
			
		}
		
		@Override
		public void onWeiboException(WeiboException e) {
		}
	};
	
	private class GetDataTask extends AsyncTask<JSONArray, Void, String[]> {

        @Override
        protected String[] doInBackground(JSONArray... params) {
        		String jsonString = new HttpData().sendFriendids(constant.HTTPFRIENDCIRCLE, params[0]);
        		FriendsCircle[] newFriendsCircle = new JsonAnalyze().jsonFriendsCircleAnalyze(jsonString);
        		if(null != newFriendsCircle){
        			listFriendCircle.remove(listFriendCircle);
        			for(int i = 0; i < newFriendsCircle.length ; i ++){
        				listFriendCircle.add(newFriendsCircle[i]);
        			}
        		}
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
        		adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }
	

	private class StaggeredAdapter extends BaseAdapter {
		
		private ImageLoader imageLoader;
        private List<FriendsCircle> list;
        private Typeface fontFace;

        public StaggeredAdapter(List<FriendsCircle> list) {
            this.list = list;
            this.imageLoader = new ImageLoader();
            this.fontFace = Typeface.createFromAsset(getAssets(), "font/font.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            FriendsCircle friendsCircle = list.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.item_food, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScaleImageView) convertView.findViewById(R.id.item_food_img);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.item_food_name);
                viewHolder.txtName.setTypeface(fontFace);
                viewHolder.txtShare = (TextView) convertView.findViewById(R.id.item_food_share);
                viewHolder.txtShare.setTypeface(fontFace);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.FriendCircleLoadImage(friendsCircle.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtName.setText(friendsCircle.getName());
            viewHolder.txtShare.setText(friendsCircle.getAddress());
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
    				Intent intent = new Intent(FriendCircleActivity.this, FoodDetailActivity.class);
    				intent.putExtra("food", food);
    				startActivity(intent);
    			}
        }
    }
	
	public final class ViewHolder {
        public ScaleImageView imageView;
        TextView txtName;
        TextView txtShare;
    }
	
	private class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(FriendCircleActivity.this,SettingActivity.class);
			startActivity(intent);
		}
		
	}
	

	
}
