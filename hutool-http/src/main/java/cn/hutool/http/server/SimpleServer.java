package cn.hutool.http.server;

import cn.hutool.core.io.IORuntimeException;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * 简易Http服务器，基于{@link HttpServer}
 *
 * @author looly
 * @since 5.2.5
 */
public class SimpleServer {

	HttpServer server;

	/**
	 * 构造
	 *
	 * @param port 监听端口
	 */
	public SimpleServer(int port) {
		this(new InetSocketAddress(port));
	}

	/**
	 * 构造
	 *
	 * @param hostname 监听地址
	 * @param port     监听端口
	 */
	public SimpleServer(String hostname, int port) {
		this(new InetSocketAddress(hostname, port));
	}

	/**
	 * 构造
	 *
	 * @param address 监听地址
	 */
	public SimpleServer(InetSocketAddress address) {
		try {
			this.server = HttpServer.create(address, 0);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	public SimpleServer addHandler(String path, HttpHandler handler) {
		this.server.createContext(path, handler);
		return this;
	}

	public SimpleServer setExecutor(Executor executor) {
		this.server.setExecutor(executor);
		return this;
	}

	public SimpleServer start() {
		this.server.start();
		return this;
	}
}
