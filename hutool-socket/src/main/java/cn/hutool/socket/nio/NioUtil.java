package cn.hutool.socket.nio;

import cn.hutool.core.io.IORuntimeException;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;

/**
 * NIO工具类
 *
 * @since 5.4.0
 */
public class NioUtil {

	/**
	 * 注册通道的指定操作到指定Selector上
	 *
	 * @param selector Selector
	 * @param channel 通道
	 * @param ops 注册的通道监听（操作）类型
	 */
	public static void registerChannel(Selector selector, SelectableChannel channel, Operation ops) {
		if (channel == null) {
			return;
		}

		try {
			channel.configureBlocking(false);
			// 注册通道
			//noinspection MagicConstant
			channel.register(selector, ops.getValue());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
