/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.socket.aio;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.socket.SocketConfig;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * 基于AIO的Socket服务端实现
 *
 * @author looly
 */
public class AioServer implements Closeable {
	private static final Log log = LogFactory.get();
	private static final AcceptHandler ACCEPT_HANDLER = new AcceptHandler();

	private AsynchronousChannelGroup group;
	private AsynchronousServerSocketChannel channel;
	protected IoAction<ByteBuffer> ioAction;
	protected final SocketConfig config;


	/**
	 * 构造
	 *
	 * @param port 端口
	 */
	public AioServer(final int port) {
		this(new InetSocketAddress(port), new SocketConfig());
	}

	/**
	 * 构造
	 *
	 * @param address 地址
	 * @param config  {@link SocketConfig} 配置项
	 */
	public AioServer(final InetSocketAddress address, final SocketConfig config) {
		this.config = config;
		init(address);
	}

	/**
	 * 初始化
	 *
	 * @param address 地址和端口
	 * @return this
	 */
	public AioServer init(final InetSocketAddress address) {
		try {
			this.group = AsynchronousChannelGroup.withFixedThreadPool(//
					config.getThreadPoolSize(), // 默认线程池大小
					ThreadFactoryBuilder.of().setNamePrefix("Hutool-socket-").build()//
			);
			this.channel = AsynchronousServerSocketChannel.open(group).bind(address);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 开始监听
	 *
	 * @param sync 是否阻塞
	 */
	public void start(final boolean sync) {
		doStart(sync);
	}

	/**
	 * 设置 Socket 的 Option 选项<br>
	 * 选项见：{@link java.net.StandardSocketOptions}
	 *
	 * @param <T>   选项泛型
	 * @param name  {@link SocketOption} 枚举
	 * @param value SocketOption参数
	 * @return this
	 * @throws IOException IO异常
	 */
	public <T> AioServer setOption(final SocketOption<T> name, final T value) throws IOException {
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
	public AioServer setIoAction(final IoAction<ByteBuffer> ioAction) {
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
		this.channel.accept(this, ACCEPT_HANDLER);
		return this;
	}

	/**
	 * 服务是否开启状态
	 *
	 * @return 服务是否开启状态
	 */
	public boolean isOpen() {
		return (null != this.channel) && this.channel.isOpen();
	}

	/**
	 * 关闭服务
	 */
	@Override
	public void close() {
		IoUtil.closeQuietly(this.channel);

		if (null != this.group && false == this.group.isShutdown()) {
			try {
				this.group.shutdownNow();
			} catch (final IOException e) {
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
	 */
	private void doStart(final boolean sync) {
		log.debug("Aio Server started, waiting for accept.");

		// 接收客户端连接
		accept();

		if (sync) {
			ThreadUtil.sync(this);
		}
	}
	// ------------------------------------------------------------------------------------- Private method end
}
