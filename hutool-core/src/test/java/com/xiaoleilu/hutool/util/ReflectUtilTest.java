package com.xiaoleilu.hutool.util;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.test.bean.ExamInfoDict;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.ReflectUtil;

/**
 * 反射工具类单元测试
 * @author Looly
 *
 */
public class ReflectUtilTest {
	
	@Test
	public void getMethodsTest(){
		Method[] methods = ReflectUtil.getMethods(ExamInfoDict.class);
		Assert.assertTrue(ArrayUtil.isNotEmpty(methods));
	}
	
	@Test
	public void getMethodTest(){
		Method method = ReflectUtil.getMethod(ExamInfoDict.class, "getId");
		Assert.assertEquals("getId", method.getName());
	}
}
