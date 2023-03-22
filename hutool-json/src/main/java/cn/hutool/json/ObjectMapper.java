package cn.hutool.json;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.mutable.Mutable;
import cn.hutool.core.lang.mutable.MutablePair;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONObjectSerializer;
import cn.hutool.json.serialize.JSONSerializer;

import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 对象和JSON映射器，用于转换对象为JSON，支持：
 * <ul>
 *     <li>Map 转 JSONObject，将键值对加入JSON对象</li>
 *     <li>Map.Entry 转 JSONObject</li>
 *     <li>CharSequence 转 JSONObject，使用JSONTokener解析</li>
 *     <li>{@link Reader} 转 JSONObject，使用JSONTokener解析</li>
 *     <li>{@link InputStream} 转 JSONObject，使用JSONTokener解析</li>
 *     <li>JSONTokener 转 JSONObject，直接解析</li>
 *     <li>ResourceBundle 转 JSONObject</li>
 *     <li>Bean 转 JSONObject，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象。例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"</li>
 * </ul>
 *
 * @author looly
 * @since 5.8.0
 */
public class ObjectMapper {

	/**
	 * 创建ObjectMapper
	 *
	 * @param source 来源对象
	 * @return ObjectMapper
	 */
	public static ObjectMapper of(Object source) {
		return new ObjectMapper(source);
	}

	private final Object source;

	/**
	 * 构造
	 *
	 * @param source 来源对象
	 */
	public ObjectMapper(Object source) {
		this.source = source;
	}

	/**
	 * 将给定对象转换为{@link JSONObject}
	 *
	 * @param jsonObject 目标{@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void map(JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		final Object source = this.source;
		if (null == source) {
			return;
		}

		// 自定义序列化
		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (serializer instanceof JSONObjectSerializer) {
			serializer.serialize(jsonObject, source);
			return;
		}

		if (source instanceof JSONArray) {
			// 不支持集合类型转换为JSONObject
			throw new JSONException("Unsupported type [{}] to JSONObject!", source.getClass());
		}

		if (source instanceof Map) {
			// Map
			for (final Map.Entry<?, ?> e : ((Map<?, ?>) source).entrySet()) {
				jsonObject.set(Convert.toStr(e.getKey()), e.getValue(), filter, jsonObject.getConfig().isCheckDuplicate());
			}
		} else if (source instanceof Map.Entry) {
			final Map.Entry entry = (Map.Entry) source;
			jsonObject.set(Convert.toStr(entry.getKey()), entry.getValue(), filter, jsonObject.getConfig().isCheckDuplicate());
		} else if (source instanceof CharSequence) {
			// 可能为JSON字符串
			mapFromStr((CharSequence) source, jsonObject, filter);
		} else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source, jsonObject.getConfig()), jsonObject, filter);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source, jsonObject.getConfig()), jsonObject, filter);
		} else if (source instanceof byte[]) {
			mapFromTokener(new JSONTokener(IoUtil.toStream((byte[]) source), jsonObject.getConfig()), jsonObject, filter);
		} else if (source instanceof JSONTokener) {
			// JSONTokener
			mapFromTokener((JSONTokener) source, jsonObject, filter);
		} else if (source instanceof ResourceBundle) {
			// JSONTokener
			mapFromResourceBundle((ResourceBundle) source, jsonObject, filter);
		} else if (BeanUtil.isReadableBean(source.getClass())) {
			// 普通Bean
			// TODO 过滤器对Bean无效，需补充。
			mapFromBean(source, jsonObject);
		} else {
			// 不支持对象类型转换为JSONObject
			throw new JSONException("Unsupported type [{}] to JSONObject!", source.getClass());
		}
	}

	/**
	 * 初始化
	 *
	 * @param jsonArray 目标{@link JSONArray}
	 * @param filter    键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 * @throws JSONException 非数组或集合
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void map(JSONArray jsonArray, Filter<Mutable<Object>> filter) throws JSONException {
		final Object source = this.source;
		if (null == source) {
			return;
		}

		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (null != serializer && JSONArray.class.equals(TypeUtil.getTypeArgument(serializer.getClass()))) {
			// 自定义序列化
			serializer.serialize(jsonArray, source);
		} else if (source instanceof CharSequence) {
			// JSON字符串
			mapFromStr((CharSequence) source, jsonArray, filter);
		}else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source, jsonArray.getConfig()), jsonArray, filter);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source, jsonArray.getConfig()), jsonArray, filter);
		} else if (source instanceof byte[]) {
			final byte[] bytesSource = (byte[]) source;
			// 如果是普通的的byte[], 要避免下标越界
			if (bytesSource.length > 1 && '[' == bytesSource[0] && ']' == bytesSource[bytesSource.length - 1]) {
				mapFromTokener(new JSONTokener(IoUtil.toStream(bytesSource), jsonArray.getConfig()), jsonArray, filter);
			}else{
				// https://github.com/dromara/hutool/issues/2369
				// 非标准的二进制流，则按照普通数组对待
				for(final byte b : bytesSource){
					jsonArray.add(b);
				}
			}
		} else if (source instanceof JSONTokener) {
			mapFromTokener((JSONTokener) source, jsonArray, filter);
		} else {
			Iterator<?> iter;
			if (ArrayUtil.isArray(source)) {// 数组
				iter = new ArrayIter<>(source);
			} else if (source instanceof Iterator<?>) {// Iterator
				iter = ((Iterator<?>) source);
			} else if (source instanceof Iterable<?>) {// Iterable
				iter = ((Iterable<?>) source).iterator();
			} else {
				throw new JSONException("JSONArray initial value should be a string or collection or array.");
			}

			final JSONConfig config = jsonArray.getConfig();
			Object next;
			while (iter.hasNext()) {
				next = iter.next();
				// 检查循环引用
				if (next != source) {
					jsonArray.addRaw(JSONUtil.wrap(next, config), filter);
				}
			}
		}
	}

	/**
	 * 从{@link ResourceBundle}转换
	 *
	 * @param bundle     ResourceBundle
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤
	 * @since 5.3.1
	 */
	private static void mapFromResourceBundle(ResourceBundle bundle, JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key != null) {
				InternalJSONUtil.propertyPut(jsonObject, key, bundle.getString(key), filter);
			}
		}
	}

	/**
	 * 从字符串转换
	 *
	 * @param source     JSON字符串
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤
	 */
	private static void mapFromStr(CharSequence source, JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		final String jsonStr = StrUtil.trim(source);
		if (StrUtil.startWith(jsonStr, '<')) {
			// 可能为XML
			XML.toJSONObject(jsonObject, jsonStr, false);
			return;
		}
		mapFromTokener(new JSONTokener(StrUtil.trim(source), jsonObject.getConfig()), jsonObject, filter);
	}

	/**
	 * 初始化
	 *
	 * @param source    JSON字符串
	 * @param jsonArray {@link JSONArray}
	 * @param filter    值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 */
	private void mapFromStr(CharSequence source, JSONArray jsonArray, Filter<Mutable<Object>> filter) {
		if (null != source) {
			mapFromTokener(new JSONTokener(StrUtil.trim(source), jsonArray.getConfig()), jsonArray, filter);
		}
	}

	/**
	 * 从{@link JSONTokener}转换
	 *
	 * @param x          JSONTokener
	 * @param jsonObject {@link JSONObject}
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作
	 */
	private static void mapFromTokener(JSONTokener x, JSONObject jsonObject, Filter<MutablePair<String, Object>> filter) {
		JSONParser.of(x).parseTo(jsonObject, filter);
	}

	/**
	 * 初始化
	 *
	 * @param x         {@link JSONTokener}
	 * @param jsonArray {@link JSONArray}
	 * @param filter    值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 */
	private static void mapFromTokener(JSONTokener x, JSONArray jsonArray, Filter<Mutable<Object>> filter) {
		JSONParser.of(x).parseTo(jsonArray, filter);
	}

	/**
	 * 从Bean转换
	 *
	 * @param bean       Bean对象
	 * @param jsonObject {@link JSONObject}
	 */
	private static void mapFromBean(Object bean, JSONObject jsonObject) {
		BeanUtil.beanToMap(bean, jsonObject, InternalJSONUtil.toCopyOptions(jsonObject.getConfig()));
	}
}
