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
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.InternalJSONUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONException;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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
	 * @param config       JSON选项
	 * @param predicate    predicate    字段过滤器
	 * @return JSONWriter
	 */
	public static JSONWriter of(final Appendable appendable,
								final int indentFactor,
								final JSONConfig config,
								final Predicate<MutableEntry<Object, Object>> predicate) {
		return of(appendable, indentFactor, 0, config, predicate);
	}

	/**
	 * 创建JSONWriter
	 *
	 * @param appendable   {@link Appendable}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param config       JSON选项
	 * @param predicate    predicate    字段过滤器
	 * @return JSONWriter
	 */
	public static JSONWriter of(final Appendable appendable,
								final int indentFactor,
								final int indent,
								final JSONConfig config,
								final Predicate<MutableEntry<Object, Object>> predicate) {
		return new JSONWriter(appendable, indentFactor, indent, config, predicate);
	}

	/**
	 * JS中表示的数字最大值
	 */
	private static final long JS_MAX_NUMBER = 9007199254740992L;
	// Syntax as defined by https://datatracker.ietf.org/doc/html/rfc8259#section-6
	private static final Pattern JSON_NUMBER_PATTERN =
		Pattern.compile("-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][-+]?[0-9]+)?");

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
	private final Predicate<MutableEntry<Object, Object>> predicate;


	/**
	 * 写出当前值是否需要分隔符
	 */
	private boolean needSeparator;
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
	 * @param predicate    字段过滤器
	 */
	public JSONWriter(final Appendable appendable,
					  final int indentFactor,
					  final int indent,
					  final JSONConfig config,
					  final Predicate<MutableEntry<Object, Object>> predicate) {
		this.appendable = appendable;
		this.indentFactor = indentFactor;
		this.indent = indent;
		this.config = ObjUtil.defaultIfNull(config, JSONConfig::of);
		this.predicate = predicate;
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
		needSeparator = false;
		indent += indentFactor;
		return this;
	}

	/**
	 * 结束JSON对象，默认根据开始的类型，补充"}"
	 *
	 * @return this
	 */
	public JSONWriter endObj() {
		return end(false);
	}

	/**
	 * JSONArray写出开始，默认写出"["
	 *
	 * @return this
	 */
	@SuppressWarnings("resource")
	public JSONWriter beginArray() {
		append(CharUtil.BRACKET_START);
		needSeparator = false;
		indent += indentFactor;
		return this;
	}

	/**
	 * 结束JSON数组，默认根据开始的类型，补充"]"
	 *
	 * @return this
	 */
	public JSONWriter endArray() {
		return end(true);
	}

	/**
	 * 写出字段名及字段值，如果字段值是{@code null}且忽略null值，则不写出任何内容<br>
	 *
	 * @param pair 键值对
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

		final Object key = pair.getKey();
		if (key instanceof Integer) {
			// 数组模式，只写出值
			return writeValueDirect(pair.getValue(), true);
		}

		// 键值对模式
		writeKey(StrUtil.toString(key));
		return writeValueDirect(pair.getValue(), false);
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

	/**
	 * 写出数字，根据{@link JSONConfig#isStripTrailingZeros()} 配置不同，写出不同数字<br>
	 * 主要针对Double型是否去掉小数点后多余的0<br>
	 * 此方法输出的值不包装引号。
	 *
	 * @param number 数字
	 * @return this
	 */
	public JSONWriter writeNumber(final Number number) {
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = (null == config) || config.isStripTrailingZeros();
		final String numberStr = NumberUtil.toStr(number, isStripTrailingZeros);

		// 检查有效性
		if(!ReUtil.isMatch(JSON_NUMBER_PATTERN, numberStr)){
			throw new JSONException("Invalid RFC8259 JSON format number: " + numberStr);
		}

		final NumberWriteMode numberWriteMode = (null == config) ? NumberWriteMode.NORMAL : config.getNumberWriteMode();
		switch (numberWriteMode){
			case JS:
				if(number.longValue() > JS_MAX_NUMBER){
					writeQuoteStrValue(numberStr);
				} else{
					return writeRaw(numberStr);
				}
				break;
			case STRING:
				writeQuoteStrValue(numberStr);
				break;
			default:
				return writeRaw(numberStr);
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
	 * 结束，默认根据开始的类型，补充"}"或"]"
	 *
	 * @param arrayMode 数组模式，true表示数组模式，false表示对象模式
	 * @return this
	 */
	@SuppressWarnings("resource")
	private JSONWriter end(final boolean arrayMode) {
		// 结束子缩进
		indent -= indentFactor;
		// 换行缩进
		writeLF().writeSpace(indent);
		append(arrayMode ? CharUtil.BRACKET_END : CharUtil.DELIM_END);
		flush();
		// 当前对象或数组结束，当新的
		needSeparator = true;
		return this;
	}

	/**
	 * 写出值，自动处理分隔符和缩进，自动判断类型，并根据不同类型写出特定格式的值
	 *
	 * @param value     值
	 * @param arrayMode 是否数组模式
	 * @return this
	 */
	private JSONWriter writeValueDirect(final Object value, final boolean arrayMode) {
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
	 * @param value 值
	 * @return this
	 */
	@SuppressWarnings("resource")
	private JSONWriter writeObjValue(final Object value) {
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
