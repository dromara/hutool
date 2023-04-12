package org.dromara.hutool.core.text;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 文本相似度计算工具类单元测试
 *
 * @author looly
 */
public class TextSimilarityTest {

	@Test
	public void similarDegreeTest() {
		final String a = "我是一个文本，独一无二的文本";
		final String b = "一个文本，独一无二的文本";

		final double degree = TextSimilarity.similar(a, b);
		Assertions.assertEquals(0.8461538462D, degree, 0.0000000001);

		final String similarPercent = TextSimilarity.similar(a, b, 2);
		Assertions.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarDegreeTest2() {
		final String a = "我是一个文本，独一无二的文本";
		final String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

		final double degree = TextSimilarity.similar(a, b);
		Assertions.assertEquals(0.8461538462D, degree, 0.0001);

		final String similarPercent = TextSimilarity.similar(a, b, 2);
		Assertions.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarTest() {
		final double abd = TextSimilarity.similar("abd", "1111");
		Assertions.assertEquals(0, abd, 1);
	}

	@Test
	@Disabled
	void longestCommonSubstringLengthTest() {
		// https://github.com/dromara/hutool/issues/3045
		final String strCommon = RandomUtil.randomString(1024 * 32);
		final String strA = RandomUtil.randomString(1024 * 32) + strCommon;
		final String strB = RandomUtil.randomString(1024 * 32) + strCommon;

		final int i = TextSimilarity.longestCommonSubstringLength(strA, strB);
		Console.log(i);
	}
}
