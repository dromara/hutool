package cn.hutool.socket.aio;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;

public class AioClientTest {
	public static void main(String[] args) throws IOException {
		final AsynchronousChannelGroup GROUP = AsynchronousChannelGroup.withFixedThreadPool(//
				RuntimeUtil.getProcessorCount(), // 默认线程池大小
				ThreadFactoryBuilder.create().setNamePrefix("Huool-socket-").build()//
		);

		AioClient client = new AioClient(new InetSocketAddress("localhost", 8899), new SimpleIoAction() {

			@Override
			public void doAction(AioSession session, ByteBuffer data) {
				if(data.hasRemaining()) {
					Console.log(StrUtil.utf8Str(data));
					session.read();
				}
				Console.log("OK");
			}
		});

		client.write(ByteBuffer.wrap("Hello".getBytes()));
		client.read();

		client.close();
	}
}
