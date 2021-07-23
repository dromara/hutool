package cn.hutool.json.serialize;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.date.format.GlobalCustomFormat;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import cn.hutool.json.JSONUtil;

import java.io.IOException;
import java.io.Writer;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

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
	 * 创建{@link JSONWriter}
	 *
	 * @param writer       {@link Writer}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param config       JSON选项
	 * @return {@link JSONWriter}
	 */
	public static JSONWriter of(Writer writer, int indentFactor, int indent, JSONConfig config) {
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
	public JSONWriter(Writer writer, int indentFactor, int indent, JSONConfig config) {
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
		writeRaw(CharUtil.DELIM_START);
		return this;
	}

	/**
	 * JSONArray写出开始，默认写出"["
	 *
	 * @return this
	 */
	public JSONWriter beginArray() {
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
		writeLF().writeSpace(indent);
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
	public JSONWriter writeKey(String key) {
		if (needSeparator) {
			writeRaw(CharUtil.COMMA);
		}
		// 换行缩进
		writeLF().writeSpace(indentFactor + indent);
		return writeRaw(JSONUtil.quote(key));
	}

	/**
	 * 写出值，自动处理分隔符和缩进，自动判断类型，并根据不同类型写出特定格式的值
	 *
	 * @param value 值
	 * @return this
	 */
	public JSONWriter writeValue(Object value) {
		if (arrayMode) {
			if (needSeparator) {
				writeRaw(CharUtil.COMMA);
			}
			// 换行缩进
			writeLF().writeSpace(indentFactor + indent);
		} else {
			writeRaw(CharUtil.COLON).writeSpace(1);
		}
		needSeparator = true;
		return writeObjValue(value);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		this.writer.write(cbuf, off, len);
	}

	@Override
	public void flush() {
		try {
			this.writer.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

	// ------------------------------------------------------------------------------ Private methods

	/**
	 * 写出JSON的值，根据值类型不同，输出不同内容
	 *
	 * @param value 值
	 * @return this
	 */
	private JSONWriter writeObjValue(Object value) {
		final int indent = indentFactor + this.indent;
		if (value == null || value instanceof JSONNull) {
			writeRaw(JSONNull.NULL.toString());
		} else if (value instanceof JSON) {
			((JSON) value).write(writer, indentFactor, indent);
		} else if (value instanceof Map) {
			new JSONObject(value).write(writer, indentFactor, indent);
		} else if (value instanceof Iterable || value instanceof Iterator || ArrayUtil.isArray(value)) {
			new JSONArray(value).write(writer, indentFactor, indent);
		} else if (value instanceof Number) {
			writeNumberValue((Number) value);
		} else if (value instanceof Date || value instanceof Calendar || value instanceof TemporalAccessor) {
			final String format = (null == config) ? null : config.getDateFormat();
			writeRaw(formatDate(value, format));
		} else if (value instanceof Boolean) {
			writeBooleanValue((Boolean) value);
		} else if (value instanceof JSONString) {
			writeJSONStringValue((JSONString) value);
		} else {
			writeStrValue(value.toString());
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
	private JSONWriter writeNumberValue(Number number) {
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = null == config || config.isStripTrailingZeros();
		return writeRaw(NumberUtil.toStr(number, isStripTrailingZeros));
	}

	/**
	 * 写出Boolean值，直接写出true或false,不适用引号包装
	 *
	 * @param value Boolean值
	 * @return this
	 */
	private JSONWriter writeBooleanValue(Boolean value) {
		return writeRaw(value.toString());
	}

	/**
	 * 输出实现了{@link JSONString}接口的对象，通过调用{@link JSONString#toJSONString()}获取JSON字符串<br>
	 * {@link JSONString}按照JSON对象对待，此方法输出的JSON字符串不包装引号。<br>
	 * 如果toJSONString()返回null，调用toString()方法并使用双引号包装。
	 *
	 * @param jsonString {@link JSONString}
	 * @return this
	 */
	private JSONWriter writeJSONStringValue(JSONString jsonString) {
		String valueStr;
		try {
			valueStr = jsonString.toJSONString();
		} catch (Exception e) {
			throw new JSONException(e);
		}
		if (null != valueStr) {
			writeRaw(valueStr);
		} else {
			writeStrValue(jsonString.toString());
		}
		return this;
	}

	/**
	 * 写出字符串值，并包装引号并转义字符<br>
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param csq 字符串
	 * @return this
	 */
	private JSONWriter writeStrValue(String csq) {
		try {
			JSONUtil.quote(csq, writer);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}

	/**
	 * 写出空格
	 *
	 * @param count 空格数
	 * @return this
	 */
	private JSONWriter writeSpace(int count) {
		if (indentFactor > 0) {
			for (int i = 0; i < count; i++) {
				writeRaw(CharUtil.SPACE);
			}
		}
		return this;
	}

	/**
	 * 写出换换行符
	 *
	 * @return this
	 */
	private JSONWriter writeLF() {
		if (indentFactor > 0) {
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
	private JSONWriter writeRaw(String csq) {
		try {
			writer.append(csq);
		} catch (IOException e) {
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
	private JSONWriter writeRaw(char c) {
		try {
			writer.write(c);
		} catch (IOException e) {
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
	private static String formatDate(Object dateObj, String format) {
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
			return JSONUtil.quote(dateStr);
		}

		//默认使用时间戳
		long timeMillis;
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
