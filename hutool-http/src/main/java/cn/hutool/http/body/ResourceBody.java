package cn.hutool.http.body;

import cn.hutool.core.io.resource.Resource;

import java.io.OutputStream;

/**
 * {@link Resource}类型的Http request body，主要发送编码后的表单数据或rest body（如JSON或XML）
 *
 * @author looly
 * @since 5.8.13
 */
public class ResourceBody implements RequestBody {

	private final Resource resource;

	/**
	 * 创建 Http request body
	 *
	 * @param resource body内容，编码后
	 * @return BytesBody
	 */
	public static ResourceBody create(Resource resource) {
		return new ResourceBody(resource);
	}

	/**
	 * 构造
	 *
	 * @param resource Body内容，编码后
	 */
	public ResourceBody(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void write(OutputStream out) {
		if(null != this.resource){
			this.resource.writeTo(out);
		}
	}
}
