package cn.hutool.http;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Assert;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * HTTP资源，可自定义Content-Type
 *
 * @author looly
 * @since 5.7.17
 */
public class HttpResource implements Resource, Serializable {
	private static final long serialVersionUID = 1L;

	private final Resource resource;
	private final String contentType;

	/**
	 * 构造
	 *
	 * @param resource    资源，非空
	 * @param contentType Content-Type类型，{@code null}表示不设置
	 */
	public HttpResource(Resource resource, String contentType) {
		this.resource = Assert.notNull(resource, "Resource must be not null !");
		this.contentType = contentType;
	}

	@Override
	public String getName() {
		return resource.getName();
	}

	@Override
	public URL getUrl() {
		return resource.getUrl();
	}

	@Override
	public InputStream getStream() {
		return resource.getStream();
	}

	/**
	 * 获取自定义Content-Type类型
	 *
	 * @return Content-Type类型
	 */
	public String getContentType() {
		return this.contentType;
	}
}
