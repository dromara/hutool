package cn.hutool.socket.nio;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.log.StaticLog;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 接入完成回调，单例使用
 * 
 * @author looly
 */
public class AcceptHandler implements CompletionHandler<ServerSocketChannel, NioServer> {

	@Override
	public void completed(ServerSocketChannel serverSocketChannel, NioServer nioServer) {
		SocketChannel socketChannel;
		try {
			// 获取连接到此服务器的客户端通道
			socketChannel = serverSocketChannel.accept();
			StaticLog.debug("Client [{}] accepted.", socketChannel.getRemoteAddress());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		// SocketChannel通道的可读事件注册到Selector中
		NioUtil.registerChannel(nioServer.getSelector(), socketChannel, Operation.READ);
	}

	@Override
	public void failed(Throwable exc, NioServer nioServer) {
		StaticLog.error(exc);
	}

}
