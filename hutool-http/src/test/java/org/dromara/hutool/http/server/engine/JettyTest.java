package org.dromara.hutool.http.server.engine;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.net.ssl.SSLContextUtil;
import org.dromara.hutool.crypto.KeyStoreUtil;
import org.dromara.hutool.http.server.ServerConfig;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

public class JettyTest {
	public static void main(final String[] args) {
		final char[] pwd = "123456".toCharArray();
		final KeyStore keyStore = KeyStoreUtil.readJKSKeyStore(FileUtil.file("d:/test/keystore.jks"), pwd);
		// 初始化SSLContext
		final SSLContext sslContext = SSLContextUtil.createSSLContext(keyStore, pwd);

		final ServerEngine engine = ServerEngineFactory.createEngine("jetty");
		engine.init(ServerConfig.of().setSslContext(sslContext));
		engine.setHandler((request, response) -> {
			Console.log(request.getPath());
			response.write("Hutool Jetty response test");
		});
		engine.start();
	}
}
