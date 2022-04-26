package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class AnnotationUtilTest {
	@Test
	public void getRepeatAnnotationValueTest(){
		Set<AnnotationForTest> annotations = AnnotationUtil.getRepeatedAnnotations(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertTrue(annotations != null && annotations.size() == 2);
	}

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
	@RepeatAnnotationForTest
	static class ClassWithAnnotation{
		public void test(){

		}
	}
}
