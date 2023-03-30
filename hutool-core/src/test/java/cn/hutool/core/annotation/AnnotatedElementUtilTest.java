package cn.hutool.core.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test for {@link AnnotatedElementUtil}
 *
 * @author huangchengxing
 */
public class AnnotatedElementUtilTest {

	private final static Annotation3 ANNOTATION3 = Foo.class.getAnnotation(Annotation3.class); // Foo.class's annotations
	private final static Annotation2 ANNOTATION2 = Annotation3.class.getAnnotation(Annotation2.class);
	private final static Annotation1 ANNOTATION1 = Annotation2.class.getAnnotation(Annotation1.class);

	private final static Annotation4 ANNOTATION4 = Super.class.getAnnotation(Annotation4.class); // Super.class's annotations

	private final static Annotation6 ANNOTATION6 = Interface.class.getAnnotation(Annotation6.class); // Interface.class's annotations
	private final static Annotation5 ANNOTATION5 = Annotation6.class.getAnnotation(Annotation5.class);

	private final static Annotation[] DECLARED_ANNOTATIONS = new Annotation[]{
		ANNOTATION3, // Foo.class's annotations
		ANNOTATION4, // Super.class's annotations
		ANNOTATION6  // Interface.class's annotations
	};
	private final static Annotation[] ANNOTATIONS = new Annotation[]{
		ANNOTATION3, ANNOTATION2, ANNOTATION1, // Foo.class's annotations
		ANNOTATION4,                           // Super.class's annotations
		ANNOTATION6, ANNOTATION5               // Interface.class's annotations
	};

	@Test
	public void testClearCaches() {
		final AnnotatedElement type = Foo.class;

		AnnotatedElement element = AnnotatedElementUtil.getResolvedMetaElementCache(type);
		Assertions.assertSame(element, AnnotatedElementUtil.getResolvedMetaElementCache(type));
		AnnotatedElementUtil.clearCaches();
		Assertions.assertNotSame(element, AnnotatedElementUtil.getResolvedMetaElementCache(type));

		element = AnnotatedElementUtil.getMetaElementCache(type);
		Assertions.assertSame(element, AnnotatedElementUtil.getMetaElementCache(type));
		AnnotatedElementUtil.clearCaches();
		Assertions.assertNotSame(element, AnnotatedElementUtil.getMetaElementCache(type));

		element = AnnotatedElementUtil.getResolvedRepeatableMetaElementCache(type);
		Assertions.assertSame(element, AnnotatedElementUtil.getResolvedRepeatableMetaElementCache(type));
		AnnotatedElementUtil.clearCaches();
		Assertions.assertNotSame(element, AnnotatedElementUtil.getResolvedRepeatableMetaElementCache(type));

		element = AnnotatedElementUtil.getRepeatableMetaElementCache(type);
		Assertions.assertSame(element, AnnotatedElementUtil.getRepeatableMetaElementCache(type));
		AnnotatedElementUtil.clearCaches();
		Assertions.assertNotSame(element, AnnotatedElementUtil.getRepeatableMetaElementCache(type));
	}

	@Test
	public void testIsAnnotated() {
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotated(Foo.class, Annotation1.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotated(Foo.class, Annotation2.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotated(Foo.class, Annotation3.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotated(Foo.class, Annotation4.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotated(Foo.class, Annotation5.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotated(Foo.class, Annotation6.class));
	}

	@Test
	public void testFindAnnotation() {
		Assertions.assertEquals(ANNOTATION1, AnnotatedElementUtil.findAnnotation(Foo.class, Annotation1.class));
		Assertions.assertEquals(ANNOTATION2, AnnotatedElementUtil.findAnnotation(Foo.class, Annotation2.class));
		Assertions.assertEquals(ANNOTATION3, AnnotatedElementUtil.findAnnotation(Foo.class, Annotation3.class));
		Assertions.assertEquals(ANNOTATION4, AnnotatedElementUtil.findAnnotation(Foo.class, Annotation4.class));
		Assertions.assertEquals(ANNOTATION5, AnnotatedElementUtil.findAnnotation(Foo.class, Annotation5.class));
		Assertions.assertEquals(ANNOTATION6, AnnotatedElementUtil.findAnnotation(Foo.class, Annotation6.class));
	}

	@Test
	public void testFindAllAnnotations() {
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION1}, AnnotatedElementUtil.findAllAnnotations(Foo.class, Annotation1.class));
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION2}, AnnotatedElementUtil.findAllAnnotations(Foo.class, Annotation2.class));
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION3}, AnnotatedElementUtil.findAllAnnotations(Foo.class, Annotation3.class));
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION4}, AnnotatedElementUtil.findAllAnnotations(Foo.class, Annotation4.class));
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION5}, AnnotatedElementUtil.findAllAnnotations(Foo.class, Annotation5.class));
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION6}, AnnotatedElementUtil.findAllAnnotations(Foo.class, Annotation6.class));
	}

	@Test
	public void testFindAnnotations() {
		final Annotation[] annotations = AnnotatedElementUtil.findAnnotations(Foo.class);
		Assertions.assertArrayEquals(ANNOTATIONS, annotations);
	}

	@Test
	public void testFindResolvedAnnotation() {
		final Annotation3 resolvedAnnotation3 = AnnotatedElementUtil.findResolvedAnnotation(Foo.class, Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = AnnotatedElementUtil.findResolvedAnnotation(Foo.class, Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = AnnotatedElementUtil.findResolvedAnnotation(Foo.class, Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertEquals(ANNOTATION4, AnnotatedElementUtil.findResolvedAnnotation(Foo.class, Annotation4.class));
		Assertions.assertEquals(ANNOTATION6, AnnotatedElementUtil.findResolvedAnnotation(Foo.class, Annotation6.class));
		Assertions.assertEquals(ANNOTATION5, AnnotatedElementUtil.findResolvedAnnotation(Foo.class, Annotation5.class));
	}

	@Test
	public void testFindResolvedAnnotations() {
		final Annotation[] resolvedAnnotations = AnnotatedElementUtil.findResolvedAnnotations(Foo.class);
		final Map<Class<?>, Annotation> annotationMap = Stream.of(resolvedAnnotations).collect(Collectors.toMap(Annotation::annotationType, Function.identity()));

		final Annotation3 resolvedAnnotation3 = (Annotation3)annotationMap.get(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = (Annotation2)annotationMap.get(Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = (Annotation1)annotationMap.get(Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertEquals(ANNOTATION4, annotationMap.get(Annotation4.class));
		Assertions.assertEquals(ANNOTATION6, annotationMap.get(Annotation6.class));
		Assertions.assertEquals(ANNOTATION5, annotationMap.get(Annotation5.class));
	}

	@Test
	public void testFindAllResolvedAnnotations() {
		final Annotation3 resolvedAnnotation3 = AnnotatedElementUtil.findAllResolvedAnnotations(Foo.class, Annotation3.class)[0];
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = AnnotatedElementUtil.findAllResolvedAnnotations(Foo.class, Annotation2.class)[0];
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = AnnotatedElementUtil.findAllResolvedAnnotations(Foo.class, Annotation1.class)[0];
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertEquals(ANNOTATION4, AnnotatedElementUtil.findAllResolvedAnnotations(Foo.class, Annotation4.class)[0]);
		Assertions.assertEquals(ANNOTATION6, AnnotatedElementUtil.findAllResolvedAnnotations(Foo.class, Annotation6.class)[0]);
		Assertions.assertEquals(ANNOTATION5, AnnotatedElementUtil.findAllResolvedAnnotations(Foo.class, Annotation5.class)[0]);
	}

	@Test
	public void testFindDirectlyAnnotation() {
		Assertions.assertNull(AnnotatedElementUtil.findDirectlyAnnotation(Foo.class, Annotation1.class));
		Assertions.assertNull(AnnotatedElementUtil.findDirectlyAnnotation(Foo.class, Annotation2.class));
		Assertions.assertEquals(ANNOTATION3, AnnotatedElementUtil.findDirectlyAnnotation(Foo.class, Annotation3.class));
		Assertions.assertEquals(ANNOTATION4, AnnotatedElementUtil.findDirectlyAnnotation(Foo.class, Annotation4.class));
		Assertions.assertNull(AnnotatedElementUtil.findDirectlyAnnotation(Foo.class, Annotation5.class));
		Assertions.assertEquals(ANNOTATION6, AnnotatedElementUtil.findDirectlyAnnotation(Foo.class, Annotation6.class));
	}

	@Test
	public void testFindAllDirectlyAnnotations() {
		Assertions.assertEquals(0, AnnotatedElementUtil.findAllDirectlyAnnotations(Foo.class, Annotation1.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.findAllDirectlyAnnotations(Foo.class, Annotation2.class).length);
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION3}, AnnotatedElementUtil.findAllDirectlyAnnotations(Foo.class, Annotation3.class));
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION4}, AnnotatedElementUtil.findAllDirectlyAnnotations(Foo.class, Annotation4.class));
		Assertions.assertEquals(0, AnnotatedElementUtil.findAllDirectlyAnnotations(Foo.class, Annotation5.class).length);
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION6}, AnnotatedElementUtil.findAllDirectlyAnnotations(Foo.class, Annotation6.class));
	}

	@Test
	public void testFindDirectlyAnnotations() {
		Assertions.assertArrayEquals(
			DECLARED_ANNOTATIONS, AnnotatedElementUtil.findDirectlyAnnotations(Foo.class)
		);
	}

	@Test
	public void testFindDirectlyResolvedAnnotation() {
		Assertions.assertEquals(ANNOTATION4, AnnotatedElementUtil.findDirectlyResolvedAnnotation(Foo.class, Annotation4.class));
		Assertions.assertEquals(ANNOTATION6, AnnotatedElementUtil.findDirectlyResolvedAnnotation(Foo.class, Annotation6.class));
		final Annotation3 resolvedAnnotation3 = AnnotatedElementUtil.findDirectlyResolvedAnnotation(Foo.class, Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		Assertions.assertNull(AnnotatedElementUtil.findDirectlyResolvedAnnotation(Foo.class, Annotation1.class));
		Assertions.assertNull(AnnotatedElementUtil.findDirectlyResolvedAnnotation(Foo.class, Annotation2.class));
		Assertions.assertNull(AnnotatedElementUtil.findDirectlyResolvedAnnotation(Foo.class, Annotation5.class));
	}

	@Test
	public void testFindDirectlyResolvedAnnotations() {
		final Annotation[] resolvedAnnotations = AnnotatedElementUtil.findDirectlyResolvedAnnotations(Foo.class);
		final Map<Class<?>, Annotation> annotationMap = Stream.of(resolvedAnnotations).collect(Collectors.toMap(Annotation::annotationType, Function.identity()));

		Assertions.assertEquals(ANNOTATION4, annotationMap.get(Annotation4.class));
		Assertions.assertEquals(ANNOTATION6, annotationMap.get(Annotation6.class));
		final Annotation3 resolvedAnnotation3 = (Annotation3)annotationMap.get(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		Assertions.assertNull(annotationMap.get(Annotation1.class));
		Assertions.assertNull(annotationMap.get(Annotation2.class));
		Assertions.assertNull(annotationMap.get(Annotation5.class));
	}

	@Test
	public void testFindAllDirectlyResolvedAnnotations() {

		Assertions.assertEquals(ANNOTATION4, AnnotatedElementUtil.findAllDirectlyResolvedAnnotations(Foo.class, Annotation4.class)[0]);
		Assertions.assertEquals(ANNOTATION6, AnnotatedElementUtil.findAllDirectlyResolvedAnnotations(Foo.class, Annotation6.class)[0]);
		final Annotation3 resolvedAnnotation3 = AnnotatedElementUtil.findAllDirectlyResolvedAnnotations(Foo.class, Annotation3.class)[0];
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		Assertions.assertEquals(0, AnnotatedElementUtil.findAllDirectlyResolvedAnnotations(Foo.class, Annotation1.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.findAllDirectlyResolvedAnnotations(Foo.class, Annotation2.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.findAllDirectlyResolvedAnnotations(Foo.class, Annotation5.class).length);
	}

	@Test
	public void testIsAnnotationPresent() {
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotationPresent(Foo.class, Annotation1.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotationPresent(Foo.class, Annotation2.class));
		Assertions.assertTrue(AnnotatedElementUtil.isAnnotationPresent(Foo.class, Annotation3.class));

		Assertions.assertFalse(AnnotatedElementUtil.isAnnotationPresent(Foo.class, Annotation4.class));
		Assertions.assertFalse(AnnotatedElementUtil.isAnnotationPresent(Foo.class, Annotation5.class));
		Assertions.assertFalse(AnnotatedElementUtil.isAnnotationPresent(Foo.class, Annotation6.class));
	}

	@Test
	public void testGetAnnotation() {
		Assertions.assertEquals(ANNOTATION1, AnnotatedElementUtil.getAnnotation(Foo.class, Annotation1.class));
		Assertions.assertEquals(ANNOTATION2, AnnotatedElementUtil.getAnnotation(Foo.class, Annotation2.class));
		Assertions.assertEquals(ANNOTATION3, AnnotatedElementUtil.getAnnotation(Foo.class, Annotation3.class));

		Assertions.assertNull(AnnotatedElementUtil.getAnnotation(Foo.class, Annotation4.class));
		Assertions.assertNull(AnnotatedElementUtil.getAnnotation(Foo.class, Annotation5.class));
		Assertions.assertNull(AnnotatedElementUtil.getAnnotation(Foo.class, Annotation6.class));
	}

	@Test
	public void testGetAnnotations() {
		final Annotation[] annotations = AnnotatedElementUtil.getAnnotations(Foo.class);
		Assertions.assertArrayEquals(
			new Annotation[]{ ANNOTATION3, ANNOTATION2, ANNOTATION1 },
			annotations
		);
	}

	@Test
	public void testGetAllAnnotations() {
		final Annotation3[] resolvedAnnotation3s = AnnotatedElementUtil.getAllAnnotations(Foo.class, Annotation3.class);
		Assertions.assertEquals(1, resolvedAnnotation3s.length);
		Assertions.assertEquals(ANNOTATION3, resolvedAnnotation3s[0]); // value与alias互为别名

		final Annotation2[] resolvedAnnotation2s = AnnotatedElementUtil.getAllAnnotations(Foo.class, Annotation2.class);
		Assertions.assertEquals(1, resolvedAnnotation2s.length);
		Assertions.assertEquals(ANNOTATION2, resolvedAnnotation2s[0]); // value与alias互为别名

		final Annotation1[] resolvedAnnotation1s = AnnotatedElementUtil.getAllAnnotations(Foo.class, Annotation1.class);
		Assertions.assertEquals(1, resolvedAnnotation1s.length);
		Assertions.assertEquals(ANNOTATION1, resolvedAnnotation1s[0]);

		Assertions.assertEquals(0, AnnotatedElementUtil.getAllAnnotations(Foo.class, Annotation4.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllAnnotations(Foo.class, Annotation5.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllAnnotations(Foo.class, Annotation6.class).length);
	}

	@Test
	public void testGetResolvedAnnotation() {
		final Annotation3 resolvedAnnotation3 = AnnotatedElementUtil.getResolvedAnnotation(Foo.class, Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = AnnotatedElementUtil.getResolvedAnnotation(Foo.class, Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = AnnotatedElementUtil.getResolvedAnnotation(Foo.class, Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertNull(AnnotatedElementUtil.getResolvedAnnotation(Foo.class, Annotation4.class));
		Assertions.assertNull(AnnotatedElementUtil.getResolvedAnnotation(Foo.class, Annotation5.class));
		Assertions.assertNull(AnnotatedElementUtil.getResolvedAnnotation(Foo.class, Annotation6.class));
	}

	@Test
	public void testGetAllResolvedAnnotations() {
		final Annotation3[] resolvedAnnotation3s = AnnotatedElementUtil.getAllResolvedAnnotations(Foo.class, Annotation3.class);
		Assertions.assertEquals(1, resolvedAnnotation3s.length);
		final Annotation3 resolvedAnnotation3 = resolvedAnnotation3s[0];
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2[] resolvedAnnotation2s = AnnotatedElementUtil.getAllResolvedAnnotations(Foo.class, Annotation2.class);
		Assertions.assertEquals(1, resolvedAnnotation2s.length);
		final Annotation2 resolvedAnnotation2 = resolvedAnnotation2s[0];
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1[] resolvedAnnotation1s = AnnotatedElementUtil.getAllResolvedAnnotations(Foo.class, Annotation1.class);
		Assertions.assertEquals(1, resolvedAnnotation1s.length);
		final Annotation1 resolvedAnnotation1 = resolvedAnnotation1s[0];
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertEquals(0, AnnotatedElementUtil.getAllResolvedAnnotations(Foo.class, Annotation4.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllResolvedAnnotations(Foo.class, Annotation5.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllResolvedAnnotations(Foo.class, Annotation6.class).length);
	}

	@Test
	public void testGetResolvedAnnotations() {
		final Map<Class<?>, Annotation> annotationMap = Stream.of(AnnotatedElementUtil.getResolvedAnnotations(Foo.class))
			.collect(Collectors.toMap(Annotation::annotationType, Function.identity()));

		final Annotation3 resolvedAnnotation3 = (Annotation3)annotationMap.get(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = (Annotation2)annotationMap.get(Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = (Annotation1)annotationMap.get(Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertFalse(annotationMap.containsKey(Annotation4.class));
		Assertions.assertFalse(annotationMap.containsKey(Annotation5.class));
		Assertions.assertFalse(annotationMap.containsKey(Annotation6.class));
	}

	@Test
	public void testGetDirectlyAnnotation() {
		Assertions.assertEquals(ANNOTATION3, AnnotatedElementUtil.getDirectlyAnnotation(Foo.class, Annotation3.class));

		Assertions.assertNull(AnnotatedElementUtil.getDirectlyAnnotation(Foo.class, Annotation2.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyAnnotation(Foo.class, Annotation1.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyAnnotation(Foo.class, Annotation4.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyAnnotation(Foo.class, Annotation5.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyAnnotation(Foo.class, Annotation6.class));
	}

	@Test
	public void testGetAllDirectlyAnnotations() {
		final Annotation3[] resolvedAnnotation3s = AnnotatedElementUtil.getAllDirectlyAnnotations(Foo.class, Annotation3.class);
		Assertions.assertEquals(1, resolvedAnnotation3s.length);
		final Annotation3 resolvedAnnotation3 = resolvedAnnotation3s[0];
		Assertions.assertEquals(ANNOTATION3, resolvedAnnotation3);

		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation2.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation1.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation4.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation5.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation6.class).length);
	}

	@Test
	public void testGetDirectlyAnnotations() {
		final Annotation[] annotations = AnnotatedElementUtil.getDirectlyAnnotations(Foo.class);
		Assertions.assertEquals(1, annotations.length);
		Assertions.assertEquals(ANNOTATION3, annotations[0]);
	}

	@Test
	public void testGetDirectlyResolvedAnnotation() {
		final Annotation3 resolvedAnnotation3 = AnnotatedElementUtil.getDirectlyResolvedAnnotation(Foo.class, Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		Assertions.assertNull(AnnotatedElementUtil.getDirectlyResolvedAnnotation(Foo.class, Annotation2.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyResolvedAnnotation(Foo.class, Annotation1.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyResolvedAnnotation(Foo.class, Annotation4.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyResolvedAnnotation(Foo.class, Annotation5.class));
		Assertions.assertNull(AnnotatedElementUtil.getDirectlyResolvedAnnotation(Foo.class, Annotation6.class));
	}

	@Test
	public void testGetDirectlyAllResolvedAnnotations() {
		final Annotation3[] resolvedAnnotation3s = AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation3.class);
		Assertions.assertEquals(1, resolvedAnnotation3s.length);
		final Annotation3 resolvedAnnotation3 = resolvedAnnotation3s[0];
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation2.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation1.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation4.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation5.class).length);
		Assertions.assertEquals(0, AnnotatedElementUtil.getAllDirectlyResolvedAnnotations(Foo.class, Annotation6.class).length);
	}

	@Test
	public void testGetDirectlyResolvedAnnotations() {
		final Annotation[] annotations = AnnotatedElementUtil.getDirectlyResolvedAnnotations(Foo.class);
		Assertions.assertEquals(1, annotations.length);

		final Annotation3 resolvedAnnotation3 = (Annotation3)annotations[0];
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名
	}

	@Test
	public void testToHierarchyMetaElement() {
		Assertions.assertNotNull(AnnotatedElementUtil.toHierarchyMetaElement(null, false));
		Assertions.assertNotNull(AnnotatedElementUtil.toHierarchyMetaElement(null, true));
		final AnnotatedElement element = AnnotatedElementUtil.toHierarchyMetaElement(Foo.class, false);

		// 带有元注解
		Assertions.assertArrayEquals(ANNOTATIONS, element.getAnnotations());

		// 不带元注解
		Assertions.assertArrayEquals(DECLARED_ANNOTATIONS, element.getDeclaredAnnotations());

		// 解析注解属性
		final AnnotatedElement resolvedElement = AnnotatedElementUtil.toHierarchyMetaElement(Foo.class, true);
		final Annotation3 resolvedAnnotation3 = resolvedElement.getAnnotation(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = resolvedElement.getAnnotation(Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = resolvedElement.getAnnotation(Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertEquals(ANNOTATION4, resolvedElement.getAnnotation(Annotation4.class));
		Assertions.assertEquals(ANNOTATION6, resolvedElement.getAnnotation(Annotation6.class));
		Assertions.assertEquals(ANNOTATION5, resolvedElement.getAnnotation(Annotation5.class));
	}

	@Test
	public void testToHierarchyRepeatableMetaElement() {
		Assertions.assertNotNull(AnnotatedElementUtil.toHierarchyRepeatableMetaElement(null, false));
		Assertions.assertNotNull(AnnotatedElementUtil.toHierarchyRepeatableMetaElement(null, true));
		final AnnotatedElement element = AnnotatedElementUtil.toHierarchyRepeatableMetaElement(Foo.class, false);

		// 带有元注解
		Assertions.assertArrayEquals(ANNOTATIONS, element.getAnnotations());

		// 不带元注解
		Assertions.assertArrayEquals(DECLARED_ANNOTATIONS, element.getDeclaredAnnotations());

		// 解析注解属性
		final AnnotatedElement resolvedElement = AnnotatedElementUtil.toHierarchyRepeatableMetaElement(Foo.class, true);
		final Annotation3 resolvedAnnotation3 = resolvedElement.getAnnotation(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = resolvedElement.getAnnotation(Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = resolvedElement.getAnnotation(Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名

		Assertions.assertEquals(ANNOTATION4, resolvedElement.getAnnotation(Annotation4.class));
		Assertions.assertEquals(ANNOTATION6, resolvedElement.getAnnotation(Annotation6.class));
		Assertions.assertEquals(ANNOTATION5, resolvedElement.getAnnotation(Annotation5.class));
	}

	@Test
	public void testToHierarchyElement() {
		Assertions.assertNotNull(AnnotatedElementUtil.toHierarchyElement(Foo.class));
		final AnnotatedElement element = AnnotatedElementUtil.toHierarchyElement(Foo.class);
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION3, ANNOTATION4, ANNOTATION6}, element.getAnnotations());
	}

	@Test
	public void testToMetaElement() {
		Assertions.assertNotNull(AnnotatedElementUtil.toMetaElement(null, false));
		Assertions.assertNotNull(AnnotatedElementUtil.toMetaElement(null, true));

		// 不解析注解属性
		AnnotatedElement element = AnnotatedElementUtil.toMetaElement(Foo.class, false);
		Assertions.assertSame(element, AnnotatedElementUtil.toMetaElement(Foo.class, false)); // 第二次获取时从缓存中获取
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION3, ANNOTATION2, ANNOTATION1}, element.getAnnotations());

		// 解析注解属性
		element = AnnotatedElementUtil.toMetaElement(Foo.class, true);
		Assertions.assertSame(element, AnnotatedElementUtil.toMetaElement(Foo.class, true)); // 第二次获取时从缓存中获取
		Assertions.assertEquals(3, element.getAnnotations().length);

		final Annotation3 resolvedAnnotation3 = element.getAnnotation(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = element.getAnnotation(Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = element.getAnnotation(Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名
	}

	@Test
	public void testToRepeatableMetaElement() {
		Assertions.assertNotNull(AnnotatedElementUtil.toRepeatableMetaElement(null, RepeatableAnnotationCollector.none(), false));
		Assertions.assertNotNull(AnnotatedElementUtil.toRepeatableMetaElement(null, RepeatableAnnotationCollector.none(), true));

		// 不解析注解属性
		AnnotatedElement element = AnnotatedElementUtil.toRepeatableMetaElement(Foo.class, RepeatableAnnotationCollector.none(), false);
		Assertions.assertEquals(element, AnnotatedElementUtil.toRepeatableMetaElement(Foo.class, RepeatableAnnotationCollector.none(), false)); // 第二次获取时从缓存中获取
		Assertions.assertArrayEquals(new Annotation[]{ANNOTATION3, ANNOTATION2, ANNOTATION1}, element.getAnnotations());

		// 解析注解属性
		element = AnnotatedElementUtil.toRepeatableMetaElement(Foo.class, RepeatableAnnotationCollector.none(), true);
		Assertions.assertEquals(element, AnnotatedElementUtil.toRepeatableMetaElement(Foo.class, RepeatableAnnotationCollector.none(), true)); // 第二次获取时从缓存中获取
		Assertions.assertEquals(3, element.getAnnotations().length);

		final Annotation3 resolvedAnnotation3 = element.getAnnotation(Annotation3.class);
		Assertions.assertNotNull(resolvedAnnotation3);
		Assertions.assertEquals(resolvedAnnotation3.alias(), ANNOTATION3.value());
		Assertions.assertEquals(resolvedAnnotation3.alias(), resolvedAnnotation3.value()); // value与alias互为别名

		final Annotation2 resolvedAnnotation2 = element.getAnnotation(Annotation2.class);
		Assertions.assertNotNull(resolvedAnnotation2);
		Assertions.assertEquals(resolvedAnnotation2.num(), ANNOTATION3.num()); // num属性被Annotation3.num覆盖

		final Annotation1 resolvedAnnotation1 = element.getAnnotation(Annotation1.class);
		Assertions.assertNotNull(resolvedAnnotation1);
		Assertions.assertEquals(ANNOTATION3.value(), resolvedAnnotation1.value()); // value属性被Annotation3.value覆盖
		Assertions.assertEquals(resolvedAnnotation1.value(), resolvedAnnotation1.alias()); // value与alias互为别名
	}

	@Test
	public void testAsElement() {
		final Annotation[] annotations = new Annotation[]{ANNOTATION1, ANNOTATION2};
		Assertions.assertNotNull(AnnotatedElementUtil.asElement());

		final AnnotatedElement element = AnnotatedElementUtil.asElement(ANNOTATION1, null, ANNOTATION2);
		Assertions.assertArrayEquals(annotations, element.getAnnotations());
		Assertions.assertArrayEquals(annotations, element.getDeclaredAnnotations());
		Assertions.assertEquals(ANNOTATION1, element.getAnnotation(Annotation1.class));
		Assertions.assertNull(element.getAnnotation(Annotation3.class));
	}

	@Test
	public void testEmptyElement() {
		final AnnotatedElement element = AnnotatedElementUtil.emptyElement();
		Assertions.assertSame(element, AnnotatedElementUtil.emptyElement());
		Assertions.assertNull(element.getAnnotation(Annotation1.class));
		Assertions.assertEquals(0, element.getAnnotations().length);
		Assertions.assertEquals(0, element.getDeclaredAnnotations().length);
	}

	// ================= super =================

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation4 {}

	@Annotation4
	private static class Super {}

	// ================= interface =================

	@Annotation6
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation5 {}

	@Annotation5
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation6 {}

	@Annotation6
	private interface Interface {}

	// ================= foo =================

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation1 {
		@Alias("alias")
		String value() default "";
		@Alias("value")
		String alias() default "";
	}

	@Annotation1
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation2 {
		int num() default Integer.MIN_VALUE;
	}

	@Annotation2
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface Annotation3 {
		@Alias("alias")
		String value() default "";
		@Alias("value")
		String alias() default "";
		int num() default Integer.MIN_VALUE;
	}

	/**
	 * <p>Foo.class上注解情况如下：
	 * <pre>{@code
	 * annotation3 => annotation2 => annotation1
	 * }</pre>
	 *
	 * <p>Super.class上注解情况如下：
	 * <pre>{@code
	 * annotation4
	 * }</pre>
	 *
	 * <p>Interface.class上注解情况如下：
	 * <pre>{@code
	 * annotation6 => annotation5 => annotation6【循环引用】
	 * }</pre>
	 */
	@Annotation3(value = "foo", num = Integer.MAX_VALUE)
	private static class Foo extends Super implements Interface {}

}
