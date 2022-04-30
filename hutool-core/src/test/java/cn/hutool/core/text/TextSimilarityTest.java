package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

/**
 * 文本相似度计算工具类单元测试
 * @author looly
 *
 */
public class TextSimilarityTest {

	@Test
	public void similarDegreeTest() {
		final String a = "我是一个文本，独一无二的文本";
		final String b = "一个文本，独一无二的文本";

		final double degree = TextSimilarity.similar(a, b);
		Assert.assertEquals(0.8571428571428571D, degree, 16);

		final String similarPercent = TextSimilarity.similar(a, b, 2);
		Assert.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarDegreeTest2() {
		final String a = "我是一个文本，独一无二的文本";
		final String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

		final double degree = TextSimilarity.similar(a, b);
		Assert.assertEquals(0.8571428571428571D, degree, 16);

		final String similarPercent = TextSimilarity.similar(a, b, 2);
		Assert.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarTest(){
		final double abd = TextSimilarity.similar("abd", "1111");
		Assert.assertEquals(0, abd, 1);
	}
}
