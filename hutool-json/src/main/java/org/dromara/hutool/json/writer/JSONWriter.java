/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json.writer;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.InternalJSONUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Predicate;

/**
 * JSON数据写出器<br>
 * 通过简单的append方式将JSON的键值对等信息写出到{@link Writer}中。
 *
 * @author looly
 * @since 5.7.3
 */
public class JSONWriter implements Appendable, Flushable, Closeable {

	/**
	 * 创建JSONWriter
	 *
	 * @param appendable   {@link Appendable}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param config       JSON选项
	 * @return JSONWriter
	 */
	public static JSONWriter of(final Appendable appendable, final int indentFactor, final int indent,
								final JSONConfig config) {
		return new JSONWriter(appendable, indentFactor, indent, config);
	}

	/**
	 * Writer
	 */
	private final Appendable appendable;
	/**
	 * JSON选项
	 */
	private final JSONConfig config;

	/**
	 * 缩进因子，定义每一级别增加的缩进量
	 */
	private final int indentFactor;
	/**
	 * 键值对过滤器，用于修改键值对
	 */
	private Predicate<MutableEntry<Object, Object>> predicate;


	/**
	 * 写出当前值是否需要分隔符
	 */
	private boolean needSeparator;
	/**
	 * 是否为JSONArray模式
	 */
	private boolean arrayMode;
	/**
	 * 本级别缩进量
	 */
	private int indent;

	/**
	 * 构造
	 *
	 * @param appendable   {@link Appendable}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param config       JSON选项
	 */
	public JSONWriter(final Appendable appendable, final int indentFactor, final int indent, final JSONConfig config) {
		this.appendable = appendable;
		this.indentFactor = indentFactor;
		this.indent = indent;
		this.config = ObjUtil.defaultIfNull(config, JSONConfig::of);
	}

	/**
	 * 设置键值对过滤器，用于修改键值对
	 *
	 * @param predicate 键值对过滤器，用于修改键值对
	 * @return this
	 */
	public JSONWriter setPredicate(final Predicate<MutableEntry<Object, Object>> predicate) {
		this.predicate = predicate;
		return this;
	}

	/**
	 * 获取JSON配置
	 *
	 * @return {@link JSONConfig}
	 */
	public JSONConfig getConfig() {
		return this.config;
	}

	/**
	 * JSONObject写出开始，默认写出"{"
	 *
	 * @return this
	 */
	@SuppressWarnings("resource")
	public JSONWriter beginObj() {
		append(CharUtil.DELIM_START);
		arrayMode = false;
		needSeparator = false;
		indent += indentFactor;
		return this;
	}

	/**
	 * JSONArray写出开始，默认写出"["
	 *
	 * @return this
	 */
	@SuppressWarnings("resource")
	public JSONWriter beginArray() {
		append(CharUtil.BRACKET_START);
		arrayMode = true;
		needSeparator = false;
		indent += indentFactor;
		return this;
	}

	/**
	 * 结束，默认根据开始的类型，补充"}"或"]"
	 *
	 * @return this
	 */
	@SuppressWarnings("resource")
	public JSONWriter end() {
		// 结束子缩进
		indent -= indentFactor;
		// 换行缩进
		writeLF().writeSpace(indent);
		append(arrayMode ? CharUtil.BRACKET_END : CharUtil.DELIM_END);
		flush();
		arrayMode = false;
		// 当前对象或数组结束，当新的
		needSeparator = true;
		return this;
	}

	/**
	 * 写出字段名及字段值，如果字段值是{@code null}且忽略null值，则不写出任何内容<br>
	 * 在{@link #arrayMode} 为 {@code true} 时，key是数字，此时不写出键，只写值
	 *
	 * @param pair      键值对
	 * @return this
	 * @since 6.0.0
	 */
	@SuppressWarnings("resource")
	public JSONWriter writeField(final MutableEntry<Object, Object> pair) {
		if (null == pair.getValue() && config.isIgnoreNullValue()) {
			return this;
		}

		if (null != predicate) {
			if (!predicate.test(pair)) {
				// 使用修改后的键值对
				return this;
			}
		}

		if (!arrayMode) {
			// JSONObject模式，写出键，否则只输出值
			writeKey(StrUtil.toString(pair.getKey()));
		}

		return writeValueDirect(pair.getValue());
	}

	/**
	 * 写出键，自动处理分隔符和缩进，并包装键名
	 *
	 * @param key 键名
	 * @return this
	 */
	@SuppressWarnings("resource")
	public JSONWriter writeKey(final String key) {
		if (needSeparator) {
			append(CharUtil.COMMA);
		}
		// 换行缩进
		writeLF().writeSpace(indent);
		return writeRaw(InternalJSONUtil.quote(key));
	}

	@Override
	public void flush() {
		if (this.appendable instanceof Flushable) {
			try {
				((Flushable) this.appendable).flush();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (this.appendable instanceof AutoCloseable) {
			try {
				((AutoCloseable) this.appendable).close();
			} catch (final Exception e) {
				throw new IOException(e);
			}
		}
	}

	/**
	 * 写出字符串值，并包装引号并转义字符<br>
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param csq 字符串
	 */
	public void writeQuoteStrValue(final String csq) {
		InternalJSONUtil.quote(csq, this.appendable);
	}

	/**
	 * 写出空格
	 *
	 * @param count 空格数
	 */
	public void writeSpace(final int count) {
		if (indentFactor > 0) {
			for (int i = 0; i < count; i++) {
				//noinspection resource
				append(CharUtil.SPACE);
			}
		}
	}

	/**
	 * 写出换行符
	 *
	 * @return this
	 */
	public JSONWriter writeLF() {
		if (indentFactor > 0) {
			//noinspection resource
			append(CharUtil.LF);
		}
		return this;
	}

	/**
	 * 写入原始字符串值，不做任何处理
	 *
	 * @param csq 字符串
	 * @return this
	 */
	public JSONWriter writeRaw(final String csq) {
		try {
			this.appendable.append(csq);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public JSONWriter append(final char c) throws IORuntimeException {
		try {
			this.appendable.append(c);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public JSONWriter append(final CharSequence csq) throws IORuntimeException {
		try {
			this.appendable.append(csq);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public JSONWriter append(final CharSequence csq, final int start, final int end) throws IORuntimeException {
		try {
			this.appendable.append(csq, start, end);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	@Override
	public String toString() {
		return this.appendable.toString();
	}

	// ------------------------------------------------------------------------------ Private methods

	/**
	 * 写出值，自动处理分隔符和缩进，自动判断类型，并根据不同类型写出特定格式的值
	 *
	 * @param value     值
	 * @return this
	 */
	private JSONWriter writeValueDirect(final Object value) {
		if (arrayMode) {
			if (needSeparator) {
				//noinspection resource
				append(CharUtil.COMMA);
			}
			// 换行缩进
			//noinspection resource
			writeLF().writeSpace(indent);
		} else {
			//noinspection resource
			append(CharUtil.COLON).writeSpace(1);
		}
		needSeparator = true;
		return writeObjValue(value);
	}

	/**
	 * 写出JSON的值，根据值类型不同，输出不同内容
	 *
	 * @param value     值
	 * @return this
	 */
	@SuppressWarnings("resource")
	private JSONWriter writeObjValue(final Object value) {
		// 自定义规则
		final ValueWriter valueWriter = ValueWriterManager.getInstance().get(value);
		if (null != valueWriter) {
			valueWriter.write(this, value);
			return this;
		}

		// 默认规则
		if (value == null) {
			writeRaw(StrUtil.NULL);
		} else if (value instanceof JSON) {
			((JSON) value).write(this);
		} else {
			writeQuoteStrValue(value.toString());
		}

		return this;
	}
}
