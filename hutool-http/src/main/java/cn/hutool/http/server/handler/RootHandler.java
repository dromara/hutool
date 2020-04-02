package cn.hutool.http.server.handler;

import cn.hutool.core.io.FileUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * 默认的处理器，通过解析用户传入的path，找到网页根目录下对应文件后返回
 *
 * @author looly
 * @since 5.2.6
 */
public class RootHandler implements HttpHandler {

	private final String rootDir;

	/**
	 * 构造
	 *
	 * @param rootDir 网页根目录
	 */
	public RootHandler(String rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		final URI uri = httpExchange.getRequestURI();
		File file = FileUtil.file(rootDir, uri.getPath());
		if (file.exists()) {
			if (file.isDirectory()) {
				//默认读取主页
				file = FileUtil.file(file, "index.html");
			}
			HandlerUtil.sendFile(httpExchange, file);
		}

		// 文件未找到
		HandlerUtil.send404(httpExchange, null);
	}
}
