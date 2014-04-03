package com.xt.together.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.xt.together.model.Food;
import com.xt.together.model.Invite;
import com.xt.together.model.Restaurant;

public class JsonAnalyze {

	public Restaurant[] jsonNearbyRestaurantAnalyze(String jsonString){
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
	
	public Food[] jsonFoodAnalyze(String jsonString){
		int status = -1;
		Food[] newFood  = null;
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
				newFood = new Food[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
				JSONObject jo = (JSONObject)dataArray.opt(i);
				newFood[i] = new Food(jo.getString("name"), null, 
						null, null, null,jo.getString("address"), 
						jo.getString("description"));
				
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newFood;		
	}
	
	public Invite[] jsonInviteAnalyze(String jsonString){
		int status = -1;
		Invite[] newInvite  = null;
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
				newInvite = new Invite[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
				JSONObject jo = (JSONObject)dataArray.opt(i);
				newInvite[i] = new Invite(jo.getString("name"), jo.getString("address"), 
						jo.getString("date"), null, jo.getString("invited"),jo.getString("phone"), 
						jo.getString("image"));
				
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newInvite;	
	}
	
	public JSONArray jsonWeiboFansAnalyze(String jsonString){

		JSONArray friendids = null;
		try {
			JSONObject restaurantInfo = new JSONObject(jsonString);

			friendids = restaurantInfo.getJSONArray("ids");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friendids;	
	}
	
	
	public String[] jsonFriendsAnalyze(String jsonString){
		String[] friendsName = null;
		try {
			JSONObject friendsInfo = new JSONObject(jsonString);
			JSONArray friendsArray = friendsInfo.getJSONArray("users");
			friendsName = new String[friendsArray.length()];
			for(int i = 0 ; i < friendsArray.length(); i++){
				JSONObject jo = (JSONObject)friendsArray.opt(i);
				friendsName[i] = "@" +jo.getString("screen_name");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friendsName;
	}
}
