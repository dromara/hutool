package cn.hutool.http.body;

import cn.hutool.core.io.IoUtil;

import java.io.OutputStream;

/**
 * bytes类型的Http request body，主要发送编码后的表单数据或rest body（如JSON或XML）
 *
 * @since 5.7.17
 * @author looly
 */
public class BytesBody implements RequestBody {

	private final byte[] content;

	/**
	 * 创建 Http request body
	 * @param content body内容，编码后
	 * @return BytesBody
	 */
	public static BytesBody create(byte[] content){
		return new BytesBody(content);
	}

	/**
	 * 构造
	 *
	 * @param content Body内容，编码后
	 */
	public BytesBody(byte[] content) {
		this.content = content;
	}

	@Override
	public void write(OutputStream out) {
		IoUtil.write(out, false, content);
	}
}
