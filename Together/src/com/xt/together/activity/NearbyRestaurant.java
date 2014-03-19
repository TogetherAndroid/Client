package com.xt.together.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.xt.together.R;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Restaurant;
import com.xt.together.utils.ImageLoader;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NearbyRestaurant extends ListFragment {
	
	private List<Restaurant> list;
	public final String httpRestaurantURL = "http://togetherxt.duapp.com/nearbyrestaurant";
	public final String DEBUG_TAG = "com.xt.together";
	private NearbyRestaurantAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_nearbyrestaurant, null);
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
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
		list = Restaurant.getRestaurantList();
        adapter = new NearbyRestaurantAdapter(getActivity(), list);
        setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(),RestaurantDetail.class);
		intent.putExtra("restaurant", list.get(position - 1));
		startActivity(intent);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
/*            try {
                Thread.sleep(2000);
            	
            } catch (InterruptedException e) {
                ;
            }
*/
        	HttpData httpdata = new HttpData();
        	URL url = null;
        	try {
				url = new URL(httpRestaurantURL);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Log.e(DEBUG_TAG, "the url has create with error");
				e.printStackTrace();
			}
        	String jsonText = httpdata.getPostData(url, "");
        	JsonAnalyze jsonAnalyze = new JsonAnalyze();
        	Restaurant[] newrestaurant =jsonAnalyze.jsonRestaurantAnalyze(jsonText);
        	list.removeAll(list);
        	for(int i = 0; i < newrestaurant.length; i++){
        		Log.e("com.xt.together", newrestaurant[i].getName() + "hahahhaa");
        		list.add(newrestaurant[i]);
        	}
        	
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
        	
        	adapter.notifyDataSetChanged();
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }
    
    private class NearbyRestaurantAdapter extends BaseAdapter {
    	
    		private List<Restaurant> list;
    		private LayoutInflater inflater;
    		private ImageLoader imageLoader;
    	
    		public NearbyRestaurantAdapter(Context context, List<Restaurant> list) {
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
				viewHolder.txtLike = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_like);
				viewHolder.txtName = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_name);
				viewHolder.txtSpecialty = (TextView)convertView.findViewById(R.id.item_nearbyrestaurant_specialty);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			Restaurant restaurant = list.get(position);
			imageLoader.nearybyRestaurantLoadImage(restaurant.getImage(), this, viewHolder);
			viewHolder.txtAverage.setText("人均：" + restaurant.getAverage());
			viewHolder.txtLike.setText(restaurant.getLike() + "人喜欢");
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
		TextView txtLike;
	}

}
