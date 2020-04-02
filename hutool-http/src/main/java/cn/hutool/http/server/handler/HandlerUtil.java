package cn.hutool.http.server.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 请求处理器相关工具类
 *
 * @since 5.2.6
 */
public class HandlerUtil {

	/**
	 * 返回404页面
	 *
	 * @param httpExchange HttpExchange
	 * @param content      要发送的404页面内容
	 * @throws IOException IO异常
	 */
	public static void send404(HttpExchange httpExchange, String content) throws IOException {
		if (null == httpExchange) {
			return;
		}

		if (null == content) {
			content = "404 Not Found !";
		}

		httpExchange.sendResponseHeaders(HttpStatus.HTTP_NOT_FOUND, 0);
		try (OutputStream out = httpExchange.getResponseBody()) {
			IoUtil.writeUtf8(out, false, content);
		}
	}

	/**
	 * 返回文件
	 *
	 * @param httpExchange HttpExchange
	 * @param file         要发送的文件
	 * @throws IOException IO异常
	 */
	public static void sendFile(HttpExchange httpExchange, File file) throws IOException {
		if (ArrayUtil.hasNull(httpExchange, file)) {
			return;
		}
		addHeader(httpExchange,
				Header.CONTENT_TYPE.toString(),
				HttpUtil.getMimeType(file.getName(), "text/html"));
		httpExchange.sendResponseHeaders(HttpStatus.HTTP_OK, 0);
		try (OutputStream out = httpExchange.getResponseBody()) {
			FileUtil.writeToStream(file, out);
		}
	}

	/**
	 * 增加响应头信息
	 *
	 * @param httpExchange HttpExchange
	 * @param header       头名
	 * @param value        头值
	 */
	public static void addHeader(HttpExchange httpExchange, String header, String value) {
		if (ArrayUtil.hasEmpty(httpExchange, header)) {
			return;
		}
		httpExchange.getResponseHeaders().add(header, value);
	}

	/**
	 * 获取响应头信息
	 *
	 * @param httpExchange HttpExchange
	 * @param header       头名
	 * @return 值，不存在返回null
	 */
	public static String getHeader(HttpExchange httpExchange, String header) {
		if (ArrayUtil.hasEmpty(httpExchange, header)) {
			return null;
		}
		return httpExchange.getRequestHeaders().getFirst(header);
	}
}
