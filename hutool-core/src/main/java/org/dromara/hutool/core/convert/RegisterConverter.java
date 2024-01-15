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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.convert.impl.*;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.lang.tuple.Triple;
import org.dromara.hutool.core.lang.tuple.Tuple;
import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.reflect.TypeUtil;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于类型注册的转换器，转换器默认提供一些固定的类型转换，用户可调用{@link #putCustom(Type, Converter)} 注册自定义转换规则
 *
 * @author looly
 * @since 6.0.0
 */
public class RegisterConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final RegisterConverter INSTANCE = new RegisterConverter();
	}

	/**
	 * 获得单例的 RegisterConverter
	 *
	 * @return RegisterConverter
	 */
	public static RegisterConverter getInstance() {
		return RegisterConverter.SingletonHolder.INSTANCE;
	}

	/**
	 * 默认类型转换器
	 */
	private Map<Class<?>, Converter> defaultConverterMap;
	/**
	 * 用户自定义类型转换器
	 */
	private volatile Map<Type, Converter> customConverterMap;

	/**
	 * 构造
	 */
	public RegisterConverter() {
		registerDefault();
	}

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		// 标准转换器
		final Converter converter = getConverter(targetType, true);
		if (null != converter) {
			return converter.convert(targetType, value);
		}

		// 无法转换
		throw new ConvertException("Can not convert from {}: [{}] to [{}]", value.getClass().getName(), value, targetType.getTypeName());
	}

	/**
	 * 获得转换器<br>
	 *
	 * @param type          类型
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换器
	 */
	public Converter getConverter(final Type type, final boolean isCustomFirst) {
		Converter converter;
		if (isCustomFirst) {
			converter = this.getCustomConverter(type);
			if (null == converter) {
				converter = this.getDefaultConverter(type);
			}
		} else {
			converter = this.getDefaultConverter(type);
			if (null == converter) {
				converter = this.getCustomConverter(type);
			}
		}
		return converter;
	}

	/**
	 * 获得默认转换器
	 *
	 * @param type 类型
	 * @return 转换器
	 */
	public Converter getDefaultConverter(final Type type) {
		return (null == defaultConverterMap) ? null : defaultConverterMap.get(TypeUtil.getClass(type));
	}

	/**
	 * 获得自定义转换器
	 *
	 * @param type 类型
	 * @return 转换器
	 */
	public Converter getCustomConverter(final Type type) {
		return (null == customConverterMap) ? null : customConverterMap.get(type);
	}

	/**
	 * 登记自定义转换器
	 *
	 * @param type      转换的目标类型
	 * @param converter 转换器
	 * @return ConverterRegistry
	 */
	public RegisterConverter putCustom(final Type type, final Converter converter) {
		if (null == customConverterMap) {
			synchronized (this) {
				if (null == customConverterMap) {
					customConverterMap = new SafeConcurrentHashMap<>();
				}
			}
		}
		customConverterMap.put(type, converter);
		return this;
	}

	/**
	 * 注册默认转换器
	 */
	private void registerDefault() {
		defaultConverterMap = new SafeConcurrentHashMap<>(64);

		// 包装类转换器
		defaultConverterMap.put(Character.class, new CharacterConverter());
		defaultConverterMap.put(Boolean.class, new BooleanConverter());
		defaultConverterMap.put(AtomicBoolean.class, new AtomicBooleanConverter());// since 3.0.8
		defaultConverterMap.put(CharSequence.class, new StringConverter());
		defaultConverterMap.put(String.class, new StringConverter());

		// URI and URL
		defaultConverterMap.put(URI.class, new URIConverter());
		defaultConverterMap.put(URL.class, new URLConverter());

		// 日期时间
		defaultConverterMap.put(Calendar.class, new CalendarConverter());
		defaultConverterMap.put(XMLGregorianCalendar.class, new XMLGregorianCalendarConverter());

		// 日期时间 JDK8+(since 5.0.0)
		defaultConverterMap.put(TemporalAccessor.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(Instant.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(LocalDateTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(LocalDate.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(LocalTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(ZonedDateTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(OffsetDateTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(OffsetTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(DayOfWeek.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(Month.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(MonthDay.class, TemporalAccessorConverter.INSTANCE);

		defaultConverterMap.put(Period.class, new PeriodConverter());
		defaultConverterMap.put(Duration.class, new DurationConverter());

		// Reference
		defaultConverterMap.put(WeakReference.class, ReferenceConverter.INSTANCE);// since 3.0.8
		defaultConverterMap.put(SoftReference.class, ReferenceConverter.INSTANCE);// since 3.0.8
		defaultConverterMap.put(AtomicReference.class, new AtomicReferenceConverter());// since 3.0.8

		//AtomicXXXArray，since 5.4.5
		defaultConverterMap.put(AtomicIntegerArray.class, new AtomicIntegerArrayConverter());
		defaultConverterMap.put(AtomicLongArray.class, new AtomicLongArrayConverter());

		// 其它类型
		defaultConverterMap.put(TimeZone.class, new TimeZoneConverter());
		defaultConverterMap.put(ZoneId.class, new ZoneIdConverter());
		defaultConverterMap.put(Locale.class, new LocaleConverter());
		defaultConverterMap.put(Charset.class, new CharsetConverter());
		defaultConverterMap.put(Path.class, new PathConverter());
		defaultConverterMap.put(Currency.class, new CurrencyConverter());// since 3.0.8
		defaultConverterMap.put(UUID.class, new UUIDConverter());// since 4.0.10
		defaultConverterMap.put(StackTraceElement.class, new StackTraceElementConverter());// since 4.5.2
		defaultConverterMap.put(Optional.class, new OptionalConverter());// since 5.0.0
		defaultConverterMap.put(Opt.class, new OptConverter());// since 5.7.16
		defaultConverterMap.put(Pair.class, PairConverter.INSTANCE);// since 6.0.0
		defaultConverterMap.put(Triple.class, TripleConverter.INSTANCE);// since 6.0.0
		defaultConverterMap.put(Tuple.class, TupleConverter.INSTANCE);// since 6.0.0
	}
}
