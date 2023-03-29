package cn.hutool.core.annotation;

import cn.hutool.core.array.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * test for {@link AnnotationUtil}
 */
public class AnnotationUtilTest {

	@Test
	public void testGetDeclaredAnnotations() {
		final Annotation[] annotations = AnnotationUtil.getDeclaredAnnotations(ClassForTest.class);
		Assert.assertArrayEquals(annotations, ClassForTest.class.getDeclaredAnnotations());
		Assert.assertSame(annotations, AnnotationUtil.getDeclaredAnnotations(ClassForTest.class));

		AnnotationUtil.clearCaches();
		Assert.assertNotSame(annotations, AnnotationUtil.getDeclaredAnnotations(ClassForTest.class));
	}

	@Test
	public void testToCombination() {
		final CombinationAnnotationElement element = AnnotationUtil.toCombination(ClassForTest.class);
		Assert.assertEquals(2, element.getAnnotations().length);
	}

	@Test
	public void testGetAnnotations() {
		Annotation[] annotations = AnnotationUtil.getAnnotations(ClassForTest.class, true);
		Assert.assertEquals(2, annotations.length);
		annotations = AnnotationUtil.getAnnotations(ClassForTest.class, false);
		Assert.assertEquals(1, annotations.length);
	}

	@Test
	public void testGetCombinationAnnotations() {
		final MetaAnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassForTest.class, MetaAnnotationForTest.class);
		Assert.assertEquals(1, annotations.length);
	}

	@Test
	public void testAnnotations() {
		MetaAnnotationForTest[] annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, false, MetaAnnotationForTest.class);
		Assert.assertEquals(0, annotations1.length);
		annotations1 = AnnotationUtil.getAnnotations(ClassForTest.class, true, MetaAnnotationForTest.class);
		Assert.assertEquals(1, annotations1.length);

		Annotation[] annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, false, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		Assert.assertEquals(0, annotations2.length);
		annotations2 = AnnotationUtil.getAnnotations(
			ClassForTest.class, true, t -> ObjUtil.equals(t.annotationType(), MetaAnnotationForTest.class)
		);
		Assert.assertEquals(1, annotations2.length);
	}

	@Test
	public void testGetAnnotation() {
		final MetaAnnotationForTest annotation = AnnotationUtil.getAnnotation(ClassForTest.class, MetaAnnotationForTest.class);
		Assert.assertNotNull(annotation);
	}

	@Test
	public void testHasAnnotation() {
		Assert.assertTrue(AnnotationUtil.hasAnnotation(ClassForTest.class, MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotationValue() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		Assert.assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class));
		Assert.assertEquals(annotation.value(), AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "value"));
		Assert.assertNull(AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest.class, "property"));
	}

	@Test
	public void testGetAnnotationValueMap() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final Map<String, Object> valueMap = AnnotationUtil.getAnnotationValueMap(ClassForTest.class, AnnotationForTest.class);
		Assert.assertNotNull(valueMap);
		Assert.assertEquals(2, valueMap.size());
		Assert.assertEquals(annotation.value(), valueMap.get("value"));
	}

	@Test
	public void testGetRetentionPolicy() {
		final RetentionPolicy policy = AnnotationForTest.class.getAnnotation(Retention.class).value();
		Assert.assertEquals(policy, AnnotationUtil.getRetentionPolicy(AnnotationForTest.class));
	}

	@Test
	public void testGetTargetType() {
		final ElementType[] types = AnnotationForTest.class.getAnnotation(Target.class).value();
		Assert.assertArrayEquals(types, AnnotationUtil.getTargetType(AnnotationForTest.class));
	}

	@Test
	public void testIsDocumented() {
		Assert.assertFalse(AnnotationUtil.isDocumented(AnnotationForTest.class));
	}

	@Test
	public void testIsInherited() {
		Assert.assertFalse(AnnotationUtil.isInherited(AnnotationForTest.class));
	}

	@Test
	public void testSetValue() {
		final AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final String newValue = "is a new value";
		Assert.assertNotEquals(newValue, annotation.value());
		AnnotationUtil.setValue(annotation, "value", newValue);
		Assert.assertEquals(newValue, annotation.value());
	}

	@Test
	public void testGetAnnotationAlias() {
		final MetaAnnotationForTest annotation = AnnotationUtil.getAnnotationAlias(AnnotationForTest.class, MetaAnnotationForTest.class);
		Assert.assertEquals(annotation.value(), annotation.alias());
		Assert.assertEquals(MetaAnnotationForTest.class, annotation.annotationType());
	}

	@Test
	public void testGetAnnotationAttributes() {
		final Method[] methods = AnnotationUtil.getAnnotationAttributes(AnnotationForTest.class);
		Assert.assertArrayEquals(methods, AnnotationUtil.getAnnotationAttributes(AnnotationForTest.class));
		Assert.assertEquals(2, methods.length);
		Assert.assertArrayEquals(AnnotationForTest.class.getDeclaredMethods(), methods);
	}

	@SneakyThrows
	@Test
	public void testIsAnnotationAttribute() {
		Assert.assertFalse(AnnotationUtil.isAnnotationAttribute(AnnotationForTest.class.getMethod("equals", Object.class)));
		Assert.assertTrue(AnnotationUtil.isAnnotationAttribute(AnnotationForTest.class.getMethod("value")));
	}

	@Test
	public void getAnnotationValueTest2() {
		final String[] names = AnnotationUtil.getAnnotationValue(ClassForTest.class, AnnotationForTest::names);
		Assert.assertTrue(ArrayUtil.equals(names, new String[]{"测试1", "测试2"}));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface MetaAnnotationForTest{
		@Alias(value = "alias")
		String value() default "";
		String alias() default "";
	}

	@MetaAnnotationForTest("foo")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface AnnotationForTest{
		String value() default "";
		String[] names() default "";
	}

	@AnnotationForTest(value = "foo", names = {"测试1", "测试2"})
	private static class ClassForTest{}

}
