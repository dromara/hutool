package cn.hutool.http.client.body;

import cn.hutool.core.io.resource.Resource;

import java.io.OutputStream;

/**
 * {@link Resource}类型的Http request body，主要发送资源文件中的内容
 *
 * @author looly
 * @since 6.0.0
 */
public class ResourceBody implements RequestBody {

	private final Resource resource;

	/**
	 * 创建 Http request body
	 *
	 * @param resource body内容
	 * @return BytesBody
	 */
	public static ResourceBody of(final Resource resource) {
		return new ResourceBody(resource);
	}

	/**
	 * 构造
	 *
	 * @param resource Body内容
	 */
	public ResourceBody(final Resource resource) {
		this.resource = resource;
	}

	/**
	 * 获取资源
	 *
	 * @return 资源
	 */
	public Resource getResource() {
		return this.resource;
	}

	@Override
	public void write(final OutputStream out) {
		resource.writeTo(out);
	}
}
