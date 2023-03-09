package cn.hutool.json;

import cn.hutool.core.lang.mutable.MutableEntry;
import cn.hutool.core.math.NumberUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Predicate;

/**
 * JSON数字表示
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONNumber extends Number implements JSON {
	private static final long serialVersionUID = 1L;

	private final Number value;
	/**
	 * 配置项
	 */
	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param value  值
	 * @param config JSON配置
	 */
	public JSONNumber(final Number value, final JSONConfig config) {
		this.value = value;
		this.config = config;
	}

	@Override
	public JSONConfig getConfig() {
		return this.config;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public Writer write(final Writer writer, final int indentFactor, final int indent,
						final Predicate<MutableEntry<Object, Object>> predicate) throws JSONException {
		try {
			writer.write(toString());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return writer;
	}

	@Override
	public int intValue() {
		return this.value.intValue();
	}

	@Override
	public long longValue() {
		return this.value.longValue();
	}

	@Override
	public float floatValue() {
		return this.value.floatValue();
	}

	@Override
	public double doubleValue() {
		return this.value.doubleValue();
	}

	@Override
	public String toString() {
		return NumberUtil.toStr(this.value);
	}
}
