package cn.hutool.core.net;

import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

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
		testLongToIp("192.168.1.255");
		testLongToIp("0.0.0.0");
		testLongToIp("0.0.0.255");
		testLongToIp("0.0.255.255");
		testLongToIp("0.255.255.255");
		testLongToIp("255.255.255.255");
		testLongToIp("255.255.255.0");
		testLongToIp("255.255.0.0");
		testLongToIp("255.0.0.0");
		testLongToIp("0.255.255.0");
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
		final String ip = "192.168.100.2";
		testGenerateIpList(ip, 22, false);
		testGenerateIpList(ip, 22, true);

		testGenerateIpList(ip, 24, false);
		testGenerateIpList(ip, 24, true);

		testGenerateIpList(ip, 26, false);
		testGenerateIpList(ip, 26, true);

		testGenerateIpList(ip, 30, false);
		testGenerateIpList(ip, 30, true);

		testGenerateIpList(ip, 31, false);
		testGenerateIpList(ip, 31, true);

		testGenerateIpList(ip, 32, false);
		testGenerateIpList(ip, 32, true);

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
		for (int i = 1; i < 32; i++) {
			Assert.assertTrue("掩码位非法：" + i, Ipv4Util.isMaskBitValid(i));
		}
	}

	@Test
	public void isMaskBitInvalidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
		Assert.assertFalse("掩码位非法检验", maskBitValid);
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

	@SuppressWarnings("SameParameterValue")
	private void testGenerateIpList(final String fromIp, final String toIp) {
		Assert.assertEquals(
				Ipv4Util.countByIpRange(fromIp, toIp),
				Ipv4Util.list(fromIp, toIp).size()
		);
	}

	@SuppressWarnings("SameParameterValue")
	private void testGenerateIpList(final String ip, final int maskBit, final boolean isAll) {
		Assert.assertEquals(
				Ipv4Util.countByMaskBit(maskBit, isAll),
				Ipv4Util.list(ip, maskBit, isAll).size()
		);
	}

	private static void testLongToIp(final String ip) {
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		final String ipv4 = Ipv4Util.longToIpv4(ipLong);
		Assert.assertEquals(ip, ipv4);
	}

	@Test
	public void isInnerTest() {
		Assert.assertTrue(Ipv4Util.isInnerIP(Ipv4Util.LOCAL_IP));
		Assert.assertTrue(Ipv4Util.isInnerIP("192.168.5.12"));
		Assert.assertTrue(Ipv4Util.isInnerIP("172.20.10.1"));

		Assert.assertFalse(Ipv4Util.isInnerIP("180.10.2.5"));
		Assert.assertFalse(Ipv4Util.isInnerIP("192.160.10.3"));
	}

	@Test
	public void isPublicTest() {
		Assert.assertTrue(Ipv4Util.isPublicIP("180.10.2.5"));
		Assert.assertTrue(Ipv4Util.isPublicIP("192.160.10.3"));

		Assert.assertFalse(Ipv4Util.isPublicIP(Ipv4Util.LOCAL_IP));
		Assert.assertFalse(Ipv4Util.isPublicIP("192.168.5.12"));
		Assert.assertFalse(Ipv4Util.isPublicIP("127.0.0.1"));
		Assert.assertFalse(Ipv4Util.isPublicIP("172.20.10.1"));
	}

}
