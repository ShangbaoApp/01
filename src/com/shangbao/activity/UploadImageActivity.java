package com.shangbao.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.shangbao.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class UploadImageActivity extends Activity{

	private Bitmap	bitmap;
	public String url = "http://202.115.17.218:8080/Shangbao01/app/23234/uploadpic";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadimage);
		Intent intent = getIntent();
		String filePath = intent.getStringExtra("path");
		bitmap = BitmapFactory.decodeFile(filePath);
		ImageView imageView = (ImageView)findViewById(R.id.upimage);

		imageView.setImageBitmap(bitmap);
		//imageView.setImageURI(uri);
	}
	
	public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

	
}
