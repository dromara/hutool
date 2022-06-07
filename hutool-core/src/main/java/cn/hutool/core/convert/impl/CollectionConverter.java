package cn.hutool.core.convert.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.core.reflect.TypeUtil;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 各种集合类转换器
 *
 * @author Looly
 * @since 3.0.8
 */
public class CollectionConverter implements Converter {

	public static CollectionConverter INSTANCE = new CollectionConverter();

	@Override
	public Collection<?> convert(Type targetType, final Object value) {
		if (targetType instanceof TypeReference) {
			targetType = ((TypeReference<?>) targetType).getType();
		}

		return convert(targetType, TypeUtil.getTypeArgument(targetType), value);
	}

	/**
	 * 转换
	 *
	 * @param collectionType 集合类型
	 * @param elementType    集合中元素类型
	 * @param value          被转换的值
	 * @return 转换后的集合对象
	 */
	public Collection<?> convert(final Type collectionType, final Type elementType, final Object value) {
		final Collection<Object> collection = CollUtil.create(TypeUtil.getClass(collectionType));
		return CollUtil.addAll(collection, value, elementType);
	}
}
