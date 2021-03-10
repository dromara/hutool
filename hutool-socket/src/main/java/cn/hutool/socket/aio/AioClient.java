package cn.hutool.socket.aio;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.socket.SocketConfig;
import cn.hutool.socket.SocketRuntimeException;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * Aio Socket客户端
 *
 * @author looly
 * @since 4.5.0
 */
public class AioClient implements Closeable{

	private final AioSession session;

	/**
	 * 构造
	 *
	 * @param address 地址
	 * @param ioAction IO处理类
	 */
	public AioClient(InetSocketAddress address, IoAction<ByteBuffer> ioAction) {
		this(address, ioAction, new SocketConfig());
	}

	/**
	 * 构造
	 *
	 * @param address 地址
	 * @param ioAction IO处理类
	 * @param config 配置项
	 */
	public AioClient(InetSocketAddress address, IoAction<ByteBuffer> ioAction, SocketConfig config) {
		this(createChannel(address, config.getThreadPoolSize()), ioAction, config);
	}

	/**
	 * 构造
	 *
	 * @param channel {@link AsynchronousSocketChannel}
	 * @param ioAction IO处理类
	 * @param config 配置项
	 */
	public AioClient(AsynchronousSocketChannel channel, IoAction<ByteBuffer> ioAction, SocketConfig config) {
		this.session = new AioSession(channel, ioAction, config);
		ioAction.accept(this.session);
	}

	/**
	 * 设置 Socket 的 Option 选项<br>
	 * 选项见：{@link java.net.StandardSocketOptions}
	 *
	 * @param <T> 选项泛型
	 * @param name {@link SocketOption} 枚举
	 * @param value SocketOption参数
	 * @return this
	 * @throws IOException IO异常
	 */
	public <T> AioClient setOption(SocketOption<T> name, T value) throws IOException {
		this.session.getChannel().setOption(name, value);
		return this;
	}

	/**
	 * 获取IO处理器
	 *
	 * @return {@link IoAction}
	 */
	public IoAction<ByteBuffer> getIoAction() {
		return this.session.getIoAction();
	}

	/**
	 * 从服务端读取数据
	 *
	 * @return this
	 */
	public AioClient read() {
		this.session.read();
		return this;
	}

	/**
	 * 写数据到服务端
	 *
	 * @param data 数据
	 * @return this
	 */
	public AioClient write(ByteBuffer data) {
		this.session.write(data);
		return this;
	}

	/**
	 * 关闭客户端
	 */
	@Override
	public void close() {
		this.session.close();
	}

	// ------------------------------------------------------------------------------------- Private method start
	/**
	 * 初始化
	 *
	 * @param address 地址和端口
	 * @param poolSize 线程池大小
	 * @return this
	 */
	private static AsynchronousSocketChannel createChannel(InetSocketAddress address, int poolSize) {

		AsynchronousSocketChannel channel;
		try {
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(//
					poolSize, // 默认线程池大小
					ThreadFactoryBuilder.create().setNamePrefix("Huool-socket-").build()//
			);
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
	// ------------------------------------------------------------------------------------- Private method end
}
