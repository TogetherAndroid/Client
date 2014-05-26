package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NearbyFoodActivity extends BaseActivity implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<Food> listNearbyFood = new ArrayList<Food>();
	private ImageView settingButton;
	private Button btnMenu;
	private SlideMenu slideMenu;
	private RelativeLayout cameraLayout;
	private RelativeLayout nearbyfoodLayout;
	private RelativeLayout nearbyrestaurantLayout;
	private RelativeLayout friendcircleLayout;
	private RelativeLayout myrecipeLayout;

	@Override
	public void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbyfood);
		settingButton = (ImageView)findViewById(R.id.nearbyfood_setting);
		btnMenu = (Button)findViewById(R.id.nearbyfood_menu);
		slideMenu = (SlideMenu)findViewById(R.id.nearbyfood_slide);
		listView = (XListView)findViewById(R.id.food_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		adapter = new StaggeredAdapter(listNearbyFood);
		adapter.notifyDataSetChanged();
		settingButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(NearbyFoodActivity.this, SettingActivity.class);
				startActivity(intent);		
			}
			
		});
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
		nearbyfoodLayout.setBackgroundColor(Color.argb(255, 159, 219, 174));
		nearbyfoodLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		nearbyrestaurantLayout = (RelativeLayout)findViewById(R.id.menu_nearbyrestaurant);
		nearbyrestaurantLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NearbyFoodActivity.this, NearbyRestaurant.class);
				startActivity(intent);
			}
		});
		friendcircleLayout = (RelativeLayout)findViewById(R.id.menu_friendcircle);
		friendcircleLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NearbyFoodActivity.this, FriendCircleActivity.class);
				startActivity(intent);
			}
		});
		myrecipeLayout = (RelativeLayout)findViewById(R.id.menu_myrecipe);
		myrecipeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NearbyFoodActivity.this, MyRecipe.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onRefresh() {
		new GetDataTask().execute();
		listView.stopRefresh();
	}

	@Override
	public void onLoadMore() {

		listView.stopLoadMore();
	}	
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        		String jsonText = new HttpData().getPostNearByFood(constant.HTTPNEARBYFOODURL);
        		JsonAnalyze jsonAnalyze = new JsonAnalyze();
        		Food[] newFood =jsonAnalyze.jsonNearbyFoodAnalyze(jsonText);
			if (null != newFood) {
				listNearbyFood.removeAll(listNearbyFood);
				for (int i = 0; i < newFood.length; i++) {
					listNearbyFood.add(newFood[i]);
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
		
		private List<Food> list;
        private ImageLoader imageLoader;
        private Typeface fontFace;

        public StaggeredAdapter(List<Food> list) {
            this.imageLoader = new ImageLoader();
            this.list = list;
            this.fontFace = Typeface.createFromAsset(getAssets(), "font/font.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            Food food = list.get(position);

            if (convertView == null) {
            		LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            		convertView = layoutInflator.inflate(R.layout.item_food, null);
                viewHolder = new ViewHolder();
            		viewHolder.imageView = (ScaleImageView)convertView.findViewById(R.id.item_food_img);
                viewHolder.txtName = (TextView)convertView.findViewById(R.id.item_food_name);
                viewHolder.txtName.setTypeface(fontFace);
                viewHolder.txtShare = (TextView)convertView.findViewById(R.id.item_food_share);
                viewHolder.txtShare.setTypeface(fontFace);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.nearbyFoodLoadImage(food.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtName.setText(food.getName());
            viewHolder.txtShare.setText(food.getShop());
            convertView.setOnClickListener(new ViewOnClickListener(food));
            
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
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
				Intent intent = new Intent(NearbyFoodActivity.this, FoodDetailActivity.class);
				intent.putExtra("food", this.food);
				startActivity(intent);
			}
        	
        }

    }
	
	
	public final class ViewHolder {
        public ScaleImageView imageView;
        TextView txtName;
        TextView txtShare;
    }
	
	
}
