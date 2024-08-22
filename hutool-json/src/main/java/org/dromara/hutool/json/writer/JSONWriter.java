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
public class JSONWriter extends Writer {

	/**
	 * 缩进因子，定义每一级别增加的缩进量
	 */
	private final int indentFactor;
	/**
	 * 本级别缩进量
	 */
	private final int indent;
	/**
	 * Writer
	 */
	private final Writer writer;
	/**
	 * JSON选项
	 */
	private final JSONConfig config;

	/**
	 * 写出当前值是否需要分隔符
	 */
	private boolean needSeparator;
	/**
	 * 是否为JSONArray模式
	 */
	private boolean arrayMode;

	/**
	 * 创建JSONWriter
	 *
	 * @param writer       {@link Writer}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param config       JSON选项
	 * @return JSONWriter
	 */
	public static JSONWriter of(final Writer writer, final int indentFactor, final int indent, final JSONConfig config) {
		return new JSONWriter(writer, indentFactor, indent, config);
	}

	/**
	 * 构造
	 *
	 * @param writer       {@link Writer}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param config       JSON选项
	 */
	public JSONWriter(final Writer writer, final int indentFactor, final int indent, final JSONConfig config) {
		this.writer = writer;
		this.indentFactor = indentFactor;
		this.indent = indent;
		this.config = ObjUtil.defaultIfNull(config, JSONConfig.of());
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
	public JSONWriter beginObj() {
		//noinspection resource
		writeRaw(CharUtil.DELIM_START);
		return this;
	}

	/**
	 * JSONArray写出开始，默认写出"["
	 *
	 * @return this
	 */
	public JSONWriter beginArray() {
		//noinspection resource
		writeRaw(CharUtil.BRACKET_START);
		arrayMode = true;
		return this;
	}

	/**
	 * 结束，默认根据开始的类型，补充"}"或"]"
	 *
	 * @return this
	 */
	public JSONWriter end() {
		// 换行缩进
		//noinspection resource
		writeLF().writeSpace(indent);
		//noinspection resource
		writeRaw(arrayMode ? CharUtil.BRACKET_END : CharUtil.DELIM_END);
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
	 * @param predicate 过滤修改器
	 * @return this
	 * @since 6.0.0
	 */
	@SuppressWarnings({"UnusedReturnValue", "resource"})
	public JSONWriter writeField(final MutableEntry<Object, Object> pair, final Predicate<MutableEntry<Object, Object>> predicate) {
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

		return writeValueDirect(pair.getValue(), predicate);
	}

	/**
	 * 写出键，自动处理分隔符和缩进，并包装键名
	 *
	 * @param key 键名
	 * @return this
	 */
	@SuppressWarnings({"resource", "UnusedReturnValue"})
	public JSONWriter writeKey(final String key) {
		if (needSeparator) {
			//noinspection resource
			writeRaw(CharUtil.COMMA);
		}
		// 换行缩进
		writeLF().writeSpace(indentFactor + indent);
		return writeRaw(InternalJSONUtil.quote(key));
	}

	@SuppressWarnings({"SpellCheckingInspection", "NullableProblems"})
	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		this.writer.write(cbuf, off, len);
	}

	@Override
	public void flush() {
		try {
			this.writer.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
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
		InternalJSONUtil.quote(csq, writer);
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
				writeRaw(CharUtil.SPACE);
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
			writeRaw(CharUtil.LF);
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
			writer.append(csq);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 写入原始字符值，不做任何处理
	 *
	 * @param c 字符
	 * @return this
	 */
	public JSONWriter writeRaw(final char c) {
		try {
			writer.write(c);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	// ------------------------------------------------------------------------------ Private methods

	/**
	 * 写出值，自动处理分隔符和缩进，自动判断类型，并根据不同类型写出特定格式的值
	 *
	 * @param value     值
	 * @param predicate 过滤修改器
	 * @return this
	 */
	private JSONWriter writeValueDirect(final Object value, final Predicate<MutableEntry<Object, Object>> predicate) {
		if (arrayMode) {
			if (needSeparator) {
				//noinspection resource
				writeRaw(CharUtil.COMMA);
			}
			// 换行缩进
			//noinspection resource
			writeLF().writeSpace(indentFactor + indent);
		} else {
			//noinspection resource
			writeRaw(CharUtil.COLON).writeSpace(1);
		}
		needSeparator = true;
		return writeObjValue(value, predicate);
	}

	/**
	 * 写出JSON的值，根据值类型不同，输出不同内容
	 *
	 * @param value     值
	 * @param predicate 过滤修改器
	 * @return this
	 */
	@SuppressWarnings("resource")
	private JSONWriter writeObjValue(final Object value, final Predicate<MutableEntry<Object, Object>> predicate) {
		final int indent = indentFactor + this.indent;

		// 自定义规则
		final JSONValueWriter valueWriter = GlobalValueWriters.get(value);
		if(null != valueWriter){
			valueWriter.write(this, value);
			return this;
		}

		// 默认规则
		if (value == null) {
			writeRaw(StrUtil.NULL);
		}else if (value instanceof JSON) {
			((JSON) value).write(writer, indentFactor, indent, predicate);
		} else {
			writeQuoteStrValue(value.toString());
		}

		return this;
	}
}
