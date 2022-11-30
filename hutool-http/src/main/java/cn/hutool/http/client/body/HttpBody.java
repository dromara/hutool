package cn.hutool.http.client.body;

import cn.hutool.core.io.stream.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
	 * 获取指定编码的Content-Type，类似于：application/json;charset=UTF-8
	 * @param charset 编码
	 * @return Content-Type
	 */
	default String getContentType(final Charset charset){
		final String contentType = getContentType();
		if(null == contentType){
			return null;
		}

		return contentType + ";charset=" + charset.name();
	}

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
