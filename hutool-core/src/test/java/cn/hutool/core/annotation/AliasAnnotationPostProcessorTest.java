package cn.hutool.core.annotation;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class AliasAnnotationPostProcessorTest {

	@Test
	public void processTest() {
		AliasAnnotationPostProcessor processor = new AliasAnnotationPostProcessor();

		Map<Class<?>, SynthesizedAnnotation> annotationMap = new HashMap<>();
		SynthesizedAggregateAnnotation synthesizedAnnotationAggregator = new TestSynthesizedAggregateAnnotation(annotationMap);
		AnnotationForTest annotation = ClassForTest.class.getAnnotation(AnnotationForTest.class);
		SynthesizedAnnotation synthesizedAnnotation = new TestSynthesizedAnnotation(synthesizedAnnotationAggregator, annotation);
		annotationMap.put(annotation.annotationType(), synthesizedAnnotation);

		processor.process(synthesizedAnnotation, synthesizedAnnotationAggregator);
		AnnotationAttribute valueAttribute = synthesizedAnnotation.getAttributes().get("value");
		Assert.assertEquals(ReflectUtil.getMethod(AnnotationForTest.class, "value"), valueAttribute.getAttribute());
		Assert.assertTrue(valueAttribute.isWrapped());
		Assert.assertEquals(ForceAliasedAnnotationAttribute.class, valueAttribute.getClass());

		AnnotationAttribute nameAttribute = synthesizedAnnotation.getAttributes().get("name");
		Assert.assertEquals(ReflectUtil.getMethod(AnnotationForTest.class, "name"), nameAttribute.getAttribute());
		Assert.assertFalse(nameAttribute.isWrapped());
		Assert.assertEquals(CacheableAnnotationAttribute.class, nameAttribute.getClass());

		Assert.assertEquals(nameAttribute, ((WrappedAnnotationAttribute)valueAttribute).getLinked());
	}

	@AnnotationForTest
	static class ClassForTest {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE })
	@interface AnnotationForTest {
		@Alias("name")
		String value() default "";
		String name() default "";
	}

	static class TestSynthesizedAggregateAnnotation implements SynthesizedAggregateAnnotation {

		private final Map<Class<?>, SynthesizedAnnotation> annotationMap;

		public TestSynthesizedAggregateAnnotation(Map<Class<?>, SynthesizedAnnotation> annotationMap) {
			this.annotationMap = annotationMap;
		}

		@Override
		public Object getSource() {
			return null;
		}

		@Override
		public SynthesizedAnnotationSelector getAnnotationSelector() {
			return null;
		}

		@Override
		public SynthesizedAnnotationAttributeProcessor getAnnotationAttributeProcessor() {
			return null;
		}

		@Override
		public Collection<SynthesizedAnnotationPostProcessor> getAnnotationPostProcessors() {
			return null;
		}

		@Override
		public SynthesizedAnnotation getSynthesizedAnnotation(Class<?> annotationType) {
			return annotationMap.get(annotationType);
		}

		@Override
		public Map<Class<? extends Annotation>, SynthesizedAnnotation> getAllSynthesizedAnnotation() {
			return null;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
			return null;
		}

		@Override
		public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
			return false;
		}

		@Override
		public Annotation[] getAnnotations() {
			return new Annotation[0];
		}

		@Override
		public <T extends Annotation> T synthesize(Class<T> annotationType) {
			return null;
		}

		@Override
		public Object getAttributeValue(String attributeName, Class<?> attributeType) {
			return null;
		}

		@Override
		public Object getRoot() {
			return null;
		}
	}

	static class TestSynthesizedAnnotation implements SynthesizedAnnotation {

		private final Annotation annotation;
		private final SynthesizedAggregateAnnotation owner;
		private final Map<String, AnnotationAttribute> attributeMap;

		public TestSynthesizedAnnotation(SynthesizedAggregateAnnotation owner, Annotation annotation) {
			this.owner = owner;
			this.attributeMap = new HashMap<>();
			this.annotation = annotation;
			for (Method declaredMethod : annotation.annotationType().getDeclaredMethods()) {
				attributeMap.put(declaredMethod.getName(), new CacheableAnnotationAttribute(annotation, declaredMethod));
			}
		}

		@Override
		public Object getRoot() {
			return null;
		}

		@Override
		public Annotation getAnnotation() {
			return annotation;
		}

		@Override
		public int getVerticalDistance() {
			return 0;
		}

		@Override
		public int getHorizontalDistance() {
			return 0;
		}

		@Override
		public boolean hasAttribute(String attributeName, Class<?> returnType) {
			return false;
		}

		@Override
		public Map<String, AnnotationAttribute> getAttributes() {
			return attributeMap;
		}

		@Override
		public void setAttribute(String attributeName, AnnotationAttribute attribute) {
			attributeMap.put(attributeName, attribute);
		}

		@Override
		public void replaceAttribute(String attributeName, UnaryOperator<AnnotationAttribute> operator) {
			AnnotationAttribute annotationAttribute = attributeMap.get(attributeName);
			if (ObjectUtil.isNotNull(annotationAttribute)) {
				attributeMap.put(attributeName, operator.apply(annotationAttribute));
			}
		}

		@Override
		public Object getAttributeValue(String attributeName) {
			return null;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return annotation.annotationType();
		}

		@Override
		public Object getAttributeValue(String attributeName, Class<?> attributeType) {
			return null;
		}
	}

}
