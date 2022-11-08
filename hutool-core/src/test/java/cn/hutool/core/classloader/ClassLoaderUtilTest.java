package cn.hutool.core.classloader;

import cn.hutool.core.map.Dict;
import org.junit.Assert;
import org.junit.Test;

public class ClassLoaderUtilTest {

	@Test
	public void isPresentTest() {
		final boolean present = ClassLoaderUtil.isPresent("cn.hutool.core.classloader.ClassLoaderUtil");
		Assert.assertTrue(present);
	}

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
		String name = ClassLoaderUtil.loadClass("cn.hutool.core.classloader.ClassLoaderUtilTest.A").getName();
		Assert.assertEquals("cn.hutool.core.classloader.ClassLoaderUtilTest$A", name);
		name = ClassLoaderUtil.loadClass("cn.hutool.core.classloader.ClassLoaderUtilTest.A.B").getName();
		Assert.assertEquals("cn.hutool.core.classloader.ClassLoaderUtilTest$A$B", name);
	}

	@SuppressWarnings("unused")
	private static class A{
		private static class B{

		}
	}
}
