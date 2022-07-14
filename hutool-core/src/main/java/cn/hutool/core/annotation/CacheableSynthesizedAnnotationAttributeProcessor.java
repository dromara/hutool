package cn.hutool.core.annotation;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import cn.hutool.core.util.ObjectUtil;

import java.util.Collection;
import java.util.Comparator;

/**
 * <p>带缓存功能的{@link SynthesizedAnnotationAttributeProcessor}实现，
 * 构建时需要传入比较器，获取属性值时将根据比较器对合成注解进行排序，
 * 然后选择具有所需属性的，排序最靠前的注解用于获取属性值
 *
 * <p>通过该处理器获取合成注解属性值时会出现隐式别名，
 * 即子注解和元注解中同时存在类型和名称皆相同的属性时，元注解中属性总是会被该属性覆盖，
 * 并且该覆盖关系并不会通过{@link Alias}或{@link Link}被传递到关联的属性中。
 *
 * @author huangchengxing
 */
public class CacheableSynthesizedAnnotationAttributeProcessor implements SynthesizedAnnotationAttributeProcessor {

	private final Table<String, Class<?>, Object> valueCaches = new RowKeyTable<>();
	private final Comparator<Hierarchical> annotationComparator;

	/**
	 * 创建一个带缓存的注解值选择器
	 *
	 * @param annotationComparator 注解比较器，排序更靠前的注解将被优先用于获取值
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor(Comparator<Hierarchical> annotationComparator) {
		Assert.notNull(annotationComparator, "annotationComparator must not null");
		this.annotationComparator = annotationComparator;
	}

	/**
	 * 创建一个带缓存的注解值选择器，
	 * 默认按{@link SynthesizedAnnotation#getVerticalDistance()}和{@link SynthesizedAnnotation#getHorizontalDistance()}排序，
	 * 越靠前的越优先被取值。
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor() {
		this(Hierarchical.DEFAULT_HIERARCHICAL_COMPARATOR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttributeValue(String attributeName, Class<T> attributeType, Collection<? extends SynthesizedAnnotation> synthesizedAnnotations) {
		Object value = valueCaches.get(attributeName, attributeType);
		// 此处理论上不可能出现缓存值为nul的情况
		if (ObjectUtil.isNotNull(value)) {
			return (T)value;
		}
		value = synthesizedAnnotations.stream()
			.filter(ma -> ma.hasAttribute(attributeName, attributeType))
			.min(annotationComparator)
			.map(ma -> ma.getAttributeValue(attributeName))
			.orElse(null);
		valueCaches.put(attributeName, attributeType, value);
		return (T)value;
	}
}
