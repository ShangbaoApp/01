package com.shangbao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shangbao.activity.NewsContent;
import com.shangbao.utl.GetNews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private ViewPager imagePager = null;
	private int[] imageIds;
	private ImageView[] mImageViews;
	private String url = null;
	
	private List<String> titles = new ArrayList<String>();
	private List<String> pictureUrls = new ArrayList<String>();
	//图片异步加载
	DisplayImageOptions options; //配置图片加载及显示选项
	String[] imageUrls;  //图片的URL
	//protected ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //图片的URL
        imageUrls = Constants.IMAGES;
		url = "http://120.27.47.167/Shangbao01/app/android/original/1";

        Datas datas = new Datas();
        datas.execute();
       // titles = news.getTitles();
        //pictureUrls = news.getPictureUrls();
        Log.e("++++", titles.size()+"");
		//配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)    //在ImageView加载过程中显示图片
			.showImageForEmptyUri(R.drawable.ic_empty)  //image连接地址为空时
			.showImageOnFail(R.drawable.ic_error)  //image加载失败
			.cacheInMemory(true)  //加载图片时会在内存中加载缓存
			.cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
			.displayer(new RoundedBitmapDisplayer(0))  //设置用户加载图片task(这里是圆角图片显示)
			.build();
        
        imagePager = (ViewPager)findViewById(R.id.imagePager);
        //初始化图片id
        imageIds = new int[]{R.drawable.item01,R.drawable.item02,R.drawable.item03,
        		R.drawable.item04,R.drawable.item05};
        //将图片加载到数组中
        mImageViews = new ImageView[imageIds.length];
        for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imageIds[i]);
		}
        imagePager.setAdapter(new MyAdapter());
        imagePager.setCurrentItem(0); //设置默认当前页
        Log.e("++++", titles.size()+"");

    }
    
    
    //对每个新闻条目设置监听器
    class ListClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(MainActivity.this,com.shangbao.activity.NewsContent.class);
            Log.e("11111111111",position+"+++00000000++"+id);  
            startActivity(intent);
            
        }

    }
    //滑动切换图片的适配器
    class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView(mImageViews[position%mImageViews.length]);
			return ;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(mImageViews[position%mImageViews.length], 0);
			 //mImageViews[position%mImageViews.length].setScaleType(ImageView.ScaleType.FIT_XY);
			 return mImageViews[position%mImageViews.length];
		}
    	
    }
    
    /*
     *图片异步加载 
     * 
     */
	/**自定义图片适配器**/
	class ItemAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return titles.size();
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
			final ViewHolder holder;
			if (convertView == null) {
				view = getLayoutInflater().inflate(R.layout.newslist, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.tvTitle);
				holder.image = (ImageView) view.findViewById(R.id.ivPreview);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(titles.get(position));

			//Adds display image task to execution pool. Image will be set to ImageView when it's turn.
			ImageLoader.getInstance().displayImage(pictureUrls.get(position), holder.image, options, animateFirstListener);

			return view;
		}
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

	class Datas extends AsyncTask<Void, Integer, Integer>{

		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
	        ListView listView = (ListView) findViewById(R.id.mylist);
	        //为list设置Adapter
	        listView.setAdapter(new ItemAdapter());
	        listView.setOnItemClickListener(new ListClickListener());
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
	
	
}
