package cn.hutool.core.annotation;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

/**
 * test for {@link HierarchicalAnnotatedElements}
 *
 * @author huangchengxing
 */
public class HierarchicalAnnotatedElementTest {

	private static final BiFunction<Set<AnnotatedElement>, AnnotatedElement, AnnotatedElement>  ELEMENT_MAPPING_FACTORY = (es, e) -> e;

	@SneakyThrows
	@Test
	public void testCreateFromMethod() {
		Method method1 = Foo.class.getDeclaredMethod("method");
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(method1);
		Assert.assertEquals(3, elements.getElementMappings().size());

		Method method2 = Foo.class.getDeclaredMethod("method2");
		elements = HierarchicalAnnotatedElements.create(method2);
		Assert.assertEquals(1, elements.getElementMappings().size());
	}

	@Test
	public void testCreate() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);
		Assert.assertNotNull(elements);
		Assert.assertEquals(3, elements.getElementMappings().size());

		elements = HierarchicalAnnotatedElements.create(Foo.class, ELEMENT_MAPPING_FACTORY);
		Assert.assertNotNull(elements);
		Assert.assertEquals(3, elements.getElementMappings().size());

		Assert.assertEquals(elements, HierarchicalAnnotatedElements.create(elements, ELEMENT_MAPPING_FACTORY));
	}

	@Test
	public void testEquals() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class, ELEMENT_MAPPING_FACTORY);
		Assert.assertEquals(elements, elements);
		Assert.assertEquals(elements, HierarchicalAnnotatedElements.create(Foo.class, ELEMENT_MAPPING_FACTORY));
		Assert.assertNotEquals(elements, HierarchicalAnnotatedElements.create(Super.class, ELEMENT_MAPPING_FACTORY));
		Assert.assertNotEquals(elements, HierarchicalAnnotatedElements.create(Foo.class, (es, e) -> e));
		Assert.assertNotEquals(elements, null);
	}

	@Test
	public void testHashCode() {
		int hashCode = HierarchicalAnnotatedElements.create(Foo.class, ELEMENT_MAPPING_FACTORY).hashCode();
		Assert.assertEquals(hashCode, HierarchicalAnnotatedElements.create(Foo.class, ELEMENT_MAPPING_FACTORY).hashCode());
		Assert.assertNotEquals(hashCode, HierarchicalAnnotatedElements.create(Super.class, ELEMENT_MAPPING_FACTORY).hashCode());
		Assert.assertNotEquals(hashCode, HierarchicalAnnotatedElements.create(Foo.class, (es, e) -> e).hashCode());
	}

	@Test
	public void testGetElement() {
		AnnotatedElement element = Foo.class;
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(element, ELEMENT_MAPPING_FACTORY);
		Assert.assertSame(element, elements.getElement());
	}

	@Test
	public void testIsAnnotationPresent() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);
		Assert.assertTrue(elements.isAnnotationPresent(Annotation1.class));
		Assert.assertTrue(elements.isAnnotationPresent(Annotation2.class));
		Assert.assertTrue(elements.isAnnotationPresent(Annotation3.class));
	}

	@Test
	public void testGetAnnotations() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Annotation[] annotations = new Annotation[]{ annotation1, annotation2, annotation3 };

		Assert.assertArrayEquals(annotations, elements.getAnnotations());
	}

	@Test
	public void testGetAnnotation() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assert.assertEquals(annotation1, elements.getAnnotation(Annotation1.class));

		Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assert.assertEquals(annotation2, elements.getAnnotation(Annotation2.class));

		Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assert.assertEquals(annotation3, elements.getAnnotation(Annotation3.class));
	}

	@Test
	public void testGetAnnotationsByType() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assert.assertArrayEquals(new Annotation[]{ annotation1 }, elements.getAnnotationsByType(Annotation1.class));

		Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assert.assertArrayEquals(new Annotation[]{ annotation2 }, elements.getAnnotationsByType(Annotation2.class));

		Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assert.assertArrayEquals(new Annotation[]{ annotation3 }, elements.getAnnotationsByType(Annotation3.class));
	}

	@Test
	public void testGetDeclaredAnnotationsByType() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assert.assertArrayEquals(new Annotation[]{ annotation1 }, elements.getDeclaredAnnotationsByType(Annotation1.class));

		Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assert.assertArrayEquals(new Annotation[]{ annotation2 }, elements.getDeclaredAnnotationsByType(Annotation2.class));

		Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assert.assertArrayEquals(new Annotation[]{ annotation3 }, elements.getDeclaredAnnotationsByType(Annotation3.class));
	}

	@Test
	public void testGetDeclaredAnnotation() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Assert.assertEquals(annotation1, elements.getDeclaredAnnotation(Annotation1.class));

		Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Assert.assertEquals(annotation2, elements.getDeclaredAnnotation(Annotation2.class));

		Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Assert.assertEquals(annotation3, elements.getDeclaredAnnotation(Annotation3.class));
	}

	@Test
	public void testGetDeclaredAnnotations() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);

		Annotation1 annotation1 = Foo.class.getAnnotation(Annotation1.class);
		Annotation2 annotation2 = Super.class.getAnnotation(Annotation2.class);
		Annotation3 annotation3 = Interface.class.getAnnotation(Annotation3.class);
		Annotation[] annotations = new Annotation[]{ annotation1, annotation2, annotation3 };

		Assert.assertArrayEquals(annotations, elements.getDeclaredAnnotations());
	}

	@Test
	public void testIterator() {
		HierarchicalAnnotatedElements elements = HierarchicalAnnotatedElements.create(Foo.class);
		Iterator<AnnotatedElement> iterator = elements.iterator();
		Assert.assertNotNull(iterator);

		List<AnnotatedElement> elementList = new ArrayList<>();
		iterator.forEachRemaining(elementList::add);
		Assert.assertEquals(Arrays.asList(Foo.class, Super.class, Interface.class), elementList);
	}

	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 { }

	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 { }

	@Target({ElementType.TYPE_USE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 { }

	@Annotation3
	private interface Interface {
		@Annotation3
		String method();
		@Annotation3
		static String method2() { return null; }
	}

	@Annotation2
	private static class Super {
		@Annotation2
		public String method() { return null; }
		@Annotation2
		public static String method2() { return null; }
	}

	@Annotation1
	private static class Foo extends Super implements Interface {
		@Annotation1
		@Override
		public String method() { return null; }
		@Annotation1
		public static String method2() { return null; }
	};

}
