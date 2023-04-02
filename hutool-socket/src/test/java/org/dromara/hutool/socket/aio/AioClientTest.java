package org.dromara.hutool.socket.aio;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class AioClientTest {
	public static void main(final String[] args) {
		final AioClient client = new AioClient(new InetSocketAddress("localhost", 8899), new SimpleIoAction() {

			@Override
			public void doAction(final AioSession session, final ByteBuffer data) {
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
