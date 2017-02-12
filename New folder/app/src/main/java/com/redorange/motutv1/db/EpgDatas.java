package com.redorange.motutv1.db;

import com.redorange.motutv1.LiveTvActivity;
//import org.apache.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.HashMap;

public class EpgDatas {

	public static Array epgdata;
//	private static String epg_url = "http://62.210.182.14:8080/channel_resource/live/program.html";
	private static String epg_url = "http://extremeiptv.com:8080/channel_resource/live/program.html";

	public static JSONObject myepgdata;
	public static JSONArray myepgreleaseList;
	private static HashMap<String, JSONArray> epgmap;
	
	public static void Initialize(final LiveTvActivity mActivity)
	{
		epgmap = new HashMap<String, JSONArray>();
		Thread thread = new Thread(new Runnable(){
	        @Override
	        public void run() {
	            try {
	            	myepgdata = getJSONFromUrl(epg_url);
		//			if (myepgdata!=null)
		//			{
						myepgreleaseList = myepgdata.getJSONArray("releaseList");
						for(int i = 0 ; i < myepgreleaseList.length() ; i++){
							JSONObject obj = myepgreleaseList.getJSONObject(i);
							String key = "p_"+ obj.getInt("id");
							JSONArray value = obj.getJSONObject("propMap").getJSONArray("program");
							epgmap.put(key, value);

						}
		//				mActivity.bIsLoadedIPTV =2;// Non Error occured
		//			}
		///			else
		//			{
		//				mActivity.bIsLoadedIPTV =1;// Error occured
		//				return;
		//			}


	            } catch (Exception e) {
	                e.printStackTrace();
		//			mActivity.bIsLoadedIPTV =1;// Error occured
		//			return;

	            }
	        }
	    });
	    thread.start();
	}
	
	public static JSONObject getJSONFromUrl(String url) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		//	return null;
		} catch (ClientProtocolException e) {
			//e.printStackTrace();
		//	return null;
		} catch (IOException e) {
			//e.printStackTrace();
		//	return null;
		} catch (Exception e1){
			//e1.printStackTrace();
		//	return null;
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
		}
		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
		}
		// return JSON String
		return jObj;
	}
	
	public static JSONArray getEpgProgram(String key)
	{
		return epgmap.get(key);
	}
}
