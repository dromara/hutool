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
}
