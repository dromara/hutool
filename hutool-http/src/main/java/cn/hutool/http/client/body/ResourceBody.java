package cn.hutool.http.client.body;

import cn.hutool.core.io.resource.HttpResource;
import cn.hutool.core.io.resource.Resource;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link Resource}类型的Http request body，主要发送资源文件中的内容
 *
 * @author looly
 * @since 6.0.0
 */
public class ResourceBody implements HttpBody {

	private final HttpResource resource;

	/**
	 * 创建 Http request body
	 *
	 * @param resource body内容
	 * @return BytesBody
	 */
	public static ResourceBody of(final HttpResource resource) {
		return new ResourceBody(resource);
	}

	/**
	 * 构造
	 *
	 * @param resource Body内容
	 */
	public ResourceBody(final HttpResource resource) {
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

	@Override
	public InputStream getStream() {
		return resource.getStream();
	}

	@Override
	public String getContentType() {
		return this.resource.getContentType();
	}
}
