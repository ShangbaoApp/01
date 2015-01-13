package com.shangbao;

import java.util.ArrayList;

import com.shangbao.tabactivity.LatestActivity;
import com.shangbao.tabactivity.LocalActivity;
import com.shangbao.tabactivity.OriginActivity;
import com.shangbao.tabactivity.PhotoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainTabHost extends android.app.TabActivity{
	//继承TabActivity
	//tab要显示的文字
	String[] name = {"商报原创","最新资讯","本地报告","快拍成都"};
	//tab要显示的图片、点击效果，四个selector，选中和默认状态时的图片
	int[] imaResid = {R.drawable.tab_item,R.drawable.tab_item,R.drawable.tab_item,R.drawable.tab_item};
	
	ArrayList<TabSpec> arrayList = new ArrayList<TabHost.TabSpec>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);
        TabHost tabHost = getTabHost();
          Log.e("___++++____", "msg");                                                                                                               
        //复用布局，创建四个View的对象，并且分别设置View对象里的控件值和图片
            View tabrl = getLayoutInflater().inflate(R.layout.tab_text, null);
            TextView textView = (TextView)tabrl.findViewById(R.id.tab_item_tv);
            textView.setText("商报原创");
            TabSpec spec = tabHost.newTabSpec("tab0").setIndicator(tabrl);
            arrayList.add(spec);
            
            View tabr2 = getLayoutInflater().inflate(R.layout.tab_text, null);
            TextView textView1 = (TextView)tabr2.findViewById(R.id.tab_item_tv);
            textView1.setText("最新资讯");
            TabSpec spec1 = tabHost.newTabSpec("tab1").setIndicator(tabr2);
            arrayList.add(spec1);
            
            View tabr3 = getLayoutInflater().inflate(R.layout.tab_text, null);
            TextView textView2 = (TextView)tabr3.findViewById(R.id.tab_item_tv);
            textView2.setText("本地报告");
            TabSpec spec2 = tabHost.newTabSpec("tab2").setIndicator(tabr3);
            arrayList.add(spec2);
            
            View tabr4 = getLayoutInflater().inflate(R.layout.tab_text, null);
            TextView textView3 = (TextView)tabr4.findViewById(R.id.tab_item_tv);
            textView3.setText("快拍成都");
            TabSpec spec3 = tabHost.newTabSpec("tab3").setIndicator(tabr4);
            arrayList.add(spec3);
            
            
        tabHost.addTab(arrayList.get(0).setContent(new Intent(MainTabHost.this,MainActivity.class)));
        tabHost.addTab(arrayList.get(1).setContent(new Intent(MainTabHost.this,LatestActivity.class)));
        tabHost.addTab(arrayList.get(2).setContent(new Intent(MainTabHost.this,LocalActivity.class)));
        tabHost.addTab(arrayList.get(3).setContent(new Intent(MainTabHost.this,PhotoActivity.class)));
        Log.e("___++++____", "msg"); 
        tabHost.setCurrentTab(0);
    }
}
