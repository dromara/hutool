package cn.hutool.socket.nio;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.socket.SocketRuntimeException;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO客户端
 *
 * @author looly
 * @since 4.4.5
 */
public class NioClient implements Closeable {

	private Selector selector;
	private SocketChannel channel;
	private ChannelHandler handler;

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
			//创建一个SocketChannel对象，配置成非阻塞模式
			this.channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(address);

			//创建一个选择器，并把SocketChannel交给selector对象
			this.selector = Selector.open();
			channel.register(this.selector, SelectionKey.OP_READ);

			// 等待建立连接
			//noinspection StatementWithEmptyBody
			while (false == channel.finishConnect()){}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 设置NIO数据处理器
	 *
	 * @param handler {@link ChannelHandler}
	 * @return this
	 */
	public NioClient setChannelHandler(ChannelHandler handler){
		this.handler = handler;
		return this;
	}

	/**
	 * 开始监听
	 */
	public void listen() {
		ThreadUtil.execute(() -> {
			try {
				doListen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 开始监听
	 *
	 * @throws IOException IO异常
	 */
	private void doListen() throws IOException {
		while (this.selector.isOpen() && 0 != this.selector.select()) {
			// 返回已选择键的集合
			final Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				handle(keyIter.next());
				keyIter.remove();
			}
		}
	}

	/**
	 * 处理SelectionKey
	 *
	 * @param key SelectionKey
	 */
	private void handle(SelectionKey key) {
		// 读事件就绪
		if (key.isReadable()) {
			final SocketChannel socketChannel = (SocketChannel) key.channel();
			try{
				handler.handle(socketChannel);
			} catch (Exception e){
				throw new SocketRuntimeException(e);
			}
		}
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

	/**
	 * 获取SocketChannel
	 *
	 * @return SocketChannel
	 * @since 5.3.10
	 */
	public SocketChannel getChannel() {
		return this.channel;
	}

	@Override
	public void close() {
		IoUtil.close(this.selector);
		IoUtil.close(this.channel);
	}
}
