package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.server.ServerConfig;
import org.dromara.hutool.http.server.engine.jetty.Jetty9Engine;

public class JettyTest {
	public static void main(String[] args) {
		final Jetty9Engine engine = new Jetty9Engine();
		engine.init(ServerConfig.of());
		engine.setHandler((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Jetty response test");
		});
		engine.start();
	}
}
