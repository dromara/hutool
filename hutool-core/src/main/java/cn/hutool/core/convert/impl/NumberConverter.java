package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

/**
 * 数字转换器<br>
 * 支持类型为：<br>
 * <ul>
 * <li><code>java.lang.Byte</code></li>
 * <li><code>java.lang.Short</code></li>
 * <li><code>java.lang.Integer</code></li>
 * <li><code>java.util.concurrent.atomic.AtomicInteger</code></li>
 * <li><code>java.lang.Long</code></li>
 * <li><code>java.util.concurrent.atomic.AtomicLong</code></li>
 * <li><code>java.lang.Float</code></li>
 * <li><code>java.lang.Double</code></li>
 * <li><code>java.math.BigDecimal</code></li>
 * <li><code>java.math.BigInteger</code></li>
 * </ul>
 *
 * @author Looly
 */
public class NumberConverter extends AbstractConverter<Number> {
	private static final long serialVersionUID = 1L;

	private final Class<? extends Number> targetType;

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
		return convert(value, this.targetType, this::convertToStr);
	}

	/**
	 * 转换对象为数字
	 *
	 * @param value      对象值
	 * @param targetType 目标的数字类型
	 * @param toStrFunc  转换为字符串的函数
	 * @return 转换后的数字
	 * @since 5.5.0
	 */
	protected static Number convert(Object value, Class<?> targetType, Function<Object, String> toStrFunc) {
		// 枚举转换为数字默认为其顺序
		if (value instanceof Enum) {
			return convert(((Enum<?>) value).ordinal(), targetType, toStrFunc);
		}

		if (Byte.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).byteValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toByteObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply(value);
			return StrUtil.isBlank(valueStr) ? null : Byte.valueOf(valueStr);
		} else if (Short.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).shortValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toShortObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply((value));
			return StrUtil.isBlank(valueStr) ? null : Short.valueOf(valueStr);
		} else if (Integer.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).intValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toInteger((Boolean) value);
			} else if (value instanceof Date) {
				return (int) ((Date) value).getTime();
			} else if (value instanceof Calendar) {
				return (int) ((Calendar) value).getTimeInMillis();
			} else if (value instanceof TemporalAccessor) {
				return (int) DateUtil.toInstant((TemporalAccessor) value).toEpochMilli();
			}
			final String valueStr = toStrFunc.apply((value));
			return StrUtil.isBlank(valueStr) ? null : NumberUtil.parseInt(valueStr);
		} else if (AtomicInteger.class == targetType) {
			final Number number = convert(value, Integer.class, toStrFunc);
			if (null != number) {
				final AtomicInteger intValue = new AtomicInteger();
				intValue.set(number.intValue());
				return intValue;
			}
		} else if (Long.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).longValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toLongObj((Boolean) value);
			} else if (value instanceof Date) {
				return ((Date) value).getTime();
			} else if (value instanceof Calendar) {
				return ((Calendar) value).getTimeInMillis();
			} else if (value instanceof TemporalAccessor) {
				return DateUtil.toInstant((TemporalAccessor) value).toEpochMilli();
			}
			final String valueStr = toStrFunc.apply((value));
			return StrUtil.isBlank(valueStr) ? null : NumberUtil.parseLong(valueStr);
		} else if (AtomicLong.class == targetType) {
			final Number number = convert(value, Long.class, toStrFunc);
			if (null != number) {
				final AtomicLong longValue = new AtomicLong();
				longValue.set(number.longValue());
				return longValue;
			}
		} else if (LongAdder.class == targetType) {
			//jdk8 新增
			final Number number = convert(value, Long.class, toStrFunc);
			if (null != number) {
				final LongAdder longValue = new LongAdder();
				longValue.add(number.longValue());
				return longValue;
			}
		} else if (Float.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).floatValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toFloatObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply((value));
			return StrUtil.isBlank(valueStr) ? null : Float.valueOf(valueStr);

		} else if (Double.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toDoubleObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply((value));
			return StrUtil.isBlank(valueStr) ? null : Double.valueOf(valueStr);
		} else if (DoubleAdder.class == targetType) {
			//jdk8 新增
			final Number number = convert(value, Long.class, toStrFunc);
			if (null != number) {
				final DoubleAdder doubleAdder = new DoubleAdder();
				doubleAdder.add(number.doubleValue());
				return doubleAdder;
			}
		} else if (BigDecimal.class == targetType) {
			return toBigDecimal(value, toStrFunc);
		} else if (BigInteger.class == targetType) {
			return toBigInteger(value, toStrFunc);
		} else if (Number.class == targetType) {
			if (value instanceof Number) {
				return (Number) value;
			} else if (value instanceof Boolean) {
				return BooleanUtil.toInteger((Boolean) value);
			}
			final String valueStr = toStrFunc.apply((value));
			return StrUtil.isBlank(valueStr) ? null : NumberUtil.parseNumber(valueStr);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Number type: {}", targetType.getName()));
	}

	/**
	 * 转换为BigDecimal<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value     被转换的值
	 * @param toStrFunc 转换为字符串的函数规则
	 * @return 结果
	 */
	private static BigDecimal toBigDecimal(Object value, Function<Object, String> toStrFunc) {
		if (value instanceof Number) {
			return NumberUtil.toBigDecimal((Number) value);
		} else if (value instanceof Boolean) {
			return new BigDecimal((boolean) value ? 1 : 0);
		}

		//对于Double类型，先要转换为String，避免精度问题
		return NumberUtil.toBigDecimal(toStrFunc.apply(value));
	}

	/**
	 * 转换为BigInteger<br>
	 * 如果给定的值为空，或者转换失败，返回默认值<br>
	 * 转换失败不会报错
	 *
	 * @param value     被转换的值
	 * @param toStrFunc 转换为字符串的函数规则
	 * @return 结果
	 */
	private static BigInteger toBigInteger(Object value, Function<Object, String> toStrFunc) {
		if (value instanceof Long) {
			return BigInteger.valueOf((Long) value);
		} else if (value instanceof Boolean) {
			return BigInteger.valueOf((boolean) value ? 1 : 0);
		}

		return NumberUtil.toBigInteger(toStrFunc.apply(value));
	}

	@Override
	protected String convertToStr(Object value) {
		String result = StrUtil.trim(super.convertToStr(value));
		if (StrUtil.isNotEmpty(result)) {
			final char c = Character.toUpperCase(result.charAt(result.length() - 1));
			if (c == 'D' || c == 'L' || c == 'F') {
				// 类型标识形式（例如123.6D）
				return StrUtil.subPre(result, -1);
			}
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<Number> getTargetType() {
		return (Class<Number>) this.targetType;
	}
}
