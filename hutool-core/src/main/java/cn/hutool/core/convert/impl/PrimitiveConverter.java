package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.function.Function;

/**
 * 原始类型转换器<br>
 * 支持类型为：<br>
 * <ul>
 * 		<li><code>byte</code></li>
 * 		<li><code>short</code></li>
 * 		 <li><code>int</code></li>
 * 		 <li><code>long</code></li>
 * 		<li><code>float</code></li>
 * 		<li><code>double</code></li>
 * 		<li><code>char</code></li>
 * 		<li><code>boolean</code></li>
 * </ul>
 *
 * @author Looly
 */
public class PrimitiveConverter extends AbstractConverter<Object> {
	private static final long serialVersionUID = 1L;

	private final Class<?> targetType;

	/**
	 * 构造<br>
	 *
	 * @param clazz 需要转换的原始
	 * @throws IllegalArgumentException 传入的转换类型非原始类型时抛出
	 */
	public PrimitiveConverter(Class<?> clazz) {
		if (null == clazz) {
			throw new NullPointerException("PrimitiveConverter not allow null target type!");
		} else if (false == clazz.isPrimitive()) {
			throw new IllegalArgumentException("[" + clazz + "] is not a primitive class!");
		}
		this.targetType = clazz;
	}

	@Override
	protected Object convertInternal(Object value) {
		return PrimitiveConverter.convert(value, this.targetType, this::convertToStr);
	}

	@Override
	protected String convertToStr(Object value) {
		return StrUtil.trim(super.convertToStr(value));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<Object> getTargetType() {
		return (Class<Object>) this.targetType;
	}

	/**
	 * 将指定值转换为原始类型的值
	 * @param value 值
	 * @param primitiveClass 原始类型
	 * @param toStringFunc 当无法直接转换时，转为字符串后再转换的函数
	 * @return 转换结果
	 * @since 5.5.0
	 */
	protected static Object convert(Object value, Class<?> primitiveClass, Function<Object, String> toStringFunc) {
		if (byte.class == primitiveClass) {
			return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Byte.class, toStringFunc), 0);
		} else if (short.class == primitiveClass) {
			return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Short.class, toStringFunc), 0);
		} else if (int.class == primitiveClass) {
			return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Integer.class, toStringFunc), 0);
		} else if (long.class == primitiveClass) {
			return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Long.class, toStringFunc), 0);
		} else if (float.class == primitiveClass) {
			return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Float.class, toStringFunc), 0);
		} else if (double.class == primitiveClass) {
			return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Double.class, toStringFunc), 0);
		} else if (char.class == primitiveClass) {
			return Convert.convert(Character.class, value);
		} else if (boolean.class == primitiveClass) {
			return Convert.convert(Boolean.class, value);
		}

		throw new ConvertException("Unsupported target type: {}", primitiveClass);
	}
}
