package cn.hutool.http.client.engine.httpclient4;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.client.Response;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * HttpClient响应包装<br>
 * 通过包装{@link CloseableHttpResponse}，实现获取响应状态码、响应头、响应体等内容
 *
 * @author looly
 */
public class HttpClient4Response implements Response {

	/**
	 * HttpClient的响应对象
	 */
	private final CloseableHttpResponse rawRes;
	/**
	 * 请求时的默认编码
	 */
	private final Charset requestCharset;

	/**
	 * 构造<br>
	 * 通过传入一个请求时的编码，当无法获取响应内容的编码时，默认使用响应时的编码
	 *
	 * @param rawRes         {@link CloseableHttpResponse}
	 * @param requestCharset 请求时的编码
	 */
	public HttpClient4Response(final CloseableHttpResponse rawRes, final Charset requestCharset) {
		this.rawRes = rawRes;
		this.requestCharset = requestCharset;
	}


	@Override
	public int getStatus() {
		return rawRes.getStatusLine().getStatusCode();
	}

	@Override
	public String header(final String name) {
		final Header[] headers = rawRes.getHeaders(name);
		if (ArrayUtil.isNotEmpty(headers)) {
			return headers[0].getValue();
		}

		return null;
	}

	@Override
	public long contentLength() {
		return rawRes.getEntity().getContentLength();
	}

	@Override
	public Charset charset() {
		final Charset charset = ContentType.parse(rawRes.getEntity().getContentType().getValue()).getCharset();
		return ObjUtil.defaultIfNull(charset, requestCharset);
	}

	@Override
	public InputStream bodyStream() {
		try {
			return rawRes.getEntity().getContent();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	@Override
	public String bodyStr() throws HttpException {
		try {
			return EntityUtils.toString(rawRes.getEntity(), charset());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} catch (final ParseException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public void close() throws IOException {
		rawRes.close();
	}
}
