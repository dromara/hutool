package cn.hutool.core.lang;

import cn.hutool.core.text.StrUtil;
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

	@Test
	public void equalsTest() {
		//String a="ab";
		//final String b = new String("abc");
		final String a = null;
		final String b = null;
		Assert.equals(a, b);
		Assert.equals(a, b, "{}不等于{}", a, b);
		Assert.equals(a, b, () -> new RuntimeException(StrUtil.format("{}和{}不相等", a, b)));
	}

	@Test
	public void notEqualsTest() {
		//String c="19";
		//final String d = new String("19");
		final String c = null;
		final String d = "null";
		//Assert.notEquals(c,d);
		//Assert.notEquals(c,d,"{}等于{}",c,d);
		Assert.notEquals(c, d, () -> new RuntimeException(StrUtil.format("{}和{}相等", c, d)));

	}
}
