package org.dromara.hutool.aio;

import org.dromara.hutool.date.DateUtil;
import org.dromara.hutool.io.BufferUtil;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.StaticLog;

import java.nio.ByteBuffer;

public class AioServerTest {

	public static void main(final String[] args) {

		@SuppressWarnings("resource") final AioServer aioServer = new AioServer(8899);
		aioServer.setIoAction(new SimpleIoAction() {

			@Override
			public void accept(final AioSession session) {
				StaticLog.debug("【客户端】：{} 连接。", session.getRemoteAddress());
				session.write(BufferUtil.ofUtf8("=== Welcome to Hutool socket server. ==="));
			}

			@Override
			public void doAction(final AioSession session, final ByteBuffer data) {
				Console.log(data);

				if(false == data.hasRemaining()) {
					final StringBuilder response = StrUtil.builder()//
							.append("HTTP/1.1 200 OK\r\n")//
							.append("Date: ").append(DateUtil.formatHttpDate(DateUtil.now())).append("\r\n")//
							.append("Content-Type: text/html; charset=UTF-8\r\n")//
							.append("\r\n")
							.append("Hello Hutool socket");//
					session.writeAndClose(BufferUtil.ofUtf8(response));
				}else {
					session.read();
				}
			}
		}).start(true);
	}
}
