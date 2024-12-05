package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3809Test {
	@Test
	void roundStrTest() {
		Assertions.assertEquals("9999999999999999.99", NumberUtil.roundStr("9999999999999999.99", 2));  //输出结果不符合方法声明返回值规则
		Assertions.assertEquals("11111111111111119.00", NumberUtil.roundStr("11111111111111119.00", 2));
		Assertions.assertEquals("7999999999999999.99", NumberUtil.roundStr("7999999999999999.99", 2)); //输出结果不符合方法声明返回值规则
		Assertions.assertEquals("699999999991999.92", NumberUtil.roundStr("699999999991999.92", 2)); //输出结果不符合方法声明返回值规则
		Assertions.assertEquals("10.92", NumberUtil.roundStr("10.92", 2));
		Assertions.assertEquals("10.99", NumberUtil.roundStr("10.99", 2));
	}
}
