package cn.hutool.json.writer;

import cn.hutool.core.math.NumberUtil;
import cn.hutool.json.JSONConfig;

/**
 * 数字类型的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class NumberValueWriter implements JSONValueWriter<Number> {
	/**
	 * 单例对象
	 */
	public static final NumberValueWriter INSTANCE = new NumberValueWriter();

	/**
	 * 写出数字，根据{@link JSONConfig#isStripTrailingZeros()} 配置不同，写出不同数字<br>
	 * 主要针对Double型是否去掉小数点后多余的0<br>
	 * 此方法输出的值不包装引号。
	 *
	 * @param writer {@link JSONWriter}
	 * @param number 数字
	 */
	@Override
	public void write(final JSONWriter writer, final Number number) {
		final JSONConfig config = writer.getConfig();
		// since 5.6.2可配置是否去除末尾多余0，例如如果为true,5.0返回5
		final boolean isStripTrailingZeros = null == config || config.isStripTrailingZeros();
		writer.writeRaw(NumberUtil.toStr(number, isStripTrailingZeros));
	}
}
