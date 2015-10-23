package com.xiaoleilu.hutool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 属性注入工具类
 * @author xiaoleilu
 *
 */
public class InjectUtil {
	
	private InjectUtil() {}
	
	/**
	 * 注入Request参数<br>
	 * 使用模型名称
	 * @param <T>
	 * @param model 模型
	 * @param request 请求对象
	 * @param isWithModelName 参数是否包含模型名称
	 */
	public static <T> void injectFromRequest(T model, javax.servlet.ServletRequest request, boolean isWithModelName) {
		injectFromRequest(model, model.getClass().getSimpleName(), request, isWithModelName);
	}
	
	/**
	 * 注入Request参数
	 * @param <T>
	 * @param model 模型
	 * @param modelName 模型名称
	 * @param request 请求对象
	 * @param isWithModelName 参数是否包含模型名称
	 */
	public static <T> void injectFromRequest(T model, String modelName, javax.servlet.ServletRequest request, boolean isWithModelName) {
		Method[] methods = model.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (! methodName.startsWith("set")) {
				//只查找Set方法
				continue;
			}
			
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 1)	{
				//Set方法只允许一个参数
				continue;
			}
			
			final String fieldName = StrUtil.getGeneralField(methodName);
			final String paramName = isWithModelName ? (modelName + StrUtil.DOT + fieldName) : fieldName;
			final String value = request.getParameter(paramName);
			if (StrUtil.isEmpty(value)) {
				//此处取得的值为空时跳过，包括null和""
				continue;
			}
			
			try {
				method.invoke(model, Conver.parse(types[0], value));
			} catch (Exception e) {
				throw new UtilException(StrUtil.format("Inject [{}] error!", paramName), e);
			}
		}
	}
	
	/**
	 * 从Map注入，如果key为String类型，忽略大小写
	 * @param <T>
	 * @param model 模型
	 * @param map map对象
	 */
	public static <T> void injectFromMapIgnoreCase(T model, Map<?, ?> map) {
		final Map<Object, Object> map2 = new HashMap<Object, Object>();
		for (Entry<?, ?> entry : map.entrySet()) {
			final Object key = entry.getKey();
			if(key instanceof String) {
				final String keyStr = (String)key;
				map2.put(keyStr.toLowerCase(), entry.getValue());
			}else{
				map2.put(key, entry.getValue());
			}
		}
		injectFromMap(model, map2);
	}
	
	/**
	 * 从Map注入
	 * @param <T>
	 * @param model 模型
	 * @param map map对象
	 */
	public static <T> void injectFromMap(T model, Map<?, ?> map) {
		Method[] methods = model.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (! methodName.startsWith("set")) {
				continue;
			}
			
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 1)	{
				continue;
			}
			
			String fieldName = StrUtil.getGeneralField(methodName);
			Object value = map.get(fieldName);
			if (value == null) {
				continue;
			}
			
			try {
				method.invoke(model, Conver.parse(types[0], value));
			} catch (Exception e) {
				throw new UtilException(StrUtil.format("Inject [{}] error!", fieldName), e);
			}
		}
	}
	
	/**
	 * 转换为Map
	 * @param <T>
	 * @param model 模型
	 * @param isOnlyBasicType 是否只允许基本类型，包括String
	 */
	public static <T> Map<String, Object> toMap(T model, boolean isOnlyBasicType) {
		Map<String, Object> map = new HashMap<String, Object>();
		final Method[] methods = model.getClass().getMethods();
		for (Method method : methods) {
			final String methodName = method.getName();
			if (false == methodName.startsWith("get") || "getClass".equals(methodName)) {
				continue;
			}
			
			final String fieldName = StrUtil.getGeneralField(methodName);
			
			Object value = null;
			try {
				value = method.invoke(model);
			} catch (Exception e) {
				throw new UtilException(StrUtil.format("Inject map [{}] error!", fieldName), e);
			}
			if(value != null) {
				Class<?> valuePprimitive = ClassUtil.castToPrimitive(value.getClass());
				if(value instanceof String || valuePprimitive.isPrimitive() || false == isOnlyBasicType) {
					//字段有效的三个条件：1、String 2、基本类型 3、或者允许非基本类型
					map.put(fieldName, value);
				}
			}
		}
		
		return map;
	}
}
