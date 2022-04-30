package cn.hutool.socket.aio;

import java.nio.channels.CompletionHandler;

import cn.hutool.socket.SocketRuntimeException;

/**
 * 数据读取完成回调，调用Session中相应方法处理消息，单例使用
 *
 * @author looly
 *
 */
public class ReadHandler implements CompletionHandler<Integer, AioSession> {

	@Override
	public void completed(final Integer result, final AioSession session) {
		session.callbackRead();
	}

	@Override
	public void failed(final Throwable exc, final AioSession session) {
		throw new SocketRuntimeException(exc);
	}

}
