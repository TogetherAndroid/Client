package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
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
	private List<Food> list = null;
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
		adapter = new StaggeredAdapter(listView, list, getActivity());
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
