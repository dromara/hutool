package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.Map;

/**
 * 合成注解{@link SyntheticAnnotation}的测试用例
 *
 * @author huangchengxing
 */
public class SyntheticAnnotationTest {

	@Test
	public void testSynthesisAnnotation() {
		ChildAnnotation rootAnnotation = AnnotatedClass.class.getAnnotation(ChildAnnotation.class);
		SyntheticAnnotation<ChildAnnotation> syntheticAnnotation = SyntheticAnnotation.of(rootAnnotation);
		Assert.assertEquals(syntheticAnnotation.getSource(), rootAnnotation);
		Assert.assertEquals(syntheticAnnotation.annotationType(), rootAnnotation.annotationType());
		Assert.assertEquals(1, syntheticAnnotation.getDeclaredAnnotations().length);
		Assert.assertEquals(syntheticAnnotation.getDeclaredAnnotations()[0], rootAnnotation);
		Assert.assertEquals(3, syntheticAnnotation.getAnnotations().length);

		Assert.assertEquals(syntheticAnnotation.getAttribute("childValue", String.class), "Child!");
		Assert.assertEquals(syntheticAnnotation.getAttribute("childValueAlias", String.class), "Child!");
		Assert.assertEquals(syntheticAnnotation.getAttribute("parentValue", String.class), "Child's Parent!");
		Assert.assertEquals(syntheticAnnotation.getAttribute("grandParentValue", String.class), "Child's GrandParent!");

		Map<Class<? extends Annotation>, SyntheticAnnotation.MetaAnnotation> annotationMap = syntheticAnnotation.getMetaAnnotationMap();
		ChildAnnotation childAnnotation = syntheticAnnotation.getAnnotation(ChildAnnotation.class);
		Assert.assertTrue(syntheticAnnotation.isAnnotationPresent(ChildAnnotation.class));
		Assert.assertNotNull(childAnnotation);
		Assert.assertEquals(childAnnotation.childValue(), "Child!");
		Assert.assertEquals(childAnnotation.childValueAlias(), "Child!");
		Assert.assertEquals(childAnnotation.grandParentType(), Integer.class);
		Assert.assertEquals(annotationMap, SyntheticAnnotation.of(childAnnotation).getMetaAnnotationMap());

		ParentAnnotation parentAnnotation = syntheticAnnotation.getAnnotation(ParentAnnotation.class);
		Assert.assertTrue(syntheticAnnotation.isAnnotationPresent(ParentAnnotation.class));
		Assert.assertNotNull(parentAnnotation);
		Assert.assertEquals(parentAnnotation.parentValue(), "Child's Parent!");
		Assert.assertEquals(parentAnnotation.grandParentType(), "java.lang.Void");
		Assert.assertEquals(annotationMap, SyntheticAnnotation.of(parentAnnotation).getMetaAnnotationMap());

		GrandParentAnnotation grandParentAnnotation = syntheticAnnotation.getAnnotation(GrandParentAnnotation.class);
		Assert.assertTrue(syntheticAnnotation.isAnnotationPresent(GrandParentAnnotation.class));
		Assert.assertNotNull(grandParentAnnotation);
		Assert.assertEquals(grandParentAnnotation.grandParentValue(), "Child's GrandParent!");
		Assert.assertEquals(grandParentAnnotation.grandParentType(), Integer.class);
		Assert.assertEquals(annotationMap, SyntheticAnnotation.of(grandParentAnnotation).getMetaAnnotationMap());
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
