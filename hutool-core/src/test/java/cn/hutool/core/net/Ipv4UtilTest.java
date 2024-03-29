package cn.hutool.core.net;

import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.util.List;

public class Ipv4UtilTest {

	@Test
	public void getMaskBitByMaskTest(){
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		Assert.assertEquals(24, maskBitByMask);
	}

	@Test
	public void getMaskBitByIllegalMaskTest() {
		final ThrowingRunnable getMaskBitByMaskRunnable = () -> Ipv4Util.getMaskBitByMask("255.255.0.255");
		Assert.assertThrows("非法掩码测试", IllegalArgumentException.class, getMaskBitByMaskRunnable);
	}

	@Test
	public void getMaskByMaskBitTest(){
		final String mask = Ipv4Util.getMaskByMaskBit(24);
		Assert.assertEquals("255.255.255.0", mask);
	}

	@Test
	public void longToIpTest() {
		final String ip = "192.168.1.255";
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		final String ipv4 = Ipv4Util.longToIpv4(ipLong);
		Assert.assertEquals(ip, ipv4);
	}

	@Test
	public void getEndIpStrTest(){
		final String ip = "192.168.1.1";
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
		Assert.assertEquals("192.168.1.255", endIpStr);
	}

	@Test
	public void listTest(){
		final int maskBit = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final List<String> list = Ipv4Util.list("192.168.100.2", maskBit, false);
		Assert.assertEquals(254, list.size());

		testGenerateIpList("10.1.0.1", "10.2.1.2");

		testGenerateIpList("10.2.1.1", "10.2.1.2");
		testGenerateIpList("10.2.0.1", "10.2.1.2");
		testGenerateIpList("10.1.0.1", "10.2.1.2");
		testGenerateIpList("10.1.2.1", "10.2.1.2");

		testGenerateIpList("10.2.1.2", "10.2.1.2");
		testGenerateIpList("10.2.0.2", "10.2.1.2");
		testGenerateIpList("10.1.1.2", "10.2.1.2");
		testGenerateIpList("10.1.2.2", "10.2.1.2");

		testGenerateIpList("10.2.0.3", "10.2.1.2");
		testGenerateIpList("10.1.0.3", "10.2.1.2");
		testGenerateIpList("10.1.1.3", "10.2.1.2");
		testGenerateIpList("10.1.2.3", "10.2.1.2");

		testGenerateIpList("9.255.2.1", "10.2.1.2");
		testGenerateIpList("9.255.2.2", "10.2.1.2");
		testGenerateIpList("9.255.2.3", "10.2.1.2");

		testGenerateIpList("9.255.1.2", "10.2.1.2");
		testGenerateIpList("9.255.0.2", "10.2.1.2");
		testGenerateIpList("9.255.3.2", "10.2.1.2");
	}

	private void testGenerateIpList(final String fromIp, final String toIp) {
		Assert.assertEquals(
				Ipv4Util.countByIpRange(fromIp, toIp),
				Ipv4Util.list(fromIp, toIp).size()
		);
	}

	@Test
	public void isMaskValidTest() {
		final boolean maskValid = Ipv4Util.isMaskValid("255.255.255.0");
		Assert.assertTrue("掩码合法检验", maskValid);
	}

	@Test
	public void isMaskInvalidTest() {
		Assert.assertFalse("掩码非法检验 - 255.255.0.255", Ipv4Util.isMaskValid("255.255.0.255"));
		Assert.assertFalse("掩码非法检验 - null值", Ipv4Util.isMaskValid(null));
		Assert.assertFalse("掩码非法检验 - 空字符串", Ipv4Util.isMaskValid(""));
		Assert.assertFalse("掩码非法检验 - 空白字符串", Ipv4Util.isMaskValid(" "));
	}

	@Test
	public void isMaskBitValidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(32);
		Assert.assertTrue("掩码位合法检验", maskBitValid);
	}

	@Test
	public void isMaskBitInvalidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
		Assert.assertFalse("掩码位非法检验", maskBitValid);
	}

	@Test
	public void matchesTest() {
		final boolean matches1 = Ipv4Util.matches("127.*.*.1", "127.0.0.1");
		Assert.assertTrue("IP地址通配符匹配1", matches1);

		final boolean matches2 = Ipv4Util.matches("192.168.*.1", "127.0.0.1");
		Assert.assertFalse("IP地址通配符匹配2", matches2);
	}

	@Test
	public void ipv4ToLongTest(){
		long l = Ipv4Util.ipv4ToLong("127.0.0.1");
		Assert.assertEquals(2130706433L, l);
		l = Ipv4Util.ipv4ToLong("114.114.114.114");
		Assert.assertEquals(1920103026L, l);
		l = Ipv4Util.ipv4ToLong("0.0.0.0");
		Assert.assertEquals(0L, l);
		l = Ipv4Util.ipv4ToLong("255.255.255.255");
		Assert.assertEquals(4294967295L, l);
	}

	@Test
	public void ipv4ToLongWithDefaultTest() {
		final String strIP = "不正确的 IP 地址";
		final long defaultValue = 0L;
		final long ipOfLong = Ipv4Util.ipv4ToLong(strIP, defaultValue);
		Assert.assertEquals(ipOfLong, defaultValue);

		final String strIP2 = "255.255.255.255";
		final long defaultValue2 = 0L;
		final long ipOfLong2 = Ipv4Util.ipv4ToLong(strIP2, defaultValue2);
		Assert.assertEquals(ipOfLong2, 4294967295L);
	}
}
