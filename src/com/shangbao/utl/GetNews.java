package com.shangbao.utl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetNews {
	private String url = null;
	
	private List<String> titles = new ArrayList<String>();
	private List<String> pictureUrls = new ArrayList<String>();
	
	public GetNews(Context context) {
		// TODO Auto-generated constructor stub
		this.url = "http://120.27.47.167/Shangbao01/app/android/original/1";
		GetDatas getDatas = new GetDatas(context);
		getDatas.execute();
	}
	public GetNews(Context context,String url) {
		// TODO Auto-generated constructor stub
		this.url = url;
		GetDatas getDatas = new GetDatas(context);
		getDatas.execute();
	}
		
	class GetDatas extends AsyncTask<Void, Integer, Integer>{

		private Context context;
		
		public GetDatas(Context context) {
			// TODO Auto-generated constructor stub 
			this.context = context;
		}
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub 
			getDatas();
			return null;
		}
		
		public void getDatas() {
			// 创建一个新的HttpClient Post头
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(url); 
			try {
				HttpResponse response = httpclient.execute(httppost);
				String strResult = EntityUtils.toString(response.getEntity());
				JSONObject jsonobject = new JSONObject(strResult);
				JSONArray contents = jsonobject.getJSONArray("content");
				//
				for (int i = 0; i < contents.length(); i++) {
					JSONObject content = contents.getJSONObject(i);
					Log.e("------", content.getString("title"));
					titles.add(content.getString("title"));
					pictureUrls.add("http://120.27.47.167/Shangbao01"+content.getString("pictureUrl"));
				}  
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public List<String> getTitles() {
		return titles;
	}
	public List<String> getPictureUrls() {
		return pictureUrls;
	}
	
}
