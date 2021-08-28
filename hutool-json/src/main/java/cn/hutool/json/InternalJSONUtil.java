package cn.hutool.json;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * 内部JSON工具类，仅用于JSON内部使用
 *
 * @author Looly
 */
final class InternalJSONUtil {

	private InternalJSONUtil() {
	}

	/**
	 * 如果对象是Number 且是 NaN or infinite，将抛出异常
	 *
	 * @param obj 被检查的对象
	 * @throws JSONException If o is a non-finite number.
	 */
	protected static void testValidity(Object obj) throws JSONException {
		if (false == ObjectUtil.isValidIfNumber(obj)) {
			throw new JSONException("JSON does not allow non-finite numbers.");
		}
	}

	/**
	 * 值转为String，用于JSON中。 If the object has an value.toJSONString() method, then that method will be used to produce the JSON text. <br>
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
		if (value == null || value instanceof JSONNull) {
			return "null";
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
		} else if (value.getClass().isArray()) {
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
	protected static Object stringToValue(String string) {
		// null处理
		if (null == string || "null".equalsIgnoreCase(string)) {
			return JSONNull.NULL;
		}

		// boolean处理
		if (0 == string.length()) {
			return StrUtil.EMPTY;
		}
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
	 * 用于识别类似于：com.luxiaolei.package.hutool这类用点隔开的键
	 *
	 * @param jsonObject JSONObject
	 * @param key        键
	 * @param value      值
	 * @return JSONObject
	 */
	protected static JSONObject propertyPut(JSONObject jsonObject, Object key, Object value) {
		final String[] path = StrUtil.splitToArray(Convert.toStr(key), CharUtil.DOT);
		int last = path.length - 1;
		JSONObject target = jsonObject;
		for (int i = 0; i < last; i += 1) {
			String segment = path[i];
			JSONObject nextTarget = target.getJSONObject(segment);
			if (nextTarget == null) {
				nextTarget = new JSONObject(target.getConfig());
				target.set(segment, nextTarget);
			}
			target = nextTarget;
		}
		target.set(path[last], value);
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
	protected static boolean defaultIgnoreNullValue(Object obj) {
		return (false == (obj instanceof CharSequence))//
				&& (false == (obj instanceof JSONTokener))//
				&& (false == (obj instanceof Map));
	}

	/**
	 * 判断给定对象是否有序，用于辅助创建{@link JSONObject}时是否有序
	 *
	 * <ul>
	 *     <li>对象为{@link LinkedHashMap}子类或{@link LinkedHashMap}子类</li>
	 *     <li>对象实现</li>
	 * </ul>
	 *
	 * @param value 被转换的对象
	 * @return 是否有序
	 * @since 5.7.0
	 */
	protected static boolean isOrder(Object value){
		if(value instanceof LinkedHashMap || value instanceof SortedMap){
			return true;
		} else if(value instanceof JSONGetter){
			final JSONConfig config = ((JSONGetter<?>) value).getConfig();
			if(null != config){
				return config.isOrder();
			}
		}

		return false;
	}
}
