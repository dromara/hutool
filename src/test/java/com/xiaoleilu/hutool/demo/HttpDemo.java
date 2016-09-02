package com.xiaoleilu.hutool.demo;

import java.io.File;
import java.io.IOException;

import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;
import com.xiaoleilu.hutool.http.ssl.SSLSocketFactoryBuilder;
import com.xiaoleilu.hutool.util.FileUtil;

public class HttpDemo {
	public static void main(String[] args) throws IOException {
		// HttpRequest request = HttpRequest.get("https://www.baidu.com/");
		//// request.charset("utf-8");
		// HttpResponse res = request.execute();
		// Map<String, List<String>> headers = res.headers();
		// for (Entry<String, List<String>> entry : headers.entrySet()) {
		// System.out.println(entry);
		// }
		// System.out.println(res.body());

		 HttpRequest request = HttpRequest
				 .get("https://www.baidu.com")
				 .disableCache()
		 		.setSSLProtocol(SSLSocketFactoryBuilder.TLSv12);
		 System.out.println(request.execute().body());

		// HttpRequest post = HttpRequest.post("http://localhost:8091/ga-weixin/file/upload");
		// post.form("test", "testValue");
		// post.form("test2", "testValue2");
		// post.form("testFile", FileUtil.file("d:/aaa.txt"));
		// post.form("testFile2", FileUtil.file("d:/aaa.txt"));

		// HttpResponse response = post.execute();
		// System.out.println(response.body());

//		uploadFile(FileUtil.file("D:\\face.jpg"));
//		
//		HttpRequest request = HttpRequest.get("http://www.wepe.com.cn/about.html");
//		HttpResponse res = request.execute();
//		System.out.println(res.body());
	}

	private static String uploadFile(File file) {
		HttpRequest request = HttpRequest.post("http://localhost:8080/file/upload").form("file", file).form("fileType", "图片");
		HttpResponse response = request.execute();
		System.out.println(response.body());
		return null;
	}
}
