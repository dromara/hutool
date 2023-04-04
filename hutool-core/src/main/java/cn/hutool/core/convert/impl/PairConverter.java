package cn.hutool.core.convert.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Map;

public class PairConverter extends AbstractConverter<Pair<?, ?>> {

	/** Pair类型 */
	private final Type pairType;
	/** 键类型 */
	private final Type keyType;
	/** 值类型 */
	private final Type valueType;

	/**
	 * 构造，Pair的key和value泛型类型自动获取
	 *
	 * @param pairType Map类型
	 */
	public PairConverter(Type pairType) {
		this(pairType, null, null);
	}

	/**
	 * 构造
	 *
	 * @param pairType Pair类型
	 * @param keyType 键类型
	 * @param valueType 值类型
	 */
	public PairConverter(Type pairType, Type keyType, Type valueType) {
		this.pairType = pairType;
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Pair<?, ?> convertInternal(Object value) {
		Map map = null;
		if (value instanceof Map.Entry) {
			final Map.Entry entry = (Map.Entry) value;
			map = MapUtil.of(entry.getKey(), entry.getValue());
		}else if (value instanceof Map) {
			map = (Map) value;
		} else if (value instanceof CharSequence) {
			final CharSequence str = (CharSequence) value;
			map = strToMap(str);
		} else if (BeanUtil.isReadableBean(value.getClass())) {
			map = BeanUtil.beanToMap(value);
		}

		if (null != map) {
			return mapToPair(pairType, keyType, valueType, map);
		}

		throw new ConvertException("Unsupported to map from [{}] of type: {}", value, value.getClass().getName());
	}

	/**
	 * 字符串转单个键值对的Map，支持分隔符{@code :}、{@code =}、{@code ,}
	 *
	 * @param str 字符串
	 * @return map or null
	 */
	private static Map<CharSequence, CharSequence> strToMap(final CharSequence str) {
		// key:value  key=value  key,value
		final int index = StrUtil.indexOf(str, '=', 0, str.length());

		if (index > -1) {
			return MapUtil.of(str.subSequence(0, index + 1), str.subSequence(index, str.length()));
		}
		return null;
	}

	/**
	 * Map转Entry
	 *
	 * @param targetType 目标的Map类型
	 * @param keyType    键类型
	 * @param valueType  值类型
	 * @param map        被转换的map
	 * @return Entry
	 */
	@SuppressWarnings("rawtypes")
	private static Pair<?, ?> mapToPair(final Type targetType, final Type keyType, final Type valueType, final Map map) {

		Object key = null;
		Object value = null;
		if (1 == map.size()) {
			final Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
			key = entry.getKey();
			value = entry.getValue();
		} else if (2 == map.size()) {
			key = map.get("key");
			value = map.get("value");
		}

		final ConverterRegistry convert = ConverterRegistry.getInstance();
		return (Pair<?, ?>) ReflectUtil.newInstance(TypeUtil.getClass(targetType),
			TypeUtil.isUnknown(keyType) ? key : convert.convert(keyType, key),
			TypeUtil.isUnknown(valueType) ? value : convert.convert(valueType, value)
		);
	}
}
