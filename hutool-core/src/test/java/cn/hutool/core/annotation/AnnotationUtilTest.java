package cn.hutool.core.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationUtilTest {

	@Test
	public void getAnnotationValueTest() {
		Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class);
		Assertions.assertEquals("测试", value);

	}

	@AnnotationForTest("测试")
	static class ClassWithAnnotation{
		public void test(){

		}
	}
}
