package cn.hutool.http.client;

import cn.hutool.http.client.engine.jdk.HttpUrlConnectionUtil;
import org.junit.Test;

public class HttpUrlConnectionUtilTest {
	@Test
	public void allowPatchTest() {
		HttpUrlConnectionUtil.allowPatch();
	}
}
