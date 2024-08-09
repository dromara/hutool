package cn.hutool.core.net;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import static org.junit.jupiter.api.Assertions.*;
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
		assertNotNull(localhost);
	}

	@Test
	@Disabled
	public void getLocalhostTest() {
		final InetAddress localhost = NetUtil.getLocalhost();
		assertNotNull(localhost);
	}

	@Test
	@Disabled
	public void getLocalMacAddressTest() {
		final String macAddress = NetUtil.getLocalMacAddress();
		assertNotNull(macAddress);

		// 验证MAC地址正确
		final boolean match = ReUtil.isMatch(PatternPool.MAC_ADDRESS, macAddress);
		assertTrue(match);
	}

	@Test
	public void longToIpTest() {
		final String ipv4 = NetUtil.longToIpv4(2130706433L);
		assertEquals("127.0.0.1", ipv4);
	}

	@Test
	public void ipToLongTest() {
		final long ipLong = NetUtil.ipv4ToLong("127.0.0.1");
		assertEquals(2130706433L, ipLong);
	}

	@Test
	@Disabled
	public void isUsableLocalPortTest(){
		assertTrue(NetUtil.isUsableLocalPort(80));
	}

	@Test
	public void parseCookiesTest(){
		final String cookieStr = "cookieName=\"cookieValue\";Path=\"/\";Domain=\"cookiedomain.com\"";
		final List<HttpCookie> httpCookies = NetUtil.parseCookies(cookieStr);
		assertEquals(1, httpCookies.size());

		final HttpCookie httpCookie = httpCookies.get(0);
		assertEquals(0, httpCookie.getVersion());
		assertEquals("cookieName", httpCookie.getName());
		assertEquals("cookieValue", httpCookie.getValue());
		assertEquals("/", httpCookie.getPath());
		assertEquals("cookiedomain.com", httpCookie.getDomain());
	}

	@Test
	@Disabled
	public void getLocalHostNameTest() {
		// 注意此方法会触发反向DNS解析，导致阻塞，阻塞时间取决于网络！
		assertNotNull(NetUtil.getLocalHostName());
	}

	@Test
	public void getLocalHostTest() {
		assertNotNull(NetUtil.getLocalhost());
	}

	@Test
	public void pingTest(){
		assertTrue(NetUtil.ping("127.0.0.1"));
	}

	@Test
	@Disabled
	public void isOpenTest(){
		final InetSocketAddress address = new InetSocketAddress("www.hutool.cn", 443);
		assertTrue(NetUtil.isOpen(address, 200));
	}

	@Test
	@Disabled
	public void getDnsInfoTest(){
		final List<String> txt = NetUtil.getDnsInfo("hutool.cn", "TXT");
		Console.log(txt);
	}

	@Test
	public void isInRangeTest(){
		assertTrue(NetUtil.isInRange("114.114.114.114","0.0.0.0/0"));
		assertTrue(NetUtil.isInRange("192.168.3.4","192.0.0.0/8"));
		assertTrue(NetUtil.isInRange("192.168.3.4","192.168.0.0/16"));
		assertTrue(NetUtil.isInRange("192.168.3.4","192.168.3.0/24"));
		assertTrue(NetUtil.isInRange("192.168.3.4","192.168.3.4/32"));
		assertFalse(NetUtil.isInRange("8.8.8.8","192.0.0.0/8"));
		assertFalse(NetUtil.isInRange("114.114.114.114","192.168.3.4/32"));
	}

	@Test
	public void issueI64P9JTest() {
		// 获取结果应该去掉空格
		final String ips = "unknown, 12.34.56.78, 23.45.67.89";
		final String ip = NetUtil.getMultistageReverseProxyIp(ips);
		assertEquals("12.34.56.78", ip);
	}
}
