package com.xt.together.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.xt.together.model.Restaurant;

public class JsonAnalyze {

	public Restaurant[] jsonRestaurantAnalyze(String jsonString){
		int status = -1;
		Restaurant[] newrestaurant  = null;
		try {
			JSONObject restaurantInfo = new JSONObject(jsonString);
			status = restaurantInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				Log.e("com.xt.together", "we have got the wrong message");
				return null;
			}else{
				JSONArray dataArray = restaurantInfo.getJSONArray("data");
				newrestaurant = new Restaurant[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
				JSONObject jo = (JSONObject)dataArray.opt(i);
				newrestaurant[i] = new Restaurant(jo.getString("name"), jo.getString("average"), 
						jo.getString("like"), jo.getString("specialty"), jo.getString("address"), 
						jo.getString("phone"), jo.getString("image"));
				
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newrestaurant;		
	}
}
