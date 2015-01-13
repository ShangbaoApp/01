package com.shangbao.activity;

import java.io.File;

import com.shangbao.R;

import android.net.Uri;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;

public class TkpicActivity extends Activity {

	public String filePath;
	public String picPath;
	Uri picUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tkpic);
	}
	
	public void tkpic(View v){
		/*Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 2);*/
		filePath = Environment.getExternalStorageDirectory() + "/my_camera/tmp.jpg";
		File photoFile = new File(filePath);  
		if (!photoFile.getParentFile().exists()) {  
		    photoFile.getParentFile().mkdirs();  
		}  
		if (photoFile.exists()) {
			photoFile.delete();
		}
		//ContentValues values = new ContentValues();    
		Uri picUri = Uri.fromFile(photoFile);
		//picUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); 
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);       
		// 设置系统相机拍摄照片完成后图片文件的存放地址
		intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
		startActivityForResult(intent, 1);
        
	}

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		Log.e("9999999----0000", "requestCode"+requestCode+"\tresultCode"+resultCode);
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == 2) {
				picUri = data.getData();
			}
			Intent intent = new Intent(TkpicActivity.this, UploadImageActivity.class);
			String[] pojo = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(picUri, pojo, null, null,null);
			if(cursor != null )  
	        {  
	            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);  
	            cursor.moveToFirst();  
	            filePath = cursor.getString(columnIndex);  
	            cursor.close();  
	        }  
			if(filePath != null && ( filePath.endsWith(".png") || filePath.endsWith(".PNG") ||filePath.endsWith(".jpg") ||filePath.endsWith(".JPG")  ))  
	        {  
				intent.putExtra("path", filePath);  
	            setResult(Activity.RESULT_OK, intent);  
	            finish();  
	        }
			//picUri.toString()
			//intent.putExtra("uri",picUri.toString());
			startActivity(intent);
			
			
			/////////////////////////
		}
		
	}
	*/
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == 2) {
                picPath = getAbsolutePath(data.getData());
            } else if (requestCode == 1) {
            	picPath = getImagePath();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
			Intent intent = new Intent(TkpicActivity.this, UploadImageActivity.class);
			intent.putExtra("path",picPath);
			startActivity(intent);
			finish();
        }

    }
	
	public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
	
	/***
	 * 从相册中取图片
	 */
	public void pickPhoto(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, 2);
	}
	
	
	public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "tmp.png");
        Uri imgUri = Uri.fromFile(file);
        filePath = file.getAbsolutePath();
        return imgUri;
    }

	public String getImagePath() {
        return filePath;
    }

	
}
