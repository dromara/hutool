package cn.hutool.socket.aio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 基于AIO的Socket服务端实现
 * 
 * @author looly
 *
 */
public class AioServer implements Closeable {

	AsynchronousServerSocketChannel channel;

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
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		AsynchronousChannelGroup group;
		try {
			group = AsynchronousChannelGroup.withThreadPool(threadPool);
			this.channel = AsynchronousServerSocketChannel.open(group);
			this.channel.bind(address);
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
		this.channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

			@Override
			public void completed(AsynchronousSocketChannel result, Void attachment) {
				channel.accept(attachment, this);
				try {
					Console.log(result.getRemoteAddress().toString());
				} catch (IOException e) {
					throw new IORuntimeException(e);
				}

				final ByteBuffer buffer = ByteBuffer.allocate(2);
				while(true) {
					result.read(buffer, null, new CompletionHandler<Integer, Void>() {
						
						@Override
						public void completed(Integer result, Void attachment) {
							buffer.flip();
							byte[] readBytes = BufferUtil.readBytes(buffer);
							Console.log(StrUtil.str(readBytes, CharsetUtil.CHARSET_UTF_8));
						}
						
						@Override
						public void failed(Throwable exc, Void attachment) {
							exc.printStackTrace();
						}
					});
				}
			}

			@Override
			public void failed(Throwable exc, Void attachment) {
				exc.printStackTrace();
			}
		});
	}

	@Override
	public void close() throws IOException {
		IoUtil.close(this.channel);
	}

	public static void main(String[] args) {
		final AioServer aioServer = new AioServer(8899);
		ThreadUtil.execute(new Runnable() {

			@Override
			public void run() {
				aioServer.listen();
			}
		});
		Console.log("####");
	}
}
