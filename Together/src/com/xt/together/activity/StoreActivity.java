package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StoreActivity extends ListFragment {
	
	private static List<Restaurant> listStore = new ArrayList<Restaurant>();
	private StoreAdapter adapter;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_store, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	private void initView() {
		((PullToRefreshListView) getListView()).setOnRefreshListener(new com.xt.together.control.PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        adapter = new StoreAdapter(StoreActivity.this.getActivity(), listStore);
        setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(),RestaurantDetail.class);
		intent.putExtra("restaurant", listStore.get(position - 1));
		startActivity(intent);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        		String jsonString = new HttpData().getPostStoreData(constant.HTTPSTOREURL, "1");
        		Restaurant[] restaurantInfo = new JsonAnalyze().jsonStoreAnalyze(jsonString);
			if (null != restaurantInfo) {
				listStore.remove(listStore);
				for (int i = 0; i < restaurantInfo.length; i++) {
					listStore.add(restaurantInfo[i]);
				}
			}
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
        		adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
	
	private class StoreAdapter extends BaseAdapter {
    	
		private List<Restaurant> list;
		private LayoutInflater inflater;
		private ImageLoader imageLoader;
		private Typeface fontFace;
	
		public StoreAdapter(Context context, List<Restaurant> list) {
			this.list = list;
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.imageLoader = new ImageLoader();
			this.fontFace = Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
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
			imageLoader.storeLoadImage(restaurant.getImage(), this, viewHolder);
			viewHolder.txtAverage.setText("人均：" + restaurant.getAverage());
	//		viewHolder.txtLike.setText(restaurant.getLike() + "人喜欢");
			viewHolder.txtName.setText(restaurant.getName());
			viewHolder.txtSpecialty.setText("招牌菜:" + restaurant.getSpecialty());
			return convertView;
		}
	
	}
	

	class BackOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
		}
	}
	
	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
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
