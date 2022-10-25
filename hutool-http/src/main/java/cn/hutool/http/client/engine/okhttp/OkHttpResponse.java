package cn.hutool.http.client.engine.okhttp;

import cn.hutool.core.io.EmptyInputStream;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.client.Response;
import cn.hutool.http.meta.Header;
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
		final String contentType = rawRes.header(Header.CONTENT_TYPE.getValue());
		if(StrUtil.isNotEmpty(contentType)){
			final String charset = HttpUtil.getCharset(contentType);
			CharsetUtil.parse(charset, this.requestCharset);
		}
		return this.requestCharset;
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
