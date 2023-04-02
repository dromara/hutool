package org.dromara.hutool.net;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.regex.PatternPool;
import org.dromara.hutool.regex.ReUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * NetUtil单元测试
 *
 * @author Looly
 *
 */
public class NetUtilTest {

	@Test
	@Disabled
	public void getLocalhostStrTest() {
		final String localhost = NetUtil.getLocalhostStr();
		Assertions.assertNotNull(localhost);
	}

	@Test
	@Disabled
	public void getLocalhostTest() {
		final InetAddress localhost = NetUtil.getLocalhost();
		Assertions.assertNotNull(localhost);
	}

	@Test
	@Disabled
	public void getLocalMacAddressTest() {
		final String macAddress = NetUtil.getLocalMacAddress();
		Assertions.assertNotNull(macAddress);

		// 验证MAC地址正确
		final boolean match = ReUtil.isMatch(PatternPool.MAC_ADDRESS, macAddress);
		Assertions.assertTrue(match);
	}

	@Test
	public void longToIpTest() {
		final String ipv4 = NetUtil.longToIpv4(2130706433L);
		Assertions.assertEquals("127.0.0.1", ipv4);
	}

	@Test
	public void ipToLongTest() {
		final long ipLong = NetUtil.ipv4ToLong("127.0.0.1");
		Assertions.assertEquals(2130706433L, ipLong);
	}

	@Test
	@Disabled
	public void isUsableLocalPortTest(){
		Assertions.assertTrue(NetUtil.isUsableLocalPort(80));
	}

	@Test
	public void parseCookiesTest(){
		final String cookieStr = "cookieName=\"cookieValue\";Path=\"/\";Domain=\"cookiedomain.com\"";
		final List<HttpCookie> httpCookies = NetUtil.parseCookies(cookieStr);
		Assertions.assertEquals(1, httpCookies.size());

		final HttpCookie httpCookie = httpCookies.get(0);
		Assertions.assertEquals(0, httpCookie.getVersion());
		Assertions.assertEquals("cookieName", httpCookie.getName());
		Assertions.assertEquals("cookieValue", httpCookie.getValue());
		Assertions.assertEquals("/", httpCookie.getPath());
		Assertions.assertEquals("cookiedomain.com", httpCookie.getDomain());
	}

	@Test
	@Disabled
	public void getLocalHostNameTest() {
		// 注意此方法会触发反向DNS解析，导致阻塞，阻塞时间取决于网络！
		Assertions.assertNotNull(NetUtil.getLocalHostName());
	}

	@Test
	public void getLocalHostTest() {
		Assertions.assertNotNull(NetUtil.getLocalhost());
	}

	@Test
	public void pingTest(){
		Assertions.assertTrue(NetUtil.ping("127.0.0.1"));
	}

	@Test
	@Disabled
	public void isOpenTest(){
		final InetSocketAddress address = new InetSocketAddress("www.hutool.cn", 443);
		Assertions.assertTrue(NetUtil.isOpen(address, 200));
	}

	@Test
	@Disabled
	public void getDnsInfoTest(){
		final List<String> txt = NetUtil.getDnsInfo("hutool.cn", "TXT");
		Console.log(txt);
	}

	@Test
	public void isInRangeTest(){
		Assertions.assertTrue(NetUtil.isInRange("114.114.114.114","0.0.0.0/0"));
		Assertions.assertTrue(NetUtil.isInRange("192.168.3.4","192.0.0.0/8"));
		Assertions.assertTrue(NetUtil.isInRange("192.168.3.4","192.168.0.0/16"));
		Assertions.assertTrue(NetUtil.isInRange("192.168.3.4","192.168.3.0/24"));
		Assertions.assertTrue(NetUtil.isInRange("192.168.3.4","192.168.3.4/32"));
		Assertions.assertFalse(NetUtil.isInRange("8.8.8.8","192.0.0.0/8"));
		Assertions.assertFalse(NetUtil.isInRange("114.114.114.114","192.168.3.4/32"));
	}

	@Test
	public void issueI64P9JTest() {
		// 获取结果应该去掉空格
		final String ips = "unknown, 12.34.56.78, 23.45.67.89";
		final String ip = NetUtil.getMultistageReverseProxyIp(ips);
		Assertions.assertEquals("12.34.56.78", ip);
	}
}
