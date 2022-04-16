package cn.hutool.json;

/**
 * JSON字符串解析器
 *
 * @author looly
 * @since 5.8.0
 */
public class JSONParser {
	private final JSONTokener tokener;

	/**
	 * 创建JSONParser
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSONParser
	 */
	public static JSONParser of(JSONTokener tokener) {
		return new JSONParser(tokener);
	}

	/**
	 * 构造
	 *
	 * @param tokener {@link JSONTokener}
	 */
	public JSONParser(JSONTokener tokener) {
		this.tokener = tokener;
	}

	/**
	 * 解析{@link JSONTokener}中的字符到目标的{@link JSONObject}中
	 *
	 * @param jsonObject {@link JSONObject}
	 */
	public void parseTo(JSONObject jsonObject) {
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
			jsonObject.putOnce(key, tokener.nextValue());

			// Pairs are separated by ','.

			switch (tokener.nextClean()) {
				case ';':
				case ',':
					if (tokener.nextClean() == '}') {
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
	 */
	public void parseTo(JSONArray jsonArray) {
		final JSONTokener x = this.tokener;

		if (x.nextClean() != '[') {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() != ']') {
			x.back();
			for (; ; ) {
				if (x.nextClean() == ',') {
					x.back();
					jsonArray.addRaw(JSONNull.NULL);
				} else {
					x.back();
					jsonArray.addRaw(x.nextValue());
				}
				switch (x.nextClean()) {
					case ',':
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
}
