package cn.hutool.core.lang;

import org.junit.Test;

public class AssertTest {

	@Test
	public void isNullTest(){
		final String a = null;
		Assert.isNull(a);
	}
	@Test
	public void notNullTest(){
		final String a = null;
		Assert.isNull(a);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isTrueTest() {
		final int i = 0;
		//noinspection ConstantConditions
		Assert.isTrue(i > 0, IllegalArgumentException::new);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void isTrueTest2() {
		final int i = -1;
		//noinspection ConstantConditions
		Assert.isTrue(i >= 0, IndexOutOfBoundsException::new);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void isTrueTest3() {
		final int i = -1;
		//noinspection ConstantConditions
		Assert.isTrue(i > 0, ()-> new IndexOutOfBoundsException("relation message to return"));
	}
}
