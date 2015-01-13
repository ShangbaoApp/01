package com.shangbao.activity;

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
import com.shangbao.tabactivity.PhotoActivity;
import com.shangbao.util.PhotoContent;
import com.shangbao.util.PhotoItem;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoContentActivity extends Activity{

	PhotoContent content;
	DisplayImageOptions options; //����ͼƬ���ؼ���ʾѡ��
	String  columnName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photocontent);
		Intent intent = getIntent();
		content = (PhotoContent) intent.getSerializableExtra("column");
		columnName = intent.getStringExtra("columnName");
		TextView tView = (TextView) findViewById(R.id.columnName);
		tView.setText(columnName);
		//����ͼƬ���ؼ���ʾѡ�����һЩ���������ã�����doc�ĵ��ɣ�
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)    //��ImageView���ع�������ʾͼƬ
			.showImageForEmptyUri(R.drawable.ic_empty)  //image���ӵ�ַΪ��ʱ
			.showImageOnFail(R.drawable.ic_error)  //image����ʧ��
			.cacheInMemory(true)  //����ͼƬʱ�����ڴ��м��ػ���
			.cacheOnDisc(true)   //����ͼƬʱ���ڴ����м��ػ���
			.displayer(new RoundedBitmapDisplayer(0))  //�����û�����ͼƬtask(������Բ��ͼƬ��ʾ)
			.build();
		ListView listView = (ListView) findViewById(R.id.photolist);
		listView.setAdapter(new ItemAdapter());
	}
	
	
    //��ÿ��������Ŀ���ü�����
    class ListClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(PhotoContentActivity.this,PhotoContentActivity.class);
            Log.e("11111111111",position+"+++00000000++"+id);  
            
           // intent.putExtra("column",dataKuaipai.contents.get(position));
            //startActivity(intent);
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
				public TextView text;
				public ImageView image;
				//public View view;
				
			}

			@Override
			public int getCount() {
				Log.e("++++++++++++++ooooo", ""+content.titles.size());
				return content.titles.size();
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
					view = getLayoutInflater().inflate(R.layout.photo_content_item,null);
					//view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
					viewHolder.text = (TextView) view.findViewById(R.id.title);
					viewHolder.image = (ImageView) view.findViewById(R.id.image);
					
					//Log.e("___++++____", "00000");
					//viewHolder.item_layout.removeAllViews();
					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}
				//��viewHold�����ʼ��
				viewHolder.text.setText(content.titles.get(position));
				
				ImageLoader.getInstance().displayImage(content.picUrls.get(position), viewHolder.image, options, animateFirstListener);

				
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
