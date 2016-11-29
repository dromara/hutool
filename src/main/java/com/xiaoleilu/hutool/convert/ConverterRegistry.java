package com.xiaoleilu.hutool.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaoleilu.hutool.convert.impl.BooleanConverter;
import com.xiaoleilu.hutool.convert.impl.CalendarConverter;
import com.xiaoleilu.hutool.convert.impl.CharacterConverter;
import com.xiaoleilu.hutool.convert.impl.ClassConverter;
import com.xiaoleilu.hutool.convert.impl.DateConverter;
import com.xiaoleilu.hutool.convert.impl.NumberConverter;
import com.xiaoleilu.hutool.convert.impl.PrimitiveConverter;
import com.xiaoleilu.hutool.convert.impl.StringConverter;

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
	public ConverterRegistry registerCustom(Class<?> clazz, Converter<?> converter) {
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
		
		//其它类型
		defaultConverter.put(Date.class, new DateConverter());
		defaultConverter.put(Calendar.class, new CalendarConverter());
		defaultConverter.put(Class.class, new ClassConverter());

		return this;
	}
	//----------------------------------------------------------- Private method end
}
