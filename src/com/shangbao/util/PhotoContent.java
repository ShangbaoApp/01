package com.shangbao.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoContent implements Serializable{
	private static final long serialVersionUID = -5053412967314724078L;  

	public List<String> titles = new ArrayList<String>();
	public List<String> picUrls = new ArrayList<String>();
	public List<String> summary = new ArrayList<String>();
	public List<Integer> newsId = new ArrayList<Integer>();
}
