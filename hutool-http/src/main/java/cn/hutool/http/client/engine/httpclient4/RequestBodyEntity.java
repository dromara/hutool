package cn.hutool.http.client.engine.httpclient4;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.http.client.body.BytesBody;
import cn.hutool.http.client.body.RequestBody;
import cn.hutool.http.client.body.ResourceBody;
import org.apache.http.entity.AbstractHttpEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * {@link RequestBody}转换为{@link org.apache.hc.core5.http.HttpEntity}对象
 *
 * @author looly
 * @since 6.0.0
 */
public class RequestBodyEntity extends AbstractHttpEntity {

	private final RequestBody body;

	/**
	 * 构造
	 *
	 * @param contentType Content-Type类型
	 * @param charset     自定义请求编码
	 * @param chunked     是否块模式传输
	 * @param body        {@link RequestBody}
	 */
	public RequestBodyEntity(final String contentType, final Charset charset, final boolean chunked, final RequestBody body) {
		super();
		setContentType(contentType);
		setContentEncoding(null == charset ? null : charset.name());
		setChunked(chunked);
		this.body = body;
	}

	@Override
	public void writeTo(final OutputStream outStream) {
		if (null != body) {
			body.writeClose(outStream);
		}
	}

	@Override
	public InputStream getContent() {
		if (body instanceof ResourceBody) {
			return ((ResourceBody) body).getResource().getStream();
		} else {
			final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
			body.writeClose(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

	@Override
	public boolean isStreaming() {
		return body instanceof BytesBody;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public long getContentLength() {
		return 0;
	}
}
