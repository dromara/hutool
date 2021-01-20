package cn.hutool.socket;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;

import cn.hutool.core.io.IORuntimeException;

/**
 * Socket相关工具类
 * 
 * @author looly
 * @since 4.5.0
 */
public class SocketUtil {
	
	/**
	 * 获取远程端的地址信息，包括host和端口<br>
	 * null表示channel为null或者远程主机未连接
	 * 
	 * @param channel {@link AsynchronousSocketChannel}
	 * @return 远程端的地址信息，包括host和端口，null表示channel为null或者远程主机未连接
	 */
	public static SocketAddress getRemoteAddress(AsynchronousSocketChannel channel) {
		try {
			return (null == channel) ? null : channel.getRemoteAddress();
		} catch (ClosedChannelException e) {
			// Channel未打开或已关闭，返回null表示未连接
			return null;
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 远程主机是否处于连接状态<br>
	 * 通过判断远程地址获取成功与否判断
	 * 
	 * @param channel {@link AsynchronousSocketChannel}
	 * @return 远程主机是否处于连接状态
	 */
	public static boolean isConnected(AsynchronousSocketChannel channel) {
		return null != getRemoteAddress(channel);
	}
}
