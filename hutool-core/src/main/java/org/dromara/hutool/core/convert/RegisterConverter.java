/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.collection.set.ConcurrentHashSet;
import org.dromara.hutool.core.convert.impl.*;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.lang.tuple.Triple;
import org.dromara.hutool.core.lang.tuple.Tuple;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.stream.StreamUtil;

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
 * 基于类型注册的转换器，提供两种注册方式，按照优先级依次为：
 * <ol>
 *     <li>按照匹配注册，使用{@link #register(MatcherConverter)}。
 *     注册后一旦给定的目标类型和值满足{@link MatcherConverter#match(Type, Class, Object)}，即可调用对应转换器转换。</li>
 *     <li>按照类型注册，使用{@link #register(Type, Converter)}，目标类型一致，即可调用转换。</li>
 * </ol>
 *
 * @author looly
 * @since 6.0.0
 */
public class RegisterConverter extends ConverterWithRoot implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户自定义类型转换器，存储自定义匹配规则的一类对象的转换器
	 */
	private volatile Set<MatcherConverter> converterSet;
	/**
	 * 用户自定义精确类型转换器<br>
	 * 主要存储类型明确（无子类）的转换器
	 */
	private volatile Map<Type, Converter> customConverterMap;
	/**
	 * 默认类型转换器
	 */
	private final Map<Class<?>, Converter> defaultConverterMap;

	/**
	 * 构造
	 *
	 * @param rootConverter 根转换器，用于子转换器转换
	 */
	public RegisterConverter(final Converter rootConverter) {
		super(rootConverter);
		this.defaultConverterMap = initDefault(rootConverter);
	}

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException {
		// 标准转换器
		final Converter converter = getConverter(targetType, value, true);
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
	 * @param value         转换的值
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换器
	 */
	public Converter getConverter(final Type type, final Object value, final boolean isCustomFirst) {
		Converter converter;
		if (isCustomFirst) {
			converter = this.getCustomConverter(type, value);
			if (null == converter) {
				converter = this.getCustomConverter(type);
			}
			if (null == converter) {
				converter = this.getDefaultConverter(type);
			}
		} else {
			converter = this.getDefaultConverter(type);
			if (null == converter) {
				converter = this.getCustomConverter(type, value);
			}
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
		final Class<?> key = null == type ? null : TypeUtil.getClass(type);
		return (null == defaultConverterMap || null == key) ? null : defaultConverterMap.get(key);
	}

	/**
	 * 获得匹配类型的自定义转换器
	 *
	 * @param type  类型
	 * @param value 被转换的值
	 * @return 转换器
	 */
	public Converter getCustomConverter(final Type type, final Object value) {
		return StreamUtil.of(converterSet)
			.filter((predicate) -> predicate.match(type, value))
			.findFirst()
			.orElse(null);
	}

	/**
	 * 获得指定类型对应的自定义转换器
	 *
	 * @param type 类型
	 * @return 转换器
	 */
	public Converter getCustomConverter(final Type type) {
		return (null == customConverterMap) ? null : customConverterMap.get(type);
	}

	/**
	 * 登记自定义转换器，登记的目标类型必须一致
	 *
	 * @param type      转换的目标类型
	 * @param converter 转换器
	 * @return this
	 */
	public RegisterConverter register(final Type type, final Converter converter) {
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
	 * 登记自定义转换器，符合{@link MatcherConverter#match(Type, Class, Object)}则使用其转换器
	 *
	 * @param converter 转换器
	 * @return this
	 */
	public RegisterConverter register(final MatcherConverter converter) {
		if (null == this.converterSet) {
			synchronized (this) {
				if (null == this.converterSet) {
					this.converterSet = new ConcurrentHashSet<>();
				}
			}
		}
		this.converterSet.add(converter);
		return this;
	}

	/**
	 * 初始化默认转换器
	 *
	 * @return 默认转换器
	 */
	private static Map<Class<?>, Converter> initDefault(final Converter rootConverter) {
		final Map<Class<?>, Converter> converterMap = new SafeConcurrentHashMap<>(64);

		// 包装类转换器
		converterMap.put(Character.class, CharacterConverter.INSTANCE);
		converterMap.put(Boolean.class, BooleanConverter.INSTANCE);
		converterMap.put(AtomicBoolean.class, AtomicBooleanConverter.INSTANCE);// since 3.0.8
		final StringConverter stringConverter = new StringConverter();
		converterMap.put(CharSequence.class, stringConverter);
		converterMap.put(String.class, stringConverter);

		// URI and URL
		converterMap.put(URI.class, new URIConverter());
		converterMap.put(URL.class, new URLConverter());

		// 日期时间
		converterMap.put(Calendar.class, new CalendarConverter());
		converterMap.put(XMLGregorianCalendar.class, new XMLGregorianCalendarConverter());

		// 日期时间 JDK8+(since 5.0.0)
		converterMap.put(TemporalAccessor.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(Instant.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(LocalDateTime.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(LocalDate.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(LocalTime.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(ZonedDateTime.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(OffsetDateTime.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(OffsetTime.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(DayOfWeek.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(Month.class, TemporalAccessorConverter.INSTANCE);
		converterMap.put(MonthDay.class, TemporalAccessorConverter.INSTANCE);

		converterMap.put(Period.class, new PeriodConverter());
		converterMap.put(Duration.class, new DurationConverter());

		// Reference
		final ReferenceConverter referenceConverter = new ReferenceConverter(rootConverter);
		converterMap.put(WeakReference.class, referenceConverter);// since 3.0.8
		converterMap.put(SoftReference.class, referenceConverter);// since 3.0.8
		converterMap.put(AtomicReference.class, new AtomicReferenceConverter(rootConverter));// since 3.0.8

		//AtomicXXXArray，since 5.4.5
		converterMap.put(AtomicIntegerArray.class, new AtomicIntegerArrayConverter());
		converterMap.put(AtomicLongArray.class, new AtomicLongArrayConverter());

		// 其它类型
		converterMap.put(Locale.class, new LocaleConverter());
		converterMap.put(Charset.class, new CharsetConverter());
		converterMap.put(Path.class, new PathConverter());
		converterMap.put(Currency.class, new CurrencyConverter());// since 3.0.8
		converterMap.put(UUID.class, new UUIDConverter());// since 4.0.10
		converterMap.put(StackTraceElement.class, new StackTraceElementConverter());// since 4.5.2
		converterMap.put(Optional.class, new OptionalConverter());// since 5.0.0
		converterMap.put(Opt.class, new OptConverter());// since 5.7.16
		converterMap.put(Pair.class, new PairConverter(rootConverter));// since 6.0.0
		converterMap.put(Triple.class, new TripleConverter(rootConverter));// since 6.0.0
		converterMap.put(Tuple.class, TupleConverter.INSTANCE);// since 6.0.0

		return converterMap;
	}
}
