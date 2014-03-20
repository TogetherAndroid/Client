package com.xt.together.activity;

import java.util.List;

import com.xt.together.R;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;

import android.app.ListActivity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

public class StoreActivity extends ListActivity {
	
	private Button btnBack;
	private List<Restaurant> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		btnBack = (Button)findViewById(R.id.store_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		initView();
	}
	
	private void initView() {
		((PullToRefreshListView) getListView()).setOnRefreshListener(new com.xt.together.control.PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
		list = Restaurant.getRestaurantList();
        StoreAdapter adapter = new StoreAdapter(StoreActivity.this, list);
        setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(StoreActivity.this,RestaurantDetail.class);
		intent.putExtra("restaurant", list.get(position - 1));
		startActivity(intent);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
	
	private class StoreAdapter extends BaseAdapter {
    	
		private List<Restaurant> list;
		private LayoutInflater inflater;
		private ImageLoader imageLoader;
	
		public StoreAdapter(Context context, List<Restaurant> list) {
			this.list = list;
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.imageLoader = new ImageLoader();
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
				viewHolder.txtName = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_name);
				viewHolder.txtSpecialty = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_specialty);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Restaurant restaurant = list.get(position);
			imageLoader.storeLoadImage(restaurant.getImage(), this, viewHolder);
			viewHolder.txtAverage.setText("人均：" + restaurant.getAverage());
			viewHolder.txtLike.setText(restaurant.getLike() + "人喜欢");
			viewHolder.txtName.setText(restaurant.getName());
			viewHolder.txtSpecialty.setText("招牌菜:" + restaurant.getSpecialty());
			return convertView;
		}
	
	}
	

	class BackOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			StoreActivity.this.finish();
		}
	}
	
	public final class ViewHolder {
		public ImageView imageView;
		TextView txtName;
		TextView txtAverage;
		TextView txtSpecialty;
		TextView txtLike;
	}
}
