package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangchengxing
 */
public class TestIssueI8CLBJ {

	@Test
	public void test() throws NoSuchFieldException {
		Field field = Foo.class.getDeclaredField("name");
		Assert.assertNotNull(field);
		Annotation[] annotations = field.getDeclaredAnnotations();
		Assert.assertTrue(annotations.length > 0);

		TestAnnotation annotation = AnnotationUtil.getSynthesizedAnnotation(TestAnnotation.class, annotations);
		List<Thread> threadList = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			Thread thread = new Thread(() -> {
				try {
					String valueFieldName = annotation.valueFieldName();
					//Console.log("valueFieldName:" + valueFieldName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			threadList.add(thread);
			thread.start();
		}

		try {
			for (Thread thread : threadList) {
				thread.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Foo {
		private Integer id;
		@TestAnnotation("name")
		private String name;
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface TestAnnotation {
		String value() default "";
		@Alias("value")
		String valueFieldName() default "";
	}
}
