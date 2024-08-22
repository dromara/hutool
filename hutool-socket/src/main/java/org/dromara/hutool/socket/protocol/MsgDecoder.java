/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.socket.protocol;

import java.nio.ByteBuffer;

import org.dromara.hutool.socket.aio.AioSession;

/**
 * 消息解码器
 *
 * @author looly
 *
 * @param <T> 解码后的目标类型
 */
public interface MsgDecoder<T> {
	/**
	 * 对于从Socket流中获取到的数据采用当前MsgDecoder的实现类协议进行解析。
	 *
	 * @param session 本次需要解码的session
	 * @param readBuffer 待处理的读buffer
	 * @return 本次解码成功后封装的业务消息对象, 返回null则表示解码未完成
	 */
	T decode(AioSession session, ByteBuffer readBuffer);
}
