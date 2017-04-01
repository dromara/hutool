package com.xiaoleilu.hutool.json;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.convert.ConvertException;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.NumberUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 * @author Looly
 *
 */
final class InternalJSONUtil {
	
	private InternalJSONUtil() {}
	
	/**
	 * 写入值到Writer
	 * @param writer Writer
	 * @param value 值
	 * @param indentFactor
	 * @param indent 缩进空格数
	 * @return Writer
	 * @throws JSONException
	 * @throws IOException
	 */
	protected static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
		if (value == null || value.equals(null)) {
			writer.write("null");
		} else if (value instanceof JSON) {
			((JSON) value).write(writer, indentFactor, indent);
		}else if (value instanceof Map) {
			new JSONObject((Map<?, ?>) value).write(writer, indentFactor, indent);
		} else if (value instanceof Collection) {
			new JSONArray((Collection<?>) value).write(writer, indentFactor, indent);
		} else if (value.getClass().isArray()) {
			new JSONArray(value).write(writer, indentFactor, indent);
		} else if (value instanceof Number) {
			writer.write(NumberUtil.toStr((Number) value));
		} else if (value instanceof Boolean) {
			writer.write(value.toString());
		} else if (value instanceof JSONString) {
			Object o;
			try {
				o = ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
			writer.write(o != null ? o.toString() : JSONUtil.quote(value.toString()));
		} else {
			JSONUtil.quote(value.toString(), writer);
		}
		return writer;
	}

	/**
	 * 缩进，使用空格符
	 * @param writer
	 * @param indent
	 * @throws IOException
	 */
	protected static final void indent(Writer writer, int indent) throws IOException {
		for (int i = 0; i < indent; i += 1) {
			writer.write(' ');
		}
	}
	
	/**
	 * 如果对象是Number 且是 NaN or infinite，将抛出异常
	 * @param obj 被检查的对象
	 * @throws JSONException If o is a non-finite number.
	 */
	protected static void testValidity(Object obj) throws JSONException {
		if(false == ObjectUtil.isValidIfNumber(obj)){
			throw new JSONException("JSON does not allow non-finite numbers.");
		}
	}
	
	/**
	 * 值转为String，用于JSON中。
	 * If the object has an value.toJSONString() method, then that method will be used to produce the JSON text. <br>
	 * The method is required to produce a strictly conforming text. <br>
	 * If the object does not contain a toJSONString method (which is the most common case), then a text will be produced by other means. <br>
	 * If the value is an array or Collection, then a JSONArray will be made from it and its toJSONString method will be called. <br>
	 * If the value is a MAP, then a JSONObject will be made from it and its toJSONString method will be called. <br>
	 * Otherwise, the value's toString method will be called, and the result will be quoted.<br>
	 *
	 * @param value 需要转为字符串的对象
	 * @return 字符串
	 * @throws JSONException If the value is or contains an invalid number.
	 */
	protected static String valueToString(Object value) throws JSONException {
		if (value == null || value.equals(null)) {
			return "null";
		}
		if (value instanceof JSONString) {
			try {
				return ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
		}else if (value instanceof Number) {
			return NumberUtil.toStr((Number) value);
		}else if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		}else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		}else if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			return new JSONArray(coll).toString();
		}else if (value.getClass().isArray()) {
			return new JSONArray(value).toString();
		}else{
			return JSONUtil.quote(value.toString());
		}
	}
	
	/**
	 * 尝试转换字符串为number, boolean, or null，无法转换返回String
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	protected static Object stringToValue(String string) {
		Double d;
		if(null == string || "null".equalsIgnoreCase(string)){
			return JSONNull.NULL;
		}
		
		if (StrUtil.EMPTY.equals(string)) {
			return string;
		}
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}

		/* If it might be a number, try converting it. If a number cannot be produced, then the value will just be a string. */
		char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '-') {
			try {
				if (string.indexOf('.') > -1 || string.indexOf('e') > -1 || string.indexOf('E') > -1) {
					d = Double.valueOf(string);
					if (!d.isInfinite() && !d.isNaN()) {
						return d;
					}
				} else {
					Long myLong = new Long(string);
					if (string.equals(myLong.toString())) {
						if (myLong == myLong.intValue()) {
							return myLong.intValue();
						} else {
							return myLong;
						}
					}
				}
			} catch (Exception ignore) {
			}
		}
		return string;
	}
	
	/**
	 * 将Property的键转化为JSON形式<br>
	 * 用于识别类似于：com.luxiaolei.package.hutool这类用点隔开的键
	 * 
	 * @param jsonObject JSONObject
	 * @param key 键
	 * @param value 值
	 * @return JSONObject
	 */
	protected static JSONObject propertyPut(JSONObject jsonObject, Object key, Object value){
		String keyStr = Convert.toStr(key);
		String[] path = StrUtil.split(keyStr, StrUtil.DOT);
		int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = new JSONObject();
				target.put(segment, nextTarget);
			}
			target = nextTarget;
		}
		target.put(path[last], value);
		return jsonObject;
	}
	
	/**
	 * JSON或者
	 * @param jsonObject JSON对象
	 * @param bean 目标Bean
	 * @param ignoreError 是否忽略转换错误
	 * @return 目标Bean
	 */
	protected static <T> T toBean(JSONObject jsonObject, T bean, boolean ignoreError){
		Class<?> beanClass = bean.getClass();
		try {
			PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(beanClass);
			String propertyName;
			Object value;
			for (PropertyDescriptor property : propertyDescriptors) {
				propertyName = property.getName();
				value = jsonObject.get(propertyName);
				if (null == value) {
					// 此处取得的值为空时跳过
					continue;
				}

				try {
					property.getWriteMethod().invoke(bean, jsonConvert(property.getPropertyType(), value));
				} catch (Exception e) {
					if(ignoreError){
//						StaticLog.warn("Inject [{}] error: {}", property.getName(), e.getMessage());
						continue;
					}else{
						throw new JSONException(e, "Inject [{}] error!", property.getName());
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
		return bean;
	}
	
	/**
	 * JSON递归转换<br>
	 * 首先尝试JDK类型转换，如果失败尝试JSON转Bean
	 * @param type 目标类型
	 * @param value 值
	 * @return 目标类型的值
	 * @throws ConvertException 转换失败
	 */
	private static <T> T jsonConvert(Class<T> type, Object value) throws ConvertException{
		T targetValue = null;
		try {
			targetValue = ConverterRegistry.getInstance().convert(type, value);
		} catch (ConvertException e) {
			//ignore
		}
		
		//非标准转换格式
		if(null == targetValue){
			
			//子对象递归转换
			if(value instanceof JSONObject){
				targetValue = JSONUtil.toBean((JSONObject)targetValue, type, false);
			}
		}
		
		if(null == targetValue){
			throw new ConvertException("Can not convert to type [{}]", type.getName());
		}
		
		return targetValue;
	}
}
