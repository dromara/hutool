package cn.hutool.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import cn.hutool.log.StaticLog;

/**
 * 接入完成回调
 * 
 * @author looly
 *
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

	@Override
	public void completed(AsynchronousSocketChannel socketChannel, AioServer aioServer) {
		// 继续等待接入
		aioServer.accept();

		// 处理请求接入
		final IoAction<ByteBuffer> ioAction = aioServer.getIoAction();
		ioAction.accept(socketChannel);

		// 创建Session会话
		final AioSession session = new AioSession(socketChannel, ioAction);

		// 处理读
		session.read();
	}

	@Override
	public void failed(Throwable exc, AioServer aioServer) {
		StaticLog.error(exc);
	}

}
