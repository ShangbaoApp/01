package com.shangbao.utl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.shangbao.util.Column;
import com.shangbao.util.News;

/*
 * 在 最新资讯  中的新闻列表
 * 
 */
public class GetNewsList {
	
	public List<Column> getColumns() {
		return columns;
	}

	public List<Column> columns = new ArrayList<Column>();
	public String url;
	public GetNewsList() {
		// TODO Auto-generated constructor stub
		this.url = "http://120.27.47.167/Shangbao01/app/android/newest";
		Log.e("+++++", "Hello");
		GetDatas getDatas = new GetDatas(null);
		getDatas.execute();
		Log.e("+++++", "Hello");
	}
	public GetNewsList(String url){
		this.url = url;
	}
	
	//运行异步任务时候，要调用 class.execute()方法
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

		@Override
		protected void onPostExecute(Integer result) {  
	          Log.e("****-----*****", ""+columns.size());
	    }
		
		public void getDatas() {
			// 创建一个新的HttpClient Post头
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(url); 
			try {
				HttpResponse response = httpclient.execute(httppost);
				String strResult = EntityUtils.toString(response.getEntity());
				JSONObject jsonobject = new JSONObject(strResult);
				JSONArray contentColumns = jsonobject.getJSONArray("contentColumns");
				//
				
				for (int i = 0; i < contentColumns.length(); i++) {
					Column column = new Column();
					List<News> nList = new ArrayList<News>();
					JSONObject contentColumn = contentColumns.getJSONObject(i);
					column.columnName = contentColumn.getString("columnName");
					JSONArray contents = contentColumn.getJSONArray("content");
					for (int j = 0; j < contents.length(); j++) {
						News nws = new News();
						JSONObject content = contents.getJSONObject(j);
						nws.title = content.getString("title");
						JSONArray picUrlArray = content.getJSONArray("picUrl");
						for (int k = 0; k < picUrlArray.length(); k++) {
							nws.add("http://120.27.47.167/Shangbao01"+picUrlArray.get(k).toString());
							Log.e("______-----___", picUrlArray.get(k).toString());
						}
						//content.getJSONArray("picUrl").
						nws.summary = content.getString("summary");
						nList.add(nws);
						//可以获得数组字符串
						//Log.e("___++++nws.title+++___",content.getJSONArray("picUrl").get(0).toString());
						Log.e("___++++nws.summary+++___",nws.summary);
						
					}
					column.newsList = nList;
					columns.add(column);
					//titles.add(content.getString("title"));
					//pictureUrls.add("http://120.27.47.167/Shangbao01"+content.getString("pictureUrl"));
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
	
}
