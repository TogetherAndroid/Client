package com.xt.together.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.xt.together.constant.constant;
import com.xt.together.model.Food;
import com.xt.together.model.FoodLike;
import com.xt.together.model.FriendsCirCleUser;
import com.xt.together.model.FriendsCircle;
import com.xt.together.model.Invite;
import com.xt.together.model.Restaurant;
import com.xt.together.model.Trends;
import com.xt.together.model.TrendsLike;

public class JsonAnalyze {

	public Restaurant[] jsonNearbyRestaurantAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
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
					Log.e(constant.DEBUG_TAG, "we get the dataarray length"+dataArray.length());
					JSONObject jo = (JSONObject)dataArray.opt(i);
					if(jo == null){
						Log.e(constant.DEBUG_TAG, "3333333");
					}
					Log.e(constant.DEBUG_TAG, jo.getString("id"));
					newrestaurant[i] = new Restaurant(jo.getString("id"),jo.getString("name"), jo.getString("average"), 
						jo.getString("like"), jo.getString("specialty"), jo.getString("address"), 
						jo.getString("phone"), jo.getString("image"));
				
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(constant.DEBUG_TAG, "something wrong");
			e.printStackTrace();
		}
		return newrestaurant;		
	}
	
	public Food[] jsonFoodAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
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
	
	public JSONArray jsonWeiboFansAnalyze(String jsonString){

		if(null == jsonString){
			return null;
		}
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
		if(null == jsonString){
			return null;
		}
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
	
	public String[] jsonFriendsHeadAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		String[] friendsName = null;
		try {
			JSONObject friendsInfo = new JSONObject(jsonString);
			JSONArray friendsArray = friendsInfo.getJSONArray("users");
			friendsName = new String[friendsArray.length()];
			for(int i = 0 ; i < friendsArray.length(); i++){
				JSONObject jo = (JSONObject)friendsArray.opt(i);
				friendsName[i] = jo.getString("profile_image_url");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friendsName;
	}
	
	public String[] jsonFriendsIdAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		String[] friendsName = null;
		try {
			JSONObject friendsInfo = new JSONObject(jsonString);
			JSONArray friendsArray = friendsInfo.getJSONArray("users");
			friendsName = new String[friendsArray.length()];
			for(int i = 0 ; i < friendsArray.length(); i++){
				JSONObject jo = (JSONObject)friendsArray.opt(i);
				friendsName[i] = jo.getString("idstr");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friendsName;
	}
	/*
	 * 分析从mytrends获得的json，是一个双重数组
	 */
	public Trends[] jsonMyTrendsAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		
		int status = -1;
		Trends[] trends  = null;
		try {
			JSONObject TrendsInfo = new JSONObject(jsonString);
			status = TrendsInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				Log.e("com.xt.together", "we have got the wrong message");
				return null;
			}else{
				Log.e("com.xt.together", "we start to analyze");
				JSONArray dataArray = TrendsInfo.getJSONArray("data");
				Log.e(constant.DEBUG_TAG, "we get the length" + dataArray.length());
				trends = new Trends[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
					JSONObject jo = (JSONObject)dataArray.opt(i);
					String id = jo.getString("id");
					String name = jo.getString("name");
					JSONArray likeArray = jo.getJSONArray("like");
					TrendsLike[] trendslike = new TrendsLike[likeArray.length()];
					for(int j = 0; j < likeArray.length(); j++){
						JSONObject likejo = (JSONObject)likeArray.opt(j);
						String likeid = likejo.getString("id");
						String likename = likejo.getString("name");
						String likehead = likejo.getString("head");
						trendslike[j] = new TrendsLike(likeid, likename, likehead);
					}
					String address = jo.getString("address");
					String description = jo.getString("description");
					String image = jo.getString("image");
					trends[i] = new Trends(id, name, address, description, trendslike, image);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return trends;
	}
	
	public Food[] jsonFoodLikeAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
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
						null, null, jo.getString("image"),jo.getString("address"), 
						jo.getString("description"));
				
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newFood;	
	}
	
	
	public Restaurant[] jsonStoreAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		int status = -1;
		Restaurant[] newRestaurant  = null;
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
				newRestaurant = new Restaurant[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
				JSONObject jo = (JSONObject)dataArray.opt(i);
				newRestaurant[i] = new Restaurant(jo.getString("name"), jo.getString("average"),
						jo.getString("like"), jo.getString("specialty"), jo.getString("address"),
						jo.getString("phone"), jo.getString("image")
						);
				
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newRestaurant;	
	}
	
	
	public String jsonIDAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		int status = -1;
		String HttpId = "";
		try {
			JSONObject IDInfo = new JSONObject(jsonString);
			status = IDInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				HttpId = IDInfo.getString("data");
				return HttpId;
			}else{
				JSONObject DataInfo = IDInfo.getJSONObject("data");
				HttpId = DataInfo.getString("id");
				return HttpId;
				
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public Food[] jsonNearbyFoodAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		int status = -1;
		Food[] newFood  = null;
		try {
			JSONObject foodInfo = new JSONObject(jsonString);
			status = foodInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				Log.e("com.xt.together", "we have got the wrong message");
				return null;
			}else{
				JSONArray dataArray = foodInfo.getJSONArray("data");
				newFood = new Food[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
				JSONObject jo = (JSONObject)dataArray.opt(i);
					JSONArray likeArray = jo.getJSONArray("like");
					FoodLike[] newFoodLike = new FoodLike[likeArray.length()];
					for(int j = 0; j < likeArray.length(); j++){
						JSONObject likejo = (JSONObject)likeArray.opt(j);
						newFoodLike[j] = new FoodLike(likejo.getString("id"), likejo.getString("name"), likejo.getString("head"));
					}
					newFood[i] = new Food(jo.getString("id"),jo.getString("name"), jo.getString("shop"), jo.getString("price"),
							jo.getString("share"), jo.getString("image"), jo.getString("address"), jo.getString("description"),
							newFoodLike);
					}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newFood;	
	}
	
	public boolean jsonAddFoodLikeAnalyze(String jsonString){
		boolean isAddSuccess = false;
		try {
			JSONObject status = new JSONObject(jsonString);
			String isStatus = status.getString("status");
			if("0" == isStatus){
				isAddSuccess = true;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isAddSuccess;
	}
	
	public boolean jsonAddStoreAnalyze(String jsonString){

		boolean isAddSuccess = false;
		try {
			JSONObject status = new JSONObject(jsonString);
			String isStatus = status.getString("status");
			if("0" == isStatus){
				isAddSuccess = true;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isAddSuccess;
	}
	
	public FriendsCircle[] jsonFriendsCircleAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		int status = -1;
		FriendsCircle[] newFriendsCircle  = null;
		try {
			JSONObject FriendsCircleInfo = new JSONObject(jsonString);
			status = FriendsCircleInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				Log.e("com.xt.together", "we have got the wrong message");
				return null;
			}else{
				JSONArray dataArray = FriendsCircleInfo.getJSONArray("data");
				newFriendsCircle = new FriendsCircle[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
					JSONObject jo = (JSONObject)dataArray.opt(i);
					JSONArray likeArray = jo.getJSONArray("like");
					FoodLike[] newFoodLike = new FoodLike[likeArray.length()];
					for(int j = 0; j < likeArray.length(); j++){
						JSONObject likejo = (JSONObject)likeArray.opt(j);
						newFoodLike[j] = new FoodLike(likejo.getString("id"), likejo.getString("name"), likejo.getString("head"));
					}
					JSONObject jouser = jo.getJSONObject("user");
					FriendsCirCleUser muser = new FriendsCirCleUser(jouser.getString("id"), jouser.getString("name"), jouser.getString("head"));
					newFriendsCircle[i] = new FriendsCircle(jo.getString("name"), jo.getString("address"), jo.getString("description"), 
							jo.getString("image"), muser, newFoodLike);
					}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newFriendsCircle;	
	}
	
	public Invite[] jsonInviteAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		int status = -1;
		Invite[] newInvite  = null;
		try {
			JSONObject inviteInfo = new JSONObject(jsonString);
			status = inviteInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				Log.e("com.xt.together", "we have got the wrong message");
				return null;
			}else{
				JSONArray dataArray = inviteInfo.getJSONArray("data");
				newInvite = new Invite[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
					JSONObject jo = (JSONObject)dataArray.opt(i);
					JSONArray invitedArray = jo.getJSONArray("invited");
					String invitedString = "";
					for(int j = 0; j < invitedArray.length(); j++){
						JSONObject joinvited = (JSONObject)invitedArray.opt(j);
						String invitedname = joinvited.getString("name");
						invitedname = invitedname.replace("@", "");
						invitedString += invitedname;
					}
					newInvite[i] = new Invite(jo.getString("id"), jo.getString("name"), jo.getString("address"),
							jo.getString("date"), null, invitedString, jo.getString("phone"),jo.getString("image")
							);
				
					}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newInvite;	
	}
	
	public Invite[] jsonInvitedAnalyze(String jsonString){
		if(null == jsonString){
			return null;
		}
		int status = -1;
		Invite[] newInvite  = null;
		try {
			JSONObject inviteInfo = new JSONObject(jsonString);
			status = inviteInfo.getInt("status");
			
			if(status == -1){
				Log.e("com.xt.together", "we haven't got the json message");
			}else if(status == 1){
				Log.e("com.xt.together", "we have got the wrong message");
				return null;
			}else{
				JSONArray dataArray = inviteInfo.getJSONArray("data");
				newInvite = new Invite[dataArray.length()];
				for(int i = 0; i < dataArray.length(); i++){
					JSONObject jo = (JSONObject)dataArray.opt(i);
					JSONArray invitedArray = jo.getJSONArray("invited");
					String invitedString = "";
					for(int j = 0; j < invitedArray.length(); j++){
						JSONObject joinvited = (JSONObject)invitedArray.opt(j);
						String invitedname = joinvited.getString("name");
						invitedname = invitedname.replace("@", "");
						invitedString += invitedname;
					}
					newInvite[i] = new Invite(jo.getString("id"), jo.getString("name"), jo.getString("address"),
							jo.getString("date"), null, invitedString, jo.getString("phone"),jo.getString("image")
							);
				
					}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newInvite;	
	}
}
