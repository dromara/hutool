package cn.hutool.core.net.url;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.TableMap;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * URL中查询字符串部分的封装，类似于：
 * <pre>
 *   key1=v1&amp;key2=&amp;key3=v3
 * </pre>
 *
 * @author looly
 * @since 5.3.1
 */
public class UrlQuery {

	private final TableMap<CharSequence, CharSequence> query;

	/**
	 * 构建UrlQuery
	 *
	 * @param queryMap 初始化的查询键值对
	 * @return {@link UrlQuery}
	 */
	public static UrlQuery of(Map<? extends CharSequence, ?> queryMap) {
		return new UrlQuery(queryMap);
	}

	/**
	 * 构建UrlQuery
	 *
	 * @param queryStr 初始化的查询字符串
	 * @param charset  decode用的编码，null表示不做decode
	 * @return {@link UrlQuery}
	 */
	public static UrlQuery of(String queryStr, Charset charset) {
		final UrlQuery urlQuery = new UrlQuery();
		urlQuery.parse(queryStr, charset);
		return urlQuery;
	}

	/**
	 * 构造
	 */
	public UrlQuery() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param queryMap 初始化的查询键值对
	 */
	public UrlQuery(Map<? extends CharSequence, ?> queryMap) {
		if(MapUtil.isNotEmpty(queryMap)) {
			query = new TableMap<>(queryMap.size());
			addAll(queryMap);
		} else{
			query = new TableMap<>(MapUtil.DEFAULT_INITIAL_CAPACITY);
		}
	}

	/**
	 * 增加键值对
	 *
	 * @param key   键
	 * @param value 值，集合和数组转换为逗号分隔形式
	 * @return this
	 */
	public UrlQuery add(CharSequence key, Object value) {
		this.query.put(key, toStr(value));
		return this;
	}

	/**
	 * 批量增加键值对
	 *
	 * @param queryMap query中的键值对
	 * @return this
	 */
	public UrlQuery addAll(Map<? extends CharSequence, ?> queryMap) {
		if(MapUtil.isNotEmpty(queryMap)) {
			queryMap.forEach(this::add);
		}
		return this;
	}

	/**
	 * 解析URL中的查询字符串
	 *
	 * @param queryStr 查询字符串，类似于key1=v1&amp;key2=&amp;key3=v3
	 * @param charset  decode编码，null表示不做decode
	 * @return this
	 */
	public UrlQuery parse(String queryStr, Charset charset) {
		if (StrUtil.isBlank(queryStr)) {
			return this;
		}

		// 去掉Path部分
		int pathEndPos = queryStr.indexOf('?');
		if (pathEndPos > -1) {
			queryStr = StrUtil.subSuf(queryStr, pathEndPos + 1);
			if (StrUtil.isBlank(queryStr)) {
				return this;
			}
		}

		final int len = queryStr.length();
		String name = null;
		int pos = 0; // 未处理字符开始位置
		int i; // 未处理字符结束位置
		char c; // 当前字符
		for (i = 0; i < len; i++) {
			c = queryStr.charAt(i);
			if (c == '=') { // 键值对的分界点
				if (null == name) {
					// name可以是""
					name = queryStr.substring(pos, i);
				}
				pos = i + 1;
			} else if (c == '&') { // 参数对的分界点
				if (null == name && pos != i) {
					// 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
					addParam(queryStr.substring(pos, i), StrUtil.EMPTY, charset);
				} else if (name != null) {
					addParam(name, queryStr.substring(pos, i), charset);
					name = null;
				}
				pos = i + 1;
			}
		}

		// 处理结尾
		if (pos != i) {
			if (name == null) {
				addParam(queryStr.substring(pos, i), StrUtil.EMPTY, charset);
			} else {
				addParam(name, queryStr.substring(pos, i), charset);
			}
		} else if (name != null) {
			addParam(name, StrUtil.EMPTY, charset);
		}
		return this;
	}

	/**
	 * 获得查询的Map
	 *
	 * @return 查询的Map，只读
	 */
	public Map<CharSequence, CharSequence> getQueryMap(){
		return MapUtil.unmodifiable(this.query);
	}

	/**
	 * 获取查询值
	 * @param key 键
	 * @return 值
	 */
	public CharSequence get(CharSequence key){
		if(MapUtil.isEmpty(this.query)){
			return null;
		}
		return this.query.get(key);
	}

	/**
	 * 构建URL查询字符串，即将key-value键值对转换为key1=v1&amp;key2=&amp;key3=v3形式
	 *
	 * @param charset encode编码，null表示不做encode编码
	 * @return URL查询字符串
	 */
	public String build(Charset charset) {
		if (MapUtil.isEmpty(this.query)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		CharSequence key;
		CharSequence value;
		for (Map.Entry<CharSequence, CharSequence> entry : this.query) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			key = entry.getKey();
			if (StrUtil.isNotEmpty(key)) {
				sb.append(URLUtil.encodeAll(StrUtil.str(key), charset)).append("=");
				value = entry.getValue();
				if (StrUtil.isNotEmpty(value)) {
					sb.append(URLUtil.encodeAll(StrUtil.str(value), charset));
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return build(null);
	}

	/**
	 * 对象转换为字符串，用于URL的Query中
	 *
	 * @param value 值
	 * @return 字符串
	 */
	private static String toStr(Object value) {
		String result;
		if (value instanceof Iterable) {
			result = CollUtil.join((Iterable<?>) value, ",");
		} else if (value instanceof Iterator) {
			result = IterUtil.join((Iterator<?>) value, ",");
		} else {
			result = Convert.toStr(value);
		}
		return result;
	}

	/**
	 * 将键值对加入到值为List类型的Map中
	 *
	 * @param name    key
	 * @param value   value
	 * @param charset 编码
	 */
	private void addParam(String name, String value, Charset charset) {
		name = URLUtil.decode(name, charset);
		value = URLUtil.decode(value, charset);
		this.query.put(name, value);
	}
}
