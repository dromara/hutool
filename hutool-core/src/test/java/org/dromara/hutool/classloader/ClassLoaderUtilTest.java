package org.dromara.hutool.classloader;

import org.dromara.hutool.map.Dict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassLoaderUtilTest {

	@Test
	public void isPresentTest() {
		final boolean present = ClassLoaderUtil.isPresent("classloader.org.dromara.hutool.ClassLoaderUtil");
		Assertions.assertTrue(present);
	}

	@Test
	public void loadClassTest() {
		String name = ClassLoaderUtil.loadClass("java.lang.Thread.State").getName();
		Assertions.assertEquals("java.lang.Thread$State", name);

		name = ClassLoaderUtil.loadClass("java.lang.Thread$State").getName();
		Assertions.assertEquals("java.lang.Thread$State", name);
	}

	@Test
	public void loadArrayClassTest(){
		final String s = Dict[].class.getName();

		final Class<Object> objectClass = ClassLoaderUtil.loadClass(s);
		Assertions.assertEquals(Dict[].class, objectClass);
	}

	@Test
	public void loadInnerClassTest() {
		String name = ClassLoaderUtil.loadClass("classloader.org.dromara.hutool.ClassLoaderUtilTest.A").getName();
		Assertions.assertEquals("classloader.org.dromara.hutool.ClassLoaderUtilTest$A", name);
		name = ClassLoaderUtil.loadClass("classloader.org.dromara.hutool.ClassLoaderUtilTest.A.B").getName();
		Assertions.assertEquals("classloader.org.dromara.hutool.ClassLoaderUtilTest$A$B", name);
	}

	@SuppressWarnings("unused")
	private static class A{
		private static class B{

		}
	}
}
