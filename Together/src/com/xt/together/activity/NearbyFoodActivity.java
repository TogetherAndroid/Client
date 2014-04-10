package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyFoodActivity extends Fragment implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<Food> listNearbyFood = null;
	private ImageView settingButton  ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_nearbyfood, null);		
	}

	@Override
	public void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		settingButton = (ImageView)this.getView().findViewById(R.id.nearbyfood_setting);
		listView = (XListView)getView().findViewById(R.id.food_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		if(null == listNearbyFood){
			listNearbyFood = new ArrayList<Food>();
		}

		adapter = new StaggeredAdapter(listView, listNearbyFood, getActivity());
		adapter.notifyDataSetChanged();
		
		settingButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NearbyFoodActivity.this.getActivity(), SettingActivity.class);
				startActivity(intent);		
			}
			
		});
	}

	@Override
	public void onRefresh() {
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		new GetDataTask().execute();
		listView.stopRefresh();
	}

	@Override
	public void onLoadMore() {
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		listView.stopLoadMore();
	}	
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	String resurl = "http://192.168.1.106:8080/TogetherWeb/nearbyfood";
        	String jsonText = new HttpData().getPostNearByFood(resurl);
        	JsonAnalyze jsonAnalyze = new JsonAnalyze();
        	Food[] newFood =jsonAnalyze.jsonNearbyFoodAnalyze(jsonText);
			if (null != newFood) {
				listNearbyFood.removeAll(listNearbyFood);
				for (int i = 0; i < newFood.length; i++) {
					// Log.e("com.xt.together", newFood[i].getLike() +
					// "hahahhaa");
					listNearbyFood.add(newFood[i]);
				}
			}
        	
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.

            super.onPostExecute(result);
        }
    }
	
	private class StaggeredAdapter extends BaseAdapter {
		
		private LayoutInflater inflater;
        private List<Food> list;
        private XListView mListView;
        private ImageLoader imageLoader;

        public StaggeredAdapter(XListView xListView, List<Food> list, Context context) {
            this.list = list;
            this.mListView = xListView;
            this.imageLoader = new ImageLoader();
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            Food food = list.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_food, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScaleImageView)convertView.findViewById(R.id.item_food_img);
                viewHolder.txtShop = (TextView)convertView.findViewById(R.id.item_food_shop);
                viewHolder.txtPrice = (TextView)convertView.findViewById(R.id.item_food_price);
                viewHolder.txtShare = (TextView)convertView.findViewById(R.id.item_food_share);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.nearbyFoodLoadImage(food.getImage(), this, viewHolder);
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
				Intent intent = new Intent(NearbyFoodActivity.this.getActivity(), FoodDetailActivity.class);
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
}
