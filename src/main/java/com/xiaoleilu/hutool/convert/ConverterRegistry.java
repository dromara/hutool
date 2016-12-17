package com.xiaoleilu.hutool.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaoleilu.hutool.convert.impl.ArrayConverter;
import com.xiaoleilu.hutool.convert.impl.BooleanArrayConverter;
import com.xiaoleilu.hutool.convert.impl.BooleanConverter;
import com.xiaoleilu.hutool.convert.impl.ByteArrayConverter;
import com.xiaoleilu.hutool.convert.impl.CalendarConverter;
import com.xiaoleilu.hutool.convert.impl.CharArrayConverter;
import com.xiaoleilu.hutool.convert.impl.CharacterConverter;
import com.xiaoleilu.hutool.convert.impl.CharsetConverter;
import com.xiaoleilu.hutool.convert.impl.ClassConverter;
import com.xiaoleilu.hutool.convert.impl.DateConverter;
import com.xiaoleilu.hutool.convert.impl.DateTimeConverter;
import com.xiaoleilu.hutool.convert.impl.DoubleArrayConverter;
import com.xiaoleilu.hutool.convert.impl.FloatArrayConverter;
import com.xiaoleilu.hutool.convert.impl.IntArrayConverter;
import com.xiaoleilu.hutool.convert.impl.LongArrayConverter;
import com.xiaoleilu.hutool.convert.impl.NumberConverter;
import com.xiaoleilu.hutool.convert.impl.PathConverter;
import com.xiaoleilu.hutool.convert.impl.PrimitiveConverter;
import com.xiaoleilu.hutool.convert.impl.ShortArrayConverter;
import com.xiaoleilu.hutool.convert.impl.SqlDateConverter;
import com.xiaoleilu.hutool.convert.impl.SqlTimeConverter;
import com.xiaoleilu.hutool.convert.impl.SqlTimestampConverter;
import com.xiaoleilu.hutool.convert.impl.StringConverter;
import com.xiaoleilu.hutool.convert.impl.TimeZoneConverter;
import com.xiaoleilu.hutool.convert.impl.URIConverter;
import com.xiaoleilu.hutool.convert.impl.URLConverter;
import com.xiaoleilu.hutool.date.DateTime;

/**
 * 转换器登记
 * 
 * @author Looly
 *
 */
public class ConverterRegistry {
	
	/** 默认类型转换器 */
	private Map<Class<?>, Converter<?>> defaultConverter;
	/** 用户自定义类型转换器 */
	private Map<Class<?>, Converter<?>> customConverter;

	/** 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载 */
	private static class SingletonHolder {
		/** 静态初始化器，由JVM来保证线程安全 */
		private static ConverterRegistry instance = new ConverterRegistry();
	}
	/**
	 * 获得单例的 {@link ConverterRegistry}
	 * @return {@link ConverterRegistry}
	 */
	public static ConverterRegistry getInstance(){
		return SingletonHolder.instance; 
	}

	public ConverterRegistry() {
		defaultConverter();
	}

	/**
	 * 登记自定义转换器
	 * 
	 * @param converter 转换器
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Class<?> clazz, Converter<?> converter) {
		if(null == customConverter){
			synchronized (this) {
				if(null == customConverter){
					customConverter = new ConcurrentHashMap<>();
				}
			}
		}
		customConverter.put(clazz, converter);
		return this;
	}

	/**
	 * 获得转换器
	 * @param <T>
	 * 
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getConverter(Class<T> type) {
		return (Converter<T>) defaultConverter.get(type);
	}
	
	/**
	 * 获得自定义转换器
	 * @param <T>
	 * 
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getCustomConverter(Class<T> type) {
		return (null == customConverter) ? null : (Converter<T>)customConverter.get(type);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 */
	public <T> T convert(Class<T> type, Object value, T defaultValue) {
		Converter<T> converter = getConverter(type);
		if (null == converter) {
//			return defaultValue;
			throw new ConvertException("No Converter for type [{}]", type.getName());
		}
		return converter.convert(value, defaultValue);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值，默认为<code>null</code>
	 */
	public <T> T convert(Class<T> type, Object value) {
		return convert(type, value, null);
	}

	//----------------------------------------------------------- Private method start
	/**
	 * 注册默认转换器
	 * @return 转换器
	 */
	private ConverterRegistry defaultConverter() {
		defaultConverter = new ConcurrentHashMap<>();
		
		//原始类型转换器
		defaultConverter.put(byte.class, new PrimitiveConverter(byte.class));
		defaultConverter.put(short.class, new PrimitiveConverter(short.class));
		defaultConverter.put(int.class, new PrimitiveConverter(int.class));
		defaultConverter.put(long.class, new PrimitiveConverter(long.class));
		defaultConverter.put(float.class, new PrimitiveConverter(float.class));
		defaultConverter.put(double.class, new PrimitiveConverter(double.class));
		defaultConverter.put(char.class, new PrimitiveConverter(char.class));
		defaultConverter.put(boolean.class, new PrimitiveConverter(boolean.class));
		
		//包装类转换器
		defaultConverter.put(String.class, new StringConverter());
		defaultConverter.put(Boolean.class, new BooleanConverter());
		defaultConverter.put(Character.class, new CharacterConverter());
		defaultConverter.put(Number.class, new NumberConverter());
		defaultConverter.put(Byte.class, new NumberConverter(Byte.class));
		defaultConverter.put(Short.class, new NumberConverter(Short.class));
		defaultConverter.put(Integer.class, new NumberConverter(Integer.class));
		defaultConverter.put(Long.class, new NumberConverter(Long.class));
		defaultConverter.put(Float.class, new NumberConverter(Float.class));
		defaultConverter.put(Double.class, new NumberConverter(Double.class));
		defaultConverter.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
		defaultConverter.put(BigInteger.class, new NumberConverter(BigInteger.class));
		
		//数组类型转换器
		defaultConverter.put(Integer[].class, new ArrayConverter<Integer>(Integer.class));
		defaultConverter.put(Long[].class, new ArrayConverter<Long>(Long.class));
		defaultConverter.put(Byte[].class, new ArrayConverter<Byte>(Byte.class));
		defaultConverter.put(Short[].class, new ArrayConverter<Short>(Short.class));
		defaultConverter.put(Float[].class, new ArrayConverter<Float>(Float.class));
		defaultConverter.put(Double[].class, new ArrayConverter<Double>(Double.class));
		defaultConverter.put(Boolean[].class, new ArrayConverter<Boolean>(Boolean.class));
		defaultConverter.put(Character[].class, new ArrayConverter<Character>(Character.class));
		defaultConverter.put(String[].class, new ArrayConverter<String>(String.class));
		
		//原始类型数组转换器
		defaultConverter.put(byte[].class, new ByteArrayConverter());
		defaultConverter.put(short[].class, new ShortArrayConverter());
		defaultConverter.put(int[].class, new IntArrayConverter());
		defaultConverter.put(long[].class, new LongArrayConverter());
		defaultConverter.put(float[].class, new FloatArrayConverter());
		defaultConverter.put(double[].class, new DoubleArrayConverter());
		defaultConverter.put(boolean[].class, new BooleanArrayConverter());
		defaultConverter.put(char[].class, new CharArrayConverter());
		
		//URI and URL
		defaultConverter.put(URI.class, new URIConverter());
		defaultConverter.put(URL.class, new URLConverter());
		
		//日期时间
		defaultConverter.put(Calendar.class, new CalendarConverter());
		defaultConverter.put(Date.class, new DateConverter());
		defaultConverter.put(DateTime.class, new DateTimeConverter());
		defaultConverter.put(java.sql.Date.class, new SqlDateConverter());
		defaultConverter.put(java.sql.Time.class, new SqlTimeConverter());
		defaultConverter.put(java.sql.Timestamp.class, new SqlTimestampConverter());
		
		//其它类型
		defaultConverter.put(Class.class, new ClassConverter());
		defaultConverter.put(TimeZone.class, new TimeZoneConverter());
		defaultConverter.put(Charset.class, new CharsetConverter());
		defaultConverter.put(Path.class, new PathConverter());

		return this;
	}
	//----------------------------------------------------------- Private method end
}
