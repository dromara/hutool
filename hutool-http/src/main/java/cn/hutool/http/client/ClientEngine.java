package cn.hutool.http.client;

import java.io.Closeable;

/**
 * HTTP客户端引擎接口，通过不同实现，完成HTTP请求发送
 *
 * @author looly
 * @since 6.0.0
 */
public interface ClientEngine extends Closeable {

	/**
	 * 发送HTTP请求
	 * @param message HTTP请求消息
	 * @return 响应内容
	 */
	Response send(Request message);

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return 对应HTTP客户端实现的引擎对象
	 * @since 6.0.0
	 */
	Object getRawEngine();
}
