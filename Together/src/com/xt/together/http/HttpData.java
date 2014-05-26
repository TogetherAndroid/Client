package com.xt.together.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.xt.together.constant.constant;
import com.xt.together.utils.ImageUpload;

import android.graphics.Bitmap;
import android.os.Environment;
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
				return null;
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return null;
				}
				urlConn.disconnect();
			}
			
		}else {
			Log.w(DEBUG_TAG, "传入的URL是空");
		}
		return getData;
		
	}

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
				return null;
			} finally{
				
			}
			
			
		}else{
			Log.w(DEBUG_TAG, "浼犲叆鐨刄RL鏄┖");
		}
		
		return returnLine;
	}
	*/
	public String getPostFriendCircleData(String url, String userId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "SELECT"));
		params.add(new BasicNameValuePair("userId", userId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
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
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
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
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
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
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
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
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String getPostInviteDataInterface(String url, String request, String Id){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", request));
		params.add(new BasicNameValuePair("userId", Id));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String getPostSendInviteData(String url, String userId){
		String strResult = getPostInviteDataInterface(url, "SEND", userId);
		return strResult;
	}
	
	public String getPostReceiveInviteData(String url, String userId){
		String strResult = getPostInviteDataInterface(url, "RECEIVE", userId);
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
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String sendFriendids(String url, JSONArray jsonarray){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("friendIds", jsonarray.toString()));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;

	}
	
	public String sendpicture(String url , Bitmap bitmap){
		
		if(null == url || null == bitmap){
			return "the url or the pic is null";
		}
		String strResult = "";
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		
		try {
			FileOutputStream fop = new FileOutputStream(Environment.getExternalStorageDirectory() + "/temp.jpg");
			bitmap.compress(Bitmap.CompressFormat.JPEG,  100, fop);
			fop.close();
			
			File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
			if(file.exists()){
				Log.e(constant.DEBUG_TAG, "图片攒在啊");
			}else{
				Log.e(constant.DEBUG_TAG, "图片不攒在啊");
			}
			
			FileEntity repEntity = new FileEntity(file, "binary/octet-stream");
			httpPost.setEntity(repEntity);
			repEntity.setContentType("binary/octet-stream");
			
			response = new DefaultHttpClient().execute(httpPost);
			
			if(response.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(response.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
//			file.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(constant.DEBUG_TAG, "error1");
			return null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(constant.DEBUG_TAG, "error2");
			return null;
		}
		
		
		return strResult;
	}
	
	public String sendPicUseUpload(Bitmap bitmap){
		if(null == bitmap){
			return "the pic is not exist";
		}
		try {
			File together = new File(Environment.getExternalStorageDirectory() + "/together");
			if(!together.exists()){
				together.mkdirs();
			}
			FileOutputStream fop = new FileOutputStream(Environment.getExternalStorageDirectory() + "/together/temp.jpg");
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fop);
			fop.close();
			
			File file = new File(Environment.getExternalStorageDirectory() + "/together/temp.jpg");
			
			@SuppressWarnings("static-access")
			String returnRoute = new ImageUpload().Upload(file);
			
			return returnRoute;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
	
	public String addPostStatusData(String url,  String name, String address, String description, String image, String userWeiboId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("image", image));
		params.add(new BasicNameValuePair("userId", userWeiboId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	
	
	public String getPostNearbyResData(String url){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	
	public String getPostMyTrendsData(String url, String userWeiboId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "SELECT"));
		params.add(new BasicNameValuePair("userId", userWeiboId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String getPostFoodLikeData(String url, String userWeiboId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "SELECT"));
		params.add(new BasicNameValuePair("userId", userWeiboId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String addPostWeiboIdData(String url, String userWeiboId, String userWeiboName, String userWeiboHead){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("weibo_id", userWeiboId));
		params.add(new BasicNameValuePair("weibo_name", userWeiboName));
		params.add(new BasicNameValuePair("weibo_head", userWeiboHead));
		
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	
	public String getPostNearByFood(String url){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	
	public String addPostFoodLike(String url, String foodId, String userId){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order", "ADDFOOD"));
		params.add(new BasicNameValuePair("foodId", foodId));
		params.add(new BasicNameValuePair("userId", userId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String addPostStore(String url, String userId, String restaurantId){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("restaurantId", restaurantId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	
	/*
	 * 这是在inviting界面发送要@的好友id和一些其他乱七八糟的东西
	 */
	public String addPostInviting(String url, String name, String address, String date, String userId, JSONArray invited, String phone, String image){
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order", "ADD"));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("image", image));
		params.add(new BasicNameValuePair("invited", invited.toString()));
		Log.e(constant.DEBUG_TAG, invited.toString());
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String getPostInviteData(String url, String userWeiboId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "SEND"));
		params.add(new BasicNameValuePair("userId", userWeiboId));
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
	
	public String getPostInvitedData(String url, String userWeiboId){
		
		String strResult = "";
		HttpPost httpRequest = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("order", "RECEIVE"));
		params.add(new BasicNameValuePair("userId", userWeiboId));
		Log.e(constant.DEBUG_TAG, "http send weibo id" + userWeiboId);
		
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			httpRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpRequest);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1500);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1500);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}else{
				Log.e(DEBUG_TAG, "请求相应状态码不为200，说明服务器没有正确相应客户端请求 ");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确转码");
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(DEBUG_TAG, "没有正确执行http请求");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return strResult;
	}
}

