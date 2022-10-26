package cn.hutool.http.client.body;

import cn.hutool.core.io.stream.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 定义请求体接口
 */
public interface HttpBody {

	/**
	 * 写出数据，不关闭流
	 *
	 * @param out out流
	 */
	void write(OutputStream out);

	/**
	 * 获取Content-Type
	 *
	 * @return Content-Type值
	 */
	String getContentType();

	/**
	 * 写出并关闭{@link OutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @since 5.7.17
	 */
	default void writeClose(final OutputStream out) {
		try {
			write(out);
		} finally {
			IoUtil.close(out);
		}
	}

	/**
	 * 获取body资源流
	 *
	 * @return {@link InputStream}
	 */
	default InputStream getStream() {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		writeClose(out);
		return IoUtil.toStream(out);
	}
}
