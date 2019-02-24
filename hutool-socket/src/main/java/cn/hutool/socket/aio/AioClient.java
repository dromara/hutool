package cn.hutool.socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.socket.SocketRuntimeException;

/**
 * Aio Socket客户端
 * 
 * @author looly
 * @since 4.5.0
 */
public class AioClient {

	private AioSession session;

	/**
	 * 构造
	 * 
	 * @param address 地址
	 * @param ioAction IO处理类
	 */
	public AioClient(InetSocketAddress address, IoAction<ByteBuffer> ioAction) {
		this(createChannel(address), ioAction);
	}

	/**
	 * 构造
	 * 
	 * @param channel {@link AsynchronousSocketChannel}
	 * @param ioAction IO处理类
	 */
	public AioClient(AsynchronousSocketChannel channel, IoAction<ByteBuffer> ioAction) {
		ioAction.accept(channel);
		this.session = new AioSession(channel, ioAction);
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
	 * @return
	 */
	public AioClient read() {
		this.session.read();
		return this;
	}

	/**
	 * 写数据到服务端
	 * 
	 * @return this
	 */
	public AioClient write(ByteBuffer data) {
		this.session.write(data);
		return this;
	}

	// ------------------------------------------------------------------------------------- Private method start
	/**
	 * 初始化
	 * 
	 * @param address 地址和端口
	 * @return this
	 */
	private static AsynchronousSocketChannel createChannel(InetSocketAddress address) {

		AsynchronousSocketChannel channel;
		// TODO 需要自定义线程池大小
		try {
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(//
					2, // 默认线程池大小
					ThreadFactoryBuilder.create().setNamePrefix("Huool-socket-").build()//
			);
			channel = AsynchronousSocketChannel.open(group);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} 
		
		try {
			channel.connect(address).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new SocketRuntimeException(e);
		}
		return channel;
	}
	// ------------------------------------------------------------------------------------- Private method end
}
