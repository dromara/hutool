package cn.hutool.core.annotation;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.function.UnaryOperator;

public class CacheableSynthesizedAnnotationAttributeProcessorTest {

	@Test
	public void getAttributeValueTest() {
		CacheableSynthesizedAnnotationAttributeProcessor processor = new CacheableSynthesizedAnnotationAttributeProcessor();

		Map<String, Object> values1 = MapBuilder.<String, Object> create().put("name", "name1").put("value", 111).build();
		SynthesizedAnnotation annotation1 = new TestSynthesizedAnnotation(1, 0, values1);
		Map<String, Object> values2 = MapBuilder.<String, Object> create().put("name", "name2").put("value", "value2").build();
		SynthesizedAnnotation annotation2 = new TestSynthesizedAnnotation(0, 0, values2);

		Assert.assertEquals("name2", processor.getAttributeValue("name", String.class, Arrays.asList(annotation1, annotation2)));
		Assert.assertEquals(Integer.valueOf(111), processor.getAttributeValue("value", Integer.class, Arrays.asList(annotation1, annotation2)));
	}

	static class TestSynthesizedAnnotation implements SynthesizedAnnotation {

		private final int verticalDistance;
		private final int horizontalDistance;
		private final Map<String, Object> value;

		public TestSynthesizedAnnotation(int verticalDistance, int horizontalDistance, Map<String, Object> value) {
			this.verticalDistance = verticalDistance;
			this.horizontalDistance = horizontalDistance;
			this.value = value;
		}

		@Override
		public Object getRoot() {
			return null;
		}

		@Override
		public Annotation getAnnotation() {
			return null;
		}

		@Override
		public int getVerticalDistance() {
			return verticalDistance;
		}

		@Override
		public int getHorizontalDistance() {
			return horizontalDistance;
		}

		@Override
		public boolean hasAttribute(String attributeName, Class<?> returnType) {
			return Opt.ofNullable(value.get(attributeName))
				.map(t -> ClassUtil.isAssignable(returnType, t.getClass()))
				.orElse(false);
		}

		@Override
		public Map<String, AnnotationAttribute> getAttributes() {
			return null;
		}

		@Override
		public void setAttribute(String attributeName, AnnotationAttribute attribute) {

		}

		@Override
		public void replaceAttribute(String attributeName, UnaryOperator<AnnotationAttribute> operator) {

		}

		@Override
		public Object getAttributeValue(String attributeName) {
			return value.get(attributeName);
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return null;
		}

		@Override
		public Object getAttributeValue(String attributeName, Class<?> attributeType) {
			return null;
		}
	}

}
