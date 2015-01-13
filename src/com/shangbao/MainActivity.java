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
	//ͼƬ�첽����
	DisplayImageOptions options; //����ͼƬ���ؼ���ʾѡ��
	String[] imageUrls;  //ͼƬ��URL
	//protected ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //ͼƬ��URL
        imageUrls = Constants.IMAGES;
		url = "http://120.27.47.167/Shangbao01/app/android/original/1";

        Datas datas = new Datas();
        datas.execute();
       // titles = news.getTitles();
        //pictureUrls = news.getPictureUrls();
        Log.e("++++", titles.size()+"");
		//����ͼƬ���ؼ���ʾѡ�����һЩ���������ã�����doc�ĵ��ɣ�
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)    //��ImageView���ع�������ʾͼƬ
			.showImageForEmptyUri(R.drawable.ic_empty)  //image���ӵ�ַΪ��ʱ
			.showImageOnFail(R.drawable.ic_error)  //image����ʧ��
			.cacheInMemory(true)  //����ͼƬʱ�����ڴ��м��ػ���
			.cacheOnDisc(true)   //����ͼƬʱ���ڴ����м��ػ���
			.displayer(new RoundedBitmapDisplayer(0))  //�����û�����ͼƬtask(������Բ��ͼƬ��ʾ)
			.build();
        
        imagePager = (ViewPager)findViewById(R.id.imagePager);
        //��ʼ��ͼƬid
        imageIds = new int[]{R.drawable.item01,R.drawable.item02,R.drawable.item03,
        		R.drawable.item04,R.drawable.item05};
        //��ͼƬ���ص�������
        mImageViews = new ImageView[imageIds.length];
        for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imageIds[i]);
		}
        imagePager.setAdapter(new MyAdapter());
        imagePager.setCurrentItem(0); //����Ĭ�ϵ�ǰҳ
        Log.e("++++", titles.size()+"");

    }
    
    
    //��ÿ��������Ŀ���ü�����
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
    //�����л�ͼƬ��������
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
     *ͼƬ�첽���� 
     * 
     */
	/**�Զ���ͼƬ������**/
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

	/**ͼƬ���ؼ����¼�**/
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500); //����image���ض���500ms
					displayedImages.add(imageUri); //��ͼƬuri��ӵ�������
				}
			}
		}
	}

	class Datas extends AsyncTask<Void, Integer, Integer>{

		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
	        ListView listView = (ListView) findViewById(R.id.mylist);
	        //Ϊlist����Adapter
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
			// ����һ���µ�HttpClient Postͷ
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
