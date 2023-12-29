/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
