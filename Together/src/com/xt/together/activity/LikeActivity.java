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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeActivity extends Activity implements IXListViewListener{
	
	private Button btnBack;
	private ImageView btnSetting;
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<Food> listLike = new ArrayList<Food>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_like);
		btnBack = (Button)findViewById(R.id.like_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSetting = (ImageView)findViewById(R.id.like_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		listView = (XListView)findViewById(R.id.like_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		adapter = new StaggeredAdapter(listLike);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	class BackOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			LikeActivity.this.finish();
		}
		
	}

	@Override
	public void onRefresh() {
		
		new GetDataTask().execute();
		
		listView.stopRefresh();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	
    		String jsonString = new HttpData().getPostFoodLikeData(constant.HTTPLIKEURL, "1");
    		Food[] foodInfo = new JsonAnalyze().jsonFoodLikeAnalyze(jsonString);
			if (null != foodInfo) {
				for (int i = 0; i < foodInfo.length; i++) {
					listLike.add(foodInfo[i]);
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
	

	@Override
	public void onLoadMore() {
		listView.stopLoadMore();
	}
	
	private class StaggeredAdapter extends BaseAdapter {
		
        private List<Food> list;
        private ImageLoader imageLoader;

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
                convertView = layoutInflator.inflate(R.layout.item_like, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScaleImageView) convertView.findViewById(R.id.item_like_img);
                viewHolder.txtShop = (TextView) convertView.findViewById(R.id.item_like_shop);
                viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.item_like_price);
                viewHolder.txtShare = (TextView) convertView.findViewById(R.id.item_like_share);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.likeLoadImage(food.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtPrice.setText(food.getName());
            viewHolder.txtShare.setText(food.getAddress());
            viewHolder.txtShop.setText(food.getDescription());
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
    				Intent intent = new Intent(LikeActivity.this, FoodDetailActivity.class);
    				intent.putExtra("food", this.food);
    				startActivity(intent);
    			}
    	
        }
    }
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LikeActivity.this,SettingActivity.class);
			startActivity(intent);
		}
		
	}
	
	public class ViewHolder {
        public ScaleImageView imageView;
        TextView txtShop;
        TextView txtPrice;
        TextView txtShare;
    }
}
