package cn.hutool.json.mapper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.core.text.StrUtil;
import cn.hutool.json.InternalJSONUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONParser;
import cn.hutool.json.JSONTokener;
import cn.hutool.json.xml.JSONXMLUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONSerializer;

import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * 对象和JSONObject映射器，用于转换对象为JSONObject，支持：
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
	 * @param source    来源对象
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 * @return ObjectMapper
	 */
	public static ObjectMapper of(final Object source, final Predicate<MutableEntry<String, Object>> predicate) {
		return new ObjectMapper(source, predicate);
	}

	private final Object source;
	private final Predicate<MutableEntry<String, Object>> predicate;

	/**
	 * 构造
	 *
	 * @param source    来源对象
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public ObjectMapper(final Object source, final Predicate<MutableEntry<String, Object>> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	/**
	 * 将给定对象转换为{@link JSONObject}
	 *
	 * @param jsonObject 目标{@link JSONObject}
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void mapTo(final JSONObject jsonObject) {
		final Object source = this.source;
		if (null == source) {
			return;
		}

		// 自定义序列化
		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (null != serializer) {
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
				jsonObject.set(Convert.toStr(e.getKey()), e.getValue(), predicate, false);
			}
		} else if (source instanceof Map.Entry) {
			final Map.Entry entry = (Map.Entry) source;
			jsonObject.set(Convert.toStr(entry.getKey()), entry.getValue(), predicate, false);
		} else if (source instanceof CharSequence) {
			// 可能为JSON字符串
			mapFromStr((CharSequence) source, jsonObject);
		} else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source, jsonObject.getConfig()), jsonObject);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source, jsonObject.getConfig()), jsonObject);
		} else if (source instanceof byte[]) {
			mapFromTokener(new JSONTokener(IoUtil.toStream((byte[]) source), jsonObject.getConfig()), jsonObject);
		} else if (source instanceof JSONTokener) {
			// JSONTokener
			mapFromTokener((JSONTokener) source, jsonObject);
		} else if (source instanceof ResourceBundle) {
			// JSONTokener
			mapFromResourceBundle((ResourceBundle) source, jsonObject);
		} else if (BeanUtil.isReadableBean(source.getClass())) {
			// 普通Bean
			mapFromBean(source, jsonObject);
		} else {
			// 不支持对象类型转换为JSONObject
			throw new JSONException("Unsupported type [{}] to JSONObject!", source.getClass());
		}
	}

	/**
	 * 从{@link ResourceBundle}转换
	 *
	 * @param bundle     ResourceBundle
	 * @param jsonObject {@link JSONObject}
	 * @since 5.3.1
	 */
	private void mapFromResourceBundle(final ResourceBundle bundle, final JSONObject jsonObject) {
		final Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			final String key = keys.nextElement();
			if (key != null) {
				InternalJSONUtil.propertyPut(jsonObject, key, bundle.getString(key), this.predicate);
			}
		}
	}

	/**
	 * 从字符串转换
	 *
	 * @param source     JSON字符串
	 * @param jsonObject {@link JSONObject}
	 */
	private void mapFromStr(final CharSequence source, final JSONObject jsonObject) {
		final String jsonStr = StrUtil.trim(source);
		if (StrUtil.startWith(jsonStr, '<')) {
			// 可能为XML
			JSONXMLUtil.toJSONObject(jsonObject, jsonStr, false);
			return;
		}
		mapFromTokener(new JSONTokener(StrUtil.trim(source), jsonObject.getConfig()), jsonObject);
	}

	/**
	 * 从{@link JSONTokener}转换
	 *
	 * @param x          JSONTokener
	 * @param jsonObject {@link JSONObject}
	 */
	private void mapFromTokener(final JSONTokener x, final JSONObject jsonObject) {
		JSONParser.of(x).parseTo(jsonObject, this.predicate);
	}

	/**
	 * 从Bean转换
	 *
	 * @param bean       Bean对象
	 * @param jsonObject {@link JSONObject}
	 */
	private void mapFromBean(final Object bean, final JSONObject jsonObject) {
		final CopyOptions copyOptions = InternalJSONUtil.toCopyOptions(jsonObject.getConfig());
		if (null != this.predicate) {
			copyOptions.setFieldEditor((entry -> this.predicate.test(entry) ? entry : null));
		}
		BeanUtil.beanToMap(bean, jsonObject, copyOptions);
	}
}
