package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

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
		assertEquals(GenericSynthesizedAggregateAnnotation.class, syntheticMetaAnnotation.annotationType());
		assertTrue(syntheticMetaAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ParentAnnotation.class));
		assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ChildAnnotation.class));
		assertEquals(grandParentAnnotation, syntheticMetaAnnotation.getAnnotation(GrandParentAnnotation.class));
		assertEquals(parentAnnotation, syntheticMetaAnnotation.getAnnotation(ParentAnnotation.class));
		assertEquals(childAnnotation, syntheticMetaAnnotation.getAnnotation(ChildAnnotation.class));
		Annotation[] synthesizedAnnotations = syntheticMetaAnnotation.getAnnotations();
		Arrays.sort(synthesizedAnnotations, Comparator.comparing(Annotation::toString));
		assertEquals(
			Arrays.asList(childAnnotation, grandParentAnnotation, parentAnnotation),
			Arrays.asList(synthesizedAnnotations)
		);

		// 扩展方法
		assertNotNull(syntheticMetaAnnotation.getSynthesizedAnnotation(GrandParentAnnotation.class));
		assertNotNull(syntheticMetaAnnotation.getSynthesizedAnnotation(ParentAnnotation.class));
		assertNotNull(syntheticMetaAnnotation.getSynthesizedAnnotation(ChildAnnotation.class));
		assertEquals(3, syntheticMetaAnnotation.getAllSynthesizedAnnotation().size());

		// 属性
		assertEquals(SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY, syntheticMetaAnnotation.getAnnotationSelector());
		assertEquals(CacheableSynthesizedAnnotationAttributeProcessor.class, syntheticMetaAnnotation.getAnnotationAttributeProcessor().getClass());
		assertEquals(3, syntheticMetaAnnotation.getAnnotationPostProcessors().size());
	}

	@Test
	public void synthesisAnnotationAttributeTest() {
		final ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		GenericSynthesizedAggregateAnnotation syntheticMetaAnnotation = new GenericSynthesizedAggregateAnnotation(rootAnnotation);
		assertEquals(syntheticMetaAnnotation.getSource(), Collections.singletonList(rootAnnotation));
		assertEquals(syntheticMetaAnnotation.annotationType(), GenericSynthesizedAggregateAnnotation.class);
		assertEquals(3, syntheticMetaAnnotation.getAnnotations().length);

		assertEquals("Child!", syntheticMetaAnnotation.getAttributeValue("childValue", String.class));
		assertEquals("Child!", syntheticMetaAnnotation.getAttributeValue("childValueAlias", String.class));
		assertEquals("Child's Parent!", syntheticMetaAnnotation.getAttributeValue("parentValue", String.class));
		assertEquals("Child's GrandParent!", syntheticMetaAnnotation.getAttributeValue("grandParentValue", String.class));
	}

	@Test
	public void syntheticAnnotationTest() {
		final ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		GenericSynthesizedAggregateAnnotation syntheticMetaAnnotation = new GenericSynthesizedAggregateAnnotation(rootAnnotation);

		final ChildAnnotation childAnnotation = syntheticMetaAnnotation.synthesize(ChildAnnotation.class);
		SynthesizedAnnotation childSyntheticAnnotation = syntheticMetaAnnotation.getSynthesizedAnnotation(ChildAnnotation.class);
		assertNotNull(childSyntheticAnnotation);
		assertTrue(childSyntheticAnnotation.hasAttribute("childValue", String.class));
		assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), childSyntheticAnnotation.getRoot());
		assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), childSyntheticAnnotation.getAnnotation());
		assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ChildAnnotation.class));
		assertNotNull(childAnnotation);
		assertEquals("Child!", childAnnotation.childValue());
		assertEquals("Child!", childAnnotation.childValueAlias());
		assertEquals(childAnnotation.grandParentType(), Integer.class);
		assertThrows(IllegalArgumentException.class, () -> new GenericSynthesizedAggregateAnnotation(childAnnotation));

		final ParentAnnotation parentAnnotation = syntheticMetaAnnotation.synthesize(ParentAnnotation.class);
		SynthesizedAnnotation parentSyntheticAnnotation = syntheticMetaAnnotation.getSynthesizedAnnotation(ParentAnnotation.class);
		assertNotNull(parentSyntheticAnnotation);
		assertTrue(parentSyntheticAnnotation.hasAttribute("parentValue", String.class));
		assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), parentSyntheticAnnotation.getRoot());
		assertEquals(ChildAnnotation.class.getAnnotation(ParentAnnotation.class), parentSyntheticAnnotation.getAnnotation());
		assertNotNull(parentAnnotation);
		assertEquals("Child's Parent!", parentAnnotation.parentValue());
		assertEquals("java.lang.Void", parentAnnotation.grandParentType());
		assertThrows(IllegalArgumentException.class, () -> new GenericSynthesizedAggregateAnnotation(parentAnnotation));

		final GrandParentAnnotation grandParentAnnotation = syntheticMetaAnnotation.synthesize(GrandParentAnnotation.class);
		SynthesizedAnnotation grandParentSyntheticAnnotation = syntheticMetaAnnotation.getSynthesizedAnnotation(GrandParentAnnotation.class);
		assertNotNull(grandParentSyntheticAnnotation);
		assertTrue(grandParentSyntheticAnnotation.hasAttribute("grandParentType", Class.class));
		assertEquals(AnnotatedClass.class.getAnnotation(ChildAnnotation.class), grandParentSyntheticAnnotation.getRoot());
		assertEquals(ChildAnnotation.class.getAnnotation(GrandParentAnnotation.class), grandParentSyntheticAnnotation.getAnnotation());
		assertTrue(syntheticMetaAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		assertNotNull(grandParentAnnotation);
		assertEquals("Child's GrandParent!", grandParentAnnotation.grandParentValue());
		assertEquals(grandParentAnnotation.grandParentType(), Integer.class);
		assertThrows(IllegalArgumentException.class, () -> new GenericSynthesizedAggregateAnnotation(grandParentAnnotation));
	}

	@Test
	public void linkTest() {
		final Method method = ReflectUtil.getMethod(AnnotationForLinkTest.class, "value");
		final SynthesizedAggregateAnnotation synthesizedAnnotationAggregator = new GenericSynthesizedAggregateAnnotation(method.getAnnotation(AliasFor.class));
		final Link link = synthesizedAnnotationAggregator.synthesize(Link.class);
		assertEquals(AnnotationForLinkTest.class, link.annotation());
		assertEquals("name", link.attribute());
	}

	@Test
	public void mirrorAttributeTest() {
		AnnotationForMirrorTest annotation = ClassForMirrorTest.class.getAnnotation(AnnotationForMirrorTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		AnnotationForMirrorTest syntheticAnnotation = synthetic.synthesize(AnnotationForMirrorTest.class);
		assertEquals("Foo", syntheticAnnotation.name());
		assertEquals("Foo", syntheticAnnotation.value());

		annotation = ClassForMirrorTest2.class.getAnnotation(AnnotationForMirrorTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		syntheticAnnotation = synthetic.synthesize(AnnotationForMirrorTest.class);
		assertEquals("Foo", syntheticAnnotation.name());
		assertEquals("Foo", syntheticAnnotation.value());

		annotation = ClassForMirrorTest3.class.getAnnotation(AnnotationForMirrorTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		syntheticAnnotation = synthetic.synthesize(AnnotationForMirrorTest.class);
		AnnotationForMirrorTest finalSyntheticAnnotation = syntheticAnnotation;
		assertThrows(IllegalArgumentException.class, finalSyntheticAnnotation::name);
	}

	@Test
	public void aliasForTest() {
		AnnotationForAliasForTest annotation = ClassForAliasForTest.class.getAnnotation(AnnotationForAliasForTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		MetaAnnotationForAliasForTest metaAnnotation = synthetic.synthesize(MetaAnnotationForAliasForTest.class);
		assertEquals("Meta", metaAnnotation.name());
		AnnotationForAliasForTest childAnnotation = synthetic.synthesize(AnnotationForAliasForTest.class);
		assertEquals("", childAnnotation.value());

		annotation = ClassForAliasForTest2.class.getAnnotation(AnnotationForAliasForTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		metaAnnotation = synthetic.synthesize(MetaAnnotationForAliasForTest.class);
		assertEquals("Foo", metaAnnotation.name());
		childAnnotation = synthetic.synthesize(AnnotationForAliasForTest.class);
		assertEquals("Foo", childAnnotation.value());
	}

	@Test
	public void forceAliasForTest() {
		AnnotationForceForAliasForTest annotation = ClassForForceAliasForTest.class.getAnnotation(AnnotationForceForAliasForTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		MetaAnnotationForForceAliasForTest metaAnnotation = synthetic.synthesize(MetaAnnotationForForceAliasForTest.class);
		assertEquals("", metaAnnotation.name());
		AnnotationForceForAliasForTest childAnnotation = synthetic.synthesize(AnnotationForceForAliasForTest.class);
		assertEquals("", childAnnotation.value());

		annotation = ClassForForceAliasForTest2.class.getAnnotation(AnnotationForceForAliasForTest.class);
		synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		metaAnnotation = synthetic.synthesize(MetaAnnotationForForceAliasForTest.class);
		assertEquals("Foo", metaAnnotation.name());
		childAnnotation = synthetic.synthesize(AnnotationForceForAliasForTest.class);
		assertEquals("Foo", childAnnotation.value());
	}

	@Test
	public void aliasForAndMirrorTest() {
		AnnotationForMirrorThenAliasForTest annotation = ClassForAliasForAndMirrorTest.class.getAnnotation(AnnotationForMirrorThenAliasForTest.class);
		SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);
		MetaAnnotationForMirrorThenAliasForTest metaAnnotation = synthetic.synthesize(MetaAnnotationForMirrorThenAliasForTest.class);
		assertEquals("test", metaAnnotation.name());
		assertEquals("test", metaAnnotation.value());
		AnnotationForMirrorThenAliasForTest childAnnotation = synthetic.synthesize(AnnotationForMirrorThenAliasForTest.class);
		assertEquals("test", childAnnotation.childValue());
	}

	@Test
	public void multiAliasForTest() {
		final AnnotationForMultiAliasForTest annotation = ClassForMultiAliasForTest.class.getAnnotation(AnnotationForMultiAliasForTest.class);
		final SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);

		final MetaAnnotationForMultiAliasForTest1 metaAnnotation1 = synthetic.synthesize(MetaAnnotationForMultiAliasForTest1.class);
		assertEquals("test", metaAnnotation1.name());
		assertEquals("test", metaAnnotation1.value1());
		final MetaAnnotationForMultiAliasForTest2 metaAnnotation2 = synthetic.synthesize(MetaAnnotationForMultiAliasForTest2.class);
		assertEquals("test", metaAnnotation2.value2());
		final AnnotationForMultiAliasForTest childAnnotation = synthetic.synthesize(AnnotationForMultiAliasForTest.class);
		assertEquals("test", childAnnotation.value3());
	}

	@Test
	public void implicitAliasTest() {
		final AnnotationForImplicitAliasTest annotation = ClassForImplicitAliasTest.class.getAnnotation(AnnotationForImplicitAliasTest.class);
		final SynthesizedAggregateAnnotation synthetic = new GenericSynthesizedAggregateAnnotation(annotation);

		final MetaAnnotationForImplicitAliasTest metaAnnotation = synthetic.synthesize(MetaAnnotationForImplicitAliasTest.class);
		assertEquals("Meta", metaAnnotation.name());
		assertEquals("Foo", metaAnnotation.value());
		final AnnotationForImplicitAliasTest childAnnotation = synthetic.synthesize(AnnotationForImplicitAliasTest.class);
		assertEquals("Foo", childAnnotation.value());
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
