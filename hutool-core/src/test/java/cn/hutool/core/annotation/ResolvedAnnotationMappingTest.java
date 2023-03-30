package cn.hutool.core.annotation;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		Assertions.assertNotEquals(null, mapping);
		Assertions.assertEquals(mapping, ResolvedAnnotationMapping.create(annotation, false));
		Assertions.assertNotEquals(mapping, ResolvedAnnotationMapping.create(annotation, true));

		// Annotation3没有需要解析的属性，因此即使在构造函数指定false也一样
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(
			ResolvedAnnotationMapping.create(annotation3, false),
			ResolvedAnnotationMapping.create(annotation3, true)
		);
	}

	@Test
	public void testHashCode() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final int hashCode = ResolvedAnnotationMapping.create(annotation, false).hashCode();
		Assertions.assertEquals(hashCode, ResolvedAnnotationMapping.create(annotation, false).hashCode());
		Assertions.assertNotEquals(hashCode, ResolvedAnnotationMapping.create(annotation, true).hashCode());

		// Annotation3没有需要解析的属性，因此即使在构造函数指定false也一样
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertEquals(
			ResolvedAnnotationMapping.create(annotation3, false).hashCode(),
			ResolvedAnnotationMapping.create(annotation3, true).hashCode()
		);
	}


	@Test
	public void testCreate() {
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, false);
		Assertions.assertNotNull(mapping3);

		final Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		final ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, false);
		Assertions.assertNotNull(mapping2);

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(mapping2, annotation1, false);
		Assertions.assertNotNull(mapping1);
	}

	@Test
	public void testIsRoot() {
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, false);
		Assertions.assertTrue(mapping3.isRoot());

		final Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		final ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, false);
		Assertions.assertFalse(mapping2.isRoot());
	}

	@Test
	public void testGetRoot() {
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, false);
		Assertions.assertSame(mapping3, mapping3.getRoot());

		final Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		final ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, false);
		Assertions.assertSame(mapping3, mapping2.getRoot());

		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(mapping2, annotation1, false);
		Assertions.assertSame(mapping3, mapping1.getRoot());
	}

	@Test
	public void testGetAnnotation() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		Assertions.assertSame(annotation, mapping.getAnnotation());
	}

	@SneakyThrows
	@Test
	public void testGetAttributes() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		for (int i = 0; i < mapping.getAttributes().length; i++) {
			final Method method = mapping.getAttributes()[i];
			Assertions.assertEquals(Annotation1.class.getDeclaredMethod(method.getName()), method);
		}
	}

	@Test
	public void testHasAttribute() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);

		Assertions.assertTrue(mapping.hasAttribute("value", String.class));
		Assertions.assertFalse(mapping.hasAttribute("value", Integer.class));

		final int index = mapping.getAttributeIndex("value", String.class);
		Assertions.assertTrue(mapping.hasAttribute(index));
		Assertions.assertFalse(mapping.hasAttribute(Integer.MIN_VALUE));
	}

	@Test
	public void testAnnotationType() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		Assertions.assertEquals(annotation.annotationType(), mapping.annotationType());
	}

	@Test
	public void testIsResolved() {
		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);

		final ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(annotation1, true);
		Assertions.assertTrue(mapping1.isResolved());
		Assertions.assertFalse(ResolvedAnnotationMapping.create(annotation1, false).isResolved());

		final Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(annotation2, true);
		Assertions.assertFalse(mapping2.isResolved());

		mapping2 = ResolvedAnnotationMapping.create(mapping1, annotation2, true);
		Assertions.assertTrue(mapping2.isResolved());
	}

	@Test
	public void testGetAttributeIndex() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);
		for (int i = 0; i < mapping.getAttributes().length; i++) {
			final Method method = mapping.getAttributes()[i];
			Assertions.assertEquals(i, mapping.getAttributeIndex(method.getName(), method.getReturnType()));
		}
		Assertions.assertEquals(ResolvedAnnotationMapping.NOT_FOUND_INDEX, mapping.getAttributeIndex("value", Void.class));
		Assertions.assertEquals(ResolvedAnnotationMapping.NOT_FOUND_INDEX, mapping.getAttributeIndex("nonexistent", Void.class));
	}

	@Test
	public void testGetAttributeValue() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, false);

		Assertions.assertNull(mapping.getAttribute(Integer.MAX_VALUE));

		final int valueIdx = mapping.getAttributeIndex("value", String.class);
		Assertions.assertEquals(annotation.value(), mapping.getAttributeValue(valueIdx));
		Assertions.assertEquals(annotation.value(), mapping.getAttributeValue("value", String.class));

		final int name1Idx = mapping.getAttributeIndex("value1", String.class);
		Assertions.assertEquals(annotation.value1(), mapping.getAttributeValue(name1Idx));
		Assertions.assertEquals(annotation.value1(), mapping.getAttributeValue("value1", String.class));

		final int name2Idx = mapping.getAttributeIndex("value2", String.class);
		Assertions.assertEquals(annotation.value2(), mapping.getAttributeValue(name2Idx));
		Assertions.assertEquals(annotation.value2(), mapping.getAttributeValue("value2", String.class));
	}

	@Test
	public void testGetResolvedAnnotation() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, true);
		final Annotation1 synthesis = (Annotation1)mapping.getResolvedAnnotation();

		Assertions.assertEquals(annotation.annotationType(), synthesis.annotationType());
		Assertions.assertEquals(annotation.value(), synthesis.value());
		Assertions.assertEquals(annotation.value(), synthesis.value1());
		Assertions.assertEquals(annotation.value(), synthesis.value2());

		Assertions.assertTrue(AnnotationMappingProxy.isProxied(synthesis));
		Assertions.assertSame(mapping, ((AnnotationMappingProxy.Proxied)synthesis).getMapping());

		Assertions.assertNotEquals(synthesis, annotation);
		Assertions.assertNotEquals(synthesis.hashCode(), annotation.hashCode());
		Assertions.assertNotEquals(synthesis.toString(), annotation.toString());

		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Assertions.assertSame(annotation3, ResolvedAnnotationMapping.create(annotation3, true).getResolvedAnnotation());
	}

	// ======================= resolved attribute value =======================

	@Test
	public void testGetResolvedAttributeValueWhenAliased() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping = ResolvedAnnotationMapping.create(annotation, true);
		Assertions.assertNull(mapping.getResolvedAttributeValue(Integer.MIN_VALUE));

		// value = value1 = value2
		Assertions.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value", String.class));
		Assertions.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value1", String.class));
		Assertions.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value2", String.class));

		// alias == alias1 == alias2
		Assertions.assertEquals(annotation.alias(), mapping.getResolvedAttributeValue("alias", String.class));
		Assertions.assertEquals(annotation.alias(), mapping.getResolvedAttributeValue("alias1", String.class));
		Assertions.assertEquals(annotation.alias(), mapping.getResolvedAttributeValue("alias2", String.class));

		// defVal1 == defVal2
		Assertions.assertEquals(
			mapping.getResolvedAttributeValue("defVal", String.class),
			mapping.getResolvedAttributeValue("defVal2", String.class)
		);

		// unDefVal1 == unDefVal2
		Assertions.assertEquals(
			mapping.getResolvedAttributeValue("unDefVal", String.class),
			mapping.getResolvedAttributeValue("unDefVal2", String.class)
		);
	}

	@Test
	public void testGetResolvedAttributeWhenOverwritten() {
		final Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		final ResolvedAnnotationMapping mapping3 = ResolvedAnnotationMapping.create(annotation3, true);
		Assertions.assertEquals(annotation3.value(), mapping3.getResolvedAttributeValue("value", String.class));
		Assertions.assertEquals((Integer)annotation3.alias(), mapping3.getResolvedAttributeValue("alias", Integer.class));

		// annotation2中与annotation3同名同类型的属性value、alias被覆写
		final Annotation2 annotation2 = Foo.class.getAnnotation(Annotation2.class);
		final ResolvedAnnotationMapping mapping2 = ResolvedAnnotationMapping.create(mapping3, annotation2, true);
		Assertions.assertEquals(annotation3.value(), mapping2.getResolvedAttributeValue("value", String.class));
		Assertions.assertEquals((Integer)annotation3.alias(), mapping2.getResolvedAttributeValue("alias", Integer.class));

		// annotation1中与annotation3同名同类型的属性value被覆写，由于value存在别名value1，value2因此也一并被覆写
		final Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		final ResolvedAnnotationMapping mapping1 = ResolvedAnnotationMapping.create(mapping2, annotation1, true);
		Assertions.assertEquals(annotation3.value(), mapping1.getResolvedAttributeValue("value", String.class));
		Assertions.assertEquals(annotation3.value(), mapping1.getResolvedAttributeValue("value1", String.class));
		Assertions.assertEquals(annotation3.value(), mapping1.getResolvedAttributeValue("value2", String.class));
		// 而alias由于类型不同不会被覆写
		Assertions.assertEquals(annotation1.alias(), mapping1.getResolvedAttributeValue("alias", String.class));
	}

	@SuppressWarnings("unused")
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
	private static class Foo {}

}
