package cn.hutool.json;

import cn.hutool.core.lang.mutable.MutableEntry;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Predicate;

/**
 * JSON字符串，表示一个字符串
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONString implements JSON, CharSequence {
	private static final long serialVersionUID = 1L;

	private final char[] value;
	/**
	 * 配置项
	 */
	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param value  char数组
	 * @param config 配置项
	 */
	public JSONString(final char[] value, final JSONConfig config) {
		this.value = value;
		this.config = config;
	}

	@Override
	public JSONConfig getConfig() {
		return this.config;
	}

	@Override
	public int size() {
		return length();
	}

	@Override
	public Writer write(final Writer writer, final int indentFactor, final int indent,
						final Predicate<MutableEntry<Object, Object>> predicate) throws JSONException {
		try {
			writer.write(this.value);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return writer;
	}

	@Override
	public int length() {
		return this.value.length;
	}

	@Override
	public char charAt(final int index) {
		return this.value[index];
	}

	@Override
	public CharSequence subSequence(final int start, final int end) {
		return ((start == 0) && (end == this.value.length)) ? this
				: new String(this.value, start, end);
	}

	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
}
