package cn.hutool.socket.nio;

import java.nio.channels.SocketChannel;

/**
 * NIO数据处理接口，通过实现此接口，可以从{@link SocketChannel}中读写数据
 *
 */
public interface ChannelHandler {

	/**
	 * 处理NIO数据
	 *
	 * @param socketChannel {@link SocketChannel}
	 */
	void handle(SocketChannel socketChannel);
}
