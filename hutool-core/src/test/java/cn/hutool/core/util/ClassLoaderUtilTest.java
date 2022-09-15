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
}
