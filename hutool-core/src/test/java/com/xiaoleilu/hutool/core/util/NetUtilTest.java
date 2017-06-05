package com.xiaoleilu.hutool.core.util;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.PatternPool;
import com.xiaoleilu.hutool.util.NetUtil;
import com.xiaoleilu.hutool.util.ReUtil;

/**
 * NetUtil单元测试
 * @author Looly
 *
 */
public class NetUtilTest {

	@Test
	public void getLocalhostTest(){
		InetAddress localhost = NetUtil.getLocalhost();
		Assert.assertNotNull(localhost);
	}
	
	@Test
	public void getLocalMacAddressTest(){
		String macAddress = NetUtil.getLocalMacAddress();
		Assert.assertNotNull(macAddress);
		
		//验证MAC地址正确
		boolean match = ReUtil.isMatch(PatternPool.MAC_ADDRESS, macAddress);
		Assert.assertTrue(match);
	}
}
