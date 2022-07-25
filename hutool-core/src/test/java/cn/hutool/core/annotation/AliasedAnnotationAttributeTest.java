package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class AliasedAnnotationAttributeTest {

	@Test
	public void baseInfoTest() {
		// 组合属性
		final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
		final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
		final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		final AliasedAnnotationAttribute valueAnnotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

		// 注解属性
		Assert.assertEquals(annotation, valueAnnotationAttribute.getAnnotation());
		Assert.assertEquals(annotation.annotationType(), valueAnnotationAttribute.getAnnotationType());

		// 方法属性
		Assert.assertEquals(valueMethod.getAnnotation(Alias.class), valueAnnotationAttribute.getAnnotation(Alias.class));
		Assert.assertEquals(valueMethod.getName(), valueAnnotationAttribute.getAttributeName());
		Assert.assertEquals(nameMethod.getReturnType(), valueAnnotationAttribute.getAttributeType());
	}

	@Test
	public void workWhenValueDefaultTest() {
		// 组合属性
		final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
		final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
		final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		final AliasedAnnotationAttribute annotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

		// 值处理
		Assert.assertEquals("name", annotationAttribute.getValue());
		Assert.assertFalse(annotationAttribute.isValueEquivalentToDefaultValue());
		Assert.assertTrue(annotationAttribute.isWrapped());
	}

	@Test
	public void workWhenValueNonDefaultTest() {
		// 组合属性
		final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
		final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
		final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		final AliasedAnnotationAttribute annotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

		// 值处理
		Assert.assertEquals("value", annotationAttribute.getValue());
		Assert.assertFalse(annotationAttribute.isValueEquivalentToDefaultValue());
		Assert.assertTrue(annotationAttribute.isWrapped());
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest {
		@Alias("value")
		String value() default "";
		String name() default "";
	}

	@AnnotationForTest(name = "name", value = "value")
	static class ClassForTest1 {}

	@AnnotationForTest(value = "value")
	static class ClassForTest2 {}

}
