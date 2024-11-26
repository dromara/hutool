package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.server.ServerConfig;

public class JettyTest {
	public static void main(final String[] args) {
		final ServerEngine engine = ServerEngineFactory.createEngine("jetty");
		engine.init(ServerConfig.of());
		engine.setHandler((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Jetty response test");
		});
		engine.start();
	}
}