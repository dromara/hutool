package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.server.ServerConfig;

public class TomcatTest {
	public static void main(String[] args) {
		final ServerEngine engine = ServerEngineFactory.createEngine("tomcat");
		engine.init(ServerConfig.of());
		engine.setHandler((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Tomcat response test");
		});
		engine.start();
	}
}
