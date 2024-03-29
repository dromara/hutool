package cn.hutool.core.net;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
	@Ignore
	public void getLocalhostStrTest() {
		final String localhost = NetUtil.getLocalhostStr();
		Assert.assertNotNull(localhost);
	}

	@Test
	@Ignore
	public void getLocalhostTest() {
		final InetAddress localhost = NetUtil.getLocalhost();
		Assert.assertNotNull(localhost);
	}

	@Test
	@Ignore
	public void getLocalMacAddressTest() {
		final String macAddress = NetUtil.getLocalMacAddress();
		Assert.assertNotNull(macAddress);

		// 验证MAC地址正确
		final boolean match = ReUtil.isMatch(PatternPool.MAC_ADDRESS, macAddress);
		Assert.assertTrue(match);
	}

	@Test
	public void longToIpTest() {
		final String ipv4 = NetUtil.longToIpv4(2130706433L);
		Assert.assertEquals("127.0.0.1", ipv4);
	}

	@Test
	public void ipToLongTest() {
		final long ipLong = NetUtil.ipv4ToLong("127.0.0.1");
		Assert.assertEquals(2130706433L, ipLong);
	}

	@Test
	@Ignore
	public void isUsableLocalPortTest(){
		Assert.assertTrue(NetUtil.isUsableLocalPort(80));
	}

	@Test
	public void parseCookiesTest(){
		final String cookieStr = "cookieName=\"cookieValue\";Path=\"/\";Domain=\"cookiedomain.com\"";
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
	@Ignore
	public void getLocalHostNameTest() {
		// 注意此方法会触发反向DNS解析，导致阻塞，阻塞时间取决于网络！
		Assert.assertNotNull(NetUtil.getLocalHostName());
	}

	@Test
	public void getLocalHostTest() {
		Assert.assertNotNull(NetUtil.getLocalhost());
	}

	@Test
	public void pingTest(){
		Assert.assertTrue(NetUtil.ping("127.0.0.1"));
	}

	@Test
	@Ignore
	public void isOpenTest(){
		final InetSocketAddress address = new InetSocketAddress("www.hutool.cn", 443);
		Assert.assertTrue(NetUtil.isOpen(address, 200));
	}

	@Test
	@Ignore
	public void getDnsInfoTest(){
		final List<String> txt = NetUtil.getDnsInfo("hutool.cn", "TXT");
		Console.log(txt);
	}

	@Test
	public void isInRangeTest(){
		Assert.assertTrue(NetUtil.isInRange("114.114.114.114","0.0.0.0/0"));
		Assert.assertTrue(NetUtil.isInRange("192.168.3.4","192.0.0.0/8"));
		Assert.assertTrue(NetUtil.isInRange("192.168.3.4","192.168.0.0/16"));
		Assert.assertTrue(NetUtil.isInRange("192.168.3.4","192.168.3.0/24"));
		Assert.assertTrue(NetUtil.isInRange("192.168.3.4","192.168.3.4/32"));
		Assert.assertFalse(NetUtil.isInRange("8.8.8.8","192.0.0.0/8"));
		Assert.assertFalse(NetUtil.isInRange("114.114.114.114","192.168.3.4/32"));
	}

	@Test
	public void issueI64P9JTest() {
		// 获取结果应该去掉空格
		final String ips = "unknown, 12.34.56.78, 23.45.67.89";
		final String ip = NetUtil.getMultistageReverseProxyIp(ips);
		Assert.assertEquals("12.34.56.78", ip);
	}
}
