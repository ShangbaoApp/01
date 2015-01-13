package com.shangbao.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * 在 最新资讯  中的，每个栏目的新闻
 * 
 */
public class News implements Serializable{
	private static final long serialVersionUID = -5053252967314724078L;
	
	public String title;
	public List<String> picUrls;
	public String summary;
	public int picNum;
	
	public News() {
		// TODO Auto-generated constructor stub
		picUrls = new ArrayList<String>();
	}
	
	public void add(String url) {
		picUrls.add(url);
	}
	
	public String getTitle() {
		return title;
	}
	public String getPicUrl(int i) {
		return picUrls.get(i);
	}
	public String getSummary() {
		return summary;
	}
	public int getPicNum() {
		return picUrls.size();
	}
	
	
}
