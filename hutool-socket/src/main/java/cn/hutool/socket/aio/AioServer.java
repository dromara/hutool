package cn.hutool.socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 基于AIO的Socket服务端实现
 * 
 * @author looly
 *
 */
public class AioServer {
	private static final Log log = LogFactory.get();

	private AsynchronousChannelGroup group;
	private AsynchronousServerSocketChannel channel;
	private AcceptHandler acceptHandler;
	private IoAction<ByteBuffer> ioAction;

	/**
	 * 构造
	 * 
	 * @param port 端口
	 */
	public AioServer(int port) {
		init(new InetSocketAddress(port));
	}

	/**
	 * 初始化
	 * 
	 * @param address 地址和端口
	 * @return this
	 */
	public AioServer init(InetSocketAddress address) {

		// TODO 需要自定义线程池大小
		try {
			this.group = AsynchronousChannelGroup.withFixedThreadPool(//
					2, //默认线程池大小
					ThreadFactoryBuilder.create().setNamePrefix("Huool-socket-").build()//
			);
			this.channel = AsynchronousServerSocketChannel.open(group).bind(address);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		this.acceptHandler = new AcceptHandler();

		return this;
	}

	/**
	 * 开始监听
	 * 
	 * @param sync 是否阻塞
	 */
	public void start(boolean sync) {
		try {
			doStart(sync);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 设置 Socket 的 Option 选项<br>
	 * 选项见：{@link java.net.StandardSocketOptions}
	 *
	 * @param <T> 选项泛型
	 * @param name {@link SocketOption} 枚举
	 * @param value SocketOption参数
	 * @throws IOException IO异常
	 */
	public <T> AioServer setOption(SocketOption<T> name, T value) throws IOException {
		this.channel.setOption(name, value);
		return this;
	}

	/**
	 * 获取IO处理器
	 * 
	 * @return {@link IoAction}
	 */
	public IoAction<ByteBuffer> getIoAction() {
		return this.ioAction;
	}

	/**
	 * 设置IO处理器，单例存在
	 * 
	 * @param ioAction {@link IoAction}
	 * @return this;
	 */
	public AioServer setIoAction(IoAction<ByteBuffer> ioAction) {
		this.ioAction = ioAction;
		return this;
	}

	/**
	 * 获取{@link AsynchronousServerSocketChannel}
	 * 
	 * @return {@link AsynchronousServerSocketChannel}
	 */
	public AsynchronousServerSocketChannel getChannel() {
		return this.channel;
	}

	/**
	 * 处理接入的客户端
	 * 
	 * @return this
	 */
	public AioServer accept() {
		this.channel.accept(this, this.acceptHandler);
		return this;
	}

	/**
	 * 服务是否开启状态
	 * 
	 * @return 服务是否开启状态
	 */
	public boolean isOpen() {
		return (null == this.channel) ? false : this.channel.isOpen();
	}

	/**
	 * 关闭服务
	 */
	public void close() {
		IoUtil.close(this.channel);

		if (null != this.group && false == this.group.isShutdown()) {
			try {
				this.group.shutdownNow();
			} catch (IOException e) {
				// ignore
			}
		}

		// 结束阻塞
		synchronized (this) {
			this.notify();
		}
	}

	// ------------------------------------------------------------------------------------- Private method start
	/**
	 * 开始监听
	 * 
	 * @param sync 是否阻塞
	 * @throws IOException IO异常
	 */
	private void doStart(boolean sync) throws IOException {
		log.debug("Aio Server started, waiting for accept.");

		// 接收客户端连接
		accept();

		if (sync) {
			// 阻塞当前线程，保证在main方法中执行不被退出
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}
	// ------------------------------------------------------------------------------------- Private method end
}
