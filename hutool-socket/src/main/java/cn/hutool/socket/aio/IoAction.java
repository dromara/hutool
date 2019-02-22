package cn.hutool.socket.aio;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Socket流处理接口<br>
 * 实现此接口用于处理接收到的消息，发送指定消息
 * 
 * @author looly
 *
 * @param <T> 经过解码器解码后的数据类型
 */
public interface IoAction<T> {

	/**
	 * 接收客户端连接事件处理
	 * 
	 * @param socketChannel 连接Socket对象
	 */
	void accept(AsynchronousSocketChannel socketChannel);

	/**
	 * 执行数据处理
	 * 
	 * @param session Socket Session会话
	 * @param data 解码后的数据
	 */
	void doAction(AioSession session, T data);

	/**
	 * 数据读取失败的回调事件处理
	 * 
	 * @param exc 异常
	 * @param session Session
	 */
	void failed(Throwable exc, AioSession session);
}
