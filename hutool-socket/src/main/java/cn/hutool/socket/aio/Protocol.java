/*
 * Copyright (c) 2017, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: Protocol.java
 * Date: 2017-11-25
 * Author: sandao
 */

package cn.hutool.socket.aio;

import java.nio.ByteBuffer;

/**
 * 协议接口<br>
 * 通过实现此接口完成消息的编码和解码
 * 
 * <p>
 * 所有Socket使用相同协议对象，类成员变量和对象成员变量易造成并发读写问题。
 * </p>
 *
 * @author Looly
 */
public interface Protocol<T> {
	/**
	 * 编码数据用于写出
	 *
	 * @param session 本次需要解码的session
	 * @param writeBuffer 待处理的读buffer
	 * @param data 写出的数据
	 * @return 本次解码成功后封装的业务消息对象, 返回null则表示解码未完成
	 */
	ByteBuffer encode(AioSession session, ByteBuffer writeBuffer, T data);

	/**
	 * 对于从Socket流中获取到的数据采用当前Protocol的实现类协议进行解析。
	 *
	 *
	 * @param session 本次需要解码的session
	 * @param readBuffer 待处理的读buffer
	 * @return 本次解码成功后封装的业务消息对象, 返回null则表示解码未完成
	 */
	T decode(AioSession session, ByteBuffer readBuffer);
}
