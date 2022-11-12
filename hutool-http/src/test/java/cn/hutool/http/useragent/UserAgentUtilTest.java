package cn.hutool.http.useragent;

import org.junit.Assert;
import org.junit.Test;

public class UserAgentUtilTest {

	@Test
	public void parseDesktopTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1";

		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "User-Agent:Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5";

		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Linux; Android 9; MIX 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.80 Mobile Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaString = "Mozilla/5.0 (Linux; Android 10; EML-AL00 Build/HUAWEIEML-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Dalvik/2.1.0 (Linux; U; Android 9; SM-G950U Build/PPR1.180610.011)";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Mobile; Windows Phone 8.1; Android 4.0; ARM; Trident/7.0; Touch; rv:11.0; IEMobile/11.0; NOKIA; Lumia 520) like iPhone OS 7_0_3 Mac OS X AppleWebKit/537 (KHTML, like Gecko) Mobile Safari/537 ";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Windows Phone 10.0; Android 6.0.1; Microsoft; Lumia 950XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36 Edge/15.14900";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Chrome", ua.getBrowser().toString());
		Assert.assertEquals("63.0.3239.132", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 8.1 or Windows Server 2012R2", ua.getOs().toString());
		Assert.assertEquals("6.3", ua.getOsVersion());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseIE11OnWindowsServer2008R2Test() {
		final String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.69 Safari/537.36 Edg/81.0.416.34";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
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
	 * https://github.com/dromara/hutool/issues/1177
	 */
	@Test
	public void parseMicroMessengerTest() {
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Mobile/15A372 MicroMessenger/7.0.17(0x17001127) NetType/WIFI Language/zh_CN";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 wxwork/3.0.31 MicroMessenger/7.0.1 Language/zh";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/18A373 AliApp(DingTalk/5.1.33) com.laiwang.DingTalk/13976299 Channel/201200 language/zh-Hans-CN WK";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; FRD-AL00 Build/HUAWEIFRD-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.3.8.909 UWS/2.10.2.5 Mobile Safari/537.36 UCBS/2.10.2.5 Nebula AlipayDefined(nt:WIFI,ws:360|0|3.0) AliApp(AP/10.0.18.062203) AlipayClient/10.0.18.062203 Language/zh-Hans useStatusBar/true";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI 2C Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 AliApp(TB/4.9.2) WindVane/5.2.2 TBANDROID/700342@taobao_android_4.9.2 720X1280";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/16G102 UCBrowser/12.7.6.1251 Mobile AliApp(TUnionSDK/0.1.20.3)";
		final UserAgent ua = UserAgentUtil.parse(uaString);
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
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X; zh-cn) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/16G102 Quark/3.6.2.993 Mobile";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("Quark", ua.getBrowser().toString());
		Assert.assertEquals("3.6.2.993", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("601.1.46", ua.getEngineVersion());
		Assert.assertEquals("iPhone", ua.getOs().toString());
		Assert.assertEquals("12_4_1", ua.getOsVersion());
		Assert.assertEquals("iPhone", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseWxworkTest() {
		final String uaString = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1326.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 wxwork/3.1.10 (MicroMessenger/6.2) WindowsWechat";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("wxwork", ua.getBrowser().toString());
		Assert.assertEquals("3.1.10", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("10.0", ua.getOsVersion());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseWxworkMobileTest() {
		final String uaString = "Mozilla/5.0 (Linux; Android 10; JSN-AL00 Build/HONORJSN-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045710 Mobile Safari/537.36 wxwork/3.1.10 ColorScheme/Light MicroMessenger/7.0.1 NetType/WIFI Language/zh Lang/zh";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		Assert.assertEquals("wxwork", ua.getBrowser().toString());
		Assert.assertEquals("3.1.10", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("10", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseEdgATest(){
		// https://gitee.com/dromara/hutool/issues/I4MCBP
		final String uaStr = "userAgent: Mozilla/5.0 (Linux; Android 11; MI 9 Transparent Edition) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.55 Mobile Safari/537.36 EdgA/96.0.1054.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("MSEdge", ua.getBrowser().toString());
		Assert.assertEquals("96.0.1054.36", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("11", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseLenovoTest(){
		// https://gitee.com/dromara/hutool/issues/I4QBMD
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36 SLBrowser/7.0.0.6241 SLBChan/30";
		final UserAgent ua = UserAgentUtil.parse(uaStr);

		Assert.assertEquals("Lenovo", ua.getBrowser().toString());
		Assert.assertEquals("7.0.0.6241", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("10.0", ua.getOsVersion());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}

	@Test
	public void parseXiaoMiTest(){
		final String uaStr = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/89.0.4389.116 Safari/534.24 XiaoMi/MiuiBrowser/16.0.18 swan-mibrowser";
		final UserAgent ua = UserAgentUtil.parse(uaStr);

		Assert.assertEquals("MiuiBrowser", ua.getBrowser().toString());
		Assert.assertEquals("16.0.18", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("534.24", ua.getEngineVersion());
		Assert.assertEquals("Android", ua.getOs().toString());
		Assert.assertEquals("11", ua.getOsVersion());
		Assert.assertEquals("Android", ua.getPlatform().toString());
		Assert.assertTrue(ua.isMobile());
	}

	@Test
	public void parseFromDeepinTest(){
		// https://gitee.com/dromara/hutool/issues/I50YGY
		final String uaStr = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("Linux", ua.getOs().toString());
	}

	@Test
	public void issueI60UOPTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36 dingtalk-win/1.0.0 nw(0.14.7) DingTalk(6.5.40-Release.9059101) Mojo/1.0.0 Native AppType(release) Channel/201200";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		Assert.assertEquals("DingTalk-win", ua.getBrowser().toString());
		Assert.assertEquals("6.5.40-Release.9059101", ua.getVersion());
		Assert.assertEquals("Webkit", ua.getEngine().toString());
		Assert.assertEquals("537.36", ua.getEngineVersion());
		Assert.assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		Assert.assertEquals("10.0", ua.getOsVersion());
		Assert.assertEquals("Windows", ua.getPlatform().toString());
		Assert.assertFalse(ua.isMobile());
	}
}
