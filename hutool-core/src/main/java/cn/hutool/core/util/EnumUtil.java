package cn.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.lang.func.SerFunction;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.reflect.FieldUtil;
import cn.hutool.core.text.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 枚举工具类
 *
 * @author looly
 * @since 3.3.0
 */
public class EnumUtil {

	/**
	 * 映射缓存：枚举类+code值 => 枚举值
	 * 反射获取枚举值速度较慢，因此采用缓存加速，经测评：反射时间开销约为原生的10倍，加缓存后时间开销约为原生的2倍
	 */
	private static final Map<Class<?>, Map<Object, Object>> ENUM_CODE_VALUE_CACHE = new HashMap<>();

	/**
	 * 指定类是否为Enum类
	 *
	 * @param clazz 类
	 * @return 是否为Enum类
	 */
	public static boolean isEnum(final Class<?> clazz) {
		Assert.notNull(clazz);
		return clazz.isEnum();
	}

	/**
	 * 指定类是否为Enum类
	 *
	 * @param obj 类
	 * @return 是否为Enum类
	 */
	public static boolean isEnum(final Object obj) {
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
	public static String toString(final Enum<?> e) {
		return null != e ? e.name() : null;
	}

	/**
	 * 获取给定位置的枚举值
	 *
	 * @param <E>       枚举类型泛型
	 * @param enumClass 枚举类
	 * @param index     枚举索引
	 * @return 枚举值，null表示无此对应枚举
	 * @since 5.1.6
	 */
	public static <E extends Enum<E>> E getEnumAt(final Class<E> enumClass, final int index) {
		final E[] enumConstants = enumClass.getEnumConstants();
		return index >= 0 && index < enumConstants.length ? enumConstants[index] : null;
	}

	/**
	 * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}
	 *
	 * @param <E>       枚举类型泛型
	 * @param enumClass 枚举类
	 * @param value     值
	 * @return 枚举值
	 * @since 4.1.13
	 */
	public static <E extends Enum<E>> E fromString(final Class<E> enumClass, final String value) {
		return Enum.valueOf(enumClass, value);
	}

	/**
	 * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}<br>
	 * 如果无枚举值，返回默认值
	 *
	 * @param <E>          枚举类型泛型
	 * @param enumClass    枚举类
	 * @param value        值
	 * @param defaultValue 无对应枚举值返回的默认值
	 * @return 枚举值
	 * @since 4.5.18
	 */
	public static <E extends Enum<E>> E fromString(final Class<E> enumClass, final String value, final E defaultValue) {
		return ObjUtil.defaultIfNull(fromStringQuietly(enumClass, value), defaultValue);
	}

	/**
	 * 字符串转枚举，调用{@link Enum#valueOf(Class, String)}，转换失败返回{@code null} 而非报错
	 *
	 * @param <E>       枚举类型泛型
	 * @param enumClass 枚举类
	 * @param value     值
	 * @return 枚举值
	 * @since 4.5.18
	 */
	public static <E extends Enum<E>> E fromStringQuietly(final Class<E> enumClass, final String value) {
		if (null == enumClass || StrUtil.isBlank(value)) {
			return null;
		}

		try {
			return fromString(enumClass, value);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * 模糊匹配转换为枚举，给定一个值，匹配枚举中定义的所有字段名（包括name属性），一旦匹配到返回这个枚举对象，否则返回null
	 *
	 * @param <E>       枚举类型
	 * @param enumClass 枚举类
	 * @param value     值
	 * @return 匹配到的枚举对象，未匹配到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E likeValueOf(final Class<E> enumClass, Object value) {
		if (value instanceof CharSequence) {
			value = value.toString().trim();
		}

		final Field[] fields = FieldUtil.getFields(enumClass);
		final Enum<?>[] enums = enumClass.getEnumConstants();
		String fieldName;
		for (final Field field : fields) {
			fieldName = field.getName();
			if (field.getType().isEnum() || "ENUM$VALUES".equals(fieldName) || "ordinal".equals(fieldName)) {
				// 跳过一些特殊字段
				continue;
			}
			for (final Enum<?> enumObj : enums) {
				if (ObjUtil.equals(value, FieldUtil.getFieldValue(enumObj, field))) {
					return (E) enumObj;
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
	public static List<String> getNames(final Class<? extends Enum<?>> clazz) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		if (null == enums) {
			return null;
		}
		final List<String> list = new ArrayList<>(enums.length);
		for (final Enum<?> e : enums) {
			list.add(e.name());
		}
		return list;
	}

	/**
	 * 获得枚举类中各枚举对象下指定字段的值
	 *
	 * @param clazz     枚举类
	 * @param fieldName 字段名，最终调用getXXX方法
	 * @return 字段值列表
	 */
	public static List<Object> getFieldValues(final Class<? extends Enum<?>> clazz, final String fieldName) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		if (null == enums) {
			return null;
		}
		final List<Object> list = new ArrayList<>(enums.length);
		for (final Enum<?> e : enums) {
			list.add(FieldUtil.getFieldValue(e, fieldName));
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
	public static List<String> getFieldNames(final Class<? extends Enum<?>> clazz) {
		final List<String> names = new ArrayList<>();
		final Field[] fields = FieldUtil.getFields(clazz);
		String name;
		for (final Field field : fields) {
			name = field.getName();
			if (field.getType().isEnum() || name.contains("$VALUES") || "ordinal".equals(name)) {
				continue;
			}
			if (false == names.contains(name)) {
				names.add(name);
			}
		}
		return names;
	}

	/**
	 * 通过 某字段对应值 获取 枚举，获取不到时为 {@code null}
	 *
	 * @param enumClass 枚举类
	 * @param predicate 条件
	 * @param <E>       枚举类型
	 * @return 对应枚举 ，获取不到时为 {@code null}
	 * @since 5.8.0
	 */
	public static <E extends Enum<E>> E getBy(final Class<E> enumClass, final Predicate<? super E> predicate) {
		return Arrays.stream(enumClass.getEnumConstants())
				.filter(predicate).findFirst().orElse(null);
	}

	/**
	 * 通过 某字段对应值 获取 枚举，获取不到时为 {@code null}
	 *
	 * @param condition 条件字段
	 * @param value     条件字段值
	 * @param <E>       枚举类型
	 * @param <C>       字段类型
	 * @return 对应枚举 ，获取不到时为 {@code null}
	 */
	public static <E extends Enum<E>, C> E getBy(final SerFunction<E, C> condition, final C value) {
		Class<E> implClass = LambdaUtil.getRealClass(condition);
		if (Enum.class.equals(implClass)) {
			implClass = LambdaUtil.getRealClass(condition);
		}
		return Arrays.stream(implClass.getEnumConstants()).filter(e -> condition.apply(e).equals(value)).findAny().orElse(null);
	}

	/**
	 * 通过 code字段对应值 获取 枚举，获取不到时为 {@code null}
	 * <br>枚举类中必须有一个名为code的字段
	 *
	 * @param enumClass 枚举类
	 * @param code      code值，不能为null
	 * @param <E>       枚举类型
	 * @param <T>       code类型
	 * @return 枚举值
	 */
	public static <E extends Enum<E>, T> E getByCode(Class<E> enumClass, T code) {
		if (enumClass == null || code == null) {
			throw new NullPointerException();
		}
		// 获取code => value的映射
		Map<Object, Object> codeValueMap = ENUM_CODE_VALUE_CACHE.get(enumClass);
		if (codeValueMap == null) {
			// 加锁
			synchronized (ENUM_CODE_VALUE_CACHE) {
				// 二次检查
				codeValueMap = ENUM_CODE_VALUE_CACHE.get(enumClass);
				// 新建 code => value的映射
				if (codeValueMap == null) {
					codeValueMap = new ConcurrentHashMap<>();
					ENUM_CODE_VALUE_CACHE.put(enumClass, codeValueMap);
				}
			}
		}
		// 缓存缺失则通过反射获取枚举值，并设置缓存
		if (!codeValueMap.containsKey(code)) {
			// 多线程可能导致在初始化阶段多次通过反射获取枚举值，但可忽略不计，不影响服务稳定后的性能
			E enumValue = doGetByCode(enumClass, code);
			if (enumValue == null) {
				return null;
			}
			codeValueMap.put(code, enumValue);
			return enumValue;
		} else {
			//noinspection unchecked
			return (E) codeValueMap.get(code);
		}
	}

	/**
	 * 使用反射从code字段获取枚举值，如果枚举值不存在，或者发生异常则返回 {@code null}
	 *
	 * @param enumClass 枚举类
	 * @param code      code值
	 * @param <E>       枚举类型
	 * @param <T>       code类型
	 * @return 枚举值
	 */
	private static <E extends Enum<E>, T> E doGetByCode(Class<E> enumClass, T code) {
		E target = null;
		try {
			// 调用枚举类的values方法获得枚举值列表
			Method valuesMethod = enumClass.getMethod("values");
			@SuppressWarnings("unchecked")
			E[] valueList = (E[]) valuesMethod.invoke(enumClass);
			// 遍历枚举值，根据枚举值列表的code字段查询枚举对象
			for (E e : valueList) {
				Field codeField = enumClass.getDeclaredField("code");
				codeField.setAccessible(true);
				Object valueCode = codeField.get(e);
				// 同一个枚举中，两个枚举定义了相同的code，抛出异常
				if (valueCode.equals(code) && target != null) {
					throw new IllegalArgumentException(String.format("code '%s' repeated in enum '%s'", code, enumClass));
				}
				if (valueCode.equals(code)) {
					target = e;
				}
			}
			return target;
		} catch (NoSuchFieldException e) {
			throw new UnsupportedOperationException(String.format("a field named 'code' must be declared in enum '%s'", enumClass));
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception ignored) {
		}
		return null;
	}

	/**
	 * 通过 某字段对应值 获取 枚举，获取不到时为 {@code defaultEnum}
	 *
	 * @param condition   条件字段
	 * @param value       条件字段值
	 * @param defaultEnum 条件找不到则返回结果使用这个
	 * @param <C>         值类型
	 * @param <E>         枚举类型
	 * @return 对应枚举 ，获取不到时为 {@code null}
	 */
	public static <E extends Enum<E>, C> E getBy(final SerFunction<E, C> condition, final C value, final E defaultEnum) {
		return ObjUtil.defaultIfNull(getBy(condition, value), defaultEnum);
	}

	/**
	 * 通过 某字段对应值 获取 枚举中另一字段值，获取不到时为 {@code null}
	 *
	 * @param field     你想要获取的字段
	 * @param condition 条件字段
	 * @param value     条件字段值
	 * @param <E>       枚举类型
	 * @param <F>       想要获取的字段类型
	 * @param <C>       条件字段类型
	 * @return 对应枚举中另一字段值 ，获取不到时为 {@code null}
	 * @since 5.8.0
	 */
	public static <E extends Enum<E>, F, C> F getFieldBy(final SerFunction<E, F> field,
														 final Function<E, C> condition, final C value) {
		Class<E> implClass = LambdaUtil.getRealClass(field);
		if (Enum.class.equals(implClass)) {
			implClass = LambdaUtil.getRealClass(field);
		}
		return Arrays.stream(implClass.getEnumConstants())
				// 过滤
				.filter(e -> condition.apply(e).equals(value))
				// 获取第一个并转换为结果
				.findFirst().map(field).orElse(null);
	}

	/**
	 * 获取枚举字符串值和枚举对象的Map对应，使用LinkedHashMap保证有序<br>
	 * 结果中键为枚举名，值为枚举对象
	 *
	 * @param <E>       枚举类型
	 * @param enumClass 枚举类
	 * @return 枚举字符串值和枚举对象的Map对应，使用LinkedHashMap保证有序
	 * @since 4.0.2
	 */
	public static <E extends Enum<E>> LinkedHashMap<String, E> getEnumMap(final Class<E> enumClass) {
		final LinkedHashMap<String, E> map = new LinkedHashMap<>();
		for (final E e : enumClass.getEnumConstants()) {
			map.put(e.name(), e);
		}
		return map;
	}

	/**
	 * 获得枚举名对应指定字段值的Map<br>
	 * 键为枚举名，值为字段值
	 *
	 * @param clazz     枚举类
	 * @param fieldName 字段名，最终调用getXXX方法
	 * @return 枚举名对应指定字段值的Map
	 */
	public static Map<String, Object> getNameFieldMap(final Class<? extends Enum<?>> clazz, final String fieldName) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		if (null == enums) {
			return null;
		}
		final Map<String, Object> map = MapUtil.newHashMap(enums.length, true);
		for (final Enum<?> e : enums) {
			map.put(e.name(), FieldUtil.getFieldValue(e, fieldName));
		}
		return map;
	}

	/**
	 * 判断指定名称的枚举是否存在
	 *
	 * @param <E>       枚举类型
	 * @param enumClass 枚举类
	 * @param name      需要查找的枚举名
	 * @return 是否存在
	 */
	public static <E extends Enum<E>> boolean contains(final Class<E> enumClass, final String name) {
		return getEnumMap(enumClass).containsKey(name);
	}

	/**
	 * 判断某个值是不存在枚举中
	 *
	 * @param <E>       枚举类型
	 * @param enumClass 枚举类
	 * @param val       需要查找的值
	 * @return 是否不存在
	 */
	public static <E extends Enum<E>> boolean notContains(final Class<E> enumClass, final String val) {
		return false == contains(enumClass, val);
	}

	/**
	 * 忽略大小检查某个枚举值是否匹配指定值
	 *
	 * @param e   枚举值
	 * @param val 需要判断的值
	 * @return 是非匹配
	 */
	public static boolean equalsIgnoreCase(final Enum<?> e, final String val) {
		return StrUtil.equalsIgnoreCase(toString(e), val);
	}

	/**
	 * 检查某个枚举值是否匹配指定值
	 *
	 * @param e   枚举值
	 * @param val 需要判断的值
	 * @return 是非匹配
	 */
	public static boolean equals(final Enum<?> e, final String val) {
		return StrUtil.equals(toString(e), val);
	}
}
