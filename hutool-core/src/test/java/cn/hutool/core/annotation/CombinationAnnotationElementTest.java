package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;

/**
 * test for {@link CombinationAnnotationElement}
 */
public class CombinationAnnotationElementTest {

	@Test
	public void testOf() {
		CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Assert.assertNotNull(element);
	}

	@Test
	public void testIsAnnotationPresent() {
		CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Assert.assertTrue(element.isAnnotationPresent(MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotation() {
		AnnotationForTest annotation1 = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		MetaAnnotationForTest annotation2 = AnnotationForTest.class.getAnnotation(MetaAnnotationForTest.class);
		CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Assert.assertEquals(annotation1, element.getAnnotation(AnnotationForTest.class));
		Assert.assertEquals(annotation2, element.getAnnotation(MetaAnnotationForTest.class));
	}

	@Test
	public void testGetAnnotations() {
		CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Annotation[] annotations = element.getAnnotations();
		Assert.assertEquals(2, annotations.length);
	}

	@Test
	public void testGetDeclaredAnnotations() {
		CombinationAnnotationElement element = CombinationAnnotationElement.of(ClassForTest.class, a -> true);
		Annotation[] annotations = element.getDeclaredAnnotations();
		Assert.assertEquals(2, annotations.length);
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
