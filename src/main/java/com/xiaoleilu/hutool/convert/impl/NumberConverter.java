package com.xiaoleilu.hutool.convert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数字转换器<br>
 * 支持类型为：<br>
 * <ul>
 * 		<li><code>java.lang.Byte</code></li>
 * 		<li><code>java.lang.Short</code></li>
 *		 <li><code>java.lang.Integer</code></li>
 *		 <li><code>java.lang.Long</code></li>
 * 		<li><code>java.lang.Float</code></li>
 * 		<li><code>java.lang.Double</code></li>
 *		 <li><code>java.math.BigDecimal</code></li>
 * 		<li><code>java.math.BigInteger</code></li>
 * </ul>
 * 
 * @author Looly
 *
 */
public class NumberConverter extends AbstractConverter {

	private Class<? extends Number> targetType;

	public NumberConverter() {
		this.targetType = Number.class;
	}

	/**
	 * 构造<br>
	 * @param clazz 需要转换的数字类型，默认 {@link Number}
	 */
	public NumberConverter(Class<? extends Number> clazz) {
		this.targetType = (null == clazz) ? Number.class : clazz;
	}

	@Override
	public Class<? extends Number> getTargetType() {
		return targetType;
	}

	@Override
	protected Number convertInternal(Object value) {
		try {
			if (Byte.class == this.targetType) {
				if (value instanceof Number) {
					return Byte.valueOf(((Number) value).byteValue());
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return null;
				}
				return Byte.valueOf(valueStr);
				
			} else if (Short.class == this.targetType) {
				if (value instanceof Number) {
					return Short.valueOf(((Number) value).shortValue());
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return null;
				}
				return Short.valueOf(valueStr);
				
			} else if (Integer.class == this.targetType) {
				if (value instanceof Number) {
					return Integer.valueOf(((Number) value).intValue());
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return null;
				}
				return Integer.valueOf(valueStr);
				
			} else if (Long.class == this.targetType) {
				if (value instanceof Number) {
					return Long.valueOf(((Number) value).longValue());
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return null;
				}
				return Long.valueOf(valueStr);
				
			} else if (Float.class == this.targetType) {
				if (value instanceof Number) {
					return Float.valueOf(((Number) value).floatValue());
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return null;
				}
				return Float.valueOf(valueStr);
				
			} else if (Double.class == this.targetType) {
				if (value instanceof Number) {
					return Double.valueOf(((Number) value).doubleValue());
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return null;
				}
				return Double.valueOf(valueStr);
				
			} else if (BigDecimal.class == this.targetType) {
				return toBigDecimal(value);
				
			} else if (BigInteger.class == this.targetType) {
				toBigInteger(value);
			}

			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			return NumberFormat.getInstance().parse(valueStr);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	private BigDecimal toBigDecimal(Object value) {
		if (value instanceof Long) {
			return new BigDecimal((Long) value);
		} else if (value instanceof Double) {
			return new BigDecimal((Double) value);
		} else if (value instanceof Integer) {
			return new BigDecimal((Integer) value);
		} else if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		}

		final String valueStr = convertToStr(value);
		if (StrUtil.isBlank(valueStr)) {
			return null;
		}
		return new BigDecimal(valueStr);
	}

	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 * 
	 * @param value 被转换的值
	 * @return 结果
	 */
	private BigInteger toBigInteger(Object value) {
		if (value instanceof Long) {
			return BigInteger.valueOf((Long) value);
		}
		final String valueStr = convertToStr(value);
		if (StrUtil.isBlank(valueStr)) {
			return null;
		}
		return new BigInteger(valueStr);
	}
}
