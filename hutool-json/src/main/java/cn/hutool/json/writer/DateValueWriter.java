package cn.hutool.json.writer;

import cn.hutool.json.serialize.DateJSONString;

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
public class DateValueWriter implements JSONValueWriter<Object> {
	/**
	 * 单例对象
	 */
	public static final DateValueWriter INSTANCE = new DateValueWriter();

	@Override
	public void write(final JSONWriter writer, final Object value) {
		writer.writeRaw(new DateJSONString(value, writer.getConfig()).toJSONString());
	}
}
