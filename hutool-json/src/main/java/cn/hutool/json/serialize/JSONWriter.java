package cn.hutool.json.serialize;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.date.format.GlobalCustomFormat;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.math.NumberUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.json.InternalJSONUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONException;

import java.io.IOException;
import java.io.Writer;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

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
		this.config = config;
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
	 * 写出键，自动处理分隔符和缩进，并包装键名
	 *
	 * @param key 键名
	 * @return this
	 */
	public JSONWriter writeKey(final String key) {
		if (needSeparator) {
			//noinspection resource
			writeRaw(CharUtil.COMMA);
		}
		// 换行缩进
		//noinspection resource
		writeLF().writeSpace(indentFactor + indent);
		return writeRaw(InternalJSONUtil.quote(key));
	}

	/**
	 * 写出值，自动处理分隔符和缩进，自动判断类型，并根据不同类型写出特定格式的值<br>
	 * 如果写出的值为{@code null}，且配置忽略null，则跳过。
	 *
	 * @param value 值
	 * @return this
	 */
	public JSONWriter writeValue(final Object value) {
		if (null == value && config.isIgnoreNullValue()) {
			return this;
		}
		return writeValueDirect(value);
	}

	/**
	 * 写出字段名及字段值，如果字段值是{@code null}且忽略null值，则不写出任何内容
	 *
	 * @param key   字段名
	 * @param value 字段值
	 * @return this
	 * @since 5.7.6
	 */
	public JSONWriter writeField(final String key, final Object value) {
		if (null == value && config.isIgnoreNullValue()) {
			return this;
		}

		//noinspection resource
		return writeKey(key).writeValueDirect(value);
	}

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

	// ------------------------------------------------------------------------------ Private methods

	/**
	 * 写出值，自动处理分隔符和缩进，自动判断类型，并根据不同类型写出特定格式的值
	 *
	 * @param value 值
	 * @return this
	 */
	private JSONWriter writeValueDirect(final Object value) {
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
		return writeObjValue(value);
	}

	/**
	 * 写出JSON的值，根据值类型不同，输出不同内容
	 *
	 * @param value 值
	 * @return this
	 */
	private JSONWriter writeObjValue(final Object value) {
		final int indent = indentFactor + this.indent;
		if (value == null) {
			//noinspection resource
			writeRaw(StrUtil.NULL);
		} else if (value instanceof JSON) {
			((JSON) value).write(writer, indentFactor, indent);
		} else if (value instanceof Number) {
			writeNumberValue((Number) value);
		} else if (value instanceof Date || value instanceof Calendar || value instanceof TemporalAccessor) {
			final String format = (null == config) ? null : config.getDateFormat();
			//noinspection resource
			writeRaw(formatDate(value, format));
		} else if (value instanceof Boolean) {
			writeBooleanValue((Boolean) value);
		} else if (value instanceof JSONString) {
			writeJSONStringValue((JSONString) value);
		} else {
			writeQuoteStrValue(value.toString());
		}

		return this;
	}

	/**
	 * 写出数字，根据{@link JSONConfig#isStripTrailingZeros()} 配置不同，写出不同数字<br>
	 * 主要针对Double型是否去掉小数点后多余的0<br>
	 * 此方法输出的值不包装引号。
	 *
	 * @param number 数字
	 */
	private void writeNumberValue(final Number number) {
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = null == config || config.isStripTrailingZeros();
		//noinspection resource
		writeRaw(NumberUtil.toStr(number, isStripTrailingZeros));
	}

	/**
	 * 写出Boolean值，直接写出true或false,不适用引号包装
	 *
	 * @param value Boolean值
	 */
	private void writeBooleanValue(final Boolean value) {
		//noinspection resource
		writeRaw(value.toString());
	}

	/**
	 * 输出实现了{@link JSONString}接口的对象，通过调用{@link JSONString#toJSONString()}获取JSON字符串<br>
	 * {@link JSONString}按照JSON对象对待，此方法输出的JSON字符串不包装引号。<br>
	 * 如果toJSONString()返回null，调用toString()方法并使用双引号包装。
	 *
	 * @param jsonString {@link JSONString}
	 */
	private void writeJSONStringValue(final JSONString jsonString) {
		final String valueStr;
		try {
			valueStr = jsonString.toJSONString();
		} catch (final Exception e) {
			throw new JSONException(e);
		}
		if (null != valueStr) {
			//noinspection resource
			writeRaw(valueStr);
		} else {
			writeQuoteStrValue(jsonString.toString());
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
	private void writeQuoteStrValue(final String csq) {
		InternalJSONUtil.quote(csq, writer);
	}

	/**
	 * 写出空格
	 *
	 * @param count 空格数
	 */
	private void writeSpace(final int count) {
		if (indentFactor > 0) {
			for (int i = 0; i < count; i++) {
				//noinspection resource
				writeRaw(CharUtil.SPACE);
			}
		}
	}

	/**
	 * 写出换换行符
	 *
	 * @return this
	 */
	private JSONWriter writeLF() {
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
	private JSONWriter writeRaw(final String csq) {
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
	 * @param c 字符串
	 * @return this
	 */
	private JSONWriter writeRaw(final char c) {
		try {
			writer.write(c);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 按照给定格式格式化日期，格式为空时返回时间戳字符串
	 *
	 * @param dateObj Date或者Calendar对象
	 * @param format  格式
	 * @return 日期字符串
	 */
	private static String formatDate(final Object dateObj, final String format) {
		if (StrUtil.isNotBlank(format)) {
			final String dateStr;
			if (dateObj instanceof TemporalAccessor) {
				dateStr = TemporalAccessorUtil.format((TemporalAccessor) dateObj, format);
			} else {
				dateStr = DateUtil.format(Convert.toDate(dateObj), format);
			}

			if (GlobalCustomFormat.FORMAT_SECONDS.equals(format)
					|| GlobalCustomFormat.FORMAT_MILLISECONDS.equals(format)) {
				// Hutool自定义的秒和毫秒表示，默认不包装双引号
				return dateStr;
			}
			//用户定义了日期格式
			return InternalJSONUtil.quote(dateStr);
		}

		//默认使用时间戳
		final long timeMillis;
		if (dateObj instanceof TemporalAccessor) {
			timeMillis = TemporalAccessorUtil.toEpochMilli((TemporalAccessor) dateObj);
		} else if (dateObj instanceof Date) {
			timeMillis = ((Date) dateObj).getTime();
		} else if (dateObj instanceof Calendar) {
			timeMillis = ((Calendar) dateObj).getTimeInMillis();
		} else {
			throw new UnsupportedOperationException("Unsupported Date type: " + dateObj.getClass());
		}
		return String.valueOf(timeMillis);
	}
}
