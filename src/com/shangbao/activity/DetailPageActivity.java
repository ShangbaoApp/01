package com.shangbao.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shangbao.R;
import com.shangbao.util.Column;
import com.shangbao.util.News;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailPageActivity extends Activity{

	DisplayImageOptions options; //配置图片加载及显示选项
	Column column ;
	List<News> newsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailpage);
		
		Intent intent = getIntent();
		column = (Column)intent.getSerializableExtra("column");
		String name = column.columnName;
		TextView title = (TextView)findViewById(R.id.catalog);
		title.setText(name);
		newsList = column.newsList;
		
		//配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)    //在ImageView加载过程中显示图片
			.showImageForEmptyUri(R.drawable.ic_empty)  //image连接地址为空时
			.showImageOnFail(R.drawable.ic_error)  //image加载失败
			.cacheInMemory(true)  //加载图片时会在内存中加载缓存
			.cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
			.displayer(new RoundedBitmapDisplayer(0))  //设置用户加载图片task(这里是圆角图片显示)
			.build();
		
		ListView listView = (ListView)findViewById(R.id.detaillist);
		listView.setAdapter(new ItemAdapter());
	}
	
	/**自定义图片适配器**/
	class ItemAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private class ViewHolder {
			public LinearLayout[] layouts = new LinearLayout[2];

			
		}

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		 @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewHolder ;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				//得到viewHold，并赋值
				view = getLayoutInflater().inflate(R.layout.detailpage_layout,null);
				//view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
				viewHolder.layouts[0] = (LinearLayout) view.findViewById(R.id.item);
				//Log.e("___++++____", "00000");
				//viewHolder.item_layout.removeAllViews();
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			//对viewHold具体初始化
			viewHolder.layouts[0].removeAllViews();
			//根据图片的多少
			for (int i = 0; i < 1; i++) { //添加新闻 layout数目
				//根据点击的位置position，从分栏中获取栏目，并从栏目中获取第i条新闻
				int num = newsList.get(position).getPicNum();
				View viewNews = null;
				
				TextView title;
				TextView content;
				switch (num) { //在各个布局中设置信息
				case 0:
					viewNews = getLayoutInflater().inflate(R.layout.no_image_latest_content,null);
					title = (TextView) viewNews.findViewById(R.id.title);
					title.setText(newsList.get(i).getTitle());
					content = (TextView) viewNews.findViewById(R.id.content);
					content.setText(newsList.get(i).getSummary());
					break;
				case 1:
					viewNews = getLayoutInflater().inflate(R.layout.one_image_latest_content,null);
					title = (TextView) viewNews.findViewById(R.id.title);
					title.setText(newsList.get(i).getTitle());
					content = (TextView) viewNews.findViewById(R.id.content);
					content.setText(newsList.get(i).getSummary());
					ImageView imageView = (ImageView)viewNews.findViewById(R.id.icon1);
					ImageLoader.getInstance().displayImage(newsList.get(i).getPicUrl(0), imageView, options, animateFirstListener);

					break;
				case 3:
					viewNews = getLayoutInflater().inflate(R.layout.three_image_latest_content,null);
					title = (TextView) viewNews.findViewById(R.id.title);
					title.setText(newsList.get(i).getTitle());
					ImageView icon1 = (ImageView)viewNews.findViewById(R.id.icon1);
					ImageView icon2 = (ImageView)viewNews.findViewById(R.id.icon2);
					ImageView icon3 = (ImageView)viewNews.findViewById(R.id.icon3);
					ImageLoader.getInstance().displayImage(newsList.get(i).getPicUrl(0), icon1, options, animateFirstListener);
					ImageLoader.getInstance().displayImage(newsList.get(i).getPicUrl(0), icon2, options, animateFirstListener);
					ImageLoader.getInstance().displayImage(newsList.get(i).getPicUrl(0), icon3, options, animateFirstListener);
					break;
				default:
					break;
				}
				viewHolder.layouts[0].addView(viewNews);
			}
			//viewHolder.news2_layout.removeAllViews();
			//viewHolder.news2_layout.addView(view2);
			return view;
		}
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
				//getDatas();
				return null;
			}

			@Override
			protected void onPostExecute(Integer result) {  
				ListView listView = (ListView)findViewById(R.id.columnlist);
				listView.setAdapter(new ItemAdapter());
		       // listView.setOnItemClickListener(new ListClickListener());
		    }
			
			/*public void getDatas() {
				// 创建一个新的HttpClient Post头
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httppost = new HttpGet(url); 
				try {
					HttpResponse response = httpclient.execute(httppost);
					String strResult = EntityUtils.toString(response.getEntity());
					JSONObject jsonobject = new JSONObject(strResult);
					JSONArray contentColumns = jsonobject.getJSONArray("contentColumns");
					//
					Log.e("+-+-+-+-+-",""+contentColumns.length());
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
								nws.add("http://192.168.1.119:8080/Shangbao01"+picUrlArray.get(k).toString());
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
						//pictureUrls.add("http://192.168.1.119:8080/Shangbao01"+content.getString("pictureUrl"));
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
			*/
		}
		
	/**图片加载监听事件**/
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500); //设置image隐藏动画500ms
					displayedImages.add(imageUri); //将图片uri添加到集合中
				}
			}
		}
	}

}
