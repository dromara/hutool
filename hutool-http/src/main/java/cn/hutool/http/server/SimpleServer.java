package cn.hutool.http.server;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Console;
import cn.hutool.http.server.action.Action;
import cn.hutool.http.server.action.RootAction;
import cn.hutool.http.server.handler.ActionHandler;
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

	/**
	 * 增加请求处理规则
	 *
	 * @param path    路径
	 * @param handler 处理器
	 * @return this
	 */
	public SimpleServer addHandler(String path, HttpHandler handler) {
		this.server.createContext(path, handler);
		return this;
	}

	/**
	 * 设置根目录，默认的页面从root目录中读取解析返回
	 *
	 * @param root 路径
	 * @return this
	 */
	public SimpleServer setRoot(String root) {
		return addAction("/", new RootAction(root));
	}

	/**
	 * 增加请求处理规则
	 *
	 * @param path   路径
	 * @param action 处理器
	 * @return this
	 */
	public SimpleServer addAction(String path, Action action) {
		return addHandler(path, new ActionHandler(action));
	}

	/**
	 * 设置自定义线程池
	 *
	 * @param executor {@link Executor}
	 * @return this
	 */
	public SimpleServer setExecutor(Executor executor) {
		this.server.setExecutor(executor);
		return this;
	}

	/**
	 * 获得原始HttpServer对象
	 *
	 * @return {@link HttpServer}
	 */
	public HttpServer getRawServer() {
		return this.server;
	}

	/**
	 * 获取服务器地址信息
	 *
	 * @return {@link InetSocketAddress}
	 */
	public InetSocketAddress getAddress() {
		return this.server.getAddress();
	}

	/**
	 * 启动Http服务器，启动后会阻塞当前线程
	 */
	public void start() {
		final InetSocketAddress address = getAddress();
		Console.log("Hutool Simple Http Server listen on 【{}:{}】", address.getHostName(), address.getPort());
		this.server.start();
	}
}
