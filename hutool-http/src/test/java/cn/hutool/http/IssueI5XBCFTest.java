package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.client.engine.jdk.HttpResponse;
import cn.hutool.http.meta.Header;
import org.brotli.dec.BrotliInputStream;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI5XBCFTest {

	@Test
	@Ignore
	public void getTest() {
		GlobalCompressStreamRegister.INSTANCE.register("br", BrotliInputStream.class);

		@SuppressWarnings("resource") final HttpResponse s = HttpUtil.createGet("https://static-exp1.licdn.com/sc/h/br/1cp0oqz322bdprj3qd4pojqix")
				.header(Header.ACCEPT_ENCODING, "br")
				.execute();
		Console.log(s.body());
	}
}
