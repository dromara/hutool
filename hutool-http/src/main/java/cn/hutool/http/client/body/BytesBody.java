package cn.hutool.http.client.body;

import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.io.resource.HttpResource;

/**
 * bytes类型的Http request body，主要发送编码后的表单数据或rest body（如JSON或XML）
 *
 * @since 5.7.17
 * @author looly
 */
public class BytesBody extends ResourceBody {

	/**
	 * 创建 Http request body
	 * @param content body内容，编码后
	 * @return BytesBody
	 */
	public static BytesBody of(final byte[] content){
		return new BytesBody(content);
	}

	/**
	 * 构造
	 *
	 * @param content Body内容，编码后
	 */
	public BytesBody(final byte[] content) {
		super(new HttpResource(new BytesResource(content), null));
	}
}
