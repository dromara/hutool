package cn.hutool.http.client.engine.httpclient4;

import cn.hutool.http.client.body.BytesBody;
import cn.hutool.http.client.body.HttpBody;
import org.apache.http.entity.AbstractHttpEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * {@link HttpBody}转换为{@link org.apache.hc.core5.http.HttpEntity}对象
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpClient4BodyEntity extends AbstractHttpEntity {

	private final HttpBody body;

	/**
	 * 构造
	 *
	 * @param contentType Content-Type类型
	 * @param charset     自定义请求编码
	 * @param chunked     是否块模式传输
	 * @param body        {@link HttpBody}
	 */
	public HttpClient4BodyEntity(final String contentType, final Charset charset, final boolean chunked, final HttpBody body) {
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
		return body.getStream();
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
