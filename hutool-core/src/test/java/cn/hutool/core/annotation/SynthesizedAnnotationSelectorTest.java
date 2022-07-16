package cn.hutool.core.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.UnaryOperator;

public class SynthesizedAnnotationSelectorTest {

	@Test
	public void chooseTest() {
		final SynthesizedAnnotationSelector.NearestAndOldestPrioritySelector selector = (SynthesizedAnnotationSelector.NearestAndOldestPrioritySelector)SynthesizedAnnotationSelector.NEAREST_AND_OLDEST_PRIORITY;

		TestSynthesizedAnnotation annotation1 = new TestSynthesizedAnnotation(0, 0);
		TestSynthesizedAnnotation annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation1, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(0, 1);
		annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation1, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(1, 0);
		annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation2, selector.choose(annotation1, annotation2));
	}

	@Test
	public void nearestAndNewestPriorityTest() {
		final SynthesizedAnnotationSelector.NearestAndNewestPrioritySelector selector = (SynthesizedAnnotationSelector.NearestAndNewestPrioritySelector)SynthesizedAnnotationSelector.NEAREST_AND_NEWEST_PRIORITY;

		TestSynthesizedAnnotation annotation1 = new TestSynthesizedAnnotation(0, 0);
		TestSynthesizedAnnotation annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation2, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(0, 1);
		annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation2, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(0, 0);
		annotation2 = new TestSynthesizedAnnotation(1, 0);
		Assert.assertEquals(annotation1, selector.choose(annotation1, annotation2));
	}

	@Test
	public void farthestAndOldestPriorityTest() {
		final SynthesizedAnnotationSelector.FarthestAndOldestPrioritySelector selector = (SynthesizedAnnotationSelector.FarthestAndOldestPrioritySelector)SynthesizedAnnotationSelector.FARTHEST_AND_OLDEST_PRIORITY;

		TestSynthesizedAnnotation annotation1 = new TestSynthesizedAnnotation(0, 0);
		TestSynthesizedAnnotation annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation1, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(0, 1);
		annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation1, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(0, 0);
		annotation2 = new TestSynthesizedAnnotation(1, 0);
		Assert.assertEquals(annotation2, selector.choose(annotation1, annotation2));
	}

	@Test
	public void farthestAndNewestPriorityTest() {
		final SynthesizedAnnotationSelector.FarthestAndNewestPrioritySelector selector = (SynthesizedAnnotationSelector.FarthestAndNewestPrioritySelector)SynthesizedAnnotationSelector.FARTHEST_AND_NEWEST_PRIORITY;

		TestSynthesizedAnnotation annotation1 = new TestSynthesizedAnnotation(0, 0);
		TestSynthesizedAnnotation annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation2, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(0, 1);
		annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation2, selector.choose(annotation1, annotation2));

		annotation1 = new TestSynthesizedAnnotation(1, 0);
		annotation2 = new TestSynthesizedAnnotation(0, 0);
		Assert.assertEquals(annotation1, selector.choose(annotation1, annotation2));
	}

	static class TestSynthesizedAnnotation implements SynthesizedAnnotation {

		private final int verticalDistance;
		private final int horizontalDistance;

		public TestSynthesizedAnnotation(int verticalDistance, int horizontalDistance) {
			this.verticalDistance = verticalDistance;
			this.horizontalDistance = horizontalDistance;
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
			return this.verticalDistance;
		}

		@Override
		public int getHorizontalDistance() {
			return this.horizontalDistance;
		}

		@Override
		public boolean hasAttribute(String attributeName, Class<?> returnType) {
			return false;
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
			return null;
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
