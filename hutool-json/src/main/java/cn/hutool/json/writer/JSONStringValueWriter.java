package cn.hutool.json.writer;

import cn.hutool.json.JSONException;
import cn.hutool.json.serialize.JSONStringer;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类型的值写出器<br>
 * 支持包括：{@link Date}、{@link Calendar}、{@link TemporalAccessor}
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONStringValueWriter implements JSONValueWriter<JSONStringer> {
	/**
	 * 单例对象
	 */
	public static final JSONStringValueWriter INSTANCE = new JSONStringValueWriter();

	/**
	 * 输出实现了{@link JSONStringer}接口的对象，通过调用{@link JSONStringer#toJSONString()}获取JSON字符串<br>
	 * {@link JSONStringer}按照JSON对象对待，此方法输出的JSON字符串不包装引号。<br>
	 * 如果toJSONString()返回null，调用toString()方法并使用双引号包装。
	 *
	 * @param writer {@link JSONWriter}
	 * @param jsonStringer {@link JSONStringer}
	 */
	@Override
	public void write(final JSONWriter writer, final JSONStringer jsonStringer) {
		final String valueStr;
		try {
			valueStr = jsonStringer.toJSONString();
		} catch (final Exception e) {
			throw new JSONException(e);
		}
		if (null != valueStr) {
			writer.writeRaw(valueStr);
		} else {
			writer.writeQuoteStrValue(jsonStringer.toString());
		}
	}
}
