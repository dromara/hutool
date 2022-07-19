package cn.hutool.core.annotation.scanner;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.List;

public class GenericAnnotationScannerTest {

	@Test
	public void scanDirectlyTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, false, false);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(1, annotations.size());
	}

	@Test
	public void scanDirectlyAndMetaAnnotationTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, false, false);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(2, annotations.size());
	}

	@Test
	public void scanSuperclassTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, true, false);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(2, annotations.size());
	}

	@Test
	public void scanSuperclassAndMetaAnnotationTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, true, false);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(4, annotations.size());
	}

	@Test
	public void scanInterfaceTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, false, true);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(2, annotations.size());
	}

	@Test
	public void scanInterfaceAndMetaAnnotationTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, false, true);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(4, annotations.size());
	}

	@Test
	public void scanTypeHierarchyTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, true, true);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(3, annotations.size());
	}

	@Test
	public void scanTypeHierarchyAndMetaAnnotationTest() {
		final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, true, true);
		final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
		Assert.assertEquals(6, annotations.size());
	}

	@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MetaAnnotationForTest { }

	@MetaAnnotationForTest
	@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AnnotationForTest { }

	@AnnotationForTest
	static class ClassForTest extends SupperForTest implements InterfaceForTest { }

	@AnnotationForTest
	static class SupperForTest { }

	@AnnotationForTest
	interface InterfaceForTest { }

}
