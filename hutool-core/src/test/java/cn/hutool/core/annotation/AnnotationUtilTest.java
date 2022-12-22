package cn.hutool.core.annotation;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AnnotationUtilTest {

	@Test
	public void getCombinationAnnotationsTest(){
		final Annotation[] annotations = AnnotationUtil.getAnnotations(ClassWithAnnotation.class, true);
		Assert.assertNotNull(annotations);
		Assert.assertEquals(2, annotations.length);
	}

	@Test
	public void getCombinationAnnotationsWithClassTest(){
		final AnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertNotNull(annotations);
		Assert.assertEquals(1, annotations.length);
		Assert.assertEquals("测试", annotations[0].value());
	}

	@Test
	public void getAnnotationValueTest() {
		final Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertEquals("测试", value);

	}

	@Test
	public void getAnnotationValueTest2() {
		final String[] names = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest::names);
		Assert.assertTrue(ArrayUtil.equals(names, new String[]{"测试1", "测试2"}));
	}

	@Test
	public void getAnnotationSyncAlias() {
		// 直接获取
		Assert.assertEquals("", ClassWithAnnotation.class.getAnnotation(AnnotationForTest.class).retry());

		// 加别名适配
		final AnnotationForTest annotation = AnnotationUtil.getAnnotationAlias(ClassWithAnnotation.class, AnnotationForTest.class);
		Assert.assertEquals("测试", annotation.retry());
		Assert.assertTrue(AnnotationUtil.isSynthesizedAnnotation(annotation));
	}

	@AnnotationForTest(value = "测试", names = {"测试1", "测试2"})
	@RepeatAnnotationForTest
	static class ClassWithAnnotation{
		public void test(){

		}
	}

	@Test
	public void scanMetaAnnotationTest() {
		// RootAnnotation -> RootMetaAnnotation1 -> RootMetaAnnotation2 -> RootMetaAnnotation3
		//                -> RootMetaAnnotation3
		final List<Annotation> annotations = AnnotationUtil.scanMetaAnnotation(RootAnnotation.class);
		Assert.assertEquals(4, annotations.size());
		Assert.assertEquals(RootMetaAnnotation3.class, annotations.get(0).annotationType());
		Assert.assertEquals(RootMetaAnnotation1.class, annotations.get(1).annotationType());
		Assert.assertEquals(RootMetaAnnotation2.class, annotations.get(2).annotationType());
		Assert.assertEquals(RootMetaAnnotation3.class, annotations.get(3).annotationType());
	}

	@Test
	public void scanClassTest() {
		// TargetClass -> TargetSuperClass ----------------------------------> SuperInterface
		//             -> TargetSuperInterface -> SuperTargetSuperInterface -> SuperInterface
		final List<Annotation> annotations = AnnotationUtil.scanClass(TargetClass.class);
		Assert.assertEquals(5, annotations.size());
		Assert.assertEquals("TargetClass", ((AnnotationForTest)annotations.get(0)).value());
		Assert.assertEquals("TargetSuperClass", ((AnnotationForTest)annotations.get(1)).value());
		Assert.assertEquals("TargetSuperInterface", ((AnnotationForTest)annotations.get(2)).value());
		Assert.assertEquals("SuperInterface", ((AnnotationForTest)annotations.get(3)).value());
		Assert.assertEquals("SuperTargetSuperInterface", ((AnnotationForTest)annotations.get(4)).value());
	}

	@Test
	public void scanMethodTest() {
		// TargetClass -> TargetSuperClass
		//             -> TargetSuperInterface
		final Method method = ReflectUtil.getMethod(TargetClass.class, "testMethod");
		Assert.assertNotNull(method);
		final List<Annotation> annotations = AnnotationUtil.scanMethod(method);
		Assert.assertEquals(3, annotations.size());
		Assert.assertEquals("TargetClass", ((AnnotationForTest)annotations.get(0)).value());
		Assert.assertEquals("TargetSuperClass", ((AnnotationForTest)annotations.get(1)).value());
		Assert.assertEquals("TargetSuperInterface", ((AnnotationForTest)annotations.get(2)).value());
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface RootMetaAnnotation3 {}

	@RootMetaAnnotation3
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface RootMetaAnnotation2 {}

	@RootMetaAnnotation2
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface RootMetaAnnotation1 {}

	@RootMetaAnnotation3
	@RootMetaAnnotation1
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE_USE)
	public @interface RootAnnotation {}

	@AnnotationForTest("TargetClass")
	static class TargetClass extends TargetSuperClass implements TargetSuperInterface {

		@Override
		@AnnotationForTest("TargetClass")
		public List<?> testMethod() { return Collections.emptyList(); }

	}

	@AnnotationForTest("TargetSuperClass")
	static class TargetSuperClass implements SuperInterface {

		@AnnotationForTest("TargetSuperClass")
		public Collection<?> testMethod() { return Collections.emptyList(); }

	}

	@AnnotationForTest("TargetSuperInterface")
	interface TargetSuperInterface extends SuperTargetSuperInterface {

		@AnnotationForTest("TargetSuperInterface")
		Object testMethod();

	}

	@AnnotationForTest("SuperTargetSuperInterface")
	interface SuperTargetSuperInterface extends SuperInterface{}

	@AnnotationForTest("SuperInterface")
	interface SuperInterface{}

}
