package cn.hutool.http.server;

import cn.hutool.core.lang.Console;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;

public class SimpleServerTest {

	public static void main(String[] args) {
		HttpUtil.createServer(8888)
				// 设置默认根目录，
				.setRoot("d:/test")
				// 返回JSON数据测试
				.addAction("/restTest", (request, response) ->
						response.write("{\"id\": 1, \"msg\": \"OK\"}", ContentType.JSON.toString())
				)
				// 获取表单数据测试
				// http://localhost:8888/formTest?a=1&a=2&b=3
				.addAction("/formTest", (request, response) ->
						response.write(request.getParams().toString(), ContentType.TEXT_PLAIN.toString())
				)
				// 文件上传测试
				// http://localhost:8888/formTest?a=1&a=2&b=3
				.addAction("/file", (request, response) -> {
					final UploadFile file = request.getMultipart().getFile("file");
					// 传入目录，默认读取HTTP头中的文件名然后创建文件
					file.write("d:/test/");
					Console.log("Write file to: d:/test/");
					response.write(request.getParams().toString(), ContentType.TEXT_PLAIN.toString());
						}
				)
				.start();
	}
}
