package cn.hutool.core.util;

import cn.hutool.core.date.DateException;
import org.junit.Assert;
import org.junit.Test;

/**
 * 字符串链式工具类单元测试
 *
 * @author van
 */
public class StrFunctionUtilTest {

	@Test
	public void thenTest() {
		final String abc = "van";
		final String[] a = new String[1];
		StrFunctionUtil.of(abc).then(str -> {
			a[0] = "3";
			Assert.assertEquals("van", str);
		});
		Assert.assertEquals("3", a[0]);
	}

	@Test
	public void ifBlankThenTest() {
		final String abc = "";
		final String[] a = new String[1];
		StrFunctionUtil.of(abc).then(() -> a[0] = "notBlank")
				.ifBlankThen(() -> a[0] = "blank");
		Assert.assertEquals("blank", a[0]);
	}

	@Test
	public void ifNullThenTest() {
		final String[] a = new String[1];
		StrFunctionUtil.of(null).then(() -> a[0] = "notNull").
				ifNullThen(() -> a[0] = "null");
		Assert.assertEquals("null", a[0]);
	}

	@Test
	public void blankThrowTest() {
		final String abc = "";
		Assert.assertThrows(DateException.class,
				() -> StrFunctionUtil.of(abc).blankThrow(new DateException("van")));
	}

	@Test
	public void nullThrowTest() {
		Assert.assertThrows(DateException.class,
				() -> StrFunctionUtil.of(null).nullThrow(new DateException("van")));
	}

	@Test
	public void blankReturnTest() {
		final String abc = "";
		final String edf = StrFunctionUtil.of(abc).blankReturn("edf");
		Assert.assertEquals("edf", edf);
	}

	@Test
	public void nullReturnTest() {
		final String edf = StrFunctionUtil.of(null).nullReturn("edf");
		Assert.assertEquals("edf", edf);
	}

}
