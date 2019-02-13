package cn.hutool.socket;

import cn.hutool.socket.aio.AioServer;

public class AioServerTest {
	public static void main(String[] args) {
		AioServer aioServer = new AioServer(8899);
		aioServer.start(true);
	}
}
