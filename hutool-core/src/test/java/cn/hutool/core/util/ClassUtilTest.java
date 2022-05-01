package cn.hutool.core.util;

import cn.hutool.core.reflect.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * {@link ClassUtil} 单元测试
 *
 * @author Looly
 *
 */
public class ClassUtilTest {

	@Test
	public void getClassNameTest() {
		final String className = ClassUtil.getClassName(ClassUtil.class, false);
		Assert.assertEquals("cn.hutool.core.reflect.ClassUtil", className);

		final String simpleClassName = ClassUtil.getClassName(ClassUtil.class, true);
		Assert.assertEquals("ClassUtil", simpleClassName);
	}

	@Test
	public void getClassPathTest() {
		final String classPath = ClassUtil.getClassPath();
		Assert.assertNotNull(classPath);
	}

	@Test
	public void getShortClassNameTest() {
		final String className = "cn.hutool.core.text.StrUtil";
		final String result = ClassUtil.getShortClassName(className);
		Assert.assertEquals("c.h.c.t.StrUtil", result);
	}

	@Test
	public void getLocationPathTest(){
		final String classDir = ClassUtil.getLocationPath(ClassUtilTest.class);
		Assert.assertTrue(Objects.requireNonNull(classDir).endsWith("/hutool-core/target/test-classes/"));
	}
}
