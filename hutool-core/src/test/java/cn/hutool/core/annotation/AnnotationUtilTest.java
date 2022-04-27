package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;

public class AnnotationUtilTest {

	@Test
	public void getCombinationAnnotationsTest(){
		Annotation[] annotations = AnnotationUtil.getAnnotations(ClassWithAnnotation.class, true);
		Assert.assertNotNull(annotations);
		Assert.assertEquals(3, annotations.length);
	}

	@Test
	public void getCombinationAnnotationsWithClassTest(){
		AnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertNotNull(annotations);
		Assert.assertEquals(2, annotations.length);
		Assert.assertEquals("测试", annotations[0].value());
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
