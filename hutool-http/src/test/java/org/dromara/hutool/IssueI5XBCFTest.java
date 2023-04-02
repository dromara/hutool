package org.dromara.hutool;

import org.dromara.hutool.client.Request;
import org.dromara.hutool.client.Response;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.meta.Header;
import org.brotli.dec.BrotliInputStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI5XBCFTest {

	@Test
	@Disabled
	public void getTest() {
		GlobalCompressStreamRegister.INSTANCE.register("br", BrotliInputStream.class);

		@SuppressWarnings("resource")
		final Response s = Request.of("https://static-exp1.licdn.com/sc/h/br/1cp0oqz322bdprj3qd4pojqix")
				.header(Header.ACCEPT_ENCODING, "br")
				.send();
		Console.log(s.body());
	}
}
