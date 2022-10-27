package cn.hutool.http.client.engine.okhttp;

import cn.hutool.core.io.stream.EmptyInputStream;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.client.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * OkHttp3的{@link okhttp3.Response} 响应包装
 *
 * @author looly
 */
public class OkHttpResponse implements Response {

	private final okhttp3.Response rawRes;
	/**
	 * 请求时的默认编码
	 */
	private final Charset requestCharset;

	/**
	 * @param rawRes         {@link okhttp3.Response}
	 * @param requestCharset 请求时的默认编码
	 */
	public OkHttpResponse(final okhttp3.Response rawRes, final Charset requestCharset) {
		this.rawRes = rawRes;
		this.requestCharset = requestCharset;
	}

	@Override
	public int getStatus() {
		return rawRes.code();
	}

	@Override
	public String header(final String name) {
		return rawRes.header(name);
	}

	@Override
	public Charset charset() {
		return ObjUtil.defaultIfNull(Response.super.charset(), requestCharset);
	}

	@Override
	public InputStream bodyStream() {
		final ResponseBody body = rawRes.body();
		if(null == body){
			return EmptyInputStream.INSTANCE;
		}
		return body.byteStream();
	}

	@Override
	public void close() throws IOException {
		rawRes.close();
	}
}
