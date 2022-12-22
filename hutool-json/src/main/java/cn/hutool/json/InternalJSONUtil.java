package cn.hutool.json;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.mutable.MutablePair;
import cn.hutool.core.map.CaseInsensitiveLinkedMap;
import cn.hutool.core.map.CaseInsensitiveTreeMap;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 *
 * @author Looly
 */
public final class InternalJSONUtil {

	private InternalJSONUtil() {
	}

	/**
	 * 如果对象是Number 且是 NaN or infinite，将抛出异常
	 *
	 * @param obj 被检查的对象
	 * @return 检测后的值
	 * @throws JSONException If o is a non-finite number.
	 */
	static Object testValidity(Object obj) throws JSONException {
		if (false == ObjectUtil.isValidIfNumber(obj)) {
			throw new JSONException("JSON does not allow non-finite numbers.");
		}
		return obj;
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
	static String valueToString(Object value) throws JSONException {
		if (value == null || value instanceof JSONNull) {
			return JSONNull.NULL.toString();
		}
		if (value instanceof JSONString) {
			try {
				return ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
		} else if (value instanceof Number) {
			return NumberUtil.toStr((Number) value);
		} else if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		} else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		} else if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
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
	public static Object stringToValue(String string) {
		// null处理
		if (StrUtil.isEmpty(string) || StrUtil.NULL.equalsIgnoreCase(string)) {
			return JSONNull.NULL;
		}

		// boolean处理
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}

		// Number处理
		char b = string.charAt(0);
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
			} catch (Exception ignore) {
			}
		}

		// 其它情况返回原String值下
		return string;
	}

	/**
	 * 将Property的键转化为JSON形式<br>
	 * 用于识别类似于：com.luxiaolei.package.hutool这类用点隔开的键<br>
	 * 注意：是否允许重复键，取决于JSONObject配置
	 *
	 * @param jsonObject JSONObject
	 * @param key        键
	 * @param value      值
	 * @return JSONObject
	 */
	static JSONObject propertyPut(JSONObject jsonObject, Object key, Object value, Filter<MutablePair<String, Object>> filter) {
		final String[] path = StrUtil.splitToArray(Convert.toStr(key), CharUtil.DOT);
		final int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			final String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = new JSONObject(target.getConfig());
				target.set(segment, nextTarget, filter, target.getConfig().isCheckDuplicate());
			}
			target = nextTarget;
		}
		target.set(path[last], value, filter, target.getConfig().isCheckDuplicate());
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
	static boolean defaultIgnoreNullValue(Object obj) {
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
	static CopyOptions toCopyOptions(JSONConfig config) {
		return CopyOptions.create()
				.setIgnoreCase(config.isIgnoreCase())
				.setIgnoreError(config.isIgnoreError())
				.setIgnoreNullValue(config.isIgnoreNullValue())
				.setTransientSupport(config.isTransientSupport());
	}

	/**
	 * 根据配置创建对应的原始Map
	 *
	 * @param capacity 初始大小
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 * @return Map
	 */
	static Map<String, Object> createRawMap(int capacity, JSONConfig config) {
		final Map<String, Object> rawHashMap;
		if (null == config) {
			config = JSONConfig.create();
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
