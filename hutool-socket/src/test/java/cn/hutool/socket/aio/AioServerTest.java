package cn.hutool.socket.aio;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.StrUtil;
import cn.hutool.log.StaticLog;

import java.nio.ByteBuffer;

public class AioServerTest {

	public static void main(final String[] args) {

		@SuppressWarnings("resource") final AioServer aioServer = new AioServer(8899);
		aioServer.setIoAction(new SimpleIoAction() {

			@Override
			public void accept(final AioSession session) {
				StaticLog.debug("【客户端】：{} 连接。", session.getRemoteAddress());
				session.write(BufferUtil.createUtf8("=== Welcome to Hutool socket server. ==="));
			}

			@Override
			public void doAction(final AioSession session, final ByteBuffer data) {
				Console.log(data);

				if(false == data.hasRemaining()) {
					final StringBuilder response = StrUtil.builder()//
							.append("HTTP/1.1 200 OK\r\n")//
							.append("Date: ").append(DateUtil.formatHttpDate(DateUtil.date())).append("\r\n")//
							.append("Content-Type: text/html; charset=UTF-8\r\n")//
							.append("\r\n")
							.append("Hello Hutool socket");//
					session.writeAndClose(BufferUtil.createUtf8(response));
				}else {
					session.read();
				}
			}
		}).start(true);
	}
}
