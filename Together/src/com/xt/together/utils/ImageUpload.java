package com.xt.together.utils;

import java.io.File;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import android.util.Base64;

public class ImageUpload {
	public static String Upload(File file){
		if(!file.exists()) {
			System.out.println("file do not exist");
			return null;
		}
		String url = getUrl(file.getName(), "POST");
		try {	
			PostMethod filePost = new PostMethod(url);
			Part[] parts = {new FilePart(file.getName(), file)};
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			HttpClient client = new HttpClient();
		    client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		    int status = client.executeMethod(filePost);
			if(status == 200){//如果状态码为200,就是正常返回
				System.out.println("上传成功");
				return getUrl(file.getName(), "GET");
			} else {
				System.out.println("上传失败" + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String getUrl(String imageName, String method) {
		String ak = "c3gEftkaLnLx3N4xb9FoRV5C";
		String sk = "2nsprG1mzYAhsuGlLixqWoaOiKjRMvlN";
		String content = "MBO" + "\n"
							+ "Method=" + method + "\n"
							+ "Bucket=xutebucket" + "\n"
							+ "Object=/" + imageName + "\n";
		String signature = null;
		try {
			Base64.encodeToString(hmac_sha1(sk.getBytes(), content.getBytes()), Base64.DEFAULT);
			signature = URLEncoder.encode(signature, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("http://bcs.duapp.com/xutebucket/")
			.append(imageName)
			.append("?sign=MBO:")
			.append(ak + ":")
			.append(signature);
		return sb.toString();
	}
	private static byte[] hmac_sha1(byte[] keyBytes, byte[] text)
		    throws NoSuchAlgorithmException, InvalidKeyException 
	{
		Mac hmacSha1;
		try {
		    hmacSha1 = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException nsae) {
		    hmacSha1 = Mac.getInstance("HMAC-SHA-1");
		}
		SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
		hmacSha1.init(macKey);
		return hmacSha1.doFinal(text);
	}
}
