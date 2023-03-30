package cn.hutool.http.client;

import cn.hutool.http.client.engine.jdk.HttpUrlConnectionUtil;
import org.junit.jupiter.api.Test;

public class HttpUrlConnectionUtilTest {
	@Test
	public void allowPatchTest() {
		HttpUrlConnectionUtil.allowPatch();
	}
}
