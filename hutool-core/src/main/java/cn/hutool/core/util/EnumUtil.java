package cn.hutool.core.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.map.MapUtil;

/**
 * 枚举工具类
 * 
 * @author looly
 * @since 3.3.0
 */
public class EnumUtil {

	/**
	 * 枚举类中所有枚举对象的name列表
	 * 
	 * @param clazz 枚举类
	 * @return name列表
	 */
	public static List<String> getNames(Class<? extends Enum<?>> clazz) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		if (null == enums) {
			return null;
		}
		final List<String> list = new ArrayList<>(enums.length);
		for (Enum<?> e : enums) {
			list.add(e.name());
		}
		return list;
	}

	/**
	 * 获得枚举类中各枚举对象下指定字段的值
	 * 
	 * @param clazz 枚举类
	 * @param fieldName 字段名，最终调用getXXX方法
	 * @return 字段值列表
	 */
	public static List<Object> getFieldValues(Class<? extends Enum<?>> clazz, String fieldName) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		if (null == enums) {
			return null;
		}
		final List<Object> list = new ArrayList<>(enums.length);
		for (Enum<?> e : enums) {
			list.add(ReflectUtil.getFieldValue(e, fieldName));
		}
		return list;
	}

	/**
	 * 获取枚举字符串值和枚举对象的Map对应，使用LinkedHashMap保证有序<br>
	 * 结果中键为枚举名，值为枚举对象
	 * 
	 * @param enumClass 枚举类
	 * @return 枚举字符串值和枚举对象的Map对应，使用LinkedHashMap保证有序
	 * @since 4.0.2
	 */
	public static <E extends Enum<E>> LinkedHashMap<String, E> getEnumMap(final Class<E> enumClass) {
		final LinkedHashMap<String, E> map = new LinkedHashMap<String, E>();
		for (final E e : enumClass.getEnumConstants()) {
			map.put(e.name(), e);
		}
		return map;
	}

	/**
	 * 获得枚举名对应指定字段值的Map<br>
	 * 键为枚举名，值为字段值
	 * 
	 * @param clazz 枚举类
	 * @param fieldName 字段名，最终调用getXXX方法
	 * @return 枚举名对应指定字段值的Map
	 */
	public static Map<String, Object> getNameFieldMap(Class<? extends Enum<?>> clazz, String fieldName) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		if (null == enums) {
			return null;
		}
		final Map<String, Object> map = MapUtil.newHashMap(enums.length);
		for (Enum<?> e : enums) {
			map.put(e.name(), ReflectUtil.getFieldValue(e, fieldName));
		}
		return map;
	}
}
