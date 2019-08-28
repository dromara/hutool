package cn.hutool.system;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.system.oshi.OshiUtil;

public class OshiTest {
	
	@Test
	public void getMemeryTest() {
		long total = OshiUtil.getMemory().getTotal();
		Assert.assertTrue(total > 0);
	}
}
