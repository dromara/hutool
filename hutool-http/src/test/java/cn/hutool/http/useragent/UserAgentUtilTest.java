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
		Assert.assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
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

	@Test
	public void parseMiui10WithChromeTest() {
		String uaStr = "Mozilla/5.0 (Linux; Android 9; MIX 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.80 Mobile Safari/537.36";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Chrome", ua.getBrowser().toString());
		Assert.assertEquals("70.0.3538.80", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseWindows10WithChromeTest() {
		String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Chrome", ua.getBrowser().toString());
		Assert.assertEquals("70.0.3538.102", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseWindows10WithIe11Test() {
		String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSIE11", ua.getBrowser().toString());
		Assert.assertEquals("11.0", ua.getVersion());
		Assert.assertEquals("Trident", ua.getEngine().toString());
		Assert.assertEquals("7.0", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseWindows10WithIeMobileLumia520Test() {
		String uaStr = "Mozilla/5.0 (Mobile; Windows Phone 8.1; Android 4.0; ARM; Trident/7.0; Touch; rv:11.0; IEMobile/11.0; NOKIA; Lumia 520) like iPhone OS 7_0_3 Mac OS X AppleWebKit/537 (KHTML, like Gecko) Mobile Safari/537 ";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("IEMobile", ua.getBrowser().toString());
		Assert.assertEquals("11.0", ua.getVersion());
		Assert.assertEquals("Trident", ua.getEngine().toString());
		Assert.assertEquals("7.0", ua.getEngineVersion());
		Assert.assertEquals("Windows Phone", ua.getOs().toString());
		Assert.assertEquals("Windows Phone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseWindows10WithIe8EmulatorTest() {
		String uaStr = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSIE", ua.getBrowser().toString());
		Assert.assertEquals("8.0", ua.getVersion());
		Assert.assertEquals("Trident", ua.getEngine().toString());
		Assert.assertEquals("4.0", ua.getEngineVersion());
		Assert.assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseWindows10WithEdgeTest() {
		String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSEdge", ua.getBrowser().toString());
		Assert.assertEquals("18.17763", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseEdgeOnLumia950XLTest() {
		String uaStr = "Mozilla/5.0 (Windows Phone 10.0; Android 6.0.1; Microsoft; Lumia 950XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36 Edge/15.14900";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSEdge", ua.getBrowser().toString());
		Assert.assertEquals("15.14900", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows Phone", ua.getOs().toString());
		Assert.assertEquals("Windows Phone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseChromeOnWindowsServer2012R2Test() {
		String uaStr = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Chrome", ua.getBrowser().toString());
		Assert.assertEquals("63.0.3239.132", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 8.1 or Winsows Server 2012R2", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseIE11OnWindowsServer2008R2Test() {
		String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSIE11", ua.getBrowser().toString());
		Assert.assertEquals("11.0", ua.getVersion());
		Assert.assertEquals("Trident", ua.getEngine().toString());
		Assert.assertEquals("7.0", ua.getEngineVersion());
		Assert.assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseEdgeTest() {
		String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.69 Safari/537.36 Edg/81.0.416.34";
		UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSEdge", ua.getBrowser().toString());
		Assert.assertEquals("81.0.416.34", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}
}
