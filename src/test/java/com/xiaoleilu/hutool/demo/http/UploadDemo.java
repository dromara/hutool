package com.xiaoleilu.hutool.demo.http;

import java.io.File;
import java.util.HashMap;

import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.io.FileUtil;

/**
 * 上传文件样例
 * 
 * @author Looly
 *
 */
public class UploadDemo {
	public static void main(String[] args) {
		File file = FileUtil.file("D:\\face.jpg");

		// 方法一：自定义构建表单
		HttpRequest request = HttpRequest
				.post("http://localhost:8080/file/upload")
				.form("file", file)
				.form("fileType", "图片");
		HttpResponse response = request.execute();
		System.out.println(response.body());

		// 方法二：使用统一的表单，Http模块会自动识别参数类型，并完成上传
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("city", "北京");
		paramMap.put("file", file);
		String result = HttpUtil.post("http://wthrcdn.etouch.cn/weather_mini", paramMap);
		System.out.println(result);
	}
}
