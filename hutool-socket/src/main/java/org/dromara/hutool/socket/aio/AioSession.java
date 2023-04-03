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

package org.dromara.hutool.socket.aio;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.socket.SocketConfig;
import org.dromara.hutool.socket.SocketUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * AIO会话<br>
 * 每个客户端对应一个会话对象
 *
 * @author looly
 *
 */
public class AioSession implements Closeable{

	private static final ReadHandler READ_HANDLER = new ReadHandler();

	private final AsynchronousSocketChannel channel;
	private final IoAction<ByteBuffer> ioAction;
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	/** 读取超时时长，小于等于0表示默认 */
	private final long readTimeout;
	/** 写出超时时长，小于等于0表示默认 */
	private final long writeTimeout;

	/**
	 * 构造
	 *
	 * @param channel {@link AsynchronousSocketChannel}
	 * @param ioAction IO消息处理类
	 * @param config 配置项
	 */
	public AioSession(final AsynchronousSocketChannel channel, final IoAction<ByteBuffer> ioAction, final SocketConfig config) {
		this.channel = channel;
		this.ioAction = ioAction;

		this.readBuffer = ByteBuffer.allocate(config.getReadBufferSize());
		this.writeBuffer = ByteBuffer.allocate(config.getWriteBufferSize());
		this.readTimeout = config.getReadTimeout();
		this.writeTimeout = config.getWriteTimeout();
	}

	/**
	 * 获取{@link AsynchronousSocketChannel}
	 *
	 * @return {@link AsynchronousSocketChannel}
	 */
	public AsynchronousSocketChannel getChannel() {
		return this.channel;
	}

	/**
	 * 获取读取Buffer
	 *
	 * @return 读取Buffer
	 */
	public ByteBuffer getReadBuffer() {
		return this.readBuffer;
	}

	/**
	 * 获取写Buffer
	 *
	 * @return 写Buffer
	 */
	public ByteBuffer getWriteBuffer() {
		return this.writeBuffer;
	}

	/**
	 * 获取消息处理器
	 *
	 * @return {@link IoAction}
	 */
	public IoAction<ByteBuffer> getIoAction() {
		return this.ioAction;
	}

	/**
	 * 获取远程主机（客户端）地址和端口
	 *
	 * @return 远程主机（客户端）地址和端口
	 */
	public SocketAddress getRemoteAddress() {
		return SocketUtil.getRemoteAddress(this.channel);
	}

	/**
	 * 读取数据到Buffer
	 *
	 * @return this
	 */
	public AioSession read() {
		return read(READ_HANDLER);
	}

	/**
	 * 读取数据到Buffer
	 *
	 * @param handler {@link CompletionHandler}
	 * @return this
	 */
	public AioSession read(final CompletionHandler<Integer, AioSession> handler) {
		if (isOpen()) {
			this.readBuffer.clear();
			this.channel.read(this.readBuffer, Math.max(this.readTimeout, 0L), TimeUnit.MILLISECONDS, this, handler);
		}
		return this;
	}

	/**
	 * 写数据到目标端，并关闭输出
	 *
	 * @param data 数据
	 * @return this
	 */
	public AioSession writeAndClose(final ByteBuffer data) {
		write(data);
		return closeOut();
	}

	/**
	 * 写数据到目标端
	 *
	 * @param data 数据
	 * @return {@link Future}
	 */
	public Future<Integer> write(final ByteBuffer data) {
		return this.channel.write(data);
	}

	/**
	 * 写数据到目标端
	 *
	 * @param data 数据
	 * @param handler {@link CompletionHandler}
	 * @return this
	 */
	public AioSession write(final ByteBuffer data, final CompletionHandler<Integer, AioSession> handler) {
		this.channel.write(data, Math.max(this.writeTimeout, 0L), TimeUnit.MILLISECONDS, this, handler);
		return this;
	}

	/**
	 * 会话是否打开状态<br>
	 * 当Socket保持连接时会话始终打开
	 *
	 * @return 会话是否打开状态
	 */
	public boolean isOpen() {
		return (null != this.channel) && this.channel.isOpen();
	}

	/**
	 * 关闭输出
	 *
	 * @return this
	 */
	public AioSession closeIn() {
		if (null != this.channel) {
			try {
				this.channel.shutdownInput();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this;
	}

	/**
	 * 关闭输出
	 *
	 * @return this
	 */
	public AioSession closeOut() {
		if (null != this.channel) {
			try {
				this.channel.shutdownOutput();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this;
	}

	/**
	 * 关闭会话
	 */
	@Override
	public void close() {
		IoUtil.closeQuietly(this.channel);
		this.readBuffer = null;
		this.writeBuffer = null;
	}

	/**
	 * 执行读，用于读取事件结束的回调
	 */
	protected void callbackRead() {
		readBuffer.flip();// 读模式
		ioAction.doAction(this, readBuffer);
	}
}
