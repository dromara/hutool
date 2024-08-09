package cn.hutool.http.useragent;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserAgentUtilTest {

	@Test
	public void parseDesktopTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1";

		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Chrome", ua.getBrowser().toString());
		assertEquals("14.0.835.163", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("535.1", ua.getEngineVersion());
		assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		assertEquals("6.1", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseMobileTest() {
		final String uaStr = "User-Agent:Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5";

		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Safari", ua.getBrowser().toString());
		assertEquals("5.0.2", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("533.17.9", ua.getEngineVersion());
		assertEquals("iPhone", ua.getOs().toString());
		assertEquals("4_3_3", ua.getOsVersion());
		assertEquals("iPhone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseMiui10WithChromeTest() {
		final String uaStr = "Mozilla/5.0 (Linux; Android 9; MIX 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.80 Mobile Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Chrome", ua.getBrowser().toString());
		assertEquals("70.0.3538.80", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("9", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseHuaweiPhoneWithNativeBrowserTest() {
		final String uaString = "Mozilla/5.0 (Linux; Android 10; EML-AL00 Build/HUAWEIEML-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("Android Browser", ua.getBrowser().toString());
		assertEquals("4.0", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("10", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseSamsungPhoneWithNativeBrowserTest() {
		final String uaString = "Dalvik/2.1.0 (Linux; U; Android 9; SM-G950U Build/PPR1.180610.011)";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("Android Browser", ua.getBrowser().toString());
		assertNull(ua.getVersion());
		assertEquals("Unknown", ua.getEngine().toString());
		assertNull(ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("9", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseWindows10WithChromeTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Chrome", ua.getBrowser().toString());
		assertEquals("70.0.3538.102", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseWindows10WithIe11Test() {
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSIE11", ua.getBrowser().toString());
		assertEquals("11.0", ua.getVersion());
		assertEquals("Trident", ua.getEngine().toString());
		assertEquals("7.0", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseWindows10WithIeMobileLumia520Test() {
		final String uaStr = "Mozilla/5.0 (Mobile; Windows Phone 8.1; Android 4.0; ARM; Trident/7.0; Touch; rv:11.0; IEMobile/11.0; NOKIA; Lumia 520) like iPhone OS 7_0_3 Mac OS X AppleWebKit/537 (KHTML, like Gecko) Mobile Safari/537 ";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("IEMobile", ua.getBrowser().toString());
		assertEquals("11.0", ua.getVersion());
		assertEquals("Trident", ua.getEngine().toString());
		assertEquals("7.0", ua.getEngineVersion());
		assertEquals("Windows Phone", ua.getOs().toString());
		assertEquals("8.1", ua.getOsVersion());
		assertEquals("Windows Phone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseWindows10WithIe8EmulatorTest() {
		final String uaStr = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSIE", ua.getBrowser().toString());
		assertEquals("8.0", ua.getVersion());
		assertEquals("Trident", ua.getEngine().toString());
		assertEquals("4.0", ua.getEngineVersion());
		assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		assertEquals("6.1", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseWindows10WithEdgeTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSEdge", ua.getBrowser().toString());
		assertEquals("18.17763", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseEdgeOnLumia950XLTest() {
		final String uaStr = "Mozilla/5.0 (Windows Phone 10.0; Android 6.0.1; Microsoft; Lumia 950XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36 Edge/15.14900";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSEdge", ua.getBrowser().toString());
		assertEquals("15.14900", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows Phone", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows Phone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseChromeOnWindowsServer2012R2Test() {
		final String uaStr = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Chrome", ua.getBrowser().toString());
		assertEquals("63.0.3239.132", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 8.1 or Windows Server 2012R2", ua.getOs().toString());
		assertEquals("6.3", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseIE11OnWindowsServer2008R2Test() {
		final String uaStr = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSIE11", ua.getBrowser().toString());
		assertEquals("11.0", ua.getVersion());
		assertEquals("Trident", ua.getEngine().toString());
		assertEquals("7.0", ua.getEngineVersion());
		assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		assertEquals("6.1", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseEdgeTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.69 Safari/537.36 Edg/81.0.416.34";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSEdge", ua.getBrowser().toString());
		assertEquals("81.0.416.34", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	/**
	 * https://github.com/dromara/hutool/issues/1177
	 */
	@Test
	public void parseMicroMessengerTest() {
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Mobile/15A372 MicroMessenger/7.0.17(0x17001127) NetType/WIFI Language/zh_CN";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("MicroMessenger", ua.getBrowser().toString());
		assertEquals("7.0.17", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("604.1.38", ua.getEngineVersion());
		assertEquals("iPhone", ua.getOs().toString());
		assertEquals("11_0", ua.getOsVersion());
		assertEquals("iPhone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseWorkWxTest() {
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 wxwork/3.0.31 MicroMessenger/7.0.1 Language/zh";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("wxwork", ua.getBrowser().toString());
		assertEquals("3.0.31", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("605.1.15", ua.getEngineVersion());
		assertEquals("iPhone", ua.getOs().toString());
		assertEquals("iPhone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseQQTest() {
		final String uaString = "User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("QQBrowser", ua.getBrowser().toString());
		assertEquals("26", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("533.1", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("2.3.7", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseDingTalkTest() {
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/18A373 AliApp(DingTalk/5.1.33) com.laiwang.DingTalk/13976299 Channel/201200 language/zh-Hans-CN WK";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("DingTalk", ua.getBrowser().toString());
		assertEquals("5.1.33", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("605.1.15", ua.getEngineVersion());
		assertEquals("iPhone", ua.getOs().toString());
		assertEquals("14_0", ua.getOsVersion());
		assertEquals("iPhone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseAlipayTest() {
		final String uaString = "Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; FRD-AL00 Build/HUAWEIFRD-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.3.8.909 UWS/2.10.2.5 Mobile Safari/537.36 UCBS/2.10.2.5 Nebula AlipayDefined(nt:WIFI,ws:360|0|3.0) AliApp(AP/10.0.18.062203) AlipayClient/10.0.18.062203 Language/zh-Hans useStatusBar/true";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("Alipay", ua.getBrowser().toString());
		assertEquals("10.0.18.062203", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("7.0", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseTaobaoTest() {
		final String uaString = "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI 2C Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 AliApp(TB/4.9.2) WindVane/5.2.2 TBANDROID/700342@taobao_android_4.9.2 720X1280";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("Taobao", ua.getBrowser().toString());
		assertEquals("4.9.2", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("4.4.4", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseUCTest() {
		final String uaString = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("UCBrowser", ua.getBrowser().toString());
		assertEquals("4.0.3214.0", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 7 or Windows Server 2008R2", ua.getOs().toString());
		assertEquals("6.1", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseUCTest2() {
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/16G102 UCBrowser/12.7.6.1251 Mobile AliApp(TUnionSDK/0.1.20.3)";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("UCBrowser", ua.getBrowser().toString());
		assertEquals("12.7.6.1251", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.51.1", ua.getEngineVersion());
		assertEquals("iPhone", ua.getOs().toString());
		assertEquals("12_4_1", ua.getOsVersion());
		assertEquals("iPhone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseQuarkTest() {
		final String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4_1 like Mac OS X; zh-cn) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/16G102 Quark/3.6.2.993 Mobile";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("Quark", ua.getBrowser().toString());
		assertEquals("3.6.2.993", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("601.1.46", ua.getEngineVersion());
		assertEquals("iPhone", ua.getOs().toString());
		assertEquals("12_4_1", ua.getOsVersion());
		assertEquals("iPhone", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseWxworkTest() {
		final String uaString = "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 QBCore/4.0.1326.400 QQBrowser/9.0.2524.400 Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36 wxwork/3.1.10 (MicroMessenger/6.2) WindowsWechat";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("wxwork", ua.getBrowser().toString());
		assertEquals("3.1.10", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseWxworkMobileTest() {
		final String uaString = "Mozilla/5.0 (Linux; Android 10; JSN-AL00 Build/HONORJSN-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045710 Mobile Safari/537.36 wxwork/3.1.10 ColorScheme/Light MicroMessenger/7.0.1 NetType/WIFI Language/zh Lang/zh";
		final UserAgent ua = UserAgentUtil.parse(uaString);
		assertEquals("wxwork", ua.getBrowser().toString());
		assertEquals("3.1.10", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("10", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseEdgATest(){
		// https://gitee.com/dromara/hutool/issues/I4MCBP
		final String uaStr = "userAgent: Mozilla/5.0 (Linux; Android 11; MI 9 Transparent Edition) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.55 Mobile Safari/537.36 EdgA/96.0.1054.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("MSEdge", ua.getBrowser().toString());
		assertEquals("96.0.1054.36", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("11", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseLenovoTest(){
		// https://gitee.com/dromara/hutool/issues/I4QBMD
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36 SLBrowser/7.0.0.6241 SLBChan/30";
		final UserAgent ua = UserAgentUtil.parse(uaStr);

		assertEquals("Lenovo", ua.getBrowser().toString());
		assertEquals("7.0.0.6241", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	@Test
	public void parseXiaoMiTest(){
		final String uaStr = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/89.0.4389.116 Safari/534.24 XiaoMi/MiuiBrowser/16.0.18 swan-mibrowser";
		final UserAgent ua = UserAgentUtil.parse(uaStr);

		assertEquals("MiuiBrowser", ua.getBrowser().toString());
		assertEquals("16.0.18", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("534.24", ua.getEngineVersion());
		assertEquals("Android", ua.getOs().toString());
		assertEquals("11", ua.getOsVersion());
		assertEquals("Android", ua.getPlatform().toString());
		assertTrue(ua.isMobile());
	}

	@Test
	public void parseFromDeepinTest(){
		// https://gitee.com/dromara/hutool/issues/I50YGY
		final String uaStr = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Linux", ua.getOs().toString());
	}

	@Test
	public void issueI60UOPTest() {
		final String uaStr = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36 dingtalk-win/1.0.0 nw(0.14.7) DingTalk(6.5.40-Release.9059101) Mojo/1.0.0 Native AppType(release) Channel/201200";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("DingTalk-win", ua.getBrowser().toString());
		assertEquals("6.5.40-Release.9059101", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua.getOs().toString());
		assertEquals("10.0", ua.getOsVersion());
		assertEquals("Windows", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}

	/**
	 * <a href="https://gitee.com/dromara/hutool/issues/I7OTCU">fix : issues I7OTCU </a>
	 */
	@Test
	public void issuseI7OTCUTest() {
		// MAC Chrome 浏览器 ua
		final String uaStr = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";
		final UserAgent ua = UserAgentUtil.parse(uaStr);
		assertEquals("Chrome", ua.getBrowser().toString());
		assertEquals("114.0.0.0", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("537.36", ua.getEngineVersion());
		assertEquals("OSX", ua.getOs().toString());
		assertEquals("10_15_7", ua.getOsVersion());
		assertEquals("Mac", ua.getPlatform().toString());
		assertFalse(ua.isMobile());

		// iphone Chrome 浏览器ua
		final String uaStr2 = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1";
		final UserAgent ua2 = UserAgentUtil.parse(uaStr2);
		assertEquals("Chrome", ua2.getBrowser().toString());
		assertEquals("56.0.2924.75", ua2.getVersion());
		assertEquals("Webkit", ua2.getEngine().toString());
		assertEquals("602.1.50", ua2.getEngineVersion());
		assertEquals("iPhone", ua2.getOs().toString());
		assertEquals("10_3", ua2.getOsVersion());
		assertEquals("iPhone", ua2.getPlatform().toString());
		assertTrue(ua2.isMobile());
	}

	@Test
	public void issueI847JYTest() {
		final String s = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) " +
			"Mobile/15E148 SP-engine/2.80.0 main%2F1.0 baiduboxapp/13.42.0.11 (Baidu; P2 17.0) NABar/1.0 themeUA=Them";
		final UserAgent ua2 = UserAgentUtil.parse(s);

		assertEquals("Baidu", ua2.getBrowser().toString());
		assertEquals("13.42.0.11", ua2.getVersion());
		assertEquals("Webkit", ua2.getEngine().toString());
		assertEquals("605.1.15", ua2.getEngineVersion());
		assertEquals("iPhone", ua2.getOs().toString());
		assertEquals("17_0", ua2.getOsVersion());
		assertEquals("iPhone", ua2.getPlatform().toString());
		assertTrue(ua2.isMobile());
	}

	@Test
	public void issueI8X5XQTest() {
		final String s = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 " +
			"Safari/537.36 Core/1.94.218.400 QQBrowser/12.1.5496.400";
		final UserAgent ua2 = UserAgentUtil.parse(s);

		assertEquals("QQBrowser", ua2.getBrowser().toString());
		assertEquals("12.1.5496.400", ua2.getVersion());
		assertEquals("Webkit", ua2.getEngine().toString());
		assertEquals("537.36", ua2.getEngineVersion());
		assertEquals("Windows 10 or Windows Server 2016", ua2.getOs().toString());
		assertEquals("10.0", ua2.getOsVersion());
		assertEquals("Windows", ua2.getPlatform().toString());
		assertFalse(ua2.isMobile());
	}

	@Test
	public void issueIA74K2Test() {
		UserAgent ua = UserAgentUtil.parse(
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) MicroMessenger/6.8.0(0x16080000) MacWechat/3.8.7(0x13080710) Safari/605.1.15 NetType/WIFI");

		assertEquals("MicroMessenger", ua.getBrowser().toString());
		assertEquals("6.8.0", ua.getVersion());
		assertEquals("Webkit", ua.getEngine().toString());
		assertEquals("605.1.15", ua.getEngineVersion());
		assertEquals("OSX", ua.getOs().toString());
		assertEquals("10_15_7", ua.getOsVersion());
		assertEquals("Mac", ua.getPlatform().toString());
		assertFalse(ua.isMobile());
	}
}
