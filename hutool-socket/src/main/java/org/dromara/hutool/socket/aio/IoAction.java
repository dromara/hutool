/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.socket.aio;

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
