package cn.hutool.socket.nio;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClientTest {

	@SneakyThrows
	public static void main(String[] args) {
		NioClient client = new NioClient("127.0.0.1", 8080) {
			
			@SneakyThrows
			@Override
			protected void read(SocketChannel sc) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				//从channel读数据到缓冲区
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					//Flips this buffer.  The limit is set to the current position and then
					// the position is set to zero，就是表示要从起始位置开始读取数据
					readBuffer.flip();
					//returns the number of elements between the current position and the  limit.
					// 要读取的字节长度
					byte[] bytes = new byte[readBuffer.remaining()];
					//将缓冲区的数据读到bytes数组
					readBuffer.get(bytes);
					String body = StrUtil.utf8Str(bytes);
					Console.log("the read client receive message: " + body);
				} else if (readBytes < 0) {
					sc.close();
				}
			}
		};

		client.listen();
		ByteBuffer buffer = ByteBuffer.wrap("client 发生到 server".getBytes());
		client.write(buffer);
		buffer = ByteBuffer.wrap("client 再次发生到 server".getBytes());
		client.write(buffer);

		// 在控制台向服务器端发送数据
		Console.log("请在下方畅所欲言");
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String request = scanner.nextLine();
			if (request != null && request.trim().length() > 0) {
				client.write(
						CharsetUtil.CHARSET_UTF_8
								.encode("测试client" + ": " + request));
			}
		}
	}
}