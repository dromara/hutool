package com.xiaoleilu.hutool.convert;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import com.xiaoleilu.hutool.bean.BeanUtil;
import com.xiaoleilu.hutool.convert.impl.ArrayConverter;
import com.xiaoleilu.hutool.convert.impl.AtomicBooleanConverter;
import com.xiaoleilu.hutool.convert.impl.AtomicReferenceConverter;
import com.xiaoleilu.hutool.convert.impl.BooleanConverter;
import com.xiaoleilu.hutool.convert.impl.CalendarConverter;
import com.xiaoleilu.hutool.convert.impl.CharacterConverter;
import com.xiaoleilu.hutool.convert.impl.CharsetConverter;
import com.xiaoleilu.hutool.convert.impl.ClassConverter;
import com.xiaoleilu.hutool.convert.impl.CollectionConverter;
import com.xiaoleilu.hutool.convert.impl.CurrencyConverter;
import com.xiaoleilu.hutool.convert.impl.DateConverter;
import com.xiaoleilu.hutool.convert.impl.MapConverter;
import com.xiaoleilu.hutool.convert.impl.NumberConverter;
import com.xiaoleilu.hutool.convert.impl.PathConverter;
import com.xiaoleilu.hutool.convert.impl.PrimitiveConverter;
import com.xiaoleilu.hutool.convert.impl.ReferenceConverter;
import com.xiaoleilu.hutool.convert.impl.StringConverter;
import com.xiaoleilu.hutool.convert.impl.TimeZoneConverter;
import com.xiaoleilu.hutool.convert.impl.URIConverter;
import com.xiaoleilu.hutool.convert.impl.URLConverter;
import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.ReflectUtil;
import com.xiaoleilu.hutool.util.TypeUtil;

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
 *
 */
public class ConverterRegistry {

	/** 默认类型转换器 */
	private Map<Type, Converter<?>> defaultConverterMap;
	/** 用户自定义类型转换器 */
	private Map<Type, Converter<?>> customConverterMap;

	/** 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载 */
	private static class SingletonHolder {
		/** 静态初始化器，由JVM来保证线程安全 */
		private static ConverterRegistry instance = new ConverterRegistry();
	}

	/**
	 * 获得单例的 {@link ConverterRegistry}
	 * 
	 * @return {@link ConverterRegistry}
	 */
	public static ConverterRegistry getInstance() {
		return SingletonHolder.instance;
	}

	public ConverterRegistry() {
		defaultConverter();
	}

	/**
	 * 登记自定义转换器
	 * 
	 * @param type 转换的目标类型
	 * @param converterClass 转换器类，必须有默认构造方法
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Type type, Class<? extends Converter<?>> converterClass) {
		return putCustom(type, ReflectUtil.newInstance(converterClass));
	}

	/**
	 * 登记自定义转换器
	 * 
	 * @param type 转换的目标类型
	 * @param converter 转换器
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Type type, Converter<?> converter) {
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
	 * @param <T> 转换的目标类型
	 * 
	 * @param type 类型
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换器
	 */
	public <T> Converter<T> getConverter(Type type, boolean isCustomFirst) {
		Converter<T> converter = null;
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
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getDefaultConverter(Type type) {
		return (null == defaultConverterMap) ? null : (Converter<T>) defaultConverterMap.get(type);
	}

	/**
	 * 获得自定义转换器
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * 
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getCustomConverter(Type type) {
		return (null == customConverterMap) ? null : (Converter<T>) customConverterMap.get(type);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(Type type, Object value, T defaultValue, boolean isCustomFirst) throws ConvertException {
		if (null == type && null == defaultValue) {
			throw new NullPointerException("[type] and [defaultValue] are both null, we can not know what type to convert !");
		}
		if (ObjectUtil.isNull(value)) {
			return defaultValue;
		}
		if (null == type) {
			type = defaultValue.getClass();
		}

		final Class<T> rowType = (Class<T>) TypeUtil.getClass(type);
		
		// 集合转换（不可以默认强转）
		if (Collection.class.isAssignableFrom(rowType)) {
			final CollectionConverter collectionConverter = new CollectionConverter(type);
			return (T) collectionConverter.convert(value, (Collection<?>) defaultValue);
		}
		
		//Map类型（不可以默认强转）
		if(Map.class.isAssignableFrom(rowType)) {
			final MapConverter mapConverter = new MapConverter(type);
			return (T) mapConverter.convert(value, (Map<?, ?>)defaultValue);
		}

		// 默认强转
		if (rowType.isInstance(value)) {
			return (T) value;
		}

		// 数组转换
		if (rowType.isArray()) {
			final ArrayConverter arrayConverter = new ArrayConverter(rowType);
			try {
				return (T) arrayConverter.convert(value, defaultValue);
			} catch (Exception e) {
				// 强转失败进行下一步
			}
		}

		final Converter<T> converter = getConverter(type, isCustomFirst);
		if (null != converter) {
			return converter.convert(value, defaultValue);
		}

		// 尝试转Bean
		if (BeanUtil.isBean(rowType) && value instanceof Map) {
			return BeanUtil.mapToBean((Map<?, ?>) value, rowType, true);
		}

		// 无法转换
		throw new ConvertException("No Converter for type [{}]", rowType.getName());
	}

	/**
	 * 转换值为指定类型<br>
	 * 自定义转换器优先
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Type type, Object value, T defaultValue) throws ConvertException {
		return convert(type, value, defaultValue, true);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param <T> 转换的目标类型（转换器转换到的类型）
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值，默认为<code>null</code>
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Type type, Object value) throws ConvertException {
		return convert(type, value, null);
	}

	// ----------------------------------------------------------- Private method start
	/**
	 * 注册默认转换器
	 * 
	 * @return 转换器
	 */
	private ConverterRegistry defaultConverter() {
		defaultConverterMap = new ConcurrentHashMap<>();

		// 原始类型转换器
		defaultConverterMap.put(int.class, new PrimitiveConverter(int.class));
		defaultConverterMap.put(long.class, new PrimitiveConverter(long.class));
		defaultConverterMap.put(byte.class, new PrimitiveConverter(byte.class));
		defaultConverterMap.put(short.class, new PrimitiveConverter(short.class));
		defaultConverterMap.put(float.class, new PrimitiveConverter(float.class));
		defaultConverterMap.put(double.class, new PrimitiveConverter(double.class));
		defaultConverterMap.put(char.class, new PrimitiveConverter(char.class));
		defaultConverterMap.put(boolean.class, new PrimitiveConverter(boolean.class));

		// 包装类转换器
		defaultConverterMap.put(Number.class, new NumberConverter());
		defaultConverterMap.put(Integer.class, new NumberConverter(Integer.class));
		defaultConverterMap.put(AtomicInteger.class, new NumberConverter(AtomicInteger.class));// since 3.0.8
		defaultConverterMap.put(Long.class, new NumberConverter(Long.class));
		defaultConverterMap.put(AtomicLong.class, new NumberConverter(AtomicLong.class));// since 3.0.8
		defaultConverterMap.put(Byte.class, new NumberConverter(Byte.class));
		defaultConverterMap.put(Short.class, new NumberConverter(Short.class));
		defaultConverterMap.put(Float.class, new NumberConverter(Float.class));
		defaultConverterMap.put(Double.class, new NumberConverter(Double.class));
		defaultConverterMap.put(Character.class, new CharacterConverter());
		defaultConverterMap.put(Boolean.class, new BooleanConverter());
		defaultConverterMap.put(AtomicBoolean.class, new AtomicBooleanConverter());// since 3.0.8
		defaultConverterMap.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
		defaultConverterMap.put(BigInteger.class, new NumberConverter(BigInteger.class));
		defaultConverterMap.put(String.class, new StringConverter());

		// 原始类型数组转换器
//		defaultConverterMap.put(int[].class, new ArrayConverter(int[].class));
//		defaultConverterMap.put(long[].class, new ArrayConverter(long[].class));
//		defaultConverterMap.put(byte[].class, new ArrayConverter(byte[].class));
//		defaultConverterMap.put(short[].class, new ArrayConverter(short[].class));
//		defaultConverterMap.put(float[].class, new ArrayConverter(float[].class));
//		defaultConverterMap.put(double[].class, new ArrayConverter(double[].class));
//		defaultConverterMap.put(boolean[].class, new ArrayConverter(boolean[].class));
//		defaultConverterMap.put(char[].class, new ArrayConverter(char[].class));

		// 包装数组类型转换器
//		defaultConverterMap.put(Integer[].class, new ArrayConverter(Integer[].class));
//		defaultConverterMap.put(Long[].class, new ArrayConverter(Long[].class));
//		defaultConverterMap.put(Byte[].class, new ArrayConverter(Byte[].class));
//		defaultConverterMap.put(Short[].class, new ArrayConverter(Short[].class));
//		defaultConverterMap.put(Float[].class, new ArrayConverter(Float[].class));
//		defaultConverterMap.put(Double[].class, new ArrayConverter(Double[].class));
//		defaultConverterMap.put(Boolean[].class, new ArrayConverter(Boolean[].class));
//		defaultConverterMap.put(Character[].class, new ArrayConverter(Character[].class));
//		defaultConverterMap.put(String[].class, new ArrayConverter(String[].class));

		// 集合类型转换器
		// defaultConverterMap.put(Collection.class, new CollectionConverter());
		// defaultConverterMap.put(List.class, new CollectionConverter(List.class));
		// defaultConverterMap.put(ArrayList.class, new CollectionConverter(ArrayList.class));
		// defaultConverterMap.put(Set.class, new CollectionConverter(Set.class));
		// defaultConverterMap.put(HashSet.class, new CollectionConverter(HashSet.class));
		
		// URI and URL
		defaultConverterMap.put(URI.class, new URIConverter());
		defaultConverterMap.put(URL.class, new URLConverter());

		// 日期时间
		defaultConverterMap.put(Calendar.class, new CalendarConverter());
		defaultConverterMap.put(java.util.Date.class, new DateConverter(java.util.Date.class));
		defaultConverterMap.put(DateTime.class, new DateConverter(DateTime.class));
		defaultConverterMap.put(java.sql.Date.class, new DateConverter(java.sql.Date.class));
		defaultConverterMap.put(java.sql.Time.class, new DateConverter(java.sql.Time.class));
		defaultConverterMap.put(java.sql.Timestamp.class, new DateConverter(java.sql.Timestamp.class));

		// Reference
		defaultConverterMap.put(WeakReference.class, new ReferenceConverter(WeakReference.class));// since 3.0.8
		defaultConverterMap.put(SoftReference.class, new ReferenceConverter(SoftReference.class));// since 3.0.8
		defaultConverterMap.put(AtomicReference.class, new AtomicReferenceConverter());// since 3.0.8

		// 其它类型
		defaultConverterMap.put(Class.class, new ClassConverter());
		defaultConverterMap.put(TimeZone.class, new TimeZoneConverter());
		defaultConverterMap.put(Charset.class, new CharsetConverter());
		defaultConverterMap.put(Path.class, new PathConverter());
		defaultConverterMap.put(Currency.class, new CurrencyConverter());// since 3.0.8

		return this;
	}
	// ----------------------------------------------------------- Private method end
}
