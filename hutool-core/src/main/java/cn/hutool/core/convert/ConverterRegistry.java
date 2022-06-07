package cn.hutool.core.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.impl.ArrayConverter;
import cn.hutool.core.convert.impl.AtomicBooleanConverter;
import cn.hutool.core.convert.impl.AtomicIntegerArrayConverter;
import cn.hutool.core.convert.impl.AtomicLongArrayConverter;
import cn.hutool.core.convert.impl.AtomicReferenceConverter;
import cn.hutool.core.convert.impl.BeanConverter;
import cn.hutool.core.convert.impl.BooleanConverter;
import cn.hutool.core.convert.impl.CalendarConverter;
import cn.hutool.core.convert.impl.CharacterConverter;
import cn.hutool.core.convert.impl.CharsetConverter;
import cn.hutool.core.convert.impl.ClassConverter;
import cn.hutool.core.convert.impl.CollectionConverter;
import cn.hutool.core.convert.impl.CurrencyConverter;
import cn.hutool.core.convert.impl.DateConverter;
import cn.hutool.core.convert.impl.DurationConverter;
import cn.hutool.core.convert.impl.EnumConverter;
import cn.hutool.core.convert.impl.LocaleConverter;
import cn.hutool.core.convert.impl.MapConverter;
import cn.hutool.core.convert.impl.NumberConverter;
import cn.hutool.core.convert.impl.OptConverter;
import cn.hutool.core.convert.impl.OptionalConverter;
import cn.hutool.core.convert.impl.PathConverter;
import cn.hutool.core.convert.impl.PeriodConverter;
import cn.hutool.core.convert.impl.PrimitiveConverter;
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
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.core.reflect.TypeUtil;
import cn.hutool.core.util.ObjUtil;
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
import java.util.Collection;
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
 * 转换器登记中心
 * <p>
 * 将各种类型Convert对象放入登记中心，通过convert方法查找目标类型对应的转换器，将被转换对象转换之。
 * </p>
 * <p>
 * 在此类中，存放着默认转换器和自定义转换器，默认转换器是Hutool中预定义的一些转换器，自定义转换器存放用户自定的转换器。
 * </p>
 *
 * @author Looly
 */
public class ConverterRegistry implements Serializable {
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
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final ConverterRegistry INSTANCE = new ConverterRegistry();
	}

	/**
	 * 获得单例的 ConverterRegistry
	 *
	 * @return ConverterRegistry
	 */
	public static ConverterRegistry getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * 构造
	 */
	public ConverterRegistry() {
		registerDefault();
		registerCustomBySpi();
	}

	/**
	 * 登记自定义转换器
	 *
	 * @param type      转换的目标类型
	 * @param converter 转换器
	 * @return ConverterRegistry
	 */
	public ConverterRegistry putCustom(final Type type, final Converter converter) {
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
	 * 转换值为指定类型
	 *
	 * @param <T>           转换的目标类型（转换器转换到的类型）
	 * @param type          类型目标
	 * @param value         被转换值
	 * @param defaultValue  默认值
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(Type type, final Object value, final T defaultValue, final boolean isCustomFirst) throws ConvertException {
		if (TypeUtil.isUnknown(type) && null == defaultValue) {
			// 对于用户不指定目标类型的情况，返回原值
			return (T) value;
		}
		if (ObjUtil.isNull(value)) {
			return defaultValue;
		}
		if (TypeUtil.isUnknown(type)) {
			if(null == defaultValue){
				throw new ConvertException("Unsupported convert to unknow type: {}", type);
			}
			type = defaultValue.getClass();
		}

		if (type instanceof TypeReference) {
			type = ((TypeReference<?>) type).getType();
		}

		// 标准转换器
		final Converter converter = getConverter(type, isCustomFirst);
		if (null != converter) {
			return converter.convert(type, value, defaultValue);
		}

		Class<T> rowType = (Class<T>) TypeUtil.getClass(type);
		if (null == rowType) {
			if (null != defaultValue) {
				rowType = (Class<T>) defaultValue.getClass();
			} else {
				throw new ConvertException("Can not get class from type: {}", type);
			}
		}

		// 特殊类型转换，包括Collection、Map、强转、Array等
		final T result = convertSpecial(type, rowType, value, defaultValue);
		if (null != result) {
			return result;
		}

		// 尝试转Bean
		if (BeanUtil.isBean(rowType)) {
			return (T) BeanConverter.INSTANCE.convert(type, value);
		}

		// 无法转换
		throw new ConvertException("Can not convert from {}: [{}] to [{}]", value.getClass().getName(), value, type.getTypeName());
	}

	/**
	 * 转换值为指定类型<br>
	 * 自定义转换器优先
	 *
	 * @param <T>          转换的目标类型（转换器转换到的类型）
	 * @param type         类型
	 * @param value        值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(final Type type, final Object value, final T defaultValue) throws ConvertException {
		return convert(type, value, defaultValue, true);
	}

	/**
	 * 转换值为指定类型
	 *
	 * @param <T>   转换的目标类型（转换器转换到的类型）
	 * @param type  类型
	 * @param value 值
	 * @return 转换后的值，默认为{@code null}
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(final Type type, final Object value) throws ConvertException {
		return convert(type, value, null);
	}

	// ----------------------------------------------------------- Private method start

	/**
	 * 特殊类型转换<br>
	 * 包括：
	 *
	 * <pre>
	 * Collection
	 * Map
	 * 强转（无需转换）
	 * 数组
	 * </pre>
	 *
	 * @param <T>          转换的目标类型（转换器转换到的类型）
	 * @param type         类型
	 * @param value        值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	private <T> T convertSpecial(final Type type, final Class<T> rowType, final Object value, final T defaultValue) {
		if (null == rowType) {
			return null;
		}

		// 集合转换（含有泛型参数，不可以默认强转）
		if (Collection.class.isAssignableFrom(rowType)) {
			return (T) CollectionConverter.INSTANCE.convert(type, value, (Collection<?>) defaultValue);
		}

		// Map类型（含有泛型参数，不可以默认强转）
		if (Map.class.isAssignableFrom(rowType)) {
			return (T) MapConverter.INSTANCE.convert(type, value, (Map<?, ?>) defaultValue);
		}

		// 默认强转
		if (rowType.isInstance(value)) {
			return (T) value;
		}

		// 原始类型转换
		if(rowType.isPrimitive()){
			return PrimitiveConverter.INSTANCE.convert(type, value, defaultValue);
		}

		// 数字类型转换
		if(Number.class.isAssignableFrom(rowType)){
			return NumberConverter.INSTANCE.convert(type, value, defaultValue);
		}

		// 枚举转换
		if (rowType.isEnum()) {
			return EnumConverter.INSTANCE.convert(type, value, defaultValue);
		}

		// 数组转换
		if (rowType.isArray()) {
			return ArrayConverter.INSTANCE.convert(type, value, defaultValue);
		}

		// 表示非需要特殊转换的对象
		return null;
	}

	/**
	 * 注册默认转换器
	 *
	 * @return 转换器
	 */
	private ConverterRegistry registerDefault() {
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

		return this;
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
	// ----------------------------------------------------------- Private method end
}
