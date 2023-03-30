package cn.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicTypeTest {

	@Test
	public void wrapTest(){
		Assertions.assertEquals(Integer.class, BasicType.wrap(int.class));
		Assertions.assertEquals(Integer.class, BasicType.wrap(Integer.class));
		Assertions.assertEquals(String.class, BasicType.wrap(String.class));
		Assertions.assertNull(BasicType.wrap(null));
	}

	@Test
	public void unWrapTest(){
		Assertions.assertEquals(int.class, BasicType.unWrap(int.class));
		Assertions.assertEquals(int.class, BasicType.unWrap(Integer.class));
		Assertions.assertEquals(String.class, BasicType.unWrap(String.class));
		Assertions.assertNull(BasicType.unWrap(null));
	}

	@Test
	public void getPrimitiveSetTest(){
		Assertions.assertEquals(8, BasicType.getPrimitiveSet().size());
	}

	@Test
	public void getWrapperSetTest(){
		Assertions.assertEquals(8, BasicType.getWrapperSet().size());
	}
}
