package cn.hutool.core.date;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;

/**
 * {@link TemporalAccessor} 工具类封装
 *
 * @author looly
 * @since 5.3.9
 */
public class TemporalAccessorUtil {

	/**
	 * 安全获取时间的某个属性，属性不存在返回0
	 *
	 * @param temporalAccessor 需要获取的时间对象
	 * @param field            需要获取的属性
	 * @return 时间的值，如果无法获取则默认为 0
	 */
	public static int get(TemporalAccessor temporalAccessor, TemporalField field) {
		if (temporalAccessor.isSupported(field)) {
			return temporalAccessor.get(field);
		}

		return (int)field.range().getMinimum();
	}
}
