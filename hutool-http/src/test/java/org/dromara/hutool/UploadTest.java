package org.dromara.hutool;

import org.dromara.hutool.client.Request;
import org.dromara.hutool.client.Response;
import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.io.resource.MultiFileResource;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.meta.Header;
import org.dromara.hutool.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
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
	@Disabled
	public void uploadFilesTest() {
		final File file = FileUtil.file("d:\\图片1.JPG");
		final File file2 = FileUtil.file("d:\\图片3.png");

		final Map<String, Object> form = MapUtil.builder(new HashMap<String, Object>())
				.put("file", new MultiFileResource(file2, file))
				.put("fileType", "图片")
				.build();

		// 方法一：自定义构建表单
		final Request request = Request//
				.of("http://localhost:8888/file")//
				.method(Method.POST)
				.form(form);
		//noinspection resource
		final Response response = request.send();
		Console.log(response.body());
	}

	@Test
	@Disabled
	public void uploadFileTest() {
		final File file = FileUtil.file("D:\\face.jpg");

		// 方法二：使用统一的表单，Http模块会自动识别参数类型，并完成上传
		final HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("city", "北京");
		paramMap.put("file", file);
		final String result = HttpUtil.post("http://wthrcdn.etouch.cn/weather_mini", paramMap);
		System.out.println(result);
	}

	@Test
	@Disabled
	public void smmsTest(){
		// https://github.com/dromara/hutool/issues/2079
		// hutool的user agent 被封了
		final String token = "test";
		final String url = "https://sm.ms/api/v2/upload";
		//noinspection resource
		final String result = Request.of(url)
				.method(Method.POST)
				.header(Header.USER_AGENT, "PostmanRuntime/7.28.4")
				.auth(token)
				.form(MapUtil.of("smfile", FileUtil.file("d:/test/qrcodeCustom.png")))
				.send().bodyStr();

		Console.log(result);
	}
}
