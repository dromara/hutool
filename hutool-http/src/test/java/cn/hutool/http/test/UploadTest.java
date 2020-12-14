package cn.hutool.http.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

/**
 * 文件上传单元测试
 * @author looly
 *
 */
public class UploadTest {
	
	/**
	 * 多文件上传测试
	 */
	@Test
	@Ignore
	public void uploadFilesTest() {
		File file = FileUtil.file("d:\\图片1.JPG");
		File file2 = FileUtil.file("d:\\图片3.png");

		// 方法一：自定义构建表单
		HttpRequest request = HttpRequest//
				.post("http://localhost:8888/file")//
				.form("file", file2, file)//
				.form("fileType", "图片");
		HttpResponse response = request.execute();
		Console.log(response.body());
	}
	
	@Test
	@Ignore
	public void uploadFileTest() {
		File file = FileUtil.file("D:\\face.jpg");

		// 方法二：使用统一的表单，Http模块会自动识别参数类型，并完成上传
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("city", "北京");
		paramMap.put("file", file);
		String result = HttpUtil.post("http://wthrcdn.etouch.cn/weather_mini", paramMap);
		System.out.println(result);
	}
}
