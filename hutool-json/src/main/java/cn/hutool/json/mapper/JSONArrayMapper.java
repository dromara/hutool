package cn.hutool.json.mapper;

import cn.hutool.core.collection.iter.ArrayIter;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.mutable.Mutable;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONParser;
import cn.hutool.json.JSONTokener;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONSerializer;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * 对象和JSONArray映射器，用于转换对象为JSONArray，支持：
 * <ul>
 *     <li>CharSequence 转 JSONArray，使用JSONTokener解析</li>
 *     <li>{@link Reader} 转 JSONArray，使用JSONTokener解析</li>
 *     <li>{@link InputStream} 转 JSONArray，使用JSONTokener解析</li>
 *     <li>JSONTokener 转 JSONArray，直接解析</li>
 *     <li>Iterable 转 JSONArray</li>
 *     <li>Iterator 转 JSONArray</li>
 *     <li>数组 转 JSONArray</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONArrayMapper {
	/**
	 * 创建ArrayMapper
	 *
	 * @param source    来源对象
	 * @param predicate 值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤，，{@link Predicate#test(Object)}为{@code true}保留
	 * @return ObjectMapper
	 */
	public static JSONArrayMapper of(final Object source, final Predicate<Mutable<Object>> predicate) {
		return new JSONArrayMapper(source, predicate);
	}

	private final Object source;
	private final Predicate<Mutable<Object>> predicate;

	/**
	 * 构造
	 *
	 * @param source    来源对象
	 * @param predicate 值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤，，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public JSONArrayMapper(final Object source, final Predicate<Mutable<Object>> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	/**
	 * 将给定对象转换为{@link JSONArray}
	 *
	 * @param jsonArray 目标{@link JSONArray}
	 * @throws JSONException 非数组或集合
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void mapTo(final JSONArray jsonArray) throws JSONException {
		final Object source = this.source;
		if (null == source) {
			return;
		}

		// 自定义序列化
		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (null != serializer) {
			serializer.serialize(jsonArray, source);
			return;
		}

		if (source instanceof JSONTokener) {
			mapFromTokener((JSONTokener) source, jsonArray);
		}else if (source instanceof CharSequence) {
			// JSON字符串
			mapFromStr((CharSequence) source, jsonArray);
		} else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source, jsonArray.getConfig()), jsonArray);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source, jsonArray.getConfig()), jsonArray);
		} else if (source instanceof byte[]) {
			final byte[] bytesSource = (byte[]) source;
			if ('[' == bytesSource[0] && ']' == bytesSource[bytesSource.length - 1]) {
				mapFromTokener(new JSONTokener(IoUtil.toStream(bytesSource), jsonArray.getConfig()), jsonArray);
			} else {
				// https://github.com/dromara/hutool/issues/2369
				// 非标准的二进制流，则按照普通数组对待
				for (final byte b : bytesSource) {
					jsonArray.add(b);
				}
			}
		} else {
			final Iterator<?> iter;
			if (ArrayUtil.isArray(source)) {// 数组
				iter = new ArrayIter<>(source);
			} else if (source instanceof Iterator<?>) {// Iterator
				iter = ((Iterator<?>) source);
			} else if (source instanceof Iterable<?>) {// Iterable
				iter = ((Iterable<?>) source).iterator();
			} else {
				if(false == jsonArray.getConfig().isIgnoreError()){
					throw new JSONException("JSONArray initial value should be a string or collection or array.");
				}
				// 如果用户选择跳过异常，则跳过此值转换
				return;
			}

			Object next;
			while (iter.hasNext()) {
				next = iter.next();
				// 检查循环引用
				if (next != source) {
					jsonArray.add(next, predicate);
				}
			}
		}
	}

	/**
	 * 初始化
	 *
	 * @param source    JSON字符串
	 * @param jsonArray {@link JSONArray}
	 */
	private void mapFromStr(final CharSequence source, final JSONArray jsonArray) {
		if (null != source) {
			mapFromTokener(new JSONTokener(StrUtil.trim(source), jsonArray.getConfig()), jsonArray);
		}
	}

	/**
	 * 初始化
	 *
	 * @param x         {@link JSONTokener}
	 * @param jsonArray {@link JSONArray}
	 */
	private void mapFromTokener(final JSONTokener x, final JSONArray jsonArray) {
		JSONParser.of(x).parseTo(jsonArray, this.predicate);
	}
}
