package cn.hutool.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.aio.AioClient;
import cn.hutool.socket.aio.AioSession;
import cn.hutool.socket.aio.SimpleIoAction;

public class AioClientTest {
	public static void main(String[] args) {
		AioClient client = new AioClient(new InetSocketAddress("localhost", 8899), new SimpleIoAction() {
			
			@Override
			public void doAction(AioSession session, ByteBuffer data) {
				Console.log(StrUtil.utf8Str(data));
			}
		});
		
		client.write(ByteBuffer.wrap("Hello".getBytes()));
		client.read();
	}
}
