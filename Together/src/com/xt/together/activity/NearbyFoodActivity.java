package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	private static List<Food> listNearbyFood = new ArrayList<Food>();
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
		adapter = new StaggeredAdapter(listNearbyFood);
		adapter.notifyDataSetChanged();
		settingButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(NearbyFoodActivity.this.getActivity(), SettingActivity.class);
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

        public StaggeredAdapter(List<Food> list) {
            this.imageLoader = new ImageLoader();
            this.list = list;
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
            viewHolder.txtPrice.setText("人均:" + food.getPrice());
            viewHolder.txtShare.setText(food.getShare() + "人喜欢");
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
