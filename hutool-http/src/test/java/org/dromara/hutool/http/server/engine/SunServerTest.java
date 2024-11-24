package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.server.ServerConfig;

public class SunServerTest {
	public static void main(String[] args) {
		final ServerEngine engine = ServerEngineFactory.createEngine("SunHttpServer");
		engine.init(ServerConfig.of());
		engine.setHandler((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Sun Server response test");
		});
		engine.start();
	}
}
