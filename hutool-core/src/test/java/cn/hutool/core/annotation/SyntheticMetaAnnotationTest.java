package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * 合成注解{@link SyntheticMetaAnnotation}的测试用例
 *
 * @author huangchengxing
 */
public class SyntheticMetaAnnotationTest {

	@Test
	public void testSynthesisAnnotation() {
		ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		SyntheticMetaAnnotation syntheticMetaAnnotation = new SyntheticMetaAnnotation(rootAnnotation);
		Assert.assertEquals(syntheticMetaAnnotation.getSource(), rootAnnotation);
		Assert.assertEquals(syntheticMetaAnnotation.annotationType(), SyntheticMetaAnnotation.class);
		Assert.assertEquals(1, syntheticMetaAnnotation.getDeclaredAnnotations().length);
		Assert.assertEquals(syntheticMetaAnnotation.getDeclaredAnnotations()[0], rootAnnotation);
		Assert.assertEquals(3, syntheticMetaAnnotation.getAnnotations().length);

		Assert.assertEquals("Child!", syntheticMetaAnnotation.getAttribute("childValue", String.class));
		Assert.assertEquals("Child!", syntheticMetaAnnotation.getAttribute("childValueAlias", String.class));
		Assert.assertEquals("Child's Parent!", syntheticMetaAnnotation.getAttribute("parentValue", String.class));
		Assert.assertEquals("Child's GrandParent!", syntheticMetaAnnotation.getAttribute("grandParentValue", String.class));

		ChildAnnotation childAnnotation = syntheticMetaAnnotation.syntheticAnnotation(ChildAnnotation.class);
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ChildAnnotation.class));
		Assert.assertNotNull(childAnnotation);
		Assert.assertEquals("Child!", childAnnotation.childValue());
		Assert.assertEquals("Child!", childAnnotation.childValueAlias());
		Assert.assertEquals(childAnnotation.grandParentType(), Integer.class);
		Assert.assertThrows(IllegalArgumentException.class, () -> new SyntheticMetaAnnotation(childAnnotation));

		ParentAnnotation parentAnnotation = syntheticMetaAnnotation.syntheticAnnotation(ParentAnnotation.class);
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ParentAnnotation.class));
		Assert.assertNotNull(parentAnnotation);
		Assert.assertEquals("Child's Parent!", parentAnnotation.parentValue());
		Assert.assertEquals("java.lang.Void", parentAnnotation.grandParentType());
		Assert.assertThrows(IllegalArgumentException.class, () -> new SyntheticMetaAnnotation(parentAnnotation));

		GrandParentAnnotation grandParentAnnotation = syntheticMetaAnnotation.syntheticAnnotation(GrandParentAnnotation.class);
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		Assert.assertNotNull(grandParentAnnotation);
		Assert.assertEquals("Child's GrandParent!", grandParentAnnotation.grandParentValue());
		Assert.assertEquals(grandParentAnnotation.grandParentType(), Integer.class);
		Assert.assertThrows(IllegalArgumentException.class, () -> new SyntheticMetaAnnotation(grandParentAnnotation));
	}

	@Test
	public void linkTest() {
		Method method = ReflectUtil.getMethod(AnnotationForLinkTest.class, "value");
		SyntheticAnnotation syntheticAnnotation = new SyntheticMetaAnnotation(method.getAnnotation(AliasFor.class));
		Link link = syntheticAnnotation.syntheticAnnotation(Link.class);
		Assert.assertEquals(AnnotationForLinkTest.class, link.annotation());
		Assert.assertEquals("name", link.attribute());
	}

	@Test
	public void mirrorAttributeTest() {
		AnnotationForMirrorTest annotation = ClassForMirrorTest.class.getAnnotation(AnnotationForMirrorTest.class);
		SyntheticAnnotation synthetic = new SyntheticMetaAnnotation(annotation);
		AnnotationForMirrorTest syntheticAnnotation = synthetic.syntheticAnnotation(AnnotationForMirrorTest.class);
		Assert.assertEquals("Foo", syntheticAnnotation.name());
		Assert.assertEquals("Foo", syntheticAnnotation.value());

		annotation = ClassForMirrorTest2.class.getAnnotation(AnnotationForMirrorTest.class);
		synthetic = new SyntheticMetaAnnotation(annotation);
		syntheticAnnotation = synthetic.syntheticAnnotation(AnnotationForMirrorTest.class);
		Assert.assertEquals("Foo", syntheticAnnotation.name());
		Assert.assertEquals("Foo", syntheticAnnotation.value());

		annotation = ClassForMirrorTest3.class.getAnnotation(AnnotationForMirrorTest.class);
		synthetic = new SyntheticMetaAnnotation(annotation);
		syntheticAnnotation = synthetic.syntheticAnnotation(AnnotationForMirrorTest.class);
		AnnotationForMirrorTest finalSyntheticAnnotation = syntheticAnnotation;
		Assert.assertThrows(IllegalArgumentException.class, () -> finalSyntheticAnnotation.name());
	}

	@Test
	public void aliasForTest() {
		AnnotationForAliasForTest annotation = ClassForAliasForTest.class.getAnnotation(AnnotationForAliasForTest.class);
		SyntheticAnnotation synthetic = new SyntheticMetaAnnotation(annotation);
		MetaAnnotationForAliasForTest metaAnnotation = synthetic.syntheticAnnotation(MetaAnnotationForAliasForTest.class);
		Assert.assertEquals("Meta", metaAnnotation.name());
		AnnotationForAliasForTest childAnnotation = synthetic.syntheticAnnotation(AnnotationForAliasForTest.class);
		Assert.assertEquals("", childAnnotation.value());

		annotation = ClassForAliasForTest2.class.getAnnotation(AnnotationForAliasForTest.class);
		synthetic = new SyntheticMetaAnnotation(annotation);
		metaAnnotation = synthetic.syntheticAnnotation(MetaAnnotationForAliasForTest.class);
		Assert.assertEquals("Foo", metaAnnotation.name());
		childAnnotation = synthetic.syntheticAnnotation(AnnotationForAliasForTest.class);
		Assert.assertEquals("Foo", childAnnotation.value());
	}

	@Test
	public void forceAliasForTest() {
		AnnotationForceForAliasForTest annotation = ClassForForceAliasForTest.class.getAnnotation(AnnotationForceForAliasForTest.class);
		SyntheticAnnotation synthetic = new SyntheticMetaAnnotation(annotation);
		MetaAnnotationForForceAliasForTest metaAnnotation = synthetic.syntheticAnnotation(MetaAnnotationForForceAliasForTest.class);
		Assert.assertEquals("", metaAnnotation.name());
		AnnotationForceForAliasForTest childAnnotation = synthetic.syntheticAnnotation(AnnotationForceForAliasForTest.class);
		Assert.assertEquals("", childAnnotation.value());

		annotation = ClassForForceAliasForTest2.class.getAnnotation(AnnotationForceForAliasForTest.class);
		synthetic = new SyntheticMetaAnnotation(annotation);
		metaAnnotation = synthetic.syntheticAnnotation(MetaAnnotationForForceAliasForTest.class);
		Assert.assertEquals("Foo", metaAnnotation.name());
		childAnnotation = synthetic.syntheticAnnotation(AnnotationForceForAliasForTest.class);
		Assert.assertEquals("Foo", childAnnotation.value());
	}

	@Test
	public void aliasForAndMirrorTest() {
		AnnotationForMirrorThenAliasForTest annotation = ClassForAliasForAndMirrorTest.class.getAnnotation(AnnotationForMirrorThenAliasForTest.class);
		SyntheticAnnotation synthetic = new SyntheticMetaAnnotation(annotation);
		MetaAnnotationForMirrorThenAliasForTest metaAnnotation = synthetic.syntheticAnnotation(MetaAnnotationForMirrorThenAliasForTest.class);
		Assert.assertEquals("test", metaAnnotation.name());
		Assert.assertEquals("test", metaAnnotation.value());
		AnnotationForMirrorThenAliasForTest childAnnotation = synthetic.syntheticAnnotation(AnnotationForMirrorThenAliasForTest.class);
		Assert.assertEquals("test", childAnnotation.childValue());
	}

	@Test
	public void multiAliasForTest() {
		AnnotationForMultiAliasForTest annotation = ClassForMultiAliasForTest.class.getAnnotation(AnnotationForMultiAliasForTest.class);
		SyntheticAnnotation synthetic = new SyntheticMetaAnnotation(annotation);

		MetaAnnotationForMultiAliasForTest1 metaAnnotation1 = synthetic.syntheticAnnotation(MetaAnnotationForMultiAliasForTest1.class);
		Assert.assertEquals("test", metaAnnotation1.name());
		Assert.assertEquals("test", metaAnnotation1.value1());
		MetaAnnotationForMultiAliasForTest2 metaAnnotation2 = synthetic.syntheticAnnotation(MetaAnnotationForMultiAliasForTest2.class);
		Assert.assertEquals("test", metaAnnotation2.value2());
		AnnotationForMultiAliasForTest childAnnotation = synthetic.syntheticAnnotation(AnnotationForMultiAliasForTest.class);
		Assert.assertEquals("test", childAnnotation.value3());
	}

	// 注解结构如下：
	// AnnotatedClass -> @ChildAnnotation -> @ParentAnnotation -> @GrandParentAnnotation
	//                                    -> @GrandParentAnnotation
	@ChildAnnotation(childValueAlias = "Child!", grandParentType = Integer.class)
	static class AnnotatedClass {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.ANNOTATION_TYPE })
	@interface GrandParentAnnotation {
		String grandParentValue() default "";
		Class<?> grandParentType() default Void.class;
	}

	@GrandParentAnnotation(grandParentValue = "Parent's GrandParent!") // 覆盖元注解@GrandParentAnnotation的属性
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE })
	@interface ParentAnnotation {
		String parentValue() default "";
		String grandParentType() default "java.lang.Void";
	}

	@GrandParentAnnotation(grandParentValue = "Child's GrandParent!") // 重复的元注解，靠近根注解的优先级高
	@ParentAnnotation(parentValue = "Child's Parent!") // 覆盖元注解@ParentAnnotation的属性
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface ChildAnnotation {
		String childValueAlias() default "";
		@Alias("childValueAlias")
		String childValue() default "";
		Class<?> grandParentType() default Void.class;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForMirrorTest {
		//@Link(attribute = "name")
		@MirrorFor(attribute = "name")
		String value() default "";
		//@Link(attribute = "value")
		@MirrorFor(attribute = "value")
		String name() default "";
	}
	@AnnotationForMirrorTest("Foo")
	static class ClassForMirrorTest {}
	@AnnotationForMirrorTest(name = "Foo")
	static class ClassForMirrorTest2 {}
	@AnnotationForMirrorTest(value = "Aoo", name = "Foo")
	static class ClassForMirrorTest3 {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface MetaAnnotationForAliasForTest {
		String name() default "";
	}
	@MetaAnnotationForAliasForTest(name = "Meta")
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForAliasForTest {
		@AliasFor(
			annotation = MetaAnnotationForAliasForTest.class,
			attribute = "name"
		)
		String value() default "";
	}
	@AnnotationForAliasForTest
	static class ClassForAliasForTest {}
	@AnnotationForAliasForTest("Foo")
	static class ClassForAliasForTest2 {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface MetaAnnotationForForceAliasForTest {
		String name() default "";
	}
	@MetaAnnotationForForceAliasForTest(name = "Meta")
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForceForAliasForTest {
		//@Link(
		//	annotation = MetaAnnotationForForceAliasForTest.class,
		//	attribute = "name",
		//	type = RelationType.FORCE_ALIAS_FOR
		//)
		@ForceAliasFor(annotation = MetaAnnotationForForceAliasForTest.class, attribute = "name")
		String value() default "";
	}
	@AnnotationForceForAliasForTest
	static class ClassForForceAliasForTest {}
	@AnnotationForceForAliasForTest("Foo")
	static class ClassForForceAliasForTest2 {}

	@interface AnnotationForLinkTest {
		@AliasFor(attribute = "name", annotation = AnnotationForLinkTest.class)
		String value() default "value";
		String name() default "name";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface MetaAnnotationForMirrorThenAliasForTest {
		@MirrorFor(attribute = "value")
		String name() default "";
		@MirrorFor(attribute = "name")
		String value() default "";
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@MetaAnnotationForMirrorThenAliasForTest("Meta")
	@interface AnnotationForMirrorThenAliasForTest {
		@AliasFor(attribute = "name", annotation = MetaAnnotationForMirrorThenAliasForTest.class)
		String childValue() default "value";
	}
	@AnnotationForMirrorThenAliasForTest(childValue = "test")
	static class ClassForAliasForAndMirrorTest{}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface MetaAnnotationForMultiAliasForTest1 {
		@MirrorFor(attribute = "value1")
		String name() default "";
		@MirrorFor(attribute = "name")
		String value1() default "";
	}
	@MetaAnnotationForMultiAliasForTest1
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface MetaAnnotationForMultiAliasForTest2 {
		@AliasFor(attribute = "name", annotation = MetaAnnotationForMultiAliasForTest1.class)
		String value2() default "";
	}
	@MetaAnnotationForMultiAliasForTest2
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForMultiAliasForTest {
		@AliasFor(attribute = "value2", annotation = MetaAnnotationForMultiAliasForTest2.class)
		String value3() default "value";
	}
	@AnnotationForMultiAliasForTest(value3 = "test")
	static class ClassForMultiAliasForTest{}

}
