package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
		@Link(attribute = "name")
		String value() default "";
		@Link(attribute = "value")
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
		@Link(
			annotation = MetaAnnotationForAliasForTest.class,
			attribute = "name",
			type = RelationType.ALIAS_FOR
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
		@Link(
			annotation = MetaAnnotationForForceAliasForTest.class,
			attribute = "name",
			type = RelationType.FORCE_ALIAS_FOR
		)
		String value() default "";
	}
	@AnnotationForceForAliasForTest
	static class ClassForForceAliasForTest {}
	@AnnotationForceForAliasForTest("Foo")
	static class ClassForForceAliasForTest2 {}

}
