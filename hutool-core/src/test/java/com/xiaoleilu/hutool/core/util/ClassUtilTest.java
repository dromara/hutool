package com.xiaoleilu.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * {@link ClassUtil} 单元测试
 * @author Looly
 *
 */
public class ClassUtilTest {
	
	@Test
	public void getClassNameTest(){
		String className = ClassUtil.getClassName(ClassUtil.class, false);
		Assert.assertEquals("com.xiaoleilu.hutool.util.ClassUtil", className);
		
		String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		Assert.assertEquals("ClassUtil", simpleClassName);
	}
}
