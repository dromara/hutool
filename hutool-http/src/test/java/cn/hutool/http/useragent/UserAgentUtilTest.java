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
		Assert.assertEquals("6.1", ua.getOsVersion());
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
		Assert.assertEquals("4_3_3", ua.getOsVersion());
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
		Assert.assertEquals("9", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseHuaweiPhoneWithNativeBrowserTest() {
		String uaString = "Mozilla/5.0 (Linux; Android 10; EML-AL00 Build/HUAWEIEML-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("Android Browser", ua.getBrowser().toString());
		Assert.assertEquals("4.0", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("10", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseSamsungPhoneWithNativeBrowserTest() {
		String uaString = "Dalvik/2.1.0 (Linux; U; Android 9; SM-G950U Build/PPR1.180610.011)";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("Android Browser", ua.getBrowser().toString());
		Assert.assertNull(ua.getVersion());
		Assert.assertEquals("Unknown", ua.getEngine().toString());
		Assert.assertNull(ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("9", ua.getOsVersion());
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
		Assert.assertEquals("10.0", ua.getOsVersion());
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
		Assert.assertEquals("10.0", ua.getOsVersion());
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
		Assert.assertEquals("8.1", ua.getOsVersion());
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
		Assert.assertEquals("6.1", ua.getOsVersion());
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
		Assert.assertEquals("10.0", ua.getOsVersion());
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
		Assert.assertEquals("10.0", ua.getOsVersion());
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
		Assert.assertEquals("6.3", ua.getOsVersion());
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
		Assert.assertEquals("6.1", ua.getOsVersion());
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
		Assert.assertEquals("10.0", ua.getOsVersion());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	/**
	 * https://github.com/looly/hutool/issues/1177
	 */
	@Test
	public void parseMicroMessengerTest() {
		String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Mobile/15A372 MicroMessenger/7.0.17(0x17001127) NetType/WIFI Language/zh_CN";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("MicroMessenger", ua.getBrowser().toString());
		Assert.assertEquals("7.0.17", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("604.1.38", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("11_0", ua.getOsVersion());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseWorkWxTest() {
		String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 wxwork/3.0.31 MicroMessenger/7.0.1 Language/zh";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("wxwork", ua.getBrowser().toString());
		Assert.assertEquals("3.0.31", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("605.1.15", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseQQTest() {
		String uaString = "User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("QQBrowser", ua.getBrowser().toString());
		Assert.assertEquals("26", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("533.1", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("2.3.7", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseDingTalkTest() {
		String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/18A373 AliApp(DingTalk/5.1.33) com.laiwang.DingTalk/13976299 Channel/201200 language/zh-Hans-CN WK";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("DingTalk", ua.getBrowser().toString());
		Assert.assertEquals("5.1.33", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("605.1.15", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("14_0", ua.getOsVersion());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseAlipayTest() {
		String uaString = "Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; FRD-AL00 Build/HUAWEIFRD-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.3.8.909 UWS/2.10.2.5 Mobile Safari/537.36 UCBS/2.10.2.5 Nebula AlipayDefined(nt:WIFI,ws:360|0|3.0) AliApp(AP/10.0.18.062203) AlipayClient/10.0.18.062203 Language/zh-Hans useStatusBar/true";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("Alipay", ua.getBrowser().toString());
		Assert.assertEquals("10.0.18.062203", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("7.0", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseTaobaoTest() {
		String uaString = "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI 2C Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 AliApp(TB/4.9.2) WindVane/5.2.2 TBANDROID/700342@taobao_android_4.9.2 720X1280";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("Taobao", ua.getBrowser().toString());
		Assert.assertEquals("4.9.2", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("4.4.4", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseUCTest() {
		String uaString = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("UCBrowser", ua.getBrowser().toString());
		Assert.assertEquals("4.0.3214.0", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		Assert.assertEquals("6.1", ua.getOsVersion());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseUCTest2() {
		String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/16G102 UCBrowser/12.7.6.1251 Mobile AliApp(TUnionSDK/0.1.20.3)";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("UCBrowser", ua.getBrowser().toString());
		Assert.assertEquals("12.7.6.1251", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.51.1", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("12_4_1", ua.getOsVersion());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseQuarkTest() {
		String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X; zh-cn) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/16G102 Quark/3.6.2.993 Mobile";
		UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("Quark", ua.getBrowser().toString());
		Assert.assertEquals("3.6.2.993", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("601.1.46", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("12_4_1", ua.getOsVersion());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

}
