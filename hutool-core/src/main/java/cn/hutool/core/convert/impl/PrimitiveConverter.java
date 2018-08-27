package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.util.StrUtil;

/**
 * 原始类型转换器<br>
 * 支持类型为：<br>
 * <ul>
 * 		<li><code>byte</code></li>
 * 		<li><code>short</code></li>
 *		 <li><code>int</code></li>
 *		 <li><code>long</code></li>
 * 		<li><code>float</code></li>
 * 		<li><code>double</code></li>
 * 		<li><code>char</code></li>
 * 		<li><code>boolean</code></li>
 * </ul>
 * 
 * @author Looly
 *
 */
public class PrimitiveConverter extends AbstractConverter<Object> {
	
	private Class<?> targetType;

	/**
	 * 构造<br>
	 * @param clazz 需要转换的原始
	 * @throws IllegalArgumentException 传入的转换类型非原始类型时抛出
	 */
	public PrimitiveConverter(Class<?> clazz) {
		if(null == clazz){
			throw new NullPointerException("PrimitiveConverter not allow null target type!");
		}else if(false == clazz.isPrimitive()){
			throw new IllegalArgumentException("[" + clazz + "] is not a primitive class!");
		}
		this.targetType = clazz;
	}

	@Override
	protected Object convertInternal(Object value) {
		try {
			if (byte.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).byteValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Byte.parseByte(valueStr);
				
			} else if (short.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).shortValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Short.parseShort(valueStr);
				
			} else if (int.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).intValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Integer.parseInt(valueStr);
				
			} else if (long.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).longValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Long.parseLong(valueStr);
				
			} else if (float.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).floatValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Float.parseFloat(valueStr);
				
			} else if (double.class == this.targetType) {
				if (value instanceof Number) {
					return ((Number) value).doubleValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return Double.parseDouble(valueStr);
				
			} else if (char.class == this.targetType) {
				if(value instanceof Character){
					return ((Character)value).charValue();
				}
				final String valueStr = convertToStr(value);
				if (StrUtil.isBlank(valueStr)) {
					return 0;
				}
				return valueStr.charAt(0);
			} else if (boolean.class == this.targetType) {
				if(value instanceof Boolean){
					return ((Boolean)value).booleanValue();
				}
				String valueStr = convertToStr(value);
				return parseBoolean(valueStr);
			}
		} catch (Exception e) {
			// Ignore Exception
		}
		return 0;
	}
	
	/**
	 * 转换字符串为boolean值
	 * @param valueStr 字符串
	 * @return boolean值
	 */
	static boolean parseBoolean(String valueStr){
		if (StrUtil.isNotBlank(valueStr)) {
			valueStr = valueStr.trim().toLowerCase();
			switch (valueStr) {
				case "true":
					return true;
				case "yes":
					return true;
				case "y":
					return true;
				case "ok":
					return true;
				case "1":
					return true;
				case "on":
					return true;
				case "是":
					return true;
				case "对":
					return true;
				case "真":
					return true;
			}
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<Object> getTargetType() {
		return (Class<Object>) this.targetType;
	}
}
