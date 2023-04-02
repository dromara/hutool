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

package org.dromara.hutool.aio;

/**
 * Socket流处理接口<br>
 * 实现此接口用于处理接收到的消息，发送指定消息
 *
 * @author looly
 *
 * @param <T> 经过解码器解码后的数据类型
 */
public interface IoAction<T> {

	/**
	 * 接收客户端连接（会话建立）事件处理
	 *
	 * @param session 会话
	 */
	void accept(AioSession session);

	/**
	 * 执行数据处理（消息读取）
	 *
	 * @param session Socket Session会话
	 * @param data 解码后的数据
	 */
	void doAction(AioSession session, T data);

	/**
	 * 数据读取失败的回调事件处理（消息读取失败）
	 *
	 * @param exc 异常
	 * @param session Session
	 */
	void failed(Throwable exc, AioSession session);
}
