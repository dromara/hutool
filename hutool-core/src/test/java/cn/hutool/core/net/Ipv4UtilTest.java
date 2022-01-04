package cn.hutool.core.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

public class Ipv4UtilTest {

	@Test
	public void getMaskBitByMaskTest(){
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		Assertions.assertEquals(24, maskBitByMask);
	}

	@Test
	public void getMaskBitByIllegalMaskTest() {
		Executable getMaskBitByMaskExecutable = () -> Ipv4Util.getMaskBitByMask("255.255.0.255");
		Assertions.assertThrows(IllegalArgumentException.class, getMaskBitByMaskExecutable, "非法掩码测试");
	}

	@Test
	public void getMaskByMaskBitTest(){
		final String mask = Ipv4Util.getMaskByMaskBit(24);
		Assertions.assertEquals("255.255.255.0", mask);
	}

	@Test
	public void longToIpTest() {
		String ip = "192.168.1.255";
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		String ipv4 = Ipv4Util.longToIpv4(ipLong);
		Assertions.assertEquals(ip, ipv4);
	}

	@Test
	public void getEndIpStrTest(){
		String ip = "192.168.1.1";
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
		Assertions.assertEquals("192.168.1.255", endIpStr);
	}

	@Test
	public void listTest(){
		int maskBit = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final List<String> list = Ipv4Util.list("192.168.100.2", maskBit, false);
		Assertions.assertEquals(254, list.size());
	}

	@Test
	public void isMaskValidTest() {
		boolean maskValid = Ipv4Util.isMaskValid("255.255.255.0");
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
		boolean maskBitValid = Ipv4Util.isMaskBitValid(32);
		Assertions.assertTrue(maskBitValid, "掩码位合法检验");
	}

	@Test
	public void isMaskBitInvalidTest() {
		boolean maskBitValid = Ipv4Util.isMaskBitValid(33);
		Assertions.assertFalse(maskBitValid, "掩码位非法检验");
	}

	@Test
	public void ipv4ToLongTest(){
		long l = Ipv4Util.ipv4ToLong("127.0.0.1");
		Assertions.assertEquals(2130706433L, l);
		l = Ipv4Util.ipv4ToLong("114.114.114.114");
		Assertions.assertEquals(1920103026L, l);
		l = Ipv4Util.ipv4ToLong("0.0.0.0");
		Assertions.assertEquals(0L, l);
		l = Ipv4Util.ipv4ToLong("255.255.255.255");
		Assertions.assertEquals(4294967295L, l);
	}
}
