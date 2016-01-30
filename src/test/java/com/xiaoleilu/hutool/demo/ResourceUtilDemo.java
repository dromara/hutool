package com.xiaoleilu.hutool.demo;

import java.io.File;
import java.net.URL;

import com.xiaoleilu.hutool.util.ResourceUtil;

public class ResourceUtilDemo {
	public static void main(String[] args) {
		String resource = "config/demo.set";
		
		//Resource File
		File file = ResourceUtil.getFile(resource);
		System.out.println(file);
		
		URL url = ResourceUtil.getURL(resource);
		System.out.println(url.getFile());
		System.out.println(url.getPath());
	}
}
