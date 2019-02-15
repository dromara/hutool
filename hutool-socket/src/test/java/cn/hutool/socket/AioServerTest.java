package cn.hutool.socket;

import java.nio.ByteBuffer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.aio.AioServer;
import cn.hutool.socket.aio.AioSession;
import cn.hutool.socket.aio.IoAction;

public class AioServerTest {
	
	public static void main(String[] args) {
		
		AioServer aioServer = new AioServer(8899);
		aioServer.setIoAction(new IoAction<ByteBuffer>() {
			
			@Override
			public void doAction(AioSession session, ByteBuffer data) {
				Console.log(data);
				
				if(false == data.hasRemaining()) {
					StringBuilder response = StrUtil.builder()//
							.append("HTTP/1.1 200 OK\r\n")//
							.append("Date: ").append(DateUtil.formatHttpDate(DateUtil.date())).append("\r\n")//
							.append("Content-Type: text/html; charset=UTF-8\r\n")//
							.append("\r\n")
							.append("Hello Hutool socket");//
					session.write(ByteBuffer.wrap(response.toString().getBytes(CharsetUtil.CHARSET_UTF_8)));
					session.closeOut();
				}
			}
		}).start(true);
	}
}
