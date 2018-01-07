package cn.hutool.core.convert.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.StrUtil;

/**
 * 数字转换器<br>
 * 支持类型为：<br>
 * <ul>
 * <li><code>java.lang.Byte</code></li>
 * <li><code>java.lang.Short</code></li>
 * <li><code>java.lang.Integer</code></li>
 * <li><code>java.lang.Long</code></li>
 * <li><code>java.lang.Float</code></li>
 * <li><code>java.lang.Double</code></li>
 * <li><code>java.math.BigDecimal</code></li>
 * <li><code>java.math.BigInteger</code></li>
 * </ul>
 * 
 * @author Looly
 *
 */
public class NumberConverter extends AbstractConverter<Number> {

	private Class<? extends Number> targetType;

	public NumberConverter() {
		this.targetType = Number.class;
	}

	/**
	 * 构造<br>
	 * 
	 * @param clazz 需要转换的数字类型，默认 {@link Number}
	 */
	public NumberConverter(Class<? extends Number> clazz) {
		this.targetType = (null == clazz) ? Number.class : clazz;
	}

	@Override
	protected Number convertInternal(Object value) {
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

		} else if (AtomicInteger.class == this.targetType) {
			int intValue;
			if (value instanceof Number) {
				intValue = ((Number) value).intValue();
			}
			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			intValue = Integer.parseInt(valueStr);
			return new AtomicInteger(intValue);
		} else if (Long.class == this.targetType) {
			if (value instanceof Number) {
				return Long.valueOf(((Number) value).longValue());
			}
			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			return Long.valueOf(valueStr);

		} else if (AtomicLong.class == this.targetType) {
			long longValue;
			if (value instanceof Number) {
				longValue = ((Number) value).longValue();
			}
			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			longValue = Long.parseLong(valueStr);
			return new AtomicLong(longValue);
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
		}else if(Number.class == this.targetType){
			if (value instanceof Number) {
				return (Number)value;
			}
			final String valueStr = convertToStr(value);
			if (StrUtil.isBlank(valueStr)) {
				return null;
			}
			try {
				return NumberFormat.getInstance().parse(valueStr);
			} catch (ParseException e) {
				throw new ConvertException(e);
			}
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Number type: {}", this.targetType.getName()));
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
		} else if (value instanceof Integer) {
			return new BigDecimal((Integer) value);
		} else if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		}

		//对于Double类型，先要转换为String，避免精度问题
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

	@Override
	protected String convertToStr(Object value) {
		final String valueStr = super.convertToStr(value);
		return (null == valueStr) ? null : valueStr.trim();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<Number> getTargetType() {
		return (Class<Number>) this.targetType;
	}
}
