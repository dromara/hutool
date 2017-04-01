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
import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * 转换器登记中心
 * <p>将各种类型Convert对象放入登记中心，通过convert方法查找目标类型对应的转换器，将被转换对象转换之。</p>
 * <p>在此类中，存放着默认转换器和自定义转换器，默认转换器是Hutool中预定义的一些转换器，自定义转换器存放用户自定的转换器。</p>
 * 
 * @author Looly
 *
 */
public class ConverterRegistry {
	
	/** 默认类型转换器 */
	private Map<Class<?>, Converter<?>> defaultConverterMap;
	/** 用户自定义类型转换器 */
	private Map<Class<?>, Converter<?>> customConverterMap;

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
	 * @param converterClass 转换器类，必须有默认构造方法
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Class<?> clazz, Class<? extends Converter<?>> converterClass) {
		return putCustom(clazz, ClassUtil.newInstance(converterClass));
	}

	/**
	 * 登记自定义转换器
	 * 
	 * @param converter 转换器
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry putCustom(Class<?> clazz, Converter<?> converter) {
		if(null == customConverterMap){
			synchronized (this) {
				if(null == customConverterMap){
					customConverterMap = new ConcurrentHashMap<>();
				}
			}
		}
		customConverterMap.put(clazz, converter);
		return this;
	}

	/**
	 * 获得转换器<br>
	 * @param <T> 转换的目标类型
	 * 
	 * @param type 类型
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换器
	 */
	public <T> Converter<T> getConverter(Class<T> type, boolean isCustomFirst) {
		Converter<T> converter = null;
		if(isCustomFirst){
			converter = this.getCustomConverter(type);
			if(null == converter){
				converter = this.getDefaultConverter(type);
			}
		}else{
			converter = this.getDefaultConverter(type);
			if(null == converter){
				converter = this.getCustomConverter(type);
			}
		}
		return converter;
	}
	
	/**
	 * 获得默认转换器
	 * @param <T>
	 * 
	 * @param type 类型
	 * @return 转换器
	 */
	@SuppressWarnings("unchecked")
	public <T> Converter<T> getDefaultConverter(Class<T> type) {
		return (null == defaultConverterMap) ? null : (Converter<T>)defaultConverterMap.get(type);
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
		return (null == customConverterMap) ? null : (Converter<T>)customConverterMap.get(type);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @param isCustomFirst 是否自定义转换器优先
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(Class<T> type, Object value, T defaultValue, boolean isCustomFirst) throws ConvertException{
		if(null == type && null == defaultValue){
			throw new NullPointerException("[type] and [defaultValue] are both null, we can not know what type to convert !");
		}
		if(null == value ){
			return defaultValue;
		}
		if(null == type){
			type = (Class<T>) defaultValue.getClass();
		}
		if(type.isInstance(value)){
			return (T)value;
		}
		
		Converter<T> converter = getConverter(type, isCustomFirst);
		if (null == converter) {
//			return defaultValue;
			throw new ConvertException("No Converter for type [{}]", type.getName());
		}
		return converter.convert(value, defaultValue);
	}
	
	/**
	 * 转换值为指定类型<br>
	 * 自定义转换器优先
	 * 
	 * @param type 类型
	 * @param value 值
	 * @param defaultValue 默认值
	 * @return 转换后的值
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Class<T> type, Object value, T defaultValue) throws ConvertException {
		return convert(type, value, defaultValue, true);
	}

	/**
	 * 转换值为指定类型
	 * 
	 * @param type 类型
	 * @param value 值
	 * @return 转换后的值，默认为<code>null</code>
	 * @throws ConvertException 转换器不存在
	 */
	public <T> T convert(Class<T> type, Object value) throws ConvertException {
		return convert(type, value, null);
	}

	//----------------------------------------------------------- Private method start
	/**
	 * 注册默认转换器
	 * @return 转换器
	 */
	private ConverterRegistry defaultConverter() {
		defaultConverterMap = new ConcurrentHashMap<>();
		
		//原始类型转换器
		defaultConverterMap.put(byte.class, new PrimitiveConverter(byte.class));
		defaultConverterMap.put(short.class, new PrimitiveConverter(short.class));
		defaultConverterMap.put(int.class, new PrimitiveConverter(int.class));
		defaultConverterMap.put(long.class, new PrimitiveConverter(long.class));
		defaultConverterMap.put(float.class, new PrimitiveConverter(float.class));
		defaultConverterMap.put(double.class, new PrimitiveConverter(double.class));
		defaultConverterMap.put(char.class, new PrimitiveConverter(char.class));
		defaultConverterMap.put(boolean.class, new PrimitiveConverter(boolean.class));
		
		//包装类转换器
		defaultConverterMap.put(String.class, new StringConverter());
		defaultConverterMap.put(Boolean.class, new BooleanConverter());
		defaultConverterMap.put(Character.class, new CharacterConverter());
		defaultConverterMap.put(Number.class, new NumberConverter());
		defaultConverterMap.put(Byte.class, new NumberConverter(Byte.class));
		defaultConverterMap.put(Short.class, new NumberConverter(Short.class));
		defaultConverterMap.put(Integer.class, new NumberConverter(Integer.class));
		defaultConverterMap.put(Long.class, new NumberConverter(Long.class));
		defaultConverterMap.put(Float.class, new NumberConverter(Float.class));
		defaultConverterMap.put(Double.class, new NumberConverter(Double.class));
		defaultConverterMap.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
		defaultConverterMap.put(BigInteger.class, new NumberConverter(BigInteger.class));
		
		//数组类型转换器
		defaultConverterMap.put(Integer[].class, new ArrayConverter<Integer>(Integer.class));
		defaultConverterMap.put(Long[].class, new ArrayConverter<Long>(Long.class));
		defaultConverterMap.put(Byte[].class, new ArrayConverter<Byte>(Byte.class));
		defaultConverterMap.put(Short[].class, new ArrayConverter<Short>(Short.class));
		defaultConverterMap.put(Float[].class, new ArrayConverter<Float>(Float.class));
		defaultConverterMap.put(Double[].class, new ArrayConverter<Double>(Double.class));
		defaultConverterMap.put(Boolean[].class, new ArrayConverter<Boolean>(Boolean.class));
		defaultConverterMap.put(Character[].class, new ArrayConverter<Character>(Character.class));
		defaultConverterMap.put(String[].class, new ArrayConverter<String>(String.class));
		
		//原始类型数组转换器
		defaultConverterMap.put(byte[].class, new ByteArrayConverter());
		defaultConverterMap.put(short[].class, new ShortArrayConverter());
		defaultConverterMap.put(int[].class, new IntArrayConverter());
		defaultConverterMap.put(long[].class, new LongArrayConverter());
		defaultConverterMap.put(float[].class, new FloatArrayConverter());
		defaultConverterMap.put(double[].class, new DoubleArrayConverter());
		defaultConverterMap.put(boolean[].class, new BooleanArrayConverter());
		defaultConverterMap.put(char[].class, new CharArrayConverter());
		
		//URI and URL
		defaultConverterMap.put(URI.class, new URIConverter());
		defaultConverterMap.put(URL.class, new URLConverter());
		
		//日期时间
		defaultConverterMap.put(Calendar.class, new CalendarConverter());
		defaultConverterMap.put(Date.class, new DateConverter());
		defaultConverterMap.put(DateTime.class, new DateTimeConverter());
		defaultConverterMap.put(java.sql.Date.class, new SqlDateConverter());
		defaultConverterMap.put(java.sql.Time.class, new SqlTimeConverter());
		defaultConverterMap.put(java.sql.Timestamp.class, new SqlTimestampConverter());
		
		//其它类型
		defaultConverterMap.put(Class.class, new ClassConverter());
		defaultConverterMap.put(TimeZone.class, new TimeZoneConverter());
		defaultConverterMap.put(Charset.class, new CharsetConverter());
		defaultConverterMap.put(Path.class, new PathConverter());

		return this;
	}
	//----------------------------------------------------------- Private method end
}
