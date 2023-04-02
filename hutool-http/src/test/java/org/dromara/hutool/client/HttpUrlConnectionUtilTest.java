package org.dromara.hutool.client;

import org.dromara.hutool.client.engine.jdk.HttpUrlConnectionUtil;
import org.junit.jupiter.api.Test;

public class HttpUrlConnectionUtilTest {
	@Test
	public void allowPatchTest() {
		HttpUrlConnectionUtil.allowPatch();
	}
}
