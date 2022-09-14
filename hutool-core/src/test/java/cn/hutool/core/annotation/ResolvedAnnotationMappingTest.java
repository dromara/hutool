package cn.hutool.core.annotation;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * test for {@link ResolvedAnnotationMapping}
 *
 * @author huangchengxing
 */
public class ResolvedAnnotationMappingTest {

	@Test
	public void testEquals() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		Assert.assertEquals(mapping, mapping);
		Assert.assertNotEquals(null, mapping);
		Assert.assertEquals(mapping, ResolvedAnnotationMapping.create(annotation, false));
		Assert.assertNotEquals(mapping, ResolvedAnnotationMapping.create(annotation, true));

		// Annotation3没有需要解析的属性，因此即使在构造函数指定false也一样
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assert.assertEquals(
			ResolvedAnnotationMapping.create(annotation3, false),
			ResolvedAnnotationMapping.create(annotation3, true)
		);
	}

	@Test
	public void testHashCode() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		int hashCode = ResolvedAnnotationMapping.create(annotation, false).hashCode();
		Assert.assertEquals(hashCode, ResolvedAnnotationMapping.create(annotation, false).hashCode());
		Assert.assertNotEquals(hashCode, ResolvedAnnotationMapping.create(annotation, true).hashCode());

		// Annotation3没有需要解析的属性，因此即使在构造函数指定false也一样
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assert.assertEquals(
			ResolvedAnnotationMapping.create(annotation3, false).hashCode(),
			ResolvedAnnotationMapping.create(annotation3, true).hashCode()
		);
	}


	@Test
	public void testCreate() {
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, false);
		Assert.assertNotNull(mapping3);

		Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, false);
		Assert.assertNotNull(mapping2);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(mapping2, annotation1, false);
		Assert.assertNotNull(mapping1);
	}

	@Test
	public void testIsRoot() {
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, false);
		Assert.assertTrue(mapping3.isRoot());

		Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, false);
		Assert.assertFalse(mapping2.isRoot());
	}

	@Test
	public void testGetRoot() {
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, false);
		Assert.assertSame(mapping3, mapping3.getRoot());

		Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, false);
		Assert.assertSame(mapping3, mapping2.getRoot());

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(mapping2, annotation1, false);
		Assert.assertSame(mapping3, mapping1.getRoot());
	}

	@Test
	public void testGetAnnotation() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		Assert.assertSame(annotation, mapping.getAnnotation());
	}

	@SneakyThrows
	@Test
	public void testGetAttributes() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		for (int i = 0; i < mapping.getAttributes().length; i++) {
			Method method = mapping.getAttributes()[i];
			Assert.assertEquals(Annotation1.class.getDeclaredMethod(method.getName()), method);
		}
	}

	@Test
	public void testHasAttribute() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);

		Assert.assertTrue(mapping.hasAttribute("value", String.class));
		Assert.assertFalse(mapping.hasAttribute("value", Integer.class));

		int index = mapping.getAttributeIndex("value", String.class);
		Assert.assertTrue(mapping.hasAttribute(index));
		Assert.assertFalse(mapping.hasAttribute(Integer.MIN_VALUE));
	}

	@Test
	public void testAnnotationType() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		Assert.assertEquals(annotation.annotationType(), mapping.annotationType());
	}

	@Test
	public void testIsResolved() {
		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);

		ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(annotation1, true);
		Assert.assertTrue(mapping1.isResolved());
		Assert.assertFalse(ResolvedAnnotationMapping.create(annotation1, false).isResolved());

		Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(annotation2, true);
		Assert.assertFalse(mapping2.isResolved());

		mapping2 = ResolvedAnnotationMapping.create(mapping1, annotation2, true);
		Assert.assertTrue(mapping2.isResolved());
	}

	@Test
	public void testGetAttributeIndex() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		for (int i = 0; i < mapping.getAttributes().length; i++) {
			Method method = mapping.getAttributes()[i];
			Assert.assertEquals(i, mapping.getAttributeIndex(method.getName(), method.getReturnType()));
		}
		Assert.assertEquals(ResolvedAnnotationMapping.NOT_FOUND_INDEX, mapping.getAttributeIndex("value", Void.class));
		Assert.assertEquals(ResolvedAnnotationMapping.NOT_FOUND_INDEX, mapping.getAttributeIndex("nonexistent", Void.class));
	}

	@Test
	public void testGetAttributeValue() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);

		Assert.assertNull(mapping.getAttribute(Integer.MAX_VALUE));

		int valueIdx = mapping.getAttributeIndex("value", String.class);
		Assert.assertEquals(annotation.value(), mapping.getAttributeValue(valueIdx));
		Assert.assertEquals(annotation.value(), mapping.getAttributeValue("value", String.class));

		int name1Idx = mapping.getAttributeIndex("value1", String.class);
		Assert.assertEquals(annotation.value1(), mapping.getAttributeValue(name1Idx));
		Assert.assertEquals(annotation.value1(), mapping.getAttributeValue("value1", String.class));

		int name2Idx = mapping.getAttributeIndex("value2", String.class);
		Assert.assertEquals(annotation.value2(), mapping.getAttributeValue(name2Idx));
		Assert.assertEquals(annotation.value2(), mapping.getAttributeValue("value2", String.class));
	}

	@Test
	public void testGetResolvedAnnotation() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, true);
		Annotation1 synthesis = (Annotation1)mapping.getResolvedAnnotation();

		Assert.assertEquals(annotation.annotationType(), synthesis.annotationType());
		Assert.assertEquals(annotation.value(), synthesis.value());
		Assert.assertEquals(annotation.value(), synthesis.value1());
		Assert.assertEquals(annotation.value(), synthesis.value2());

		Assert.assertTrue(AnnotationMappingProxy.isProxied(synthesis));
		Assert.assertSame(mapping, ((AnnotationMappingProxy.Proxied)synthesis).getMapping());

		Assert.assertNotEquals(synthesis, annotation);
		Assert.assertNotEquals(synthesis.hashCode(), annotation.hashCode());
		Assert.assertNotEquals(synthesis.toString(), annotation.toString());

		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assert.assertSame(annotation3, ResolvedAnnotationMapping.create(annotation3, true).getResolvedAnnotation());
	}

	// ======================= resolved attribute value =======================

	@Test
	public void testGetResolvedAttributeValueWhenAliased() {
		Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, true);
		Assert.assertNull(mapping.getResolvedAttributeValue(Integer.MIN_VALUE));

		// value = value1 = value2
		Assert.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value", String.class));
		Assert.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value1", String.class));
		Assert.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value2", String.class));

		// alias == alias1 == alias2
		Assert.assertEquals(annotation.alias(), mapping.getResolvedAttributeValue("alias", String.class));
		Assert.assertEquals(annotation.alias(), mapping.getResolvedAttributeValue("alias1", String.class));
		Assert.assertEquals(annotation.alias(), mapping.getResolvedAttributeValue("alias2", String.class));

		// defVal1 == defVal2
		Assert.assertEquals(
			mapping.getResolvedAttributeValue("defVal", String.class),
			mapping.getResolvedAttributeValue("defVal2", String.class)
		);

		// unDefVal1 == unDefVal2
		Assert.assertEquals(
			mapping.getResolvedAttributeValue("unDefVal", String.class),
			mapping.getResolvedAttributeValue("unDefVal2", String.class)
		);
	}

	@Test
	public void testGetResolvedAttributeWhenOverwritten() {
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, true);
		Assert.assertEquals(annotation3.value(), mapping3.getResolvedAttributeValue("value", String.class));
		Assert.assertEquals((Integer)annotation3.alias(), mapping3.getResolvedAttributeValue("alias", Integer.class));

		// annotation2中与annotation3同名同类型的属性value、alias被覆写
		Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, true);
		Assert.assertEquals(annotation3.value(), mapping2.getResolvedAttributeValue("value", String.class));
		Assert.assertEquals((Integer)annotation3.alias(), mapping2.getResolvedAttributeValue("alias", Integer.class));

		// annotation1中与annotation3同名同类型的属性value被覆写，由于value存在别名value1，value2因此也一并被覆写
		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(mapping2, annotation1, true);
		Assert.assertEquals(annotation3.value(), mapping1.getResolvedAttributeValue("value", String.class));
		Assert.assertEquals(annotation3.value(), mapping1.getResolvedAttributeValue("value1", String.class));
		Assert.assertEquals(annotation3.value(), mapping1.getResolvedAttributeValue("value2", String.class));
		// 而alias由于类型不同不会被覆写
		Assert.assertEquals(annotation1.alias(), mapping1.getResolvedAttributeValue("alias", String.class));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		@Alias("value1")
		String value() default "";
		String value1() default "";
		@Alias("value")
		String value2() default "";

		@Alias("alias2")
		String alias() default "";
		@Alias("alias2")
		String alias1() default "";
		@Alias("alias1")
		String alias2() default "";

		@Alias("defVal2")
		String defVal() default "";
		String defVal2() default "";

		@Alias("unDefVal2")
		String unDefVal() default "";
		String unDefVal2() default "";
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 {
		String value() default "";
		int alias() default 123;
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 {
		String value() default "";
		int alias() default 123;
	}

	@Annotation3(value = "Annotation3", alias = 312)
	@Annotation2(value = "Annotation2")
	@Annotation1(value = "Annotation1", alias = "goo", unDefVal = "foo", unDefVal2 = "foo")
	private static class Foo {};

}
