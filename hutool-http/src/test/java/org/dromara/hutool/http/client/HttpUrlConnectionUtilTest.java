package org.dromara.hutool.http.client;

import org.dromara.hutool.http.client.engine.jdk.HttpUrlConnectionUtil;
import org.junit.jupiter.api.Test;

public class HttpUrlConnectionUtilTest {
	@Test
	public void allowPatchTest() {
		HttpUrlConnectionUtil.allowPatch();
	}
}
