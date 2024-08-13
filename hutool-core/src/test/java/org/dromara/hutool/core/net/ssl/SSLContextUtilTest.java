package org.dromara.hutool.core.net.ssl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;

public class SSLContextUtilTest {
	@Test
	void createTrustAnySSLContextTest() {
		final SSLContext trustAnySSLContext = SSLContextUtil.createTrustAnySSLContext();
		Assertions.assertNotNull(trustAnySSLContext);
	}
}
