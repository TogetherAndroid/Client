package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.BaseListActivity;
import com.xt.together.utils.ImageLoader;
import com.xt.together.utils.SlideMenu;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NearbyRestaurant extends BaseListActivity {
	
	private static List<Restaurant> listNearbyRestaurant = new ArrayList<Restaurant>();
	private NearbyRestaurantAdapter adapter;
	private ImageView btnSetting;
	private SlideMenu slideMenu;
	private Button btnMenu;
	private RelativeLayout cameraLayout;
	private RelativeLayout nearbyfoodLayout;
	private RelativeLayout nearbyrestaurantLayout;
	private RelativeLayout friendcircleLayout;
	private RelativeLayout myrecipeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbyrestaurant);
		btnSetting = (ImageView)findViewById(R.id.nearbyrestaurant_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		slideMenu = (SlideMenu)findViewById(R.id.nearybyrestaurant_slide);
		btnMenu = (Button)findViewById(R.id.nearbyrestaurant_menu);
		btnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
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
				Intent intent = new Intent(NearbyRestaurant.this, NearbyFoodActivity.class);
				startActivity(intent);
			}
		});
		nearbyrestaurantLayout = (RelativeLayout)findViewById(R.id.menu_nearbyrestaurant);
		nearbyrestaurantLayout.setBackgroundColor(Color.argb(255, 159, 219, 174));
		friendcircleLayout = (RelativeLayout)findViewById(R.id.menu_friendcircle);
		friendcircleLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NearbyRestaurant.this, FriendCircleActivity.class);
				startActivity(intent);
			}
		});
		myrecipeLayout = (RelativeLayout)findViewById(R.id.menu_myrecipe);
		myrecipeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NearbyRestaurant.this, MyRecipe.class);
				startActivity(intent);
			}
		});
		initView();
	}

	private void initView() {
		
		((PullToRefreshListView) getListView()).setOnRefreshListener(new com.xt.together.control.PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        adapter = new NearbyRestaurantAdapter(this, listNearbyRestaurant);
        setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this,RestaurantDetail.class);
		intent.putExtra("restaurant", listNearbyRestaurant.get(position - 1));
		startActivity(intent);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	String jsonText = new HttpData().getPostNearbyResData(constant.HTTPNERABYRESTAURANTURL);
        	JsonAnalyze jsonAnalyze = new JsonAnalyze();
        	Restaurant[] newrestaurant =jsonAnalyze.jsonNearbyRestaurantAnalyze(jsonText);
			if (null != newrestaurant) {
				listNearbyRestaurant.removeAll(listNearbyRestaurant);
				for (int i = 0; i < newrestaurant.length; i++) {
					Log.e("com.xt.together", newrestaurant[i].getLike()
							+ "hahahhaa");
					listNearbyRestaurant.add(newrestaurant[i]);
				}
			}
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
        		adapter.notifyDataSetChanged();
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
    
    private class NearbyRestaurantAdapter extends BaseAdapter {
    	
    		private List<Restaurant> list;
    		private LayoutInflater inflater;
    		private ImageLoader imageLoader;
    		private Typeface fontFace;
    	
    		public NearbyRestaurantAdapter(Context context, List<Restaurant> list) {
    			this.list = list;
    			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			this.imageLoader = new ImageLoader();
    			this.fontFace = Typeface.createFromAsset(getAssets(), "font/font.ttf");
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
				convertView = inflater.inflate(R.layout.item_nearbyrestaurant, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView)convertView.findViewById(R.id.item_nearbyrestaurant_img);
				viewHolder.txtAverage = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_average);
				viewHolder.txtAverage.setTypeface(fontFace);
				viewHolder.txtName = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_name);
				viewHolder.txtName.setTypeface(fontFace);
				viewHolder.txtSpecialty = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_specialty);
				viewHolder.txtSpecialty.setTypeface(fontFace);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Restaurant restaurant = list.get(position);
			imageLoader.nearybyRestaurantLoadImage(restaurant.getImage(), this, viewHolder);
			viewHolder.txtAverage.setText("人均：" + restaurant.getAverage());
			viewHolder.txtName.setText(restaurant.getName());
			viewHolder.txtSpecialty.setText("招牌菜:" + restaurant.getSpecialty());
			return convertView;
		}
    	
    }
    
    public final class ViewHolder {
		public ImageView imageView;
		TextView txtName;
		TextView txtAverage;
		TextView txtSpecialty;
	}
    
    private class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(NearbyRestaurant.this,SettingActivity.class);
			startActivity(intent);
		}
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		String info = null;
		if( listNearbyRestaurant.isEmpty() == true){
			info = "nothing";
		}else{
			info =  listNearbyRestaurant.get(0).getName();
		}
		Log.e(constant.DEBUG_TAG, "when the activity is onsume we get he list name " + info );
		super.onResume();
	}

    
}
