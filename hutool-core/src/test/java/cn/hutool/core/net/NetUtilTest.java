package cn.hutool.core.net;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;

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
	public void isUsableLocalPortTest(){
		Assert.assertTrue(NetUtil.isUsableLocalPort(80));
	}
}
