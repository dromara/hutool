package cn.hutool.core.util;

import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.map.Dict;
import org.junit.Assert;
import org.junit.Test;

public class ClassLoaderUtilTest {

	@Test
	public void loadClassTest() {
		String name = ClassLoaderUtil.loadClass("java.lang.Thread.State").getName();
		Assert.assertEquals("java.lang.Thread$State", name);

		name = ClassLoaderUtil.loadClass("java.lang.Thread$State").getName();
		Assert.assertEquals("java.lang.Thread$State", name);
	}

	@Test
	public void loadArrayClassTest(){
		final String s = Dict[].class.getName();

		final Class<Object> objectClass = ClassLoaderUtil.loadClass(s);
		Assert.assertEquals(Dict[].class, objectClass);
	}

	@Test
	public void loadInnerClassTest() {
		String name = ClassLoaderUtil.loadClass("cn.hutool.core.util.ClassLoaderUtilTest.A").getName();
		Assert.assertEquals("cn.hutool.core.util.ClassLoaderUtilTest$A", name);
		name = ClassLoaderUtil.loadClass("cn.hutool.core.util.ClassLoaderUtilTest.A.B").getName();
		Assert.assertEquals("cn.hutool.core.util.ClassLoaderUtilTest$A$B", name);
	}

	private static class A{
		private static class B{

		}
	}
}
