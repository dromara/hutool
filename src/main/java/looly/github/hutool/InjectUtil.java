package looly.github.hutool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import looly.github.hutool.exceptions.UtilException;

/**
 * 属性注入工具类
 * @author xiaoleilu
 *
 */
public class InjectUtil {
	
	private InjectUtil() {
	}
	
	/**
	 * 注入Request参数<br>
	 * 使用模型名称
	 * @param model 模型
	 * @param request 请求对象
	 * @param isWithModelName 参数是否包含模型名称
	 */
	public static void injectFromRequest(Object model, HttpServletRequest request, boolean isWithModelName) {
		injectFromRequest(model, model.getClass().getSimpleName(), request, isWithModelName);
	}
	
	/**
	 * 注入Request参数
	 * @param model 模型
	 * @param modelName 模型名称
	 * @param request 请求对象
	 * @param isWithModelName 参数是否包含模型名称
	 */
	public static void injectFromRequest(Object model, String modelName, HttpServletRequest request, boolean isWithModelName) {
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
			
			String fieldName = StrUtil.lowerFirst(methodName.substring(3));
			String paramName = isWithModelName ? (modelName + StrUtil.DOT + fieldName) : fieldName;
			String value = request.getParameter(paramName);
			if (StrUtil.isEmpty(value)) {
				//此处取得的值为空时跳过，包括null和""
				continue;
			}
			
			try {
				method.invoke(model, ClassUtil.parse(types[0], value));
			} catch (Exception e) {
				throw new UtilException(StrUtil.format("Inject [{}] error!", paramName), e);
			}
		}
	}
	
	/**
	 * 从Map注入
	 * @param model 模型
	 * @param map map对象
	 */
	public static void injectFromMap(Object model, Map<?, ?> map) {
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
			
			String fieldName = StrUtil.lowerFirst(methodName.substring(3));
			Object value = map.get(fieldName);
			if (value == null) {
				continue;
			}
			
			try {
				method.invoke(model, ClassUtil.parse(types[0], value));
			} catch (Exception e) {
				throw new UtilException(StrUtil.format("Inject [{}] error!", fieldName), e);
			}
		}
	}
	
	/**
	 * 转换为Map
	 * @param model 模型
	 * @param isOnlyBasicType 是否只允许基本类型，包括String
	 */
	public static void toMap(Object model, boolean isOnlyBasicType) {
		Map<String, Object> map = new HashMap<String, Object>();
		final Method[] methods = model.getClass().getMethods();
		for (Method method : methods) {
			final String methodName = method.getName();
			if (! methodName.startsWith("get")) {
				continue;
			}
			final String fieldName = StrUtil.lowerFirst(methodName.substring(3));
			
			Object value = null;
			try {
				value = method.invoke(model);
			} catch (Exception e) {
				throw new UtilException(StrUtil.format("Inject map [{}] error!", fieldName), e);
			}
			if(value != null) {
				if(value instanceof String) {
					map.put(fieldName, value);
				}else if(isOnlyBasicType) {
					if(value.getClass().isPrimitive() == false) {
						map.put(fieldName, value);
					}
				}
			}
		}
	}
}
