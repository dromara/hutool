package com.xiaoleilu.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.ReflectUtil;

/**
 * 反射工具类单元测试
 * @author Looly
 *
 */
public class ReflectUtilTest {
	@Test
	public void getCallerTest() {
		Class<?> caller = ReflectUtil.getCaller();
		Assert.assertEquals("com.xiaoleilu.hutool.core.util.ReflectUtilTest", caller.getName());
	}
	
	@Test
	public void getCallerStackTraceTest() {
		StackTraceElement caller = ReflectUtil.getCallerStackTrace();
		Assert.assertEquals("com.xiaoleilu.hutool.core.util.ReflectUtilTest", caller.getClassName());
	}
}
