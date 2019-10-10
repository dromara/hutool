package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JDK8中新加入的java.time包对象解析转换器<br>
 * 通过反射调用“parse方法”，支持的对象包括：
 * 
 * <pre>
 * java.time.LocalDateTime
 * java.time.LocalDate
 * java.time.LocalTime
 * java.time.ZonedDateTime
 * java.time.OffsetDateTime
 * java.time.OffsetTime
 * java.time.Period
 * java.time.Instant
 * </pre>
 * 
 * @author looly
 *
 */
public class Jdk8DateConverter extends AbstractConverter<Object> {
	private static final long serialVersionUID = 1L;

	/** 支持的JDK中的类名 */
	public static String[] supportClassNames = new String[] { //
			"java.time.LocalDateTime", //
			"java.time.LocalDate", //
			"java.time.LocalTime", //
			"java.time.ZonedDateTime", //
			"java.time.OffsetDateTime", //
			"java.time.OffsetTime", //
			"java.time.Period", //
			"java.time.Instant"//
	};

	private Class<?> targetType;
	/** 日期格式化 */
	private String format;

	/**
	 * 构造
	 * 
	 * @param targetType 目标类型
	 */
	public Jdk8DateConverter(Class<?> targetType) {
		this.targetType = targetType;
	}

	/**
	 * 构造
	 * 
	 * @param targetType 目标类型
	 * @param format 日期格式
	 */
	public Jdk8DateConverter(Class<?> targetType, String format) {
		this.targetType = targetType;
		this.format = format;
	}

	/**
	 * 获取日期格式
	 * 
	 * @return 设置日期格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置日期格式
	 * 
	 * @param format 日期格式
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	protected Object convertInternal(Object value) {
		if (value instanceof Long) {
			return parseFromLong((Long) value);
		} else {
			return parseFromCharSequence(convertToStr(value));
		}
	}

	/**
	 * 通过反射从字符串转java.time中的对象
	 * 
	 * @param value 字符串值
	 * @return 日期对象
	 */
	private Object parseFromCharSequence(CharSequence value) {
		Method method;
		if (null != this.format) {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.format);
			method = ReflectUtil.getMethod(this.targetType, "parse", CharSequence.class, DateTimeFormatter.class);
			if(Instant.class.isAssignableFrom(this.targetType)){
				return formatter.parse(value, Instant::from);
			}
			return ReflectUtil.invokeStatic(method, value, formatter);
		} else {
			method = ReflectUtil.getMethod(this.targetType, "parse", CharSequence.class);
			return ReflectUtil.invokeStatic(method, value);
		}
	}

	/**
	 * 通过反射将Long型时间戳转换为java.time中的对象
	 *
	 * @param time 时间戳
	 * @return java.time中的对象
	 */
	private Object parseFromLong(Long time) {
		String targetName = this.targetType.getName();
		if ("java.time.Instant".equals(targetName)) {
			return Instant.ofEpochMilli(time);
		}
		return null;
	}
}
