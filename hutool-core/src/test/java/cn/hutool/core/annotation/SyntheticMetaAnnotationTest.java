package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.Map;

/**
 * 合成注解{@link SyntheticMetaAnnotation}的测试用例
 *
 * @author huangchengxing
 */
public class SyntheticMetaAnnotationTest {

	@Test
	public void testSynthesisAnnotation() {
		ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		SyntheticMetaAnnotation<ChildAnnotation> syntheticMetaAnnotation = new SyntheticMetaAnnotation<>(rootAnnotation);
		Assert.assertEquals(syntheticMetaAnnotation.getSource(), rootAnnotation);
		Assert.assertEquals(syntheticMetaAnnotation.annotationType(), SyntheticMetaAnnotation.class);
		Assert.assertEquals(1, syntheticMetaAnnotation.getDeclaredAnnotations().length);
		Assert.assertEquals(syntheticMetaAnnotation.getDeclaredAnnotations()[0], rootAnnotation);
		Assert.assertEquals(3, syntheticMetaAnnotation.getAnnotations().length);

		Assert.assertEquals("Child!", syntheticMetaAnnotation.getAttribute("childValue", String.class));
		Assert.assertEquals("Child!", syntheticMetaAnnotation.getAttribute("childValueAlias", String.class));
		Assert.assertEquals("Child's Parent!", syntheticMetaAnnotation.getAttribute("parentValue", String.class));
		Assert.assertEquals("Child's GrandParent!", syntheticMetaAnnotation.getAttribute("grandParentValue", String.class));

		Map<Class<? extends Annotation>, SyntheticMetaAnnotation.MetaAnnotation> annotationMap = syntheticMetaAnnotation.getMetaAnnotationMap();
		ChildAnnotation childAnnotation = syntheticMetaAnnotation.syntheticAnnotation(ChildAnnotation.class);
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ChildAnnotation.class));
		Assert.assertNotNull(childAnnotation);
		Assert.assertEquals("Child!", childAnnotation.childValue());
		Assert.assertEquals("Child!", childAnnotation.childValueAlias());
		Assert.assertEquals(childAnnotation.grandParentType(), Integer.class);
		Assert.assertEquals(annotationMap, new SyntheticMetaAnnotation<>(childAnnotation).getMetaAnnotationMap());

		ParentAnnotation parentAnnotation = syntheticMetaAnnotation.syntheticAnnotation(ParentAnnotation.class);
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(ParentAnnotation.class));
		Assert.assertNotNull(parentAnnotation);
		Assert.assertEquals("Child's Parent!", parentAnnotation.parentValue());
		Assert.assertEquals("java.lang.Void", parentAnnotation.grandParentType());
		Assert.assertEquals(annotationMap, new SyntheticMetaAnnotation<>(parentAnnotation).getMetaAnnotationMap());

		GrandParentAnnotation grandParentAnnotation = syntheticMetaAnnotation.syntheticAnnotation(GrandParentAnnotation.class);
		Assert.assertTrue(syntheticMetaAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		Assert.assertNotNull(grandParentAnnotation);
		Assert.assertEquals("Child's GrandParent!", grandParentAnnotation.grandParentValue());
		Assert.assertEquals(grandParentAnnotation.grandParentType(), Integer.class);
		Assert.assertEquals(annotationMap, new SyntheticMetaAnnotation<>(grandParentAnnotation).getMetaAnnotationMap());
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

}
