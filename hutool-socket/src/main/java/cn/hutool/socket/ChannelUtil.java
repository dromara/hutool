package cn.hutool.socket;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Channel相关封装
 *
 * @author looly
 * @since 5.8.2
 */
public class ChannelUtil {

	/**
	 * 创建{@link AsynchronousChannelGroup}
	 *
	 * @param poolSize 线程池大小
	 * @return {@link AsynchronousChannelGroup}
	 */
	public static AsynchronousChannelGroup createFixedGroup(int poolSize) {

		try {
			return AsynchronousChannelGroup.withFixedThreadPool(//
					poolSize, // 默认线程池大小
					ThreadFactoryBuilder.create().setNamePrefix("Huool-socket-").build()//
			);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 连接到指定地址
	 *
	 * @param group   {@link AsynchronousChannelGroup}
	 * @param address 地址信息，包括地址和端口
	 * @return {@link AsynchronousSocketChannel}
	 */
	public static AsynchronousSocketChannel connect(AsynchronousChannelGroup group, InetSocketAddress address) {
		AsynchronousSocketChannel channel;
		try {
			channel = AsynchronousSocketChannel.open(group);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		try {
			channel.connect(address).get();
		} catch (InterruptedException | ExecutionException e) {
			IoUtil.close(channel);
			throw new SocketRuntimeException(e);
		}
		return channel;
	}
}
