package com.xt.together.activity;

import java.util.ArrayList;
import java.util.List;

import com.xt.together.R;
import com.xt.together.constant.constant;
import com.xt.together.http.HttpData;
import com.xt.together.json.JsonAnalyze;
import com.xt.together.model.Food;
import com.xt.together.model.Trends;
import com.xt.together.utils.ImageLoader;
import com.xt.together.waterfall.ScaleImageView;
import com.xt.together.waterfall.XListView;
import com.xt.together.waterfall.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
	private static List<Trends> listTrends = new ArrayList<Trends>();
	private Button btnBack;
	private ImageView btnSetting;

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
		new GetDataTask().execute();		
		listView.stopRefresh();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
        	String jsonText = new HttpData().getPostMyTrendsData(constant.HTTPMYRECIPEURL, "1");
        	JsonAnalyze jsonAnalyze = new JsonAnalyze();
        	Trends[] trends =jsonAnalyze.jsonMyTrendsAnalyze(jsonText);
		if (null != trends) {
			listTrends.remove(listTrends);
			for (int i = 0; i < trends.length; i++) {
				listTrends.add(trends[i]);
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
		
		private ImageLoader imageLoader;
        private List<Trends> list;
        private Typeface fontFace;

        public StaggeredAdapter(List<Trends> list) {
            this.list = list;
            this.imageLoader = new ImageLoader();
            this.fontFace = Typeface.createFromAsset(getAssets(), "font/font.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            Trends trends = list.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.item_food, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ScaleImageView) convertView.findViewById(R.id.item_food_img);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.item_food_name);
                viewHolder.txtName.setTypeface(fontFace);
                viewHolder.txtShare = (TextView) convertView.findViewById(R.id.item_food_share);
                viewHolder.txtShare.setTypeface(fontFace);
                convertView.setTag(viewHolder);
            } else {
            		viewHolder = (ViewHolder) convertView.getTag();
            }
            
            imageLoader.MyTrendsLoadImage(trends.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtName.setText(trends.getName());
            viewHolder.txtShare.setText(trends.getDescription());
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
        TextView txtName;
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
