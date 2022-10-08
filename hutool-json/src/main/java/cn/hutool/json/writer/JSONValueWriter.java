package cn.hutool.json.writer;

import java.io.IOException;

/**
 * JSON的值自定义写出
 *
 * @param <T> 写出的对象类型
 * @author looly
 * @since 6.0.0
 */
public interface JSONValueWriter<T> {

	/**
	 * 使用{@link JSONWriter} 写出对象
	 *
	 * @param writer {@link JSONWriter}
	 * @param value  被写出的值
	 * @throws IOException IO异常
	 */
	void write(JSONWriter writer, T value);
}
