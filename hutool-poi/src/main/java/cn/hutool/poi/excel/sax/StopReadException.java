package cn.hutool.poi.excel.sax;

import cn.hutool.poi.exceptions.POIException;

/**
 * 读取结束异常，用于标记读取结束<br>
 * Sax方式读取时，如果用户在RowHandler中抛出此异常，表示读取结束，此时不再读取其他数据
 *
 * @author Looly
 * @since 5.8.35
 */
public class StopReadException extends POIException {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 */
	public StopReadException() {
		this("Stop read by user.");
	}

	/**
	 * 构造
	 *
	 * @param message 消息
	 */
	public StopReadException(final String message) {
		super(message);
		// 去除堆栈
		setStackTrace(new StackTraceElement[0]);
	}
}
