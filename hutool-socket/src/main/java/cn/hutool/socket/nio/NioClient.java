package cn.hutool.socket.nio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

/**
 * NIO客户端
 *
 * @author looly
 * @since 4.4.5
 */
public class NioClient implements Closeable {

	private SocketChannel channel;

	/**
	 * 构造
	 *
	 * @param host 服务器地址
	 * @param port 端口
	 */
	public NioClient(String host, int port) {
		init(new InetSocketAddress(host, port));
	}

	/**
	 * 构造
	 *
	 * @param address 服务器地址
	 */
	public NioClient(InetSocketAddress address) {
		init(address);
	}

	/**
	 * 初始化
	 *
	 * @param address 地址和端口
	 * @return this
	 */
	public NioClient init(InetSocketAddress address) {
		try {
			this.channel = SocketChannel.open(address);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 处理读事件<br>
	 * 当收到读取准备就绪的信号后，回调此方法，用户可读取从客户端传世来的消息
	 *
	 * @param buffer 服务端数据存储缓存
	 * @return this
	 */
	public NioClient read(ByteBuffer buffer) {
		try {
			this.channel.read(buffer);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 实现写逻辑<br>
	 * 当收到写出准备就绪的信号后，回调此方法，用户可向客户端发送消息
	 *
	 * @param datas 发送的数据
	 * @return this
	 */
	public NioClient write(ByteBuffer... datas) {
		try {
			this.channel.write(datas);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public void close() {
		IoUtil.close(this.channel);
	}
}
