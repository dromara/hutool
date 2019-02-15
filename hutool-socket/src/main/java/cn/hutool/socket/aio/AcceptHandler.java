package cn.hutool.socket.aio;

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
		
		//创建Session会话
		final AioSession session = new AioSession(socketChannel, aioServer.getIoAction());

		// 处理读
		session.read();
	}

	@Override
	public void failed(Throwable exc, AioServer attachment) {
		StaticLog.error(exc);
	}

}
