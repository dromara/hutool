package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.engine.jetty.JettyEngine;

public class JettyTest {
	public static void main(String[] args) {
		final JettyEngine engine = new JettyEngine();
		engine.init(ServerConfig.of());
		engine.setHandler((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Jetty response test");
		});
		engine.start();
	}
}
