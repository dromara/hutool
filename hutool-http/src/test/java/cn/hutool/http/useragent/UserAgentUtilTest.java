package cn.hutool.http.useragent;

import org.junit.Assert;
import org.junit.Test;

public class UserAgentUtilTest {

	@Test
	public void parseDesktopTest() {
		String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1";

		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Chrome", ua.getBrowser().toString());
		Assert.assertEquals("14.0.835.163", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("535.1", ua.getEngineVersion());
		Assert.assertEquals("Windows 7", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseMobileTest() {
		String uaStr = "User-Agent:Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5";

		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Safari", ua.getBrowser().toString());
		Assert.assertEquals("5.0.2", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("533.17.9", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}
}
