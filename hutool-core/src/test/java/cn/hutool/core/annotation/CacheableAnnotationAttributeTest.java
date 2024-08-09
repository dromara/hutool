package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class CacheableAnnotationAttributeTest {

	@Test
	public void baseInfoTest() {
		final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
		final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);
		// 注解属性
		assertEquals(annotation, annotationAttribute.getAnnotation());
		assertEquals(annotation.annotationType(), annotationAttribute.getAnnotationType());
		// 方法属性
		assertEquals(attribute.getName(), annotationAttribute.getAttributeName());
		assertEquals(attribute.getReturnType(), annotationAttribute.getAttributeType());
	}

	@Test
	public void workWhenValueDefaultTest() {
		final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
		final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);

		// 值处理
		assertEquals("", annotationAttribute.getValue());
		assertTrue(annotationAttribute.isValueEquivalentToDefaultValue());
		assertFalse(annotationAttribute.isWrapped());
	}

	@Test
	public void workWhenValueNonDefaultTest() {
		final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
		final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
		final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);

		// 值处理
		assertEquals("test", annotationAttribute.getValue());
		assertFalse(annotationAttribute.isValueEquivalentToDefaultValue());
		assertFalse(annotationAttribute.isWrapped());
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest {
		String value() default "";
	}

	@AnnotationForTest("")
	static class ClassForTest1 {}

	@AnnotationForTest("test")
	static class ClassForTest2 {}

}
