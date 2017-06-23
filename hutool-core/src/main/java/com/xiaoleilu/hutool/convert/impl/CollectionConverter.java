package com.xiaoleilu.hutool.convert.impl;

import java.util.Collection;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 各种集合类转换器
 * @author Looly
 *
 */
@SuppressWarnings("rawtypes")
public class CollectionConverter extends AbstractConverter<Collection<?>>{
	
	private final Class<? extends Collection> collectionType;
	private final Class<?> elementType;
	
	/**
	 * 构造，默认集合类型使用{@link Collection}
	 */
	public CollectionConverter() {
		this(Collection.class);
	}
	
	/**
	 * 构造
	 * @param collectionType 集合类型
	 */
	public CollectionConverter(Class<? extends Collection> collectionType) {
		this(collectionType, ClassUtil.getTypeArgument(collectionType));
	}
	
	/**
	 * 构造
	 * @param collectionType 集合类型
	 * @param elementType 集合元素类型
	 */
	public CollectionConverter(Class<? extends Collection> collectionType, Class<?> elementType) {
		this.collectionType = collectionType;
		this.elementType = elementType;
	}

	@Override
	protected Collection<?> convertInternal(Object value) {
		final Collection<Object> collection = CollectionUtil.create(this.collectionType);
		
		return CollectionUtil.addAll(collection, value, elementType);
	}
}
