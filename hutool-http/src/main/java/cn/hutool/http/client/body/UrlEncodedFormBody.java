package cn.hutool.http.client.body;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.http.meta.ContentType;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * application/x-www-form-urlencoded 类型请求body封装
 *
 * @author looly
 * @since 5.7.17
 */
public class UrlEncodedFormBody extends FormBody<UrlEncodedFormBody> {

	/**
	 * 创建 Http request body
	 *
	 * @param form    表单
	 * @param charset 编码
	 * @return FormUrlEncodedBody
	 */
	public static UrlEncodedFormBody of(final Map<String, Object> form, final Charset charset) {
		return new UrlEncodedFormBody(form, charset);
	}

	/**
	 * 构造
	 *
	 * @param form    表单
	 * @param charset 编码
	 */
	public UrlEncodedFormBody(final Map<String, Object> form, final Charset charset) {
		super(form, charset);
	}

	@Override
	public void write(final OutputStream out) {
		final byte[] bytes = ByteUtil.toBytes(UrlQuery.of(form, true).build(charset), charset);
		IoUtil.write(out, false, bytes);
	}

	@Override
	public String getContentType() {
		return ContentType.FORM_URLENCODED.toString(charset);
	}
}
