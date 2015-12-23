package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.FileUtil;
import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;

public class HttpTest {
	public static void main(String[] args) {
//		HttpRequest request = HttpRequest.post("http://my.oschina.net/looly");
//		HttpResponse response = request.execute();
//		System.out.println(response.body());
		
		HttpRequest post = HttpRequest.post("http://localhost:8091/ga-weixin/file/upload");
		post.form("test", "testValue");
		post.form("test2", "testValue2");
		post.form("testFile", FileUtil.file("d:/aaa.txt"));
		post.form("testFile2", FileUtil.file("d:/aaa.txt"));
		
		HttpResponse response = post.execute();
		System.out.println(response.body());
	}
}
