package cn.hutool.http;

import org.junit.jupiter.api.Test;

public class HttpGlobalConfigTest {
	@Test
	public void allowPatchTest() {
		HttpGlobalConfig.allowPatch();
	}
}
