package cn.hutool.core.io.stream;

import java.io.OutputStream;

/**
 * 此OutputStream写出数据到<b>/dev/null</b>，即忽略所有数据<br>
 * 来自 Apache Commons io
 *
 * @author looly
 * @since 4.0.6
 */
public class EmptyOutputStream extends OutputStream {

	/**
	 * 单例
	 */
	public static final EmptyOutputStream INSTANCE = new EmptyOutputStream();

	private EmptyOutputStream() {
	}

	/**
	 * 什么也不做，写出到{@code /dev/null}.
	 *
	 * @param b   写出的数据
	 * @param off 开始位置
	 * @param len 长度
	 */
	@SuppressWarnings("NullableProblems")
	@Override
	public void write(final byte[] b, final int off, final int len) {
		// to /dev/null
	}

	/**
	 * 什么也不做，写出到 {@code /dev/null}.
	 *
	 * @param b 写出的数据
	 */
	@Override
	public void write(final int b) {
		// to /dev/null
	}

	/**
	 * 什么也不做，写出到 {@code /dev/null}.
	 *
	 * @param b 写出的数据
	 */
	@SuppressWarnings("NullableProblems")
	@Override
	public void write(final byte[] b) {
		// to /dev/null
	}

}
