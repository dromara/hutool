package com.xiaoleilu.hutool.core.util;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.NetUtil;

public class NetUtilTest {

	@Test
	public void getLocalhostTest(){
		InetAddress localhost = NetUtil.getLocalhost();
		Assert.assertNotNull(localhost);
	}
}
