package cn.hutool.http.body;

import cn.hutool.core.io.IoUtil;

import java.io.OutputStream;

/**
 * 定义请求体接口
 */
public interface RequestBody {

	/**
	 * 写出数据，不关闭流
	 *
	 * @param out out流
	 */
	void write(OutputStream out);

	/**
	 * 写出并关闭{@link OutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @since 5.7.17
	 */
	default void writeClose(OutputStream out) {
		try {
			write(out);
		} finally {
			IoUtil.close(out);
		}
	}
}
