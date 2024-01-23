/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.BooleanUtil;
import org.dromara.hutool.core.util.ByteUtil;

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
 * <li>{@code java.lang.Byte}</li>
 * <li>{@code java.lang.Short}</li>
 * <li>{@code java.lang.Integer}</li>
 * <li>{@code java.util.concurrent.atomic.AtomicInteger}</li>
 * <li>{@code java.lang.Long}</li>
 * <li>{@code java.util.concurrent.atomic.AtomicLong}</li>
 * <li>{@code java.lang.Float}</li>
 * <li>{@code java.lang.Double}</li>
 * <li>{@code java.math.BigDecimal}</li>
 * <li>{@code java.math.BigInteger}</li>
 * </ul>
 *
 * @author Looly
 */
public class NumberConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final NumberConverter INSTANCE = new NumberConverter();

	@SuppressWarnings("unchecked")
	@Override
	protected Number convertInternal(final Class<?> targetClass, final Object value) {
		return convert(value, (Class<? extends Number>) targetClass, this::convertToStr);
	}

	@Override
	protected String convertToStr(final Object value) {
		final String result = StrUtil.trim(super.convertToStr(value));
		if(StrUtil.isEmpty(result)){
			throw new ConvertException("Can not convert empty value to Number!");
		}

		if (result.length() > 1) {
			// 非单个字符才判断末尾的标识符
			final char c = Character.toUpperCase(result.charAt(result.length() - 1));
			if (c == 'D' || c == 'L' || c == 'F') {
				// 类型标识形式（例如123.6D）
				return StrUtil.subPre(result, -1);
			}
		}

		return result;
	}

	/**
	 * 转换对象为数字，支持的对象包括：
	 * <ul>
	 *     <li>Number对象</li>
	 *     <li>Boolean</li>
	 *     <li>byte[]</li>
	 *     <li>String</li>
	 * </ul>
	 *
	 *
	 * @param value      对象值
	 * @param targetType 目标的数字类型
	 * @param toStrFunc  转换为字符串的函数
	 * @return 转换后的数字
	 * @since 5.5.0
	 */
	protected static Number convert(final Object value, final Class<? extends Number> targetType, final Function<Object, String> toStrFunc) {
		// 枚举转换为数字默认为其顺序
		if (value instanceof Enum) {
			return convert(((Enum<?>) value).ordinal(), targetType, toStrFunc);
		}

		// since 5.7.18
		if(value instanceof byte[]){
			return ByteUtil.toNumber((byte[])value, targetType, ByteUtil.DEFAULT_ORDER);
		}

		if (Byte.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).byteValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toByteObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply(value);
			try{
				return Byte.valueOf(valueStr);
			} catch (final NumberFormatException e){
				return NumberUtil.parseNumber(valueStr).byteValue();
			}
		} else if (Short.class == targetType) {
			if (value instanceof Number) {
				return ((Number) value).shortValue();
			} else if (value instanceof Boolean) {
				return BooleanUtil.toShortObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply((value));
			try{
				return Short.valueOf(valueStr);
			} catch (final NumberFormatException e){
				return NumberUtil.parseNumber(valueStr).shortValue();
			}
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
			return NumberUtil.parseInt(valueStr);
		} else if (AtomicInteger.class == targetType) {
			final Number number = convert(value, Integer.class, toStrFunc);
			if (null != number) {
				return new AtomicInteger(number.intValue());
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
			return NumberUtil.parseLong(valueStr);
		} else if (AtomicLong.class == targetType) {
			final Number number = convert(value, Long.class, toStrFunc);
			if (null != number) {
				return new AtomicLong(number.longValue());
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
			return NumberUtil.parseFloat(valueStr);
		} else if (Double.class == targetType) {
			if (value instanceof Number) {
				return NumberUtil.toDouble((Number) value);
			} else if (value instanceof Boolean) {
				return BooleanUtil.toDoubleObj((Boolean) value);
			}
			final String valueStr = toStrFunc.apply((value));
			return NumberUtil.parseDouble(valueStr);
		} else if (DoubleAdder.class == targetType) {
			//jdk8 新增
			final Number number = convert(value, Double.class, toStrFunc);
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
			return NumberUtil.parseNumber(valueStr);
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
	private static BigDecimal toBigDecimal(final Object value, final Function<Object, String> toStrFunc) {
		if (value instanceof Number) {
			return NumberUtil.toBigDecimal((Number) value);
		} else if (value instanceof Boolean) {
			return ((boolean) value) ? BigDecimal.ONE : BigDecimal.ZERO;
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
	private static BigInteger toBigInteger(final Object value, final Function<Object, String> toStrFunc) {
		if (value instanceof Long) {
			return BigInteger.valueOf((Long) value);
		} else if (value instanceof Boolean) {
			return (boolean) value ? BigInteger.ONE : BigInteger.ZERO;
		}

		return NumberUtil.toBigInteger(toStrFunc.apply(value));
	}
}
