package com.shangbao.tabactivity;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shangbao.R;
import com.shangbao.activity.DetailPageActivity;
import com.shangbao.tabactivity.LocalActivity.GetDatas;
import com.shangbao.tabactivity.LocalActivity.ItemAdapter;
import com.shangbao.tabactivity.LocalActivity.ListClickListener;
import com.shangbao.util.Column;
import com.shangbao.util.News;
import com.shangbao.utl.GetNewsList;

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
import android.widget.Toast;

/*
 * ������Ѷ��activity
 */
public class LatestActivity extends Activity {
	
	public List<Column> columns = new ArrayList<Column>();
	public String url;
	public PullToRefreshListView mPullRefreshListView;
	ListView listView;
	DisplayImageOptions options; //����ͼƬ���ؼ���ʾѡ��
	ItemAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latest);
		Log.e("++++++++++++++ooooo", "+++++");
		url = "http://120.27.47.167/Shangbao01/app/android/newest";

		//����ͼƬ���ؼ���ʾѡ�����һЩ���������ã�����doc�ĵ��ɣ�
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_stub)    //��ImageView���ع�������ʾͼƬ
			.showImageForEmptyUri(R.drawable.ic_empty)  //image���ӵ�ַΪ��ʱ
			.showImageOnFail(R.drawable.ic_error)  //image����ʧ��
			.cacheInMemory(true)  //����ͼƬʱ�����ڴ��м��ػ���
			.cacheOnDisc(true)   //����ͼƬʱ���ڴ����м��ػ���
			.displayer(new RoundedBitmapDisplayer(0))  //�����û�����ͼƬtask(������Բ��ͼƬ��ʾ)
			.build();
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		
		//�������������¼�
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				//Toast.makeText(this, "����ˢ��",Toast.LENGTH_SHORT).show();
				Log.e("+++-88888888--+++", "000000000"+columns.size());
				new GetDatas(null).execute();
			}
			
		});
		mPullRefreshListView.setMode(Mode.PULL_FROM_END);
		listView = mPullRefreshListView.getRefreshableView(); 

		
		//��÷����б�
		//GetNewsList getNewsList = new GetNewsList();
		//columns = getNewsList.getColumns();
		//Log.e("++++++++++++++ooooo", ""+columns.size());
		GetDatas getDatas = new GetDatas(this);
		getDatas.execute();
		adapter = new ItemAdapter();
		listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListClickListener());

	}
	
	  //��ÿ��������Ŀ���ü�����
    class ListClickListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(LatestActivity.this,DetailPageActivity.class);
            Log.e("11111111111",position+"+++00000000++"+id);  
            intent.putExtra("column",columns.get(position));
            startActivity(intent);
        }
    }
	
    public void call(View v){
    	new ListClickListener();
    }
	
	/**�Զ���ͼƬ������**/
	class ItemAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
		private class ViewHolder {
			public LinearLayout[] layouts = new LinearLayout[3];
			//public LinearLayout col_layout;
			//public LinearLayout news1_layout;
			//public LinearLayout news2_layout;
			//public TextView text;
			//public ImageView image;
			//public View view;
			
		}

		@Override
		public int getCount() {
			//Log.e("++++++++++++++ooooo", ""+columns.size());
			return columns.size();
		}

		@Override
		public Object getItem(int position) {
			Log.e("++++++++++++++ooooo", ""+columns.size());
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
				view = getLayoutInflater().inflate(R.layout.news_item_layout,null);
				//view = getLayoutInflater().inflate(R.layout.news_item_layout, parent, false);
				viewHolder.layouts[0] = (LinearLayout) view.findViewById(R.id.col);
				viewHolder.layouts[1] = (LinearLayout) view.findViewById(R.id.news1);
				viewHolder.layouts[2] = (LinearLayout) view.findViewById(R.id.news2);
				//Log.e("___++++____", "00000");
				//viewHolder.item_layout.removeAllViews();
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			//��viewHold�����ʼ��
			View viewCol = getLayoutInflater().inflate(R.layout.col,null);
			TextView colView = (TextView) viewCol.findViewById(R.id.clmn);
			//������Ŀ����
			colView.setText(columns.get(position).columnName);
			viewHolder.layouts[0].removeAllViews();
			viewHolder.layouts[1].removeAllViews();
			viewHolder.layouts[2].removeAllViews();
			viewHolder.layouts[0].addView(viewCol);
			//����ͼƬ�Ķ���
			for (int i = 0; i < 2; i++) { //�����������
				//���ݵ����λ��position���ӷ����л�ȡ��Ŀ��������Ŀ�л�ȡ��i������
				int num = columns.get(position).newsList.get(i).getPicNum();
				View viewNews = null;
				
				TextView title;
				TextView content;
				switch (num) { //�ڸ���������������Ϣ
				case 0:
					viewNews = getLayoutInflater().inflate(R.layout.no_image_latest_content,null);
					title = (TextView) viewNews.findViewById(R.id.title);
					title.setText(columns.get(position).newsList.get(i).getTitle());
					content = (TextView) viewNews.findViewById(R.id.content);
					content.setText(columns.get(position).newsList.get(i).getSummary());
					break;
				case 1:
					viewNews = getLayoutInflater().inflate(R.layout.one_image_latest_content,null);
					title = (TextView) viewNews.findViewById(R.id.title);
					title.setText(columns.get(position).newsList.get(i).getTitle());
					content = (TextView) viewNews.findViewById(R.id.content);
					content.setText(columns.get(position).newsList.get(i).getSummary());
					ImageView imageView = (ImageView)viewNews.findViewById(R.id.icon1);
					ImageLoader.getInstance().displayImage(columns.get(position).newsList.get(i).getPicUrl(0), imageView, options, animateFirstListener);

					break;
				case 3:
					viewNews = getLayoutInflater().inflate(R.layout.three_image_latest_content,null);
					title = (TextView) viewNews.findViewById(R.id.title);
					title.setText(columns.get(position).newsList.get(i).getTitle());
					ImageView icon1 = (ImageView)viewNews.findViewById(R.id.icon1);
					ImageView icon2 = (ImageView)viewNews.findViewById(R.id.icon2);
					ImageView icon3 = (ImageView)viewNews.findViewById(R.id.icon3);
					ImageLoader.getInstance().displayImage(columns.get(position).newsList.get(i).getPicUrl(0), icon1, options, animateFirstListener);
					ImageLoader.getInstance().displayImage(columns.get(position).newsList.get(i).getPicUrl(0), icon2, options, animateFirstListener);
					ImageLoader.getInstance().displayImage(columns.get(position).newsList.get(i).getPicUrl(0), icon3, options, animateFirstListener);
					break;
				default:
					break;
				}
				viewHolder.layouts[i+1].addView(viewNews);
			}
			//viewHolder.news2_layout.removeAllViews();
			//viewHolder.news2_layout.addView(view2);
			Log.e("++++++++++++++ooooo", ""+columns.size());
			return view;
		}
	}

	
	//�����첽����ʱ��Ҫ���� class.execute()����
	class GetDatas extends AsyncTask<Void, Integer, ArrayList<Column>>{

			private Context context;
			
			public GetDatas(Context context) {
				// TODO Auto-generated constructor stub 
				this.context = context;
			}
			@Override
			protected ArrayList<Column> doInBackground(Void... params) {
				// TODO Auto-generated method stub 
				Log.e("+++////**", "doInBackgroud");
				getDatas();
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Column> result) {  
				//columns = result;
				Log.e("+++88888++++", columns.size()+"");
				adapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();
		    }
			
			public void getDatas() {
				// ����һ���µ�HttpClient Postͷ
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
							//���Ի�������ַ���
							//Log.e("___++++nws.title+++___",content.getJSONArray("picUrl").get(0).toString());
							Log.e("___++++nws.summary+++___",nws.summary);
							
						}
						column.newsList = nList;
						//Log.e("++++++++++++++88888", ""+columns.size());
						columns.add(column);
						//Log.e("++++++++++++++88888", ""+columns.size());
						columns.add(column);
						//Log.e("++++++++++++++88888", ""+columns.size());
						//columns.add(column);
						//Log.e("++++++++++++++88888", ""+columns.size());
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