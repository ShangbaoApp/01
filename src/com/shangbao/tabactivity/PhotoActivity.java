package com.shangbao.tabactivity;

import java.io.IOException;
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
import com.shangbao.activity.DetailPageActivity;
import com.shangbao.activity.PhotoContentActivity;
import com.shangbao.activity.TkpicActivity;
import com.shangbao.tabactivity.LatestActivity.ItemAdapter;
import com.shangbao.tabactivity.LocalActivity.ListClickListener;
import com.shangbao.util.Column;
import com.shangbao.util.ContentKuaipai;
import com.shangbao.util.DataKuaipai;
import com.shangbao.util.News;
import com.shangbao.util.PhotoContent;
import com.shangbao.util.PhotoItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/*
 *���ĳɶ���activity 
 */
public class PhotoActivity extends Activity {
	String url;
	String channelName;
	PhotoItem dataKuaipai;
	
	DisplayImageOptions options; //����ͼƬ���ؼ���ʾѡ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		url = "http://202.115.17.218:8080/Shangbao01/app/android/kuaipai";
		
		//����ͼƬ���ؼ���ʾѡ�����һЩ���������ã�����doc�ĵ��ɣ�
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)    //��ImageView���ع�������ʾͼƬ
			.showImageForEmptyUri(R.drawable.ic_empty)  //image���ӵ�ַΪ��ʱ
			.showImageOnFail(R.drawable.ic_error)  //image����ʧ��
			.cacheInMemory(true)  //����ͼƬʱ�����ڴ��м��ػ���
			.cacheOnDisc(true)   //����ͼƬʱ���ڴ����м��ػ���
			.displayer(new RoundedBitmapDisplayer(0))  //�����û�����ͼƬtask(������Բ��ͼƬ��ʾ)
			.build();
		DatasKuaipai datasKuaipai = new DatasKuaipai();
		datasKuaipai.execute();
		
	}
	public void click(View v){
		int id = v.getId();
		if (id == R.id.img) {
			Intent intent = new Intent(PhotoActivity.this,TkpicActivity.class);
			startActivity(intent);
		} else {
		}
	}
	
    //��ÿ��������Ŀ���ü�����
    class ListClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(PhotoActivity.this,PhotoContentActivity.class);
            Log.e("11111111111",position+"+++00000000++"+id);  
            intent.putExtra("columnName", dataKuaipai.columnName.get(position));
            intent.putExtra("column",dataKuaipai.contents.get(position));
            startActivity(intent);
        }
    }
	
	//�����첽����ʱ��Ҫ���� class.execute()����
	//�첽���ؼ���photo������
		class DatasKuaipai extends AsyncTask<Void, Integer, Integer>{

				@Override
				protected Integer doInBackground(Void... params) {
					// TODO Auto-generated method stub 
					Log.e("---+++------o0o0o0o", "hhelll");
					getDatas();
					return null;
				}

				@Override
				protected void onPostExecute(Integer result) {  
					ListView listView = (ListView)findViewById(R.id.imagelist);
					listView.setAdapter(new ItemAdapter());
			        listView.setOnItemClickListener(new ListClickListener());

			    }
				
				public void getDatas() {
					// ����һ���µ�HttpClient Postͷ
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet httppost = new HttpGet(url); 
					try {
						HttpResponse response = httpclient.execute(httppost);
						String strResult = EntityUtils.toString(response.getEntity());
						JSONObject jsonobject = new JSONObject(strResult);
						//��ȡ��Ŀ��  "���ĳɶ�"
						channelName = jsonobject.getString("channelName");
						JSONArray contentColumns = jsonobject.getJSONArray("contentColumns");
						//
						dataKuaipai = new PhotoItem();
						Log.e("+-+-+-+-+-",""+contentColumns.length());
						for (int i = 0; i < contentColumns.length(); i++) {
							//��ȡÿ����Ŀ��һ������  "contentColumns"
							
							JSONObject contentColumn = contentColumns.getJSONObject(i);
							dataKuaipai.columnName.add(contentColumn.getString("columnName"));
							JSONArray contents = contentColumn.getJSONArray("content");
							PhotoContent cKuaipai = new PhotoContent();
							for (int j = 0; j < contents.length(); j++) {
								//һ����Ŀ��һ��content  �� title��picUrl��summary��newsId
								
								JSONObject content = contents.getJSONObject(j);
								cKuaipai.titles.add(content.getString("title"));
								Log.e("++++++++++++++ooooo", ""+content.getString("title"));
								Log.e("++++++++++++++ooooo", ""+content.getString("picUrl"));
								cKuaipai.picUrls.add("http://202.115.17.218:8080/Shangbao01"+content.getString("picUrl"));
								cKuaipai.summary.add(content.getString("summary"));
								cKuaipai.newsId.add(content.getInt("newsId"));
								dataKuaipai.contents.add(cKuaipai);
								//nList.add(nws);
								//���Ի�������ַ���
								//Log.e("___++++nws.title+++___",content.getJSONArray("picUrl").get(0).toString());
								//Log.e("___++++nws.summary+++___",nws.summary);
								
							}
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
		
		
		/**�Զ���ͼƬ������**/
		class ItemAdapter extends BaseAdapter {
			private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
			
			private class ViewHolder {
				//public LinearLayout[] layouts = new LinearLayout[3];
				//public LinearLayout col_layout;
				//public LinearLayout news1_layout;
				//public LinearLayout news2_layout;
				public TextView catlog;
				public TextView text;
				public ImageView image;
				public TextView text1;
				public ImageView image1;
				//public View view;
				
			}

			@Override
			public int getCount() {
				Log.e("++++++++++++++ooooo", ""+dataKuaipai.columnName.size());
				return dataKuaipai.columnName.size();
			}

			@Override
			public Object getItem(int position) {
				//Log.e("++++++++++++++ooooo", ""+dataList.size());
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
					//�õ�viewHold������ֵ
					view = getLayoutInflater().inflate(R.layout.photo_item_layout,null);
					//view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
					viewHolder.catlog = (TextView) view.findViewById(R.id.catlog);
					viewHolder.text = (TextView) view.findViewById(R.id.title);
					viewHolder.image = (ImageView) view.findViewById(R.id.image);
					
					viewHolder.text1 = (TextView) view.findViewById(R.id.title1);
					viewHolder.image1 = (ImageView) view.findViewById(R.id.image1);
					//Log.e("___++++____", "00000");
					//viewHolder.item_layout.removeAllViews();
					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}
				//��viewHold�����ʼ��
				viewHolder.catlog.setText(dataKuaipai.columnName.get(position));
				viewHolder.text.setText(dataKuaipai.contents.get(position).titles.get(0));
				
				viewHolder.text1.setText(dataKuaipai.contents.get(position).titles.get(1));
				ImageLoader.getInstance().displayImage(dataKuaipai.contents.get(position).picUrls.get(0), viewHolder.image, options, animateFirstListener);
				ImageLoader.getInstance().displayImage(dataKuaipai.contents.get(position).picUrls.get(1), viewHolder.image1, options, animateFirstListener);

				
				//viewHolder.news2_layout.removeAllViews();
				//viewHolder.news2_layout.addView(view2);
				//Log.e("++++++++++++++ooooo", ""+columns.size());
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

		
	
}