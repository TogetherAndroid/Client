package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.control.PullToRefreshListView;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.model.Restaurant;
import com.xt.together.model.Trends;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

public class MyTrendsActivity extends Activity implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<Trends> listTrends;
	private Button btnBack;
	private ImageView btnSetting;
	private Oauth2AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytrends);
		btnBack = (Button)findViewById(R.id.mytrends_back);
		btnBack.setOnClickListener(new BackOnClickListener());
		btnSetting = (ImageView)findViewById(R.id.mytrends_setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		listView = (XListView)findViewById(R.id.mytrends_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		if(null == listTrends){
			listTrends = new ArrayList<Trends>();
		}

		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		adapter = new StaggeredAdapter(listTrends);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		listView.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
/*		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
*/				
		new GetDataTask().execute();		
		listView.stopRefresh();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	Log.e(constant.DEBUG_TAG, "zhixingle!");
        	String resurl = "http://192.168.1.106:8080/TogetherWeb/dynamic";
        	String jsonText = new HttpData().getPostMyTrendsData(resurl, "1");
        	JsonAnalyze jsonAnalyze = new JsonAnalyze();
        	Trends[] trends =jsonAnalyze.jsonMyTrendsAnalyze(jsonText);
  //      	Log.e(constant.DEBUG_TAG,trends[0].getName());
			if (null != trends) {
				listTrends.remove(listTrends);
				for (int i = 0; i < trends.length; i++) {
					for (int j = 0; j < trends[i].getTrendslike().length; j++) {
						Log.e(constant.DEBUG_TAG, "my liketrends is "
								+ trends[i].getTrendslike()[j].getHead());
					}
					listTrends.add(trends[i]);
				}
			}
        	
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
        	adapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }
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
        private List<Trends> list;

        public StaggeredAdapter(List<Trends> list) {
            this.list = list;
            this.imageLoader = new ImageLoader();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            Trends trends = list.get(position);

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
            
            imageLoader.MyTrendsLoadImage(trends.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtPrice.setText(trends.getName());
            viewHolder.txtShare.setText(trends.getDescription());
            viewHolder.txtShop.setText(trends.getAddress());
            convertView.setOnClickListener(new ViewOnClickListener(trends));
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
        	
			private Trends trends;
	
			public ViewOnClickListener (Trends trends){
				this.trends = trends;
			}

			@Override
			public void onClick(View v) {
				Food food = new Food(trends.getId(), trends.getName(), "", "", "", trends.getImage(),
						trends.getAddress(), trends.getDescription(), null);
				SharedPreferences userInfo = getSharedPreferences("user_info", 0);
				String selfImage = userInfo.getString("image_head", "");
				Log.e(constant.DEBUG_TAG, selfImage + "we get the iamge");
				Intent intent = new Intent(MyTrendsActivity.this, FoodDetailActivity.class);
				intent.putExtra("food", food);
				intent.putExtra("image", selfImage);
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
