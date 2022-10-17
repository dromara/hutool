package cn.hutool.json;

import cn.hutool.core.lang.mutable.Mutable;
import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.core.util.CharUtil;

import java.util.function.Predicate;

/**
 * JSON字符串解析器，实现：
 * <ul>
 *     <li>JSON字符串 --&gt; {@link JSONTokener} --&gt; {@link JSONObject}</li>
 *     <li>JSON字符串 --&gt; {@link JSONTokener} --&gt; {@link JSONArray}</li>
 * </ul>
 *
 * @author looly
 * @since 5.8.0
 */
public class JSONParser {

	/**
	 * 创建JSONParser
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSONParser
	 */
	public static JSONParser of(final JSONTokener tokener) {
		return new JSONParser(tokener);
	}

	private final JSONTokener tokener;

	/**
	 * 构造
	 *
	 * @param tokener {@link JSONTokener}
	 */
	public JSONParser(final JSONTokener tokener) {
		this.tokener = tokener;
	}

	// region parseTo

	/**
	 * 解析{@link JSONTokener}中的字符到目标的{@link JSONObject}中
	 *
	 * @param jsonObject {@link JSONObject}
	 * @param predicate  键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public void parseTo(final JSONObject jsonObject, final Predicate<MutableEntry<String, Object>> predicate) {
		final JSONTokener tokener = this.tokener;

		char c;
		String key;

		if (tokener.nextClean() != '{') {
			throw tokener.syntaxError("A JSONObject text must begin with '{'");
		}
		while (true) {
			c = tokener.nextClean();
			switch (c) {
				case 0:
					throw tokener.syntaxError("A JSONObject text must end with '}'");
				case '}':
					return;
				default:
					tokener.back();
					key = tokener.nextValue().toString();
			}

			// The key is followed by ':'.

			c = tokener.nextClean();
			if (c != ':') {
				throw tokener.syntaxError("Expected a ':' after a key");
			}

			jsonObject.set(key, tokener.nextValue(), predicate);

			// Pairs are separated by ','.

			switch (tokener.nextClean()) {
				case ';':
				case CharUtil.COMMA:
					if (tokener.nextClean() == '}') {
						// issue#2380
						// 尾后逗号（Trailing Commas），JSON中虽然不支持，但是ECMAScript 2017支持，此处做兼容。
						return;
					}
					tokener.back();
					break;
				case '}':
					return;
				default:
					throw tokener.syntaxError("Expected a ',' or '}'");
			}
		}
	}

	/**
	 * 解析JSON字符串到{@link JSONArray}中
	 *
	 * @param jsonArray {@link JSONArray}
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null} 表示不过滤，，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public void parseTo(final JSONArray jsonArray, final Predicate<Mutable<Object>> predicate) {
		final JSONTokener x = this.tokener;

		if (x.nextClean() != '[') {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() != ']') {
			x.back();
			for (; ; ) {
				if (x.nextClean() == CharUtil.COMMA) {
					x.back();
					jsonArray.addRaw(null, predicate);
				} else {
					x.back();
					jsonArray.addRaw(x.nextValue(), predicate);
				}
				switch (x.nextClean()) {
					case CharUtil.COMMA:
						if (x.nextClean() == ']') {
							return;
						}
						x.back();
						break;
					case ']':
						return;
					default:
						throw x.syntaxError("Expected a ',' or ']'");
				}
			}
		}
	}
	// endregion
}
