package org.dromara.hutool.aio;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.text.StrUtil;

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
