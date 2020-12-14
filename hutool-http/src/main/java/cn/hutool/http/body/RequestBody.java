package cn.hutool.http.body;

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
}
