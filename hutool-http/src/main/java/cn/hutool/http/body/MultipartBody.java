package cn.hutool.http.body;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.MultipartOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Multipart/form-data数据的请求体封装<br>
 * 遵循RFC2388规范
 *
 * @author looly
 * @since 5.3.5
 */
public class MultipartBody implements RequestBody {

	private static final String CONTENT_TYPE_MULTIPART_PREFIX = ContentType.MULTIPART.getValue() + "; boundary=";

	/**
	 * 存储表单数据
	 */
	private final Map<String, Object> form;
	/**
	 * 编码
	 */
	private final Charset charset;
	/**
	 * 边界
	 */
	private final String boundary = HttpGlobalConfig.getBoundary();

	/**
	 * 根据已有表单内容，构建MultipartBody
	 *
	 * @param form    表单
	 * @param charset 编码
	 * @return MultipartBody
	 */
	public static MultipartBody create(Map<String, Object> form, Charset charset) {
		return new MultipartBody(form, charset);
	}

	/**
	 * 获取Multipart的Content-Type类型
	 *
	 * @return Multipart的Content-Type类型
	 */
	public String getContentType() {
		return CONTENT_TYPE_MULTIPART_PREFIX + boundary;
	}

	/**
	 * 构造
	 *
	 * @param form    表单
	 * @param charset 编码
	 */
	public MultipartBody(Map<String, Object> form, Charset charset) {
		this.form = form;
		this.charset = charset;
	}

	/**
	 * 写出Multiparty数据，不关闭流
	 *
	 * @param out out流
	 */
	@Override
	public void write(OutputStream out) {
		final MultipartOutputStream stream = new MultipartOutputStream(out, this.charset, this.boundary);
		if (MapUtil.isNotEmpty(this.form)) {
			this.form.forEach(stream::write);
		}
		stream.finish();
	}

	@Override
	public String toString() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(out);
		return IoUtil.toStr(out, this.charset);
	}
}
