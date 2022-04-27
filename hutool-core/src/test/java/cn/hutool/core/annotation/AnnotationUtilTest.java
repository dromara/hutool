package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

public class AnnotationUtilTest {

	@Test
	public void getAnnotationValueTest() {
		Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertEquals("测试", value);

	}

	@Test
	public void getAnnotationSyncAlias() {
		// 直接获取
		Assert.assertEquals("", ClassWithAnnotation.class.getAnnotation(AnnotationForTest.class).retry());

		// 加别名适配
		AnnotationForTest annotation = AnnotationUtil.getAnnotationAlias(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertEquals("测试", annotation.retry());
	}

	@AnnotationForTest("测试")
	static class ClassWithAnnotation{
		public void test(){

		}
	}
}
