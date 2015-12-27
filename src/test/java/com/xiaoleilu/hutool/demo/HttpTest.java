package com.xiaoleilu.hutool.demo;

import java.io.IOException;

import com.xiaoleilu.hutool.http.HttpRequest;

public class HttpTest {
	public static void main(String[] args) throws IOException {
		HttpRequest request = HttpRequest.get("http://www.cnblogs.com/zhuawang/archive/2012/12/08/2809380.html");
		request.charset("utf-8");
		System.out.println(request.execute().body());
		
//		HttpUtil.get("http://www.cnblogs.com/zhuawang/archive/2012/12/08/2809380.html", "utf-8");
		
//		HttpRequest post = HttpRequest.post("http://localhost:8091/ga-weixin/file/upload");
//		post.form("test", "testValue");
//		post.form("test2", "testValue2");
//		post.form("testFile", FileUtil.file("d:/aaa.txt"));
//		post.form("testFile2", FileUtil.file("d:/aaa.txt"));
		
//		HttpResponse response = post.execute();
//		System.out.println(response.body());
	}
}
