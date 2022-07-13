package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class MirroredAnnotationAttributeTest {

	@Test
	public void baseInfoTest() {
		// 组合属性
		final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
		final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
		final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

		// 注解属性
		Assert.assertEquals(annotation, nameAnnotationAttribute.getAnnotation());
		Assert.assertEquals(annotation.annotationType(), nameAnnotationAttribute.getAnnotationType());

		// 方法属性
		Assert.assertEquals(nameMethod.getName(), nameAnnotationAttribute.getAttributeName());
		Assert.assertEquals(nameMethod.getReturnType(), nameAnnotationAttribute.getAttributeType());
	}

	@Test
	public void workWhenValueDefaultTest() {
		// 组合属性
		final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
		final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
		final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

		// 值处理
		Assert.assertEquals("", nameAnnotationAttribute.getValue());
		Assert.assertTrue(nameAnnotationAttribute.isValueEquivalentToDefaultValue());
		Assert.assertTrue(nameAnnotationAttribute.isWrapped());
	}

	@Test
	public void workWhenValueNonDefaultTest() {
		// 组合属性
		final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
		final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
		final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

		// 值处理
		Assert.assertEquals("name", nameAnnotationAttribute.getValue());
		Assert.assertFalse(nameAnnotationAttribute.isValueEquivalentToDefaultValue());
		Assert.assertTrue(nameAnnotationAttribute.isWrapped());
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest {
		String value() default "";
		String name() default "";
	}

	@AnnotationForTest(value = "name")
	static class ClassForTest1 {}

	@AnnotationForTest
	static class ClassForTest2 {}

}
