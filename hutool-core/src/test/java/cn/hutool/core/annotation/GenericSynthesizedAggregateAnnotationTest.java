package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

/**
 * 合成注解{@link GenericSynthesizedAggregateAnnotation}的测试用例
 *
 * @author huangchengxing
 */
public class GenericSynthesizedAggregateAnnotationTest {

	@Test
	public void baseSynthesisAnnotationWorkTest() {
		// AnnotatedClass -> @ChildAnnotation -> @ParentAnnotation -> @GrandParentAnnotation
		//                                    -> @GrandParentAnnotation
		final GrandParentAnnotation grandParentAnnotation = ChildAnnotation.class.getAnnotation(GrandParentAnnotation.class);
		final ParentAnnotation parentAnnotation = ChildAnnotation.class.getAnnotation(ParentAnnotation.class);
		final ChildAnnotation childAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		final GenericSynthesizedAggregateAnnotation syntheticMetaAnnotation = new GenericSynthesizedAggregateAnnotation(childAnnotation);

		// Annotation & AnnotatedElement
		Assert.assertEquals(GenericSynthesizedAggregateAnnotation.class, syntheticMetaAnnotation.annotationType());
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ParentAnnotation.class));
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ChildAnnotation.class));
		Assert.assertEquals(grandParentAnnotation, syntheticMetaAnnotation.getAnnotation(GrandParentAnnotation.class));
		Assert.assertEquals(parentAnnotation, syntheticMetaAnnotation.getAnnotation(ParentAnnotation.class));
		Assert.assertEquals(childAnnotation, syntheticMetaAnnotation.getAnnotation(ChildAnnotation.class));
		Assert.assertEquals(
			Arrays.asList(childAnnotation, grandParentAnnotation, parentAnnotation),
			Arrays.asList(syntheticMetaAnnotation.getAnnotations())
		);

		// 扩展方法
		Assert.assertNotNull(syntheticMetaAnnotation.getSynthesizedAnnotation(GrandParentAnnotation.class));
		Assert.assertNotNull(syntheticMetaAnnotation.getSynthesizedAnnotation(ParentAnnotation.class));
		Assert.assertNotNull(syntheticMetaAnnotation.getSynthesizedAnnotation(ChildAnnotation.class));
		Assert.assertEquals(3, syntheticMetaAnnotation.getAllSynthesizedAnnotation().size());

		// 属性
		Assert.assertEquals(SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY, syntheticMetaAnnotation.getAnnotationSelector());
		Assert.assertEquals(CacheableSynthesizedAnnotationAttributeProcessor.class, syntheticMetaAnnotation.getAnnotationAttributeProcessor().getClass());
		Assert.assertEquals(3, syntheticMetaAnnotation.getAnnotationPostProcessors().size());
	}

	@Test
	public void synthesisAnnotationAttributeTest() {
		final ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		GenericSynthesizedAggregateAnnotation syntheticMetaAnnotation = new GenericSynthesizedAggregateAnnotation(rootAnnotation);
		Assert.assertEquals(syntheticMetaAnnotation.getSource(), Collections.singletonList(rootAnnotation));
		Assert.assertEquals(syntheticMetaAnnotation.annotationType(), GenericSynthesizedAggregateAnnotation.class);
		Assert.assertEquals(3, syntheticMetaAnnotation.getAnnotations().length);

		Assert.assertEquals("Child!", syntheticMetaAnnotation.getAttributeValue("childValue", String.class));
		Assert.assertEquals("Child!", syntheticMetaAnnotation.getAttributeValue("childValueAlias", String.class));
		Assert.assertEquals("Child's Parent!", syntheticMetaAnnotation.getAttributeValue("parentValue", String.class));
		Assert.assertEquals("Child's GrandParent!", syntheticMetaAnnotation.getAttributeValue("grandParentValue", String.class));
	}

	@Test
	public void syntheticAnnotationTest() {
		final ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		GenericSynthesizedAggregateAnnotation syntheticMetaAnnotation = new GenericSynthesizedAggregateAnnotation(rootAnnotation);

		final ChildAnnotation childAnnotation = syntheticMetaAnnotation.synthesize(ChildAnnotation.class);
		SynthesizedAnnotation childSyntheticAnnotation = syntheticMetaAnnotation.getSynthesizedAnnotation(ChildAnnotation.class);
		Assert.assertNotNull(childSyntheticAnnotation);
		Assert.assertTrue(childSyntheticAnnotation.hasAttribute("childValue", String.class));
		Assert.assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), childSyntheticAnnotation.getRoot());
		Assert.assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), childSyntheticAnnotation.getAnnotation());
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ChildAnnotation.class));
		Assert.assertNotNull(childAnnotation);
		Assert.assertEquals("Child!", childAnnotation.childValue());
		Assert.assertEquals("Child!", childAnnotation.childValueAlias());
		Assert.assertEquals(childAnnotation.grandParentType(), Integer.class);
		Assert.assertThrows(IllegalArgumentException.class, () -> new GenericSynthesizedAggregateAnnotation(childAnnotation));

		final ParentAnnotation parentAnnotation = syntheticMetaAnnotation.synthesize(ParentAnnotation.class);
		SynthesizedAnnotation parentSyntheticAnnotation = syntheticMetaAnnotation.getSynthesizedAnnotation(ParentAnnotation.class);
		Assert.assertNotNull(parentSyntheticAnnotation);
		Assert.assertTrue(parentSyntheticAnnotation.hasAttribute("parentValue", String.class));
		Assert.assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), parentSyntheticAnnotation.getRoot());
		Assert.assertEquals(ChildAnnotation.class.getAnnotation(ParentAnnotation.class), parentSyntheticAnnotation.getAnnotation());
		Assert.assertNotNull(parentAnnotation);
		Assert.assertEquals("Child's Parent!", parentAnnotation.parentValue());
		Assert.assertEquals("java.lang.Void", parentAnnotation.grandParentType());
		Assert.assertThrows(IllegalArgumentException.class, () -> new GenericSynthesizedAggregateAnnotation(parentAnnotation));

		final GrandParentAnnotation grandParentAnnotation = syntheticMetaAnnotation.synthesize(GrandParentAnnotation.class);
		SynthesizedAnnotation grandParentSyntheticAnnotation = syntheticMetaAnnotation.getSynthesizedAnnotation(GrandParentAnnotation.class);
		Assert.assertNotNull(grandParentSyntheticAnnotation);
		Assert.assertTrue(grandParentSyntheticAnnotation.hasAttribute("grandParentType", Class.class));
		Assert.assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), grandParentSyntheticAnnotation.getRoot());
		Assert.assertEquals(ChildAnnotation.class.getAnnotation(GrandParentAnnotation.class), grandParentSyntheticAnnotation.getAnnotation());
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		Assert.assertNotNull(grandParentAnnotation);
		Assert.assertEquals("Child's GrandParent!", grandParentAnnotation.grandParentValue());
		Assert.assertEquals(grandParentAnnotation.grandParentType(), Integer.class);
		Assert.assertThrows(IllegalArgumentException.class, () -> new GenericSynthesizedAggregateAnnotation(grandParentAnnotation));
	}

	@Test
	public void linkTest() {
		final Method method = ReflectUtil.getMethod(AnnotationForLinkTest.class, "value");
		final SynthesizedAggregateAnnotation synthesizedAnnotationAggregator = new GenericSynthesizedAggregateAnnotation(method.getAnnotation(AliasFor.class));
		final Link link = synthesizedAnnotationAggregator.synthesize(Link.class);
		Assert.assertEquals(AnnotationForLinkTest.class, link.annotation());
		Assert.assertEquals("name", link.attribute());
	}

	@Test
	public void mirrorAttributeTest() {
		AnnotationForMirrorTest annotation = ClassForMirrorTest.class.getAnnotation(AnnotationForMirrorTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		AnnotationForMirrorTest syntheticAnnotation = synthetic.synthesize(AnnotationForMirrorTest.class);
		Assert.assertEquals("Foo", syntheticAnnotation.name());
		Assert.assertEquals("Foo", syntheticAnnotation.value());

		annotation = ClassForMirrorTest2.class.getAnnotation(AnnotationForMirrorTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		syntheticAnnotation = synthetic.synthesize(AnnotationForMirrorTest.class);
		Assert.assertEquals("Foo", syntheticAnnotation.name());
		Assert.assertEquals("Foo", syntheticAnnotation.value());

		annotation = ClassForMirrorTest3.class.getAnnotation(AnnotationForMirrorTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		syntheticAnnotation = synthetic.synthesize(AnnotationForMirrorTest.class);
		AnnotationForMirrorTest finalSyntheticAnnotation = syntheticAnnotation;
		Assert.assertThrows(IllegalArgumentException.class, finalSyntheticAnnotation::name);
	}

	@Test
	public void aliasForTest() {
		AnnotationForAliasForTest annotation = ClassForAliasForTest.class.getAnnotation(AnnotationForAliasForTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		MetaAnnotationForAliasForTest metaAnnotation = synthetic.synthesize(MetaAnnotationForAliasForTest.class);
		Assert.assertEquals("Meta", metaAnnotation.name());
		AnnotationForAliasForTest childAnnotation = synthetic.synthesize(AnnotationForAliasForTest.class);
		Assert.assertEquals("", childAnnotation.value());

		annotation = ClassForAliasForTest2.class.getAnnotation(AnnotationForAliasForTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		metaAnnotation = synthetic.synthesize(MetaAnnotationForAliasForTest.class);
		Assert.assertEquals("Foo", metaAnnotation.name());
		childAnnotation = synthetic.synthesize(AnnotationForAliasForTest.class);
		Assert.assertEquals("Foo", childAnnotation.value());
	}

	@Test
	public void forceAliasForTest() {
		AnnotationForceForAliasForTest annotation = ClassForForceAliasForTest.class.getAnnotation(AnnotationForceForAliasForTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		MetaAnnotationForForceAliasForTest metaAnnotation = synthetic.synthesize(MetaAnnotationForForceAliasForTest.class);
		Assert.assertEquals("", metaAnnotation.name());
		AnnotationForceForAliasForTest childAnnotation = synthetic.synthesize(AnnotationForceForAliasForTest.class);
		Assert.assertEquals("", childAnnotation.value());

		annotation = ClassForForceAliasForTest2.class.getAnnotation(AnnotationForceForAliasForTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		metaAnnotation = synthetic.synthesize(MetaAnnotationForForceAliasForTest.class);
		Assert.assertEquals("Foo", metaAnnotation.name());
		childAnnotation = synthetic.synthesize(AnnotationForceForAliasForTest.class);
		Assert.assertEquals("Foo", childAnnotation.value());
	}

	@Test
	public void aliasForAndMirrorTest() {
		AnnotationForMirrorThenAliasForTest annotation = ClassForAliasForAndMirrorTest.class.getAnnotation(AnnotationForMirrorThenAliasForTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		MetaAnnotationForMirrorThenAliasForTest metaAnnotation = synthetic.synthesize(MetaAnnotationForMirrorThenAliasForTest.class);
		Assert.assertEquals("test", metaAnnotation.name());
		Assert.assertEquals("test", metaAnnotation.value());
		AnnotationForMirrorThenAliasForTest childAnnotation = synthetic.synthesize(AnnotationForMirrorThenAliasForTest.class);
		Assert.assertEquals("test", childAnnotation.childValue());
	}

	@Test
	public void multiAliasForTest() {
		final AnnotationForMultiAliasForTest annotation = ClassForMultiAliasForTest.class.getAnnotation(AnnotationForMultiAliasForTest.class);
		final SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);

		final MetaAnnotationForMultiAliasForTest1 metaAnnotation1 = synthetic.synthesize(MetaAnnotationForMultiAliasForTest1.class);
		Assert.assertEquals("test", metaAnnotation1.name());
		Assert.assertEquals("test", metaAnnotation1.value1());
		final MetaAnnotationForMultiAliasForTest2 metaAnnotation2 = synthetic.synthesize(MetaAnnotationForMultiAliasForTest2.class);
		Assert.assertEquals("test", metaAnnotation2.value2());
		final AnnotationForMultiAliasForTest childAnnotation = synthetic.synthesize(AnnotationForMultiAliasForTest.class);
		Assert.assertEquals("test", childAnnotation.value3());
	}

	@Test
	public void implicitAliasTest() {
		final AnnotationForImplicitAliasTest annotation = ClassForImplicitAliasTest.class.getAnnotation(AnnotationForImplicitAliasTest.class);
		final SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);

		final MetaAnnotationForImplicitAliasTest metaAnnotation = synthetic.synthesize(MetaAnnotationForImplicitAliasTest.class);
		Assert.assertEquals("Meta", metaAnnotation.name());
		Assert.assertEquals("Foo", metaAnnotation.value());
		final AnnotationForImplicitAliasTest childAnnotation = synthetic.synthesize(AnnotationForImplicitAliasTest.class);
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

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface MetaAnnotationForImplicitAliasTest {
		@MirrorFor(attribute = "value")
		String name() default "";
		@MirrorFor(attribute = "name")
		String value() default "";
	}
	@MetaAnnotationForImplicitAliasTest("Meta")
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForImplicitAliasTest {
		String value() default "";
	}
	@AnnotationForImplicitAliasTest("Foo")
	static class ClassForImplicitAliasTest {}

}
