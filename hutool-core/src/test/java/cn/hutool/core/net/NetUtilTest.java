package cn.hutool.core.net;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.net.HttpCookie;
import java.net.InetAddress;
import java.util.List;

/**
 * NetUtil单元测试
 * 
 * @author Looly
 *
 */
public class NetUtilTest {

	@Test
	@Ignore
	public void getLocalhostStrTest() {
		String localhost = NetUtil.getLocalhostStr();
		Assert.assertNotNull(localhost);
	}

	@Test
	@Ignore
	public void getLocalhostTest() {
		InetAddress localhost = NetUtil.getLocalhost();
		Assert.assertNotNull(localhost);
	}

	@Test
	@Ignore
	public void getLocalMacAddressTest() {
		String macAddress = NetUtil.getLocalMacAddress();
		Assert.assertNotNull(macAddress);

		// 验证MAC地址正确
		boolean match = ReUtil.isMatch(PatternPool.MAC_ADDRESS, macAddress);
		Assert.assertTrue(match);
	}

	@Test
	public void longToIpTest() {
		String ipv4 = NetUtil.longToIpv4(2130706433L);
		Assert.assertEquals("127.0.0.1", ipv4);
	}

	@Test
	public void ipToLongTest() {
		long ipLong = NetUtil.ipv4ToLong("127.0.0.1");
		Assert.assertEquals(2130706433L, ipLong);
	}

	@Test
	@Ignore
	public void isUsableLocalPortTest(){
		Assert.assertTrue(NetUtil.isUsableLocalPort(80));
	}

	@Test
	public void parseCookiesTest(){
		String cookieStr = "cookieName=\"cookieValue\";Path=\"/\";Domain=\"cookiedomain.com\"";
		final List<HttpCookie> httpCookies = NetUtil.parseCookies(cookieStr);
		Assert.assertEquals(1, httpCookies.size());

		final HttpCookie httpCookie = httpCookies.get(0);
		Assert.assertEquals(0, httpCookie.getVersion());
		Assert.assertEquals("cookieName", httpCookie.getName());
		Assert.assertEquals("cookieValue", httpCookie.getValue());
		Assert.assertEquals("/", httpCookie.getPath());
		Assert.assertEquals("cookiedomain.com", httpCookie.getDomain());
	}

	@Test
	public void getLocalHostNameTest() {
		Assert.assertNotNull(NetUtil.getLocalHostName());
	}
}
