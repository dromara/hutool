package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

public class BasicTypeTest {

	@Test
	public void wrapTest(){
		Assert.assertEquals(Integer.class, BasicType.wrap(int.class));
		Assert.assertEquals(Integer.class, BasicType.wrap(Integer.class));
		Assert.assertEquals(String.class, BasicType.wrap(String.class));
		Assert.assertNull(BasicType.wrap(null));
	}

	@Test
	public void unWrapTest(){
		Assert.assertEquals(int.class, BasicType.unWrap(int.class));
		Assert.assertEquals(int.class, BasicType.unWrap(Integer.class));
		Assert.assertEquals(String.class, BasicType.unWrap(String.class));
		Assert.assertNull(BasicType.unWrap(null));
	}

	@Test
	public void getPrimitiveSetTest(){
		Assert.assertEquals(8, BasicType.getPrimitiveSet().size());
	}

	@Test
	public void getWrapperSetTest(){
		Assert.assertEquals(8, BasicType.getWrapperSet().size());
	}
}
