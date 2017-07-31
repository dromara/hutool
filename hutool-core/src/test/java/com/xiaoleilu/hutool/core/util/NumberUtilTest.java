package com.xiaoleilu.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.NumberUtil;

/**
 * {@link NumberUtil} 单元测试类
 * @author Looly
 *
 */
public class NumberUtilTest {
	
	@Test
	public void divTest() {
		double result = NumberUtil.div(0, 1);
		Assert.assertEquals(0.0, result, 0);
	}

	@Test
	public void roundTest(){
		
		//四舍
		double round3 = NumberUtil.round(2.674, 2);
		double round4 = NumberUtil.round("2.674", 2);
		Assert.assertEquals(round3, 2.67, 0);
		Assert.assertEquals(round4, 2.67, 0);

		//五入
		double round1 = NumberUtil.round(2.675, 2);
		double round2 = NumberUtil.round("2.675", 2);
		Assert.assertEquals(round1, 2.68, 0);
		Assert.assertEquals(round2, 2.68, 0);
	}
	
	@Test
	public void roundStrTest(){
		String roundStr = NumberUtil.roundStr(2.647, 2);
		Assert.assertEquals(roundStr, "2.65");
	}
	
	@Test
	public void decimalFormatTest(){
		long c=299792458;//光速
		
		String format = NumberUtil.decimalFormat(",###", c);
		Assert.assertEquals("299,792,458", format);
	}
}
