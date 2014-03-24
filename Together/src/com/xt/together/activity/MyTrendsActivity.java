package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.model.Food;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MyTrendsActivity extends Activity implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private List<Food> list;
	private Button btnBack;
	private Button btnSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytrends);
		btnBack = (Button)findViewById(R.id.mytrends_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSetting = (Button)findViewById(R.id.invite_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		listView = (XListView)findViewById(R.id.mytrends_list);
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
	}

	@Override
	protected void onResume() {
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
                convertView = layoutInflator.inflate(R.layout.item_mytrends, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScaleImageView) convertView.findViewById(R.id.item_mytrends_img);
                viewHolder.txtShop = (TextView) convertView.findViewById(R.id.item_mytrends_shop);
                viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.item_mytrends_price);
                viewHolder.txtShare = (TextView) convertView.findViewById(R.id.item_mytrends_share);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.MyTrendsLoadImage(food.getImage(), this, viewHolder);
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
				Intent intent = new Intent(MyTrendsActivity.this, FoodDetailActivity.class);
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

	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			MyTrendsActivity.this.finish();
		}
		
	}
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyTrendsActivity.this,SettingActivity.class);
			startActivity(intent);
		}
		
	}
}
