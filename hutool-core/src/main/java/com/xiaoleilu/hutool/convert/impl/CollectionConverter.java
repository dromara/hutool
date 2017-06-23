package com.xiaoleilu.hutool.convert.impl;

import java.util.Collection;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 各种集合类转换器
 * @author Looly
 *
 */
@SuppressWarnings("rawtypes")
public class CollectionConverter extends AbstractConverter<Collection<?>>{
	
	private final Class<? extends Collection> collectionType;
	
	/**
	 * 构造
	 * @param collectionType 集合类型
	 */
	public CollectionConverter() {
		this.collectionType = Collection.class;
	}
	
	/**
	 * 构造
	 * @param collectionType 集合类型
	 */
	public CollectionConverter(Class<? extends Collection> collectionType) {
		this.collectionType = collectionType;
	}

	@Override
	protected Collection<?> convertInternal(Object value) {
		final Collection<Object> collection = CollectionUtil.create(this.collectionType);
		
		return CollectionUtil.addAll(collection, value);
	}
}
