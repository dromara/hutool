package cn.hutool.json;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.core.map.CaseInsensitiveLinkedMap;
import cn.hutool.core.map.CaseInsensitiveTreeMap;
import cn.hutool.core.math.NumberUtil;
import cn.hutool.core.reflect.ClassUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.serialize.JSONString;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 *
 * @author Looly
 */
public final class InternalJSONUtil {

	private InternalJSONUtil() {
	}

	/**
	 * 在需要的时候包装对象<br>
	 * 包装包括：
	 * <ul>
	 * <li>array or collection =》 JSONArray</li>
	 * <li>map =》 JSONObject</li>
	 * <li>standard property (Double, String, et al) =》 原对象</li>
	 * <li>来自于java包 =》 字符串</li>
	 * <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param object     被包装的对象
	 * @param jsonConfig JSON选项
	 * @return 包装后的值，null表示此值需被忽略
	 */
	static Object wrap(final Object object, final JSONConfig jsonConfig) {
		if (object == null) {
			return null;
		}
		if (object instanceof JSON //
				|| object instanceof JSONString //
				|| object instanceof CharSequence //
				|| object instanceof Number //
				|| ObjUtil.isBasicType(object) //
		) {
			if (false == ObjUtil.isValidIfNumber(object)) {
				throw new JSONException("JSON does not allow non-finite numbers.");
			}
			return object;
		}

		try {
			// fix issue#1399@Github
			if (object instanceof SQLException) {
				return object.toString();
			}

			// JSONArray
			if (object instanceof Iterable || ArrayUtil.isArray(object)) {
				return new JSONArray(object, jsonConfig);
			}
			// JSONObject
			if (object instanceof Map || object instanceof Map.Entry) {
				return new JSONObject(object, jsonConfig);
			}

			// 日期类型做包装，以便自定义输出格式
			if (object instanceof Date
					|| object instanceof Calendar
					|| object instanceof TemporalAccessor
			) {
				return object;
			}
			// 枚举类保存其字符串形式（4.0.2新增）
			if (object instanceof Enum) {
				return object.toString();
			}

			// Java内部类不做转换
			if (ClassUtil.isJdkClass(object.getClass())) {
				return object.toString();
			}

			// 默认按照JSONObject对待
			return new JSONObject(object, jsonConfig);
		} catch (final Exception exception) {
			return null;
		}
	}

	/**
	 * 值转为String，用于JSON中。规则为：
	 * <ul>
	 *     <li>对象如果实现了{@link JSONString}接口，调用{@link JSONString#toJSONString()}方法</li>
	 *     <li>对象如果实现了{@link JSONString}接口，调用{@link JSONString#toJSONString()}方法</li>
	 *     <li>对象如果是数组或Collection，包装为{@link JSONArray}</li>
	 *     <li>对象如果是Map，包装为{@link JSONObject}</li>
	 *     <li>对象如果是数字，使用{@link NumberUtil#toStr(Number)}转换为字符串</li>
	 *     <li>其他情况调用toString并使用双引号包装</li>
	 * </ul>
	 *
	 * @param value 需要转为字符串的对象
	 * @return 字符串
	 * @throws JSONException If the value is or contains an invalid number.
	 */
	static String valueToString(final Object value) throws JSONException {
		if (value == null) {
			return StrUtil.NULL;
		}
		if (value instanceof JSONString) {
			try {
				return ((JSONString) value).toJSONString();
			} catch (final Exception e) {
				throw new JSONException(e);
			}
		} else if (value instanceof Number) {
			return NumberUtil.toStr((Number) value);
		} else if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		} else if (value instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		} else if (value instanceof Collection) {
			final Collection<?> coll = (Collection<?>) value;
			return new JSONArray(coll).toString();
		} else if (ArrayUtil.isArray(value)) {
			return new JSONArray(value).toString();
		} else {
			return JSONUtil.quote(value.toString());
		}
	}

	/**
	 * 尝试转换字符串为number, boolean, or null，无法转换返回String
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	public static Object stringToValue(final String string) {
		// null处理
		if (StrUtil.isEmpty(string) || StrUtil.NULL.equalsIgnoreCase(string)) {
			return null;
		}

		// boolean处理
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}

		// Number处理
		final char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '-') {
			try {
				if (StrUtil.containsAnyIgnoreCase(string, ".", "e")) {
					// pr#192@Gitee，Double会出现小数精度丢失问题，此处使用BigDecimal
					return new BigDecimal(string);
				} else {
					final long myLong = Long.parseLong(string);
					if (string.equals(Long.toString(myLong))) {
						if (myLong == (int) myLong) {
							return (int) myLong;
						} else {
							return myLong;
						}
					}
				}
			} catch (final Exception ignore) {
			}
		}

		// 其它情况返回原String值下
		return string;
	}

	/**
	 * 将Property的键转化为JSON形式<br>
	 * 用于识别类似于：cn.hutool.json这类用点隔开的键<br>
	 * 注意：不允许重复键
	 *
	 * @param jsonObject JSONObject
	 * @param key        键
	 * @param value      值
	 * @param predicate  属性过滤器，{@link Predicate#test(Object)}为{@code true}保留
	 * @return JSONObject
	 */
	public static JSONObject propertyPut(final JSONObject jsonObject, final Object key, final Object value, final Predicate<MutableEntry<String, Object>> predicate) {
		final String[] path = StrUtil.splitToArray(Convert.toStr(key), CharUtil.DOT);
		final int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			final String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = new JSONObject(target.getConfig());
				target.setOnce(segment, nextTarget, predicate);
			}
			target = nextTarget;
		}
		target.setOnce(path[last], value, predicate);
		return jsonObject;
	}

	/**
	 * 默认情况下是否忽略null值的策略选择，以下对象不忽略null值，其它对象忽略：
	 *
	 * <pre>
	 *     1. CharSequence
	 *     2. JSONTokener
	 *     3. Map
	 * </pre>
	 *
	 * @param obj 需要检查的对象
	 * @return 是否忽略null值
	 * @since 4.3.1
	 */
	static boolean defaultIgnoreNullValue(final Object obj) {
		return (false == (obj instanceof CharSequence))//
				&& (false == (obj instanceof JSONTokener))//
				&& (false == (obj instanceof Map));
	}

	/**
	 * 将{@link JSONConfig}参数转换为Bean拷贝所用的{@link CopyOptions}
	 *
	 * @param config {@link JSONConfig}
	 * @return {@link CopyOptions}
	 * @since 5.8.0
	 */
	public static CopyOptions toCopyOptions(final JSONConfig config) {
		return CopyOptions.of()
				.setIgnoreCase(config.isIgnoreCase())
				.setIgnoreError(config.isIgnoreError())
				.setIgnoreNullValue(config.isIgnoreNullValue())
				.setTransientSupport(config.isTransientSupport())
				// 使用JSON转换器
				.setConverter((type, value) ->
						JSONConverter.convertWithCheck(type, value, null, config.isIgnoreError()));
	}

	/**
	 * 根据配置创建对应的原始Map
	 *
	 * @param capacity 初始大小
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 * @return Map
	 */
	static Map<String, Object> createRawMap(final int capacity, JSONConfig config) {
		final Map<String, Object> rawHashMap;
		if (null == config) {
			config = JSONConfig.of();
		}
		final Comparator<String> keyComparator = config.getKeyComparator();
		if (config.isIgnoreCase()) {
			if (null != keyComparator) {
				rawHashMap = new CaseInsensitiveTreeMap<>(keyComparator);
			} else {
				rawHashMap = new CaseInsensitiveLinkedMap<>(capacity);
			}
		} else {
			if (null != keyComparator) {
				rawHashMap = new TreeMap<>(keyComparator);
			} else {
				rawHashMap = new LinkedHashMap<>(capacity);
			}
		}
		return rawHashMap;
	}
}
