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
 * test for {@link GenericAnnotationMapping}
 *
 * @author huangchengxing
 */
public class GenericAnnotationMappingTest {

	@Test
	public void testEquals() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertEquals(mapping, mapping);
		Assert.assertNotEquals(mapping, null);
		Assert.assertEquals(mapping, GenericAnnotationMapping.create(annotation, false));
		Assert.assertNotEquals(mapping, GenericAnnotationMapping.create(annotation, true));
	}

	@Test
	public void testHashCode() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final int hashCode = GenericAnnotationMapping.create(annotation, false).hashCode();
		Assert.assertEquals(hashCode, GenericAnnotationMapping.create(annotation, false).hashCode());
		Assert.assertNotEquals(hashCode, GenericAnnotationMapping.create(annotation, true).hashCode());
	}


	@Test
	public void testCreate() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertNotNull(mapping);
	}

	@Test
	public void testIsRoot() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, true);
		Assert.assertTrue(mapping.isRoot());

		mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertFalse(mapping.isRoot());
	}

	@Test
	public void testGetAnnotation() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertSame(annotation, mapping.getAnnotation());
	}

	@SneakyThrows
	@Test
	public void testGetAttributes() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		for (int i = 0; i < mapping.getAttributes().length; i++) {
			final Method method = mapping.getAttributes()[i];
			Assert.assertEquals(Annotation1.class.getDeclaredMethod(method.getName()), method);
		}
	}

	@Test
	public void testAnnotationType() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertEquals(annotation.annotationType(), mapping.annotationType());
	}

	@Test
	public void testIsResolved() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertFalse(mapping.isResolved());
	}

	@Test
	public void testGetAttributeValue() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertEquals(annotation.value(), mapping.getAttributeValue("value", String.class));
		Assert.assertNull(mapping.getAttributeValue("value", Integer.class));
	}

	@Test
	public void testGetResolvedAnnotation() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertSame(annotation, mapping.getResolvedAnnotation());
	}

	@Test
	public void testGetResolvedAttributeValue() {
		final Annotation1 annotation = Foo.class.getAnnotation(Annotation1.class);
		final GenericAnnotationMapping mapping = GenericAnnotationMapping.create(annotation, false);
		Assert.assertEquals(annotation.value(), mapping.getResolvedAttributeValue("value", String.class));
		Assert.assertNull(mapping.getResolvedAttributeValue("value", Integer.class));
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		String value() default "";
	}

	@Annotation1("foo")
	private static class Foo {}

}
