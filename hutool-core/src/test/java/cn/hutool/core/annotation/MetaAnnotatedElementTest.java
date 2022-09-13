package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

/**
 * test for {@link MetaAnnotatedElement}
 *
 * @author huangchengxing
 */
public class MetaAnnotatedElementTest {

	private static final BiFunction<ResolvedAnnotationMapping, Annotation, ResolvedAnnotationMapping> RESOLVED_MAPPING_FACTORY =
		(source, annotation) -> new ResolvedAnnotationMapping(source, annotation, true);

	private static final BiFunction<ResolvedAnnotationMapping, Annotation, ResolvedAnnotationMapping> MAPPING_FACTORY =
		(source, annotation) -> new ResolvedAnnotationMapping(source, annotation, false);

	@Test
	public void testEquals() {
		AnnotatedElement element = new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY);
		Assert.assertEquals(element, element);
		Assert.assertNotEquals(element, null);
		Assert.assertEquals(element, new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY));
		Assert.assertNotEquals(element, new MetaAnnotatedElement<>(Foo.class, MAPPING_FACTORY));
		Assert.assertNotEquals(element, new MetaAnnotatedElement<>(Annotation1.class, MAPPING_FACTORY));
	}

	@Test
	public void testHashCode() {
		int hashCode = new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY).hashCode();
		Assert.assertEquals(hashCode, new MetaAnnotatedElement<>(Foo.class, RESOLVED_MAPPING_FACTORY).hashCode());
		Assert.assertNotEquals(hashCode, new MetaAnnotatedElement<>(Foo.class, MAPPING_FACTORY).hashCode());
	}

	@Test
	public void testCreate() {
		// 第二次创建时优先从缓存中获取
		AnnotatedElement resolvedElement = MetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY);
		Assert.assertEquals(resolvedElement, MetaAnnotatedElement.create(Foo.class, RESOLVED_MAPPING_FACTORY));
		AnnotatedElement element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assert.assertEquals(element, MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY));
	}

	@Test
	public void testGetMapping() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assert.assertTrue(element.getMapping(Annotation1.class).isPresent());
		Assert.assertTrue(element.getMapping(Annotation2.class).isPresent());
		Assert.assertTrue(element.getMapping(Annotation3.class).isPresent());
		Assert.assertTrue(element.getMapping(Annotation4.class).isPresent());
	}

	@Test
	public void testGetDeclaredMapping() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assert.assertFalse(element.getDeclaredMapping(Annotation1.class).isPresent());
		Assert.assertFalse(element.getDeclaredMapping(Annotation2.class).isPresent());
		Assert.assertTrue(element.getDeclaredMapping(Annotation3.class).isPresent());
		Assert.assertTrue(element.getDeclaredMapping(Annotation4.class).isPresent());
	}

	@Test
	public void testIsAnnotationPresent() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Assert.assertTrue(element.isAnnotationPresent(Annotation1.class));
		Assert.assertTrue(element.isAnnotationPresent(Annotation2.class));
		Assert.assertTrue(element.isAnnotationPresent(Annotation3.class));
		Assert.assertTrue(element.isAnnotationPresent(Annotation4.class));
	}

	@Test
	public void testGetAnnotation() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Annotation2 annotation2 = Annotation3.class.getAnnotation(Annotation2.class);
		Annotation1 annotation1 = Annotation2.class.getAnnotation(Annotation1.class);

		Assert.assertEquals(annotation1, element.getAnnotation(Annotation1.class));
		Assert.assertEquals(annotation2, element.getAnnotation(Annotation2.class));
		Assert.assertEquals(annotation3, element.getAnnotation(Annotation3.class));
		Assert.assertEquals(annotation4, element.getAnnotation(Annotation4.class));
	}

	@Test
	public void testGetDeclaredAnnotation() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);

		Assert.assertNull(element.getDeclaredAnnotation(Annotation1.class));
		Assert.assertNull(element.getDeclaredAnnotation(Annotation2.class));
		Assert.assertEquals(annotation3, element.getDeclaredAnnotation(Annotation3.class));
		Assert.assertEquals(annotation4, element.getDeclaredAnnotation(Annotation4.class));
	}

	@Test
	public void testGetAnnotationByType() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Assert.assertArrayEquals(
			new Annotation[]{ annotation4 },
			element.getAnnotationsByType(Annotation4.class)
		);
	}

	@Test
	public void testGetDeclaredAnnotationByType() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Assert.assertArrayEquals(
			new Annotation[]{ annotation4 },
			element.getDeclaredAnnotationsByType(Annotation4.class)
		);
	}

	@Test
	public void testGetAnnotations() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Annotation2 annotation2 = Annotation3.class.getAnnotation(Annotation2.class);
		Annotation1 annotation1 = Annotation2.class.getAnnotation(Annotation1.class);
		Annotation[] annotations = new Annotation[]{ annotation3, annotation4, annotation2, annotation1 };

		Assert.assertArrayEquals(annotations, element.getAnnotations());
	}

	@Test
	public void testGetDeclaredAnnotations() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Annotation[] annotations = new Annotation[]{ annotation3, annotation4 };

		Assert.assertArrayEquals(annotations, element.getDeclaredAnnotations());
	}

	@Test
	public void testIterator() {
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(Foo.class, MAPPING_FACTORY);
		Annotation4 annotation4 = Foo.class.getAnnotation(Annotation4.class);
		Annotation3 annotation3 = Foo.class.getAnnotation(Annotation3.class);
		Annotation2 annotation2 = Annotation3.class.getAnnotation(Annotation2.class);
		Annotation1 annotation1 = Annotation2.class.getAnnotation(Annotation1.class);
		Annotation[] annotations = new Annotation[]{ annotation3, annotation4, annotation2, annotation1 };

		Iterator<ResolvedAnnotationMapping> iterator = element.iterator();
		List<Annotation> mappingList = new ArrayList<>();
		iterator.forEachRemaining(mapping -> mappingList.add(mapping.getAnnotation()));

		Assert.assertEquals(Arrays.asList(annotations), mappingList);
	}

	@Test
	public void testGetElement() {
		AnnotatedElement source = Foo.class;
		MetaAnnotatedElement<ResolvedAnnotationMapping> element = MetaAnnotatedElement.create(source, MAPPING_FACTORY);
		Assert.assertSame(source, element.getElement());
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		@Alias("name")
		String value() default "";
		@Alias("value")
		String name() default "";
	}

	@Annotation1("Annotation2")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 {
		@Alias("name")
		String value() default "";
		@Alias("value")
		String name() default "";
	}

	@Annotation2("Annotation3")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 {
		String name() default "";
	}

	@Annotation2("Annotation4")
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation4 {
		String value() default "";
	}

	@Annotation3(name = "foo")
	@Annotation4("foo")
	private static class Foo {};

}
