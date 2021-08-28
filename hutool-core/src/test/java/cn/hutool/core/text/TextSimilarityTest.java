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
		String a = "我是一个文本，独一无二的文本";
		String b = "一个文本，独一无二的文本";

		double degree = TextSimilarity.similar(a, b);
		Assert.assertEquals(0.8571428571428571D, degree, 16);

		String similarPercent = TextSimilarity.similar(a, b, 2);
		Assert.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarDegreeTest2() {
		String a = "我是一个文本，独一无二的文本";
		String b = "一个文本，独一无二的文本,#,>>?#$%^%$&^&^%";

		double degree = TextSimilarity.similar(a, b);
		Assert.assertEquals(0.8571428571428571D, degree, 16);

		String similarPercent = TextSimilarity.similar(a, b, 2);
		Assert.assertEquals("84.62%", similarPercent);
	}

	@Test
	public void similarTest(){
		final double abd = TextSimilarity.similar("abd", "1111");
		Assert.assertEquals(0, abd, 1);
	}
}
