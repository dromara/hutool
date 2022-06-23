package cn.hutool.core.convert;

import cn.hutool.core.convert.impl.AtomicBooleanConverter;
import cn.hutool.core.convert.impl.AtomicIntegerArrayConverter;
import cn.hutool.core.convert.impl.AtomicLongArrayConverter;
import cn.hutool.core.convert.impl.AtomicReferenceConverter;
import cn.hutool.core.convert.impl.BooleanConverter;
import cn.hutool.core.convert.impl.CalendarConverter;
import cn.hutool.core.convert.impl.CharacterConverter;
import cn.hutool.core.convert.impl.CharsetConverter;
import cn.hutool.core.convert.impl.ClassConverter;
import cn.hutool.core.convert.impl.CurrencyConverter;
import cn.hutool.core.convert.impl.DateConverter;
import cn.hutool.core.convert.impl.DurationConverter;
import cn.hutool.core.convert.impl.LocaleConverter;
import cn.hutool.core.convert.impl.OptConverter;
import cn.hutool.core.convert.impl.OptionalConverter;
import cn.hutool.core.convert.impl.PathConverter;
import cn.hutool.core.convert.impl.PeriodConverter;
import cn.hutool.core.convert.impl.ReferenceConverter;
import cn.hutool.core.convert.impl.StackTraceElementConverter;
import cn.hutool.core.convert.impl.StringConverter;
import cn.hutool.core.convert.impl.TemporalAccessorConverter;
import cn.hutool.core.convert.impl.TimeZoneConverter;
import cn.hutool.core.convert.impl.URIConverter;
import cn.hutool.core.convert.impl.URLConverter;
import cn.hutool.core.convert.impl.UUIDConverter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.reflect.ClassUtil;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.util.ServiceLoaderUtil;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
	 * 默认类型转换器
	 */
	private Map<Type, Converter> defaultConverterMap;
	/**
	 * 用户自定义类型转换器
	 */
	private volatile Map<Type, Converter> customConverterMap;

	/**
	 * 构造
	 */
	public RegisterConverter() {
		registerDefault();
		registerCustomBySpi();
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
		return (null == defaultConverterMap) ? null : defaultConverterMap.get(type);
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
					customConverterMap = new ConcurrentHashMap<>();
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
		defaultConverterMap = new ConcurrentHashMap<>();

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
		defaultConverterMap.put(java.util.Date.class, DateConverter.INSTANCE);
		defaultConverterMap.put(DateTime.class, DateConverter.INSTANCE);
		defaultConverterMap.put(java.sql.Date.class, DateConverter.INSTANCE);
		defaultConverterMap.put(java.sql.Time.class, DateConverter.INSTANCE);
		defaultConverterMap.put(java.sql.Timestamp.class, DateConverter.INSTANCE);

		// 日期时间 JDK8+(since 5.0.0)
		defaultConverterMap.put(TemporalAccessor.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(Instant.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(LocalDateTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(LocalDate.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(LocalTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(ZonedDateTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(OffsetDateTime.class, TemporalAccessorConverter.INSTANCE);
		defaultConverterMap.put(OffsetTime.class, TemporalAccessorConverter.INSTANCE);
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
		defaultConverterMap.put(Class.class, new ClassConverter());
		defaultConverterMap.put(TimeZone.class, new TimeZoneConverter());
		defaultConverterMap.put(Locale.class, new LocaleConverter());
		defaultConverterMap.put(Charset.class, new CharsetConverter());
		defaultConverterMap.put(Path.class, new PathConverter());
		defaultConverterMap.put(Currency.class, new CurrencyConverter());// since 3.0.8
		defaultConverterMap.put(UUID.class, new UUIDConverter());// since 4.0.10
		defaultConverterMap.put(StackTraceElement.class, new StackTraceElementConverter());// since 4.5.2
		defaultConverterMap.put(Optional.class, new OptionalConverter());// since 5.0.0
		defaultConverterMap.put(Opt.class, new OptConverter());// since 5.7.16
	}

	/**
	 * 使用SPI加载转换器
	 */
	private void registerCustomBySpi() {
		ServiceLoaderUtil.load(Converter.class).forEach(converter -> {
			try {
				final Type type = TypeUtil.getTypeArgument(ClassUtil.getClass(converter));
				if (null != type) {
					putCustom(type, converter);
				}
			} catch (final Exception ignore) {
				// 忽略注册失败的
			}
		});
	}
}
