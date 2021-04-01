package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilTest {

	@Test
	public void getAnnotationValueTest() {
		Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertEquals("测试", value);

	}

	@AnnotationForTest("测试")
	static class ClassWithAnnotation{
		public void test(){

		}
	}
}
