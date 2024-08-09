package cn.hutool.core.net;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Ipv4UtilTest {

	@Test
	public void getMaskBitByMaskTest(){
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		assertEquals(24, maskBitByMask);
	}

	@Test
	public void getMaskBitByIllegalMaskTest() {
		assertThrows(IllegalArgumentException.class, () -> Ipv4Util.getMaskBitByMask("255.255.0.255"));
	}

	@Test
	public void getMaskByMaskBitTest(){
		final String mask = Ipv4Util.getMaskByMaskBit(24);
		assertEquals("255.255.255.0", mask);
	}

	@Test
	public void longToIpTest() {
		final String ip = "192.168.1.255";
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		final String ipv4 = Ipv4Util.longToIpv4(ipLong);
		assertEquals(ip, ipv4);
	}

	@Test
	public void getEndIpStrTest(){
		final String ip = "192.168.1.1";
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
		assertEquals("192.168.1.255", endIpStr);
	}

	@Test
	public void listTest(){
		final int maskBit = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final List<String> list = Ipv4Util.list("192.168.100.2", maskBit, false);
		assertEquals(254, list.size());

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

	@SuppressWarnings("SameParameterValue")
	private void testGenerateIpList(final String fromIp, final String toIp) {
		assertEquals(
				Ipv4Util.countByIpRange(fromIp, toIp),
				Ipv4Util.list(fromIp, toIp).size()
		);
	}

	@Test
	public void isMaskValidTest() {
		final boolean maskValid = Ipv4Util.isMaskValid("255.255.255.0");
		assertTrue(maskValid, "掩码合法检验");
	}

	@Test
	public void isMaskInvalidTest() {
		assertFalse(Ipv4Util.isMaskValid("255.255.0.255"), "掩码非法检验 - 255.255.0.255");
		assertFalse(Ipv4Util.isMaskValid(null), "掩码非法检验 - null值");
		assertFalse(Ipv4Util.isMaskValid(""), "掩码非法检验 - 空字符串");
		assertFalse(Ipv4Util.isMaskValid(" "), "掩码非法检验 - 空白字符串");
	}

	@Test
	public void isMaskBitValidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(32);
		assertTrue( maskBitValid);
	}

	@Test
	public void isMaskBitInvalidTest() {
		final boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
		assertFalse(maskBitValid);
	}

	@Test
	public void matchesTest() {
		final boolean matches1 = Ipv4Util.matches("127.*.*.1", "127.0.0.1");
		assertTrue(matches1);

		final boolean matches2 = Ipv4Util.matches("192.168.*.1", "127.0.0.1");
		assertFalse(matches2);
	}

	@Test
	public void ipv4ToLongTest(){
		long l = Ipv4Util.ipv4ToLong("127.0.0.1");
		assertEquals(2130706433L, l);
		l = Ipv4Util.ipv4ToLong("114.114.114.114");
		assertEquals(1920103026L, l);
		l = Ipv4Util.ipv4ToLong("0.0.0.0");
		assertEquals(0L, l);
		l = Ipv4Util.ipv4ToLong("255.255.255.255");
		assertEquals(4294967295L, l);
	}

	@Test
	public void ipv4ToLongWithDefaultTest() {
		final String strIP = "不正确的 IP 地址";
		final long defaultValue = 0L;
		final long ipOfLong = Ipv4Util.ipv4ToLong(strIP, defaultValue);
		assertEquals(ipOfLong, defaultValue);

		final String strIP2 = "255.255.255.255";
		final long defaultValue2 = 0L;
		final long ipOfLong2 = Ipv4Util.ipv4ToLong(strIP2, defaultValue2);
		assertEquals(ipOfLong2, 4294967295L);
	}
}
