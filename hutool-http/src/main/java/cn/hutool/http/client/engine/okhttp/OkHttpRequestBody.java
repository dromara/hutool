package cn.hutool.http.client.engine.okhttp;

import cn.hutool.http.client.body.RequestBody;
import okhttp3.MediaType;
import okio.BufferedSink;

/**
 * OkHttp的请求体实现，通过{@link RequestBody}转换实现
 *
 * @author looly
 */
public class OkHttpRequestBody extends okhttp3.RequestBody {

	private final RequestBody body;

	/**
	 * 构造
	 *
	 * @param body 请求体{@link RequestBody}
	 */
	public OkHttpRequestBody(final RequestBody body) {
		this.body = body;
	}

	public MediaType contentType() {
		return null;
	}

	@Override
	public void writeTo(final BufferedSink bufferedSink) {
		body.writeClose(bufferedSink.outputStream());
	}
}
