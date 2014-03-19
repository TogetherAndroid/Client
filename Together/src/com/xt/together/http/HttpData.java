package com.xt.together.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpData {
	
	public final String DEBUG_TAG = "System.out";
	
	public String getHttpDataFrom(URL url)
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
	
public String getPostData(URL url, String postdata){
		
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

}
