package cn.hutool.socket.nio;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 基于NIO的Socket服务端实现
 * 
 * @author looly
 *
 */
public abstract class NioServer implements Closeable {

	private Selector selector;
	private ServerSocketChannel serverSocketChannel;

	/**
	 * 构造
	 * 
	 * @param port 端口
	 */
	public NioServer(int port) {
		init(new InetSocketAddress(port));
	}

	/**
	 * 初始化
	 * 
	 * @param address 地址和端口
	 * @return this
	 */
	public NioServer init(InetSocketAddress address) {
		try {
			// 打开服务器套接字通道
			this.serverSocketChannel = ServerSocketChannel.open();
			// 设置为非阻塞状态
			serverSocketChannel.configureBlocking(false);
			// 获取通道相关联的套接字
			final ServerSocket serverSocket = serverSocketChannel.socket();
			// 绑定端口号
			serverSocket.bind(address);

			// 打开一个选择器
			selector = Selector.open();
			// 服务器套接字注册到Selector中 并指定Selector监控连接事件
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		return this;
	}

	/**
	 * 开始监听
	 */
	public void listen() {
		try {
			doListen();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 开始监听
	 * 
	 * @throws IOException IO异常
	 */
	private void doListen() throws IOException {
		while (0 != this.selector.select()) {
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
		// 有客户端接入此服务端
		if (key.isAcceptable()) {
			// 获取通道 转化为要处理的类型
			final ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel socketChannel;
			try {
				// 获取连接到此服务器的客户端通道
				socketChannel = server.accept();
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}

			// SocketChannel通道的可读事件注册到Selector中
			registerChannel(selector, socketChannel, Operation.READ);
		}

		// 读事件就绪
		if (key.isReadable()) {
			final SocketChannel socketChannel = (SocketChannel) key.channel();
			read(socketChannel);

			// SocketChannel通道的可写事件注册到Selector中
			registerChannel(selector, socketChannel, Operation.WRITE);
		}

		// 写事件就绪
		if (key.isWritable()) {
			final SocketChannel socketChannel = (SocketChannel) key.channel();
			write(socketChannel);
			// SocketChannel通道的可读事件注册到Selector中
			registerChannel(selector, socketChannel, Operation.READ);
		}
	}

	@Override
	public void close() {
		IoUtil.close(this.selector);
		IoUtil.close(this.serverSocketChannel);
	}

	/**
	 * 处理读事件<br>
	 * 当收到读取准备就绪的信号后，回调此方法，用户可读取从客户端传世来的消息
	 * 
	 * @param socketChannel SocketChannel
	 */
	protected abstract void read(SocketChannel socketChannel);

	/**
	 * 实现写逻辑<br>
	 * 当收到写出准备就绪的信号后，回调此方法，用户可向客户端发送消息
	 * 
	 * @param socketChannel SocketChannel
	 */
	protected abstract void write(SocketChannel socketChannel);

	/**
	 * 注册通道到指定Selector上
	 * 
	 * @param selector Selector
	 * @param channel 通道
	 * @param ops 注册的通道监听类型
	 */
	private void registerChannel(Selector selector, SelectableChannel channel, Operation ops) {
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
