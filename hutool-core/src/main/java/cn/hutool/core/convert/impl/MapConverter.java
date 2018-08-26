package cn.hutool.core.convert.impl;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

/**
 * {@link Map} 转换器
 * 
 * @author Looly
 * @since 3.0.8
 */
public class MapConverter extends AbstractConverter<Map<?, ?>> {
	
	/** Map类型 */
	private final Type mapType;
	/** 键类型 */
	private final Type keyType;
	/** 值类型 */
	private final Type valueType;
	
	/**
	 * 构造，Map的key和value泛型类型自动获取
	 * 
	 * @param mapType Map类型
	 */
	public MapConverter(Type mapType) {
		this(mapType, TypeUtil.getTypeArgument(mapType, 0), TypeUtil.getTypeArgument(mapType, 1));
	}
	
	/**
	 * 构造
	 * @param mapType Map类型
	 * @param keyType 键类型
	 * @param valueType 值类型
	 */
	public MapConverter(Type mapType, Type keyType, Type valueType) {
		this.mapType = mapType;
		this.keyType = keyType;
		this.valueType = valueType;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map<?, ?> convertInternal(Object value) {
		Map map = null;
		if(value instanceof Map){
			map = MapUtil.createMap(TypeUtil.getClass(this.mapType));
			convertMapToMap((Map)value, map);
		}else if(BeanUtil.isBean(value.getClass())){
			map = BeanUtil.beanToMap(value);
		}else{
			throw new UnsupportedOperationException(StrUtil.format("Unsupport toMap value type: {}", value.getClass().getName()));
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
		Object key;
		Object value;
		for (Entry<?, ?> entry : srcMap.entrySet()) {
			key = (null == this.keyType) ? entry.getKey() : convert.convert(this.keyType, entry.getKey());
			value = (null == this.valueType) ? entry.getValue() : convert.convert(this.keyType, entry.getValue());
			targetMap.put(key, value);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<Map<?, ?>> getTargetType() {
		return (Class<Map<?, ?>>) TypeUtil.getClass(this.mapType);
	}
}
