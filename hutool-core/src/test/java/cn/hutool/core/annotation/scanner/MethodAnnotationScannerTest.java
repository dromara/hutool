package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author huangchengxing
 */
public class MethodAnnotationScannerTest {

	@Test
	public void testMethodAnnotationScanner() {
		AnnotationScanner scanner = new MethodAnnotationScanner();
		Method method = ReflectUtil.getMethod(Example.class, "test");
		Assert.assertNotNull(method);
		Assert.assertTrue(scanner.support(method));
		List<Annotation> annotations = scanner.getAnnotations(method);
		Assert.assertEquals(1, annotations.size());
		Assert.assertEquals(CollUtil.getFirst(annotations).annotationType(), AnnotationForScannerTest.class);
	}

	static class Example {
		@AnnotationForScannerTest
		public void test() {

		}
	}

}
