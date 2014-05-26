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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LikeActivity extends Fragment implements IXListViewListener{
	
	private XListView listView;
	private StaggeredAdapter adapter;
	private static List<Food> listLike = new ArrayList<Food>();
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = (XListView)getActivity().findViewById(R.id.like_list);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		adapter = new StaggeredAdapter(listLike);
		adapter.notifyDataSetChanged();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_like, null);
	}
	
	
	@Override
	public void onResume() {
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
        private Typeface fontFace;

        public StaggeredAdapter(List<Food> list) {
            this.list = list;
            this.imageLoader = new ImageLoader();
            this.fontFace = Typeface.createFromAsset(getActivity().getAssets(), "font/font.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            Food food = list.get(position);

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
            
            imageLoader.likeLoadImage(food.getImage(), this, viewHolder);
            viewHolder.imageView.setImageWidth(240);
            viewHolder.imageView.setImageHeight(240);
            viewHolder.txtName.setText(food.getName());
            viewHolder.txtShare.setText(food.getShare());
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
    				Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
    				intent.putExtra("food", this.food);
    				startActivity(intent);
    			}
    	
        }
    }
	
	public class ViewHolder {
        public ScaleImageView imageView;
        TextView txtName;
        TextView txtShare;
    }
}
