package cn.hutool.http.client.body;

import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.text.StrUtil;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * application/x-www-form-urlencoded 类型请求body封装
 *
 * @author looly
 * @since 5.7.17
 */
public class FormUrlEncodedBody extends BytesBody {

	/**
	 * 创建 Http request body
	 *
	 * @param form    表单
	 * @param charset 编码
	 * @return FormUrlEncodedBody
	 */
	public static FormUrlEncodedBody of(final Map<String, Object> form, final Charset charset) {
		return new FormUrlEncodedBody(form, charset);
	}

	/**
	 * 构造
	 *
	 * @param form    表单
	 * @param charset 编码
	 */
	public FormUrlEncodedBody(final Map<String, Object> form, final Charset charset) {
		super(StrUtil.bytes(UrlQuery.of(form, true).build(charset), charset));
	}

}
