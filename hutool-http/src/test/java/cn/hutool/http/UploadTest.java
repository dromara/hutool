package cn.hutool.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传单元测试
 *
 * @author looly
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

	@Test
	@Ignore
	public void uploadTest2() {
		//客户端
		String url = "http://192.168.1.200:8888/meta/upload/img";
		Path file = Paths.get("D:\\test\\testBigData_upload.xlsx");
		Map<String, String> headers = new HashMap<>(16);
		headers.put("md5", "aaaaaaaa");

		Map<String, Object> params = new HashMap<>(16);
		params.put("fileName", file.toFile().getName());
		params.put("file", file.toFile());
		HttpRequest httpRequest = HttpRequest.post(url)
				.setChunkedStreamingMode(1024 * 1024)
				.headerMap(headers, false)
				.form(params);
		HttpResponse httpResponse = httpRequest.execute();
		Console.log(httpResponse);
	}

	@Test
	@Ignore
	public void smmsTest(){
		// https://github.com/dromara/hutool/issues/2079
		// hutool的user agent 被封了
		String token = "test";
		String url = "https://sm.ms/api/v2/upload";
		String result = HttpUtil.createPost(url)
				.header(Header.USER_AGENT, "PostmanRuntime/7.28.4")
				.auth(token)
				.form("smfile", FileUtil.file("d:/test/qrcodeCustom.png"))
				.execute().body();

		Console.log(result);
	}
}
