package cn.hutool.json.writer;

/**
 * Boolean类型的值写出器
 *
 * @author looly
 * @since 6.0.0
 */
public class BooleanValueWriter implements JSONValueWriter<Boolean> {
	/**
	 * 单例对象
	 */
	public static final BooleanValueWriter INSTANCE = new BooleanValueWriter();

	@Override
	public void write(final JSONWriter writer, final Boolean bool) {
		writer.writeRaw(bool.toString());
	}
}
