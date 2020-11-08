package cn.hutool.core.net;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Ipv4UtilTest {

	@Test
	public void getMaskBitByMaskTest(){
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		Assert.assertEquals(24, maskBitByMask);
	}

	@Test
	public void getMaskByMaskBitTest(){
		final String mask = Ipv4Util.getMaskByMaskBit(24);
		Assert.assertEquals("255.255.255.0", mask);
	}

	@Test
	public void longToIpTest() {
		String ip = "192.168.1.255";
		final long ipLong = Ipv4Util.ipv4ToLong(ip);
		String ipv4 = Ipv4Util.longToIpv4(ipLong);
		Assert.assertEquals(ip, ipv4);
	}

	@Test
	public void getEndIpStrTest(){
		String ip = "192.168.1.1";
		final int maskBitByMask = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final String endIpStr = Ipv4Util.getEndIpStr(ip, maskBitByMask);
		Console.log(endIpStr);
	}

	@Test
	public void listTest(){
		int maskBit = Ipv4Util.getMaskBitByMask("255.255.255.0");
		final List<String> list = Ipv4Util.list("192.168.100.2", maskBit, false);
		Assert.assertEquals(254, list.size());
	}
}
