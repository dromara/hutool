package org.dromara.hutool.core.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Ipv4UtilTest {

	@Test
	public void formatIpBlockTest() {
		for (int i = Ipv4Util.IPV4_MASK_BIT_VALID_MIN; i < Ipv4Util.IPV4_MASK_BIT_MAX; i++) {
			Assertions.assertEquals("192.168.1.101/" + i, Ipv4Util.formatIpBlock("192.168.1.101", Ipv4Util.getMaskByMaskBit(i)));
		}
	}

	@Test
	public void getMaskBitByMaskTest() {
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		Assertions.assertEquals(24, maskBitByMask);
	}

	@Test
	public void getMaskBitByIllegalMaskTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> Ipv4Util.getMaskBitByMask("255.255.0.255"), "非法掩码测试");
	}

	@Test
	public void getMaskByMaskBitTest() {
		final String mask = Ipv4Util.getMaskByMaskBit(24);
		Assertions.assertEquals("255.255.255.0", mask);
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
	public void getEndIpStrTest() {
		final String ip = "192.168.1.1";
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
		Assertions.assertEquals("192.168.1.255", endIpStr);
	}

	@Test
	public void listTest() {
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
	}

	@Test
	public void listTest2() {
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
		Assertions.assertTrue(maskValid, "掩码合法检验");
	}

	@Test
	public void isMaskInvalidTest() {
		Assertions.assertFalse(Ipv4Util.isMaskValid("255.255.0.255"), "掩码非法检验 - 255.255.0.255");
		Assertions.assertFalse(Ipv4Util.isMaskValid(null), "掩码非法检验 - null值");
		Assertions.assertFalse(Ipv4Util.isMaskValid(""), "掩码非法检验 - 空字符串");
		Assertions.assertFalse(Ipv4Util.isMaskValid(" "), "掩码非法检验 - 空白字符串");
	}

	@Test
	public void isMaskBitValidTest() {
		for (int i = 1; i < 32; i++) {
			Assertions.assertTrue(Ipv4Util.isMaskBitValid(i), "掩码位非法：" + i);
		}
	}

	@Test
	public void isMaskBitInvalidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
		Assertions.assertFalse(maskBitValid, "掩码位非法检验");
	}

	@Test
	public void ipv4ToLongTest() {
		long l = Ipv4Util.ipv4ToLong("127.0.0.1");
		Assertions.assertEquals(2130706433L, l);
		l = Ipv4Util.ipv4ToLong("114.114.114.114");
		Assertions.assertEquals(1920103026L, l);
		l = Ipv4Util.ipv4ToLong("0.0.0.0");
		Assertions.assertEquals(0L, l);
		l = Ipv4Util.ipv4ToLong("255.255.255.255");
		Assertions.assertEquals(4294967295L, l);
	}

	@Test
	public void getMaskIpLongTest() {
		for (int i = 1; i <= 32; i++) {
			Assertions.assertEquals(Ipv4Util.ipv4ToLong(MaskBit.get(i)), MaskBit.getMaskIpLong(i));
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void testGenerateIpList(final String fromIp, final String toIp) {
		Assertions.assertEquals(
				Ipv4Util.countByIpRange(fromIp, toIp),
				Ipv4Util.list(fromIp, toIp).size()
		);
	}

	@SuppressWarnings("SameParameterValue")
	private void testGenerateIpList(final String ip, final int maskBit, final boolean isAll) {
		Assertions.assertEquals(
				Ipv4Util.countByMaskBit(maskBit, isAll),
				Ipv4Util.list(ip, maskBit, isAll).size()
		);
	}

	private static void testLongToIp(final String ip) {
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		final String ipv4 = Ipv4Util.longToIpv4(ipLong);
		Assertions.assertEquals(ip, ipv4);
	}

	@Test
	public void isInnerTest() {
		Assertions.assertTrue(Ipv4Util.isInnerIP(Ipv4Util.LOCAL_IP));
		Assertions.assertTrue(Ipv4Util.isInnerIP("192.168.5.12"));
		Assertions.assertTrue(Ipv4Util.isInnerIP("172.20.10.1"));

		Assertions.assertFalse(Ipv4Util.isInnerIP("180.10.2.5"));
		Assertions.assertFalse(Ipv4Util.isInnerIP("192.160.10.3"));
	}

	@Test
	public void isPublicTest() {
		Assertions.assertTrue(Ipv4Util.isPublicIP("180.10.2.5"));
		Assertions.assertTrue(Ipv4Util.isPublicIP("192.160.10.3"));

		Assertions.assertFalse(Ipv4Util.isPublicIP(Ipv4Util.LOCAL_IP));
		Assertions.assertFalse(Ipv4Util.isPublicIP("192.168.5.12"));
		Assertions.assertFalse(Ipv4Util.isPublicIP("127.0.0.1"));
		Assertions.assertFalse(Ipv4Util.isPublicIP("172.20.10.1"));
	}

	@Test
	public void getMaskBitByIpRange() {
		final String ip = "192.168.100.2";
		for (int i = 1; i <= 32; i++) {
			final String beginIpStr = Ipv4Util.getBeginIpStr(ip, i);
			final String endIpStr = Ipv4Util.getEndIpStr(ip, i);
			Assertions.assertEquals(Ipv4Util.getMaskByMaskBit(i), Ipv4Util.getMaskByIpRange(beginIpStr, endIpStr));
		}
	}
}
