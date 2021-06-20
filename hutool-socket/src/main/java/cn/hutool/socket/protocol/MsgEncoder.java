package cn.hutool.socket.protocol;

import java.nio.ByteBuffer;

import cn.hutool.socket.aio.AioSession;

/**
 * 消息编码器
 * 
 * @author looly
 *
 * @param <T> 编码前后的数据类型
 */
public interface MsgEncoder<T> {
	/**
	 * 编码数据用于写出
	 *
	 * @param session 本次需要解码的session
	 * @param writeBuffer 待处理的读buffer
	 * @param data 写出的数据
	 */
	void encode(AioSession session, ByteBuffer writeBuffer, T data);
}
