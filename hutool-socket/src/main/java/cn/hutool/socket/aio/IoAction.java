package cn.hutool.socket.aio;

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
	 * 执行数据处理
	 * 
	 * @param session Socket Session会话
	 * @param data 解码后的数据
	 */
	public void doAction(AioSession session, T data);
}
