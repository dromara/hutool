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

package org.dromara.hutool.nio;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.Log;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
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
public class NioServer implements Closeable {
	private static final Log log = Log.get();

	private static final AcceptHandler ACCEPT_HANDLER = new AcceptHandler();

	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private ChannelHandler handler;

	/**
	 * 构造
	 *
	 * @param port 端口
	 */
	@SuppressWarnings("resource")
	public NioServer(final int port) {
		init(new InetSocketAddress(port));
	}

	/**
	 * 初始化
	 *
	 * @param address 地址和端口
	 * @return this
	 */
	public NioServer init(final InetSocketAddress address) {
		try {
			// 打开服务器套接字通道
			this.serverSocketChannel = ServerSocketChannel.open();
			// 设置为非阻塞状态
			this.serverSocketChannel.configureBlocking(false);
			// 绑定端口号
			this.serverSocketChannel.bind(address);

			// 打开一个选择器
			this.selector = Selector.open();
			// 服务器套接字注册到Selector中 并指定Selector监控连接事件
			this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (final IOException e) {
			close();
			throw new IORuntimeException(e);
		}

		log.debug("Server listen on: [{}]...", address);

		return this;
	}

	/**
	 * 设置NIO数据处理器
	 *
	 * @param handler {@link ChannelHandler}
	 * @return this
	 */
	public NioServer setChannelHandler(final ChannelHandler handler){
		this.handler = handler;
		return this;
	}

	/**
	 * 获取{@link Selector}
	 *
	 * @return {@link Selector}
	 */
	public Selector getSelector(){
		return this.selector;
	}

	/**
	 * 启动NIO服务端，即开始监听
	 *
	 * @see #listen()
	 */
	public void start(){
		listen();
	}

	/**
	 * 开始监听
	 */
	public void listen() {
		try {
			doListen();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
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
	private void handle(final SelectionKey key) {
		// 有客户端接入此服务端
		if (key.isAcceptable()) {
			ACCEPT_HANDLER.completed((ServerSocketChannel) key.channel(), this);
		}

		// 读事件就绪
		if (key.isReadable()) {
			final SocketChannel socketChannel = (SocketChannel) key.channel();
			try{
				handler.handle(socketChannel);
			} catch (final Exception e){
				IoUtil.closeQuietly(socketChannel);
				log.error(e);
			}
		}
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(this.selector);
		IoUtil.closeQuietly(this.serverSocketChannel);
	}
}
