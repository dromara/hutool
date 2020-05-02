package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

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
		try {
			if (byte.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).byteValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toByte((Boolean) value);
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Byte.parseByte(valueStr);

			} else if (short.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).shortValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toShort((Boolean) value);
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Short.parseShort(valueStr);

			} else if (int.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).intValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toInt((Boolean) value);
				} else if (value instanceof Date) {
					return ((Date) value).getTime();
				} else if (value instanceof Calendar) {
					return ((Calendar) value).getTimeInMillis();
				} else if (value instanceof TemporalAccessor) {
					return DateUtil.toInstant((TemporalAccessor) value).toEpochMilli();
				}

				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return NumberUtil.parseInt(valueStr);

			} else if (long.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).longValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toLong((Boolean) value);
				} else if (value instanceof Date) {
					return ((Date) value).getTime();
				} else if (value instanceof Calendar) {
					return ((Calendar) value).getTimeInMillis();
				} else if (value instanceof TemporalAccessor) {
					return DateUtil.toInstant((TemporalAccessor) value).toEpochMilli();
				}

				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return NumberUtil.parseLong(valueStr);

			} else if (float.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).floatValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toFloat((Boolean) value);
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Float.parseFloat(valueStr);

			} else if (double.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).doubleValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toDouble((Boolean) value);
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Double.parseDouble(valueStr);

			} else if (char.class == this.targetType) {
				if (value instanceof Character) {
					//noinspection UnnecessaryUnboxing
					return ((Character) value).charValue();
				} else if (value instanceof Boolean) {
					return BooleanUtil.toChar((Boolean) value);
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return valueStr.charAt(0);
			} else if (boolean.class == this.targetType) {
				if (value instanceof Boolean) {
					//noinspection UnnecessaryUnboxing
					return ((Boolean) value).booleanValue();
				}
				String valueStr = convertToStr(value);
				return BooleanUtil.toBoolean(valueStr);
			}
		} catch (Exception e) {
			// Ignore Exception
		}
		return 0;
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
}
