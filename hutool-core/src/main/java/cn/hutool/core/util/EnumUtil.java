package cn.hutool.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;

/**
 * 枚举工具类
 * 
 * @author looly
 * @since 3.3.0
 */
public class EnumUtil {

	/**
	 * 指定类是否为Enum类
	 * 
	 * @param clazz 类
	 * @return 是否为Enum类
	 */
	public static boolean isEnum(Class<?> clazz) {
		Assert.notNull(clazz);
		return clazz.isEnum();
	}
	
	/**
	 * 指定类是否为Enum类
	 * 
	 * @param obj 类
	 * @return 是否为Enum类
	 */
	public static boolean isEnum(Object obj) {
		Assert.notNull(obj);
		return obj.getClass().isEnum();
	}

	/**
	 * Enum对象转String，调用{@link Enum#name()} 方法
	 * 
	 * @param e Enum
	 * @return name值
	 * @since 4.1.13
	 */
	public static String toString(Enum<?> e) {
		return null != e ? e.name() : null;
	}

	/**
	 * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}
	 * 
	 * @param <T> 枚举类型泛型
	 * @param enumClass 枚举类
	 * @param value 值
	 * @return 枚举值
	 * @since 4.1.13
	 */
	public static <T extends Enum<T>> T fromString(Class<T> enumClass, String value) {
		return Enum.valueOf(enumClass, value);
	}
	
	/**
	 * 模糊匹配转换为枚举，给定一个值，匹配枚举中定义的所有字段名（包括name属性），一旦匹配到返回这个枚举对象，否则返回null
	 * 
	 * @param enumClass 枚举类
	 * @param value 值
	 * @return 匹配到的枚举对象，未匹配到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T likeValueOf(Class<T> enumClass, Object value) {
		final Field[] fields = ReflectUtil.getFields(enumClass);
		final Enum<?>[] enums = enumClass.getEnumConstants();
		String fieldName;
		for (Field field : fields) {
			fieldName = field.getName();
			if (field.getType().isEnum() || "ENUM$VALUES".equals(fieldName) || "ordinal".equals(fieldName)) {
				//跳过一些特殊字段
				continue;
			}
			for (Enum<?> enumObj : enums) {
				if(ObjectUtil.equal(value, ReflectUtil.getFieldValue(enumObj, field))) {
					return (T) enumObj;
				}
			}
		}
		return null;
	}

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
	 * 获得枚举类中所有的字段名<br>
	 * 除用户自定义的字段名，也包括“name”字段，例如：
	 * 
	 * <pre>
	*   EnumUtil.getFieldNames(Color.class) == ["name", "index"]
	 * </pre>
	 * 
	 * @param clazz 枚举类
	 * @return 字段名列表
	 * @since 4.1.20
	 */
	public static List<String> getFieldNames(Class<? extends Enum<?>> clazz) {
		final List<String> names = new ArrayList<>();
		final Field[] fields = ReflectUtil.getFields(clazz);
		String name;
		for (Field field : fields) {
			name = field.getName();
			if (field.getType().isEnum() || name.contains("$VALUES") || "ordinal".equals(name)) {
				continue;
			}
			names.add(name);
		}
		return names;
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
