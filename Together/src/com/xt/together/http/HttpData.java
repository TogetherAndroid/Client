package com.xt.together.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpData {
	
	public final String DEBUG_TAG = "System.out";
	
/*	public String getHttpDataFrom(URL url)
	{
		String getData = "";
		
		HttpURLConnection urlConn = null;
		InputStreamReader in = null;
		BufferedReader buffer = null;
		String inputLine = null;
		
		if(url != null){
			
			try {
				urlConn = (HttpURLConnection)url.openConnection();
				in = new InputStreamReader(urlConn.getInputStream());
				buffer = new BufferedReader(in);
				while((inputLine = buffer.readLine()) != null){
					getData += inputLine  + "\n";
				}
				
				if(getData == null || getData.equals("")){
					Log.w(DEBUG_TAG, "没有读到任何消息");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				urlConn.disconnect();
			}
			
		}else {
			Log.w(DEBUG_TAG, "传入的URL是空");
		}
		return getData;
		
	}
*/
	public String getPostNearbyResData(URL url, String postdata){
		
		HttpURLConnection urlConn = null;
		BufferedReader buffer = null;
		String returnLine = "";
		DataOutputStream out = null;
		
		if(url != null){
			try {
				urlConn = (HttpURLConnection)url.openConnection();
				urlConn.setConnectTimeout(5000);
				urlConn.setDoInput(true);
				urlConn.setDoOutput(true);
				urlConn.setRequestMethod("POST");
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(false);
				urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				urlConn.connect();
				
				out = new DataOutputStream(urlConn.getOutputStream());
				byte[] content = postdata.getBytes("utf-8");
				out.write(content, 0, content.length);
				
				out.flush();
				out.close();
				
				buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"utf-8"));
				String line = "";
				while((line = buffer.readLine()) != null){
					returnLine += line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				
			}
			
			
		}else{
			Log.w(DEBUG_TAG, "浼犲叆鐨刄RL鏄┖");
		}
		
		return returnLine;
	}

	public String getPostFriendCircleData(String url, String userId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "SELECT"));
		params.add(new BasicNameValuePair("userId", userId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}

	
	public String addPostFriendCircleData(String url, String userId, String name, String average, String address, String description){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("average", average));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("description", description));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}
	
	public String getPostLikeData(String url, String request, String IdName, String Id){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", request));
		params.add(new BasicNameValuePair(IdName, Id));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}
	
	public String getPostLikeResData(String url, String userId){
		String strResult = getPostLikeData(url, "SELECT", "userId", userId);
		return strResult;
	}
	
	public String AddPostLikeResData(String url, String restaurantId){
		String strResult = getPostLikeData(url, "ADDRESTAURANT", "restaurantId", restaurantId);
		return strResult;
	}
	
    public String addPostLikeDynamicData(String url, String dynamicId){
		
		String strResult = getPostLikeData(url,"ADDDYNAMIC", "dynamicId", dynamicId);
		return strResult;
	}
    
	public String getPostStoreData(String url, String Id){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "SELECT"));
		params.add(new BasicNameValuePair("userId", Id));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}
	
	public String addPostStoreData(String url, String restaurantId, String Id){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("restaurantId",restaurantId));
		params.add(new BasicNameValuePair("userId", Id));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}
	
	public String getPostInviteData(String url, String request, String Id){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", request));
		params.add(new BasicNameValuePair("userId", Id));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}
	
	public String getPostSendInviteData(String url, String userId){
		String strResult = getPostInviteData(url, "SEND", userId);
		return strResult;
	}
	
	public String getPostReceiveInviteData(String url, String userId){
		String strResult = getPostInviteData(url, "RECEIVE", userId);
		return strResult;
	}
	
	public String addPostInviteData(String url, String name, String address, String date, String invited, String phone, String image, String userId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("invited", invited));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("image", image));
		params.add(new BasicNameValuePair("userId", userId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResult;
	}
}

