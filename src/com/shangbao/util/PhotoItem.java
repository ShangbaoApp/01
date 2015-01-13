package com.shangbao.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoItem implements Serializable{
	private static final long serialVersionUID = -5053412967214724078L;  

	public List<String> columnName = new ArrayList<String>();
	public List<PhotoContent> contents = new ArrayList<PhotoContent>();
}
