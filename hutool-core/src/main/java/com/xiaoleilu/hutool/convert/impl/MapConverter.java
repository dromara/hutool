package com.xiaoleilu.hutool.convert.impl;

import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * {@link Map} 转换器
 * 
 * @author Looly
 * @since 3.0.8
 */
public class MapConverter extends AbstractConverter<Map<?, ?>> {
	
	/** Map类型 */
	private final Class<?> mapType;
	/** 键类型 */
	private final Class<?> keyType;
	/** 值类型 */
	private final Class<?> valueType;
	
	/**
	 * 构造
	 * @param mapType Map类型
	 */
	public MapConverter(Class<?> mapType) {
		this.mapType = mapType;
		this.keyType = ClassUtil.getTypeArgument(mapType, 0);
		this.valueType = ClassUtil.getTypeArgument(mapType, 1);
	}
	
	/**
	 * 构造
	 * @param mapType Map类型
	 * @param keyType 键类型
	 * @param valueType 值类型
	 */
	public MapConverter(Class<?> mapType, Class<?> keyType, Class<?> valueType) {
		this.mapType = mapType;
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map<?, ?> convertInternal(Object value) {
		Map map = CollectionUtil.createMap(mapType);
		
		Class<?> valueType = value.getClass();
		if(value instanceof Map){
			convertMapToMap((Map)value, map);
		}else if(BeanUtil.isBean(valueType)){
			BeanUtil.beanToMap(map);
		}else{
			throw new UnsupportedOperationException(StrUtil.format("Unsupport toMap value type: {}", valueType.getName()));
		}
		return map;
	}

	/**
	 * Map转Map
	 * @param srcMap 源Map
	 * @param targetMap 目标Map
	 */
	private void convertMapToMap(Map<?, ?> srcMap, Map<Object, Object> targetMap){
		final ConverterRegistry convert = ConverterRegistry.getInstance();
		for (Entry<?, ?> entry : srcMap.entrySet()) {
			targetMap.put(convert.convert(this.keyType, entry.getKey()), convert.convert(this.valueType, entry.getValue()));
		}
	}
}
