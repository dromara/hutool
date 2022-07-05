package cn.hutool.core.annotation;

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import cn.hutool.core.util.ObjectUtil;

import java.util.Collection;
import java.util.Comparator;

/**
 * 带缓存功能的{@link SynthesizedAnnotationAttributeProcessor}实现，
 * 构建时需要传入比较器，获取属性值时将根据比较器对合成注解进行排序，
 * 然后选择具有所需属性的，排序最靠前的注解用于获取属性值
 *
 * @param <A> 合成注解类型
 * @author huangchengxing
 */
public class CacheableSynthesizedAnnotationAttributeProcessor<A extends SynthesizedAnnotation<?>> implements
	SynthesizedAnnotationAttributeProcessor<A> {

	private final Table<String, Class<?>, Object> valueCaches = new RowKeyTable<>();
	private final Comparator<A> annotationComparator;

	/**
	 * 创建一个带缓存的注解值选择器
	 *
	 * @param annotationComparator 注解比较器，排序更靠前的注解将被优先用于获取值
	 */
	public CacheableSynthesizedAnnotationAttributeProcessor(Comparator<A> annotationComparator) {
		this.annotationComparator = annotationComparator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttributeValue(String attributeName, Class<T> attributeType, Collection<A> synthesizedAnnotations) {
		Object value = valueCaches.get(attributeName, attributeType);
		// 此处理论上不可能出现缓存值为nul的情况
		if (ObjectUtil.isNotNull(value)) {
			return (T)value;
		}
		value = synthesizedAnnotations.stream()
			.filter(ma -> ma.hasAttribute(attributeName, attributeType))
			.min(annotationComparator)
			.map(ma -> ma.getAttribute(attributeName))
			.orElse(null);
		valueCaches.put(attributeName, attributeType, value);
		return (T)value;
	}
}
