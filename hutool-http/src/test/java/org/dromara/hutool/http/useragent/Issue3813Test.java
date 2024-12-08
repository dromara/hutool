package org.dromara.hutool.http.useragent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * https://android-developers.googleblog.com/2024/12/user-agent-reduction-on-android-webview.html
 */
public class Issue3813Test {
	@Test
	void parseTest() {
		final String ua = "Mozilla/5.0 (Linux; Android 10; K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/125.000 Mobile Safari/537.36";
		final UserAgent parse = UserAgentUtil.parse(ua);
		Assertions.assertEquals("UserAgent{mobile=true, browser=Chrome, version='125.000', platform=Android, os=Android, osVersion='10', engine=Webkit, engineVersion='537.36'}", parse.toString());
	}
}
