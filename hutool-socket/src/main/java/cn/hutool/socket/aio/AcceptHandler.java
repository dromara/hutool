package cn.hutool.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import cn.hutool.log.StaticLog;
import cn.hutool.socket.SocketUtil;

/**
 * 接入完成回调
 * 
 * @author looly
 *
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

	@Override
	public void completed(AsynchronousSocketChannel socketChannel, AioServer aioServer) {
		//继续等待接入
		aioServer.accept();

		// 处理接入后的相关动作
		StaticLog.debug("客户端 {} 接入。", SocketUtil.getRemoteAddress(socketChannel));

		// 处理读
		final ByteBuffer readBuffer = ByteBuffer.allocate(5);
		socketChannel.read(readBuffer, readBuffer, new ReadHandler(socketChannel));
	}

	@Override
	public void failed(Throwable exc, AioServer attachment) {
		StaticLog.error(exc);
	}

}
