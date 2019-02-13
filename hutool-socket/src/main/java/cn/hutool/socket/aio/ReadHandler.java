package cn.hutool.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.log.StaticLog;

/**
 * 读取完成回调
 * 
 * @author looly
 *
 */
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

	AsynchronousSocketChannel socketChannel;
	
	public ReadHandler(AsynchronousSocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void completed(Integer result, ByteBuffer buffer) {
		if(result < 0) {
			// 客户端关闭
			return;
		}
		
		buffer.flip();//读模式
		
		
//		StaticLog.debug("客户端消息：[{}] {}", result, BufferUtil.readUtf8Str(buffer));
		Console.print(BufferUtil.readUtf8Str(buffer));
		
		// 继续读取
		if(this.socketChannel.isOpen()) {
			buffer.clear();
			socketChannel.read(buffer, buffer, this);
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		StaticLog.error(exc);
	}

}
