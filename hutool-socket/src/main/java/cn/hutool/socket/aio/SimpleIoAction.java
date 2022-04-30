package cn.hutool.socket.aio;

import java.nio.ByteBuffer;

import cn.hutool.log.StaticLog;

/**
 * 简易IO信息处理类<br>
 * 简单实现了accept和failed事件
 *
 * @author looly
 *
 */
public abstract class SimpleIoAction implements IoAction<ByteBuffer> {

	@Override
	public void accept(final AioSession session) {
	}

	@Override
	public void failed(final Throwable exc, final AioSession session) {
		StaticLog.error(exc);
	}
}
