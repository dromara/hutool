package cn.hutool.core.annotation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class AbstractWrappedAnnotationAttributeTest {

	@Test
	public void workTest() {
		Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest1.class);
		Method valueMethod = ReflectUtil.getMethod(AnnotationForTest1.class, "value1");
		CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
		Method nameMethod = ReflectUtil.getMethod(AnnotationForTest1.class, "name1");
		CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
		AbstractWrappedAnnotationAttribute nameWrapper = new TestWrappedAnnotationAttribute(nameAttribute, valueAttribute);

		Assert.assertEquals(nameWrapper.getAnnotation(), annotation);

		// 注解属性
		Assert.assertEquals(annotation, nameWrapper.getAnnotation());
		Assert.assertEquals(annotation.annotationType(), nameWrapper.getAnnotationType());
		Assert.assertEquals(nameAttribute, nameWrapper.getOriginal());
		Assert.assertEquals(valueAttribute, nameWrapper.getLinked());

		// 方法属性
		Assert.assertEquals(nameMethod.getName(), nameWrapper.getAttributeName());
		Assert.assertEquals(nameMethod.getReturnType(), nameWrapper.getAttributeType());
		Assert.assertTrue(nameWrapper.isWrapped());
		Assert.assertEquals("value1", nameWrapper.getValue());
	}

	@Test
	public void multiWrapperTest() {
		// 包装第一层： name1 + value1
		Annotation annotation1 = ClassForTest1.class.getAnnotation(AnnotationForTest1.class);
		Method value1Method = ReflectUtil.getMethod(AnnotationForTest1.class, "value1");
		CacheableAnnotationAttribute value1Attribute = new CacheableAnnotationAttribute(annotation1, value1Method);
		Method name1Method = ReflectUtil.getMethod(AnnotationForTest1.class, "name1");
		CacheableAnnotationAttribute name1Attribute = new CacheableAnnotationAttribute(annotation1, name1Method);
		AbstractWrappedAnnotationAttribute wrapper1 = new TestWrappedAnnotationAttribute(name1Attribute, value1Attribute);
		Assert.assertEquals(name1Attribute, wrapper1.getNonWrappedOriginal());
		Assert.assertEquals(CollUtil.newArrayList(name1Attribute, value1Attribute), wrapper1.getAllLinkedNonWrappedAttributes());

		// 包装第二层：( name1 + value1 ) + value2
		Annotation annotation2 = ClassForTest1.class.getAnnotation(AnnotationForTest2.class);
		Method value2Method = ReflectUtil.getMethod(AnnotationForTest2.class, "value2");
		CacheableAnnotationAttribute value2Attribute = new CacheableAnnotationAttribute(annotation2, value2Method);
		AbstractWrappedAnnotationAttribute wrapper2 = new TestWrappedAnnotationAttribute(wrapper1, value2Attribute);
		Assert.assertEquals(name1Attribute, wrapper2.getNonWrappedOriginal());
		Assert.assertEquals(CollUtil.newArrayList(name1Attribute, value1Attribute, value2Attribute), wrapper2.getAllLinkedNonWrappedAttributes());

		// 包装第二层：value3 + ( ( name1 + value1 ) + value2 )
		Annotation annotation3 = ClassForTest1.class.getAnnotation(AnnotationForTest3.class);
		Method value3Method = ReflectUtil.getMethod(AnnotationForTest3.class, "value3");
		CacheableAnnotationAttribute value3Attribute = new CacheableAnnotationAttribute(annotation3, value3Method);
		AbstractWrappedAnnotationAttribute wrapper3 = new TestWrappedAnnotationAttribute(value3Attribute, wrapper2);
		Assert.assertEquals(value3Attribute, wrapper3.getNonWrappedOriginal());
		Assert.assertEquals(CollUtil.newArrayList(value3Attribute, name1Attribute, value1Attribute, value2Attribute), wrapper3.getAllLinkedNonWrappedAttributes());

	}

	static class TestWrappedAnnotationAttribute extends AbstractWrappedAnnotationAttribute {
		protected TestWrappedAnnotationAttribute(AnnotationAttribute original, AnnotationAttribute linked) {
			super(original, linked);
		}
		@Override
		public Object getValue() {
			return linked.getValue();
		}

		@Override
		public boolean isValueEquivalentToDefaultValue() {
			return getOriginal().isValueEquivalentToDefaultValue() && getLinked().isValueEquivalentToDefaultValue();
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest1 {
		String value1() default "";
		String name1() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest2 {
		String value2() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest3 {
		String value3() default "";
	}

	@AnnotationForTest1(name1 = "name1", value1 = "value1")
	@AnnotationForTest2(value2 = "value2")
	@AnnotationForTest3(value3 = "value3")
	static class ClassForTest1 {}

}
