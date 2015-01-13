package com.shangbao.tabactivity;

import com.shangbao.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 商报原创的activity
 */
public class OriginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ListView listView = (ListView)findViewById(R.id.columnlist);
		listView.setAdapter(new ItemAdapter());
	}
	
	/**自定义图片适配器**/
	class ItemAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return 3;
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
				view = getLayoutInflater().inflate(R.layout.contentlist, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.tvTitle);
				holder.image = (ImageView) view.findViewById(R.id.ivPreview);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			//holder.text.setText(titles.get(position));

			//Adds display image task to execution pool. Image will be set to ImageView when it's turn.
			//ImageLoader.getInstance().displayImage(pictureUrls.get(position), holder.image, options, animateFirstListener);

			return view;
		}
	}

}