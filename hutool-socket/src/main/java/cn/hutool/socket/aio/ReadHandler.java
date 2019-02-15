package cn.hutool.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import cn.hutool.log.StaticLog;

/**
 * 读取完成回调
 * 
 * @author looly
 *
 */
public class ReadHandler implements CompletionHandler<Integer, AioSession> {

	@Override
	public void completed(Integer result, AioSession session) {
		if (result < 0) {
			// 客户端关闭
			return;
		}

		final ByteBuffer readBuffer = session.getReadBuffer();
		readBuffer.flip();// 读模式
		session.getIoAction().doAction(session, readBuffer);

		// 继续读取
		session.read();
	}

	@Override
	public void failed(Throwable exc, AioSession attachment) {
		StaticLog.error(exc);
	}

}
