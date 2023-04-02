package org.dromara.hutool.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

/**
 * test for {@link CombinationAnnotationElement}
 */
public class CombinationAnnotationElementTest {

	@Test
	public void testOf() {
		final CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Assertions.assertNotNull(element);
	}

	@Test
	public void testIsAnnotationPresent() {
		final CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Assertions.assertTrue(element.isAnnotationPresent(MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotation() {
		final AnnotationForTest annotation1 = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		final MetaAnnotationForTest annotation2 = AnnotationForTest.class.getAnnotation(MetaAnnotationForTest.class);
		final CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Assertions.assertEquals(annotation1, element.getAnnotation(AnnotationForTest.class));
		Assertions.assertEquals(annotation2, element.getAnnotation(MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotations() {
		final CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		final Annotation[] annotations = element.getAnnotations();
		Assertions.assertEquals(2, annotations.length);
	}

	@Test
	public void testGetDeclaredAnnotations() {
		final CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		final Annotation[] annotations = element.getDeclaredAnnotations();
		Assertions.assertEquals(2, annotations.length);
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface MetaAnnotationForTest{ }

	@MetaAnnotationForTest
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface AnnotationForTest{ }

	@AnnotationForTest
	private static class ClassForTest{}

}
