package com.shangbao.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shangbao.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ListView;

/*
 * 新闻详细页的activity
 */

public class NewsContent extends Activity {

	private WebView webView;
	EditText commentText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		
		webView = (WebView)findViewById(R.id.webview);
		//设置WebView属性，能够执行JavaScript脚本
		webView.getSettings().setJavaScriptEnabled(true);
        //加载URL内容
		webView.loadUrl("http://192.168.1.119:8080/Shangbao01/app/android/1");//"http://www.baidu.com");
        //设置web视图客户端
		webView.setWebViewClient(new MyWebViewClient());
    
		commentText = (EditText)findViewById(R.id.comment);
		commentText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					Log.e("33333333333", "ooooooooooo");
					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					if(imm.isActive()){  
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
					}
					commentText.setText("");
					return true;
				}
				return false;
			}
		});
	}
	    //设置回退，如果迭代打开页面，一层一层返回
	    public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	            if((keyCode==KeyEvent.KEYCODE_BACK)&&webView.canGoBack())
	            {
	            	webView.goBack();
	                    return true;
	            }
	            return super.onKeyDown(keyCode,event);
	    }
	    //web视图客户端,在客户端继续访问页面内的url
	    public class MyWebViewClient extends WebViewClient
	    {
	            public boolean shouldOverviewUrlLoading(WebView view,String url)
	            {
	                    view.loadUrl(url);
	                    return true;
	            }
	    }
	
	    public void click(View view){
	    	int id = view.getId();
			if (id == R.id.post) {
				Log.e("++++----++++", "R.id.post");
				Datas datas = new Datas();
				datas.execute();
			} else if (id == R.id.like) {
				Log.e("++++----++++", "R.id.like");
			} else {
			}
	    }
	    
		class Datas extends AsyncTask<Void, Integer, Integer>{

			//String postString = "http://www.baidu.com/";//
			String postString = "http://202.115.17.218:8080/Shangbao01/app/iphone/1/comment";
			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
		       // ListView listView = (ListView) findViewById(R.id.mylist);
		        //为list设置Adapter
		        //listView.setAdapter(new ItemAdapter());
		        //listView.setOnItemClickListener(new ListClickListener());
				Log.e("-----over-----", "OK");
			}
			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub 
				Log.e("strResult-------0000000", "4+++++++5555");
				requestAction();
				Log.e("strResult-------0000000", "4+----------5555");
				return null;
			}
			
			public void requestAction() 
			{
			    HttpClient hc = new DefaultHttpClient();
			    String message;
			    HttpPost p = new HttpPost(postString);
			    JSONObject object = new JSONObject();
			    try 
			    {
			        object.put("content", "1"); 
			        object.put("userName", "1");
			        object.put("newsId", "1");
			       // object.put("description","1");
			    } 
			    catch (Exception ex) 
			    { }
			    try 
			    {
			        message = object.toString();
			        p.setEntity(new StringEntity(message, "UTF8"));
			        p.setHeader("Accept", "application/json");
			        p.setHeader("Content-type", "application/json");
			        HttpResponse resp = hc.execute(p);
			        Log.e("Status line", "" + resp.getStatusLine().getStatusCode());
			    } catch (Exception e) 
			        {e.printStackTrace();}

			}
			
			public void postData() {
			    // Create a new HttpClient and Post Header
			    HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(postString);
			    httppost.setHeader("Accept", "application/json");
			    httppost.addHeader("Content-Type", "application/json");

			    try {
			        // Add your data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			        nameValuePairs.add(new BasicNameValuePair("content", "12345"));
			        nameValuePairs.add(new BasicNameValuePair("userName", "Hi"));
			        nameValuePairs.add(new BasicNameValuePair("newsId", "1"));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			        // Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(httppost);
					Log.e("----------8888888", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码 

			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			    }
			} 
			
			public void getDatas(){
				List params = new LinkedList(); 
				params.add(new BasicNameValuePair("content", "中国")); 
				params.add(new BasicNameValuePair("userName", "Jack")); 
				params.add(new BasicNameValuePair("newsId", "1")); 
				//nameValuePairs.add(new BasicNameValuePair("timeDate", "2010-01-12 01:21:30"));
				//params.add(new BasicNameValuePair("timeDate", "100000")); 
				HttpPost postMethod = new HttpPost(postString); 
				postMethod.addHeader("Content-Type", "application/json");
				try {
					HttpClient httpClient = new DefaultHttpClient(); 
					postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
					HttpResponse response;
					response = httpClient.execute(postMethod);
					Log.e("----------8888888", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码 

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //执行POST方法 
				catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			public void gettDatas() {
				// 创建一个新的HttpClient Post头
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(postString); 
				httppost.addHeader("charset", HTTP.UTF_8);
				httppost.addHeader("Content-Type", "application/json");
				Log.e("strResult-------0000000", "4555555555");
				//实例化  
		        MultipartEntity me = new MultipartEntity();  
		        try { 
					/*me.addPart("content","good");
					me.addPart("userName","Jack");
					me.addPart("userId","1");*/
					//me.addPart("timeDate",new StringBody("100000"));
					httppost.setEntity(me);
					 //获得响应消息  
					HttpResponse response = httpclient.execute(httppost); 
					String strResult = EntityUtils.toString(response.getEntity());
					Log.e("strResult-------0000000", "----++++"+strResult);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				/*try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("content", "好顶赞！"));
					nameValuePairs.add(new BasicNameValuePair("userName", "Jack"));
					nameValuePairs.add(new BasicNameValuePair("newsId", "1"));
					nameValuePairs.add(new BasicNameValuePair("timeDate", "2010-01-12 01:21:30"));

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

					HttpResponse response = httpclient.execute(httppost);
					String strResult = EntityUtils.toString(response.getEntity());
					Log.e("strResult-------0000000", strResult);
					//
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			
		}
		
}
