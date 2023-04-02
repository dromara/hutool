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

import java.nio.channels.SocketChannel;

/**
 * NIO数据处理接口，通过实现此接口，可以从{@link SocketChannel}中读写数据
 *
 */
@FunctionalInterface
public interface ChannelHandler {

	/**
	 * 处理NIO数据
	 *
	 * @param socketChannel {@link SocketChannel}
	 * @throws Exception 可能的处理异常
	 */
	void handle(SocketChannel socketChannel) throws Exception;
}
