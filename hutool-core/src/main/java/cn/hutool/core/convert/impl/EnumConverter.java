package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 无泛型检查的枚举转换器
 *
 * @author Looly
 * @since 4.0.2
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EnumConverter extends AbstractConverter<Object> {
	private static final long serialVersionUID = 1L;

	private static final Map<Class<?>, Map<Class<?>, Method>> VALUE_OF_METHOD_CACHE = new ConcurrentHashMap<>();

	private final Class enumClass;

	/**
	 * 构造
	 *
	 * @param enumClass 转换成的目标Enum类
	 */
	public EnumConverter(Class enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	protected Object convertInternal(Object value) {
		Enum enumValue = tryConvertEnum(value, this.enumClass);
		if(null == enumValue && false == value instanceof String){
			// 最后尝试valueOf转换
			enumValue = Enum.valueOf(this.enumClass, convertToStr(value));
		}
		return enumValue;
	}

	@Override
	public Class getTargetType() {
		return this.enumClass;
	}

	/**
	 * 尝试找到类似转换的静态方法调用实现转换
	 *
	 * @param value     被转换的值
	 * @param enumClass enum类
	 * @return 对应的枚举值
	 */
	protected static Enum tryConvertEnum(Object value, Class enumClass) {
		Enum enumResult = null;
		if (value instanceof Integer) {
			enumResult = EnumUtil.getEnumAt(enumClass, (Integer)value);
		} else if (value instanceof String) {
			try {
				enumResult = Enum.valueOf(enumClass, (String) value);
			} catch (IllegalArgumentException e) {
				//ignore
			}
		}

		// 尝试查找其它用户自定义方法
		if(null == enumResult){
			final Map<Class<?>, Method> valueOfMethods = getValueOfMethods(enumClass);
			if (MapUtil.isNotEmpty(valueOfMethods)) {
				final Class<?> valueClass = value.getClass();
				for (Map.Entry<Class<?>, Method> entry : valueOfMethods.entrySet()) {
					if (ClassUtil.isAssignable(entry.getKey(), valueClass)) {
						enumResult = ReflectUtil.invokeStatic(entry.getValue(), value);
					}
				}
			}
		}

		return enumResult;
	}

	/**
	 * 获取用于转换为enum的所有static方法
	 *
	 * @param enumClass 枚举类
	 * @return 转换方法map
	 */
	private static Map<Class<?>, Method> getValueOfMethods(Class<?> enumClass) {
		Map<Class<?>, Method> valueOfMethods = VALUE_OF_METHOD_CACHE.get(enumClass);
		if (null == valueOfMethods) {
			valueOfMethods = Arrays.stream(enumClass.getMethods())
					.filter(ModifierUtil::isStatic)
					.filter(m -> m.getReturnType() == enumClass)
					.filter(m -> m.getParameterCount() == 1)
					.filter(m -> false == "valueOf".equals(m.getName()))
					.collect(Collectors.toMap(m -> m.getParameterTypes()[0], m -> m, (existing, replacement) -> existing));
			VALUE_OF_METHOD_CACHE.put(enumClass, valueOfMethods);
		}
		return valueOfMethods;
	}
}
