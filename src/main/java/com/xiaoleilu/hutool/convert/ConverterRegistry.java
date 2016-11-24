package com.xiaoleilu.hutool.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
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
	private Map<Class<?>, Converter> defaultConverter;

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
		registerDefault();
	}

	/**
	 * 登记
	 * 
	 * @param converter 转换器
	 * @return {@link ConverterRegistry}
	 */
	public ConverterRegistry register(Converter converter) {
		defaultConverter.put(converter.getTargetType(), converter);
		return this;
	}

	/**
	 * 获得转换器
	 * 
	 * @param type 类型
	 * @return 转换器
	 */
	public Converter getConverter(Class<?> type) {
		return defaultConverter.get(type);
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
		Converter converter = getConverter(type);
		if (null != converter) {
			return converter.convert(value, defaultValue);
		}
		return null;
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

	private ConverterRegistry registerDefault() {
		defaultConverter = new ConcurrentHashMap<>();

		
		//原始类型转换器
		register(new PrimitiveConverter(byte.class));
		register(new PrimitiveConverter(short.class));
		register(new PrimitiveConverter(int.class));
		register(new PrimitiveConverter(float.class));
		register(new PrimitiveConverter(double.class));
		register(new PrimitiveConverter(char.class));
		register(new PrimitiveConverter(boolean.class));
		
		//包装类转换器
		register(new StringConverter());
		register(new BooleanConverter());
		register(new CharacterConverter());
		register(new NumberConverter());
		register(new NumberConverter(Byte.class));
		register(new NumberConverter(Short.class));
		register(new NumberConverter(Integer.class));
		register(new NumberConverter(Float.class));
		register(new NumberConverter(Double.class));
		register(new NumberConverter(BigDecimal.class));
		register(new NumberConverter(BigInteger.class));
		
		//其它类型
		register(new DateConverter());
		register(new CalendarConverter());
		register(new ClassConverter());

		return this;
	}
}
