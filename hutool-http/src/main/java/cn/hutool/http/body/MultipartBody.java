package cn.hutool.http.body;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResource;
import cn.hutool.http.HttpUtil;

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

	private static final String BOUNDARY = "--------------------Hutool_" + RandomUtil.randomString(16);
	private static final String BOUNDARY_END = StrUtil.format("--{}--\r\n", BOUNDARY);
	private static final String CONTENT_DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n";
	private static final String CONTENT_DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";

	private static final String CONTENT_TYPE_MULTIPART_PREFIX = ContentType.MULTIPART.getValue() + "; boundary=";
	private static final String CONTENT_TYPE_FILE_TEMPLATE = "Content-Type: {}\r\n";

	/**
	 * 存储表单数据
	 */
	private final Map<String, Object> form;
	/**
	 * 编码
	 */
	private final Charset charset;

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
	public static String getContentType() {
		return CONTENT_TYPE_MULTIPART_PREFIX + BOUNDARY;
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
		writeForm(out);
		formEnd(out);
	}

	@Override
	public String toString() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(out);
		return IoUtil.toStr(out, this.charset);
	}

	// 普通字符串数据

	/**
	 * 发送文件对象表单
	 *
	 * @param out 输出流
	 */
	private void writeForm(OutputStream out) {
		if (MapUtil.isNotEmpty(this.form)) {
			for (Map.Entry<String, Object> entry : this.form.entrySet()) {
				appendPart(entry.getKey(), entry.getValue(), out);
			}
		}
	}

	/**
	 * 添加Multipart表单的数据项<br>
	 * <pre>
	 *     --分隔符(boundary)[换行]
	 *     Content-Disposition: form-data; name="参数名"[换行]
	 *     [换行]
	 *     参数值[换行]
	 * </pre>
	 *
	 * 或者：
	 *
	 * <pre>
	 *     --分隔符(boundary)[换行]
	 *     Content-Disposition: form-data; name="表单名"; filename="文件名"[换行]
	 *     Content-Type: MIME类型[换行]
	 *     [换行]
	 *     文件的二进制内容[换行]
	 * </pre>
	 *
	 * @param formFieldName 表单名
	 * @param value         值，可以是普通值、资源（如文件等）
	 * @param out           Http流
	 * @throws IORuntimeException IO异常
	 */
	private void appendPart(String formFieldName, Object value, OutputStream out) throws IORuntimeException {
		// 多资源
		if (value instanceof MultiResource) {
			for (Resource subResource : (MultiResource) value) {
				appendPart(formFieldName, subResource, out);
			}
			return;
		}

		// --分隔符(boundary)[换行]
		write(out, "--", BOUNDARY, StrUtil.CRLF);

		if (value instanceof Resource) {
			appendResource(formFieldName, (Resource) value, out);
		} else {
			/*
			 * Content-Disposition: form-data; name="参数名"[换行]
			 * [换行]
			 * 参数值
			 */
			write(out, StrUtil.format(CONTENT_DISPOSITION_TEMPLATE, formFieldName));
			write(out, StrUtil.CRLF);
			write(out, value);
		}

		write(out, StrUtil.CRLF);
	}

	/**
	 * 添加Multipart表单的Resource数据项，支持包括{@link HttpResource}资源格式
	 *
	 * @param formFieldName 表单名
	 * @param resource      资源
	 * @param out           Http流
	 * @throws IORuntimeException IO异常
	 */
	private void appendResource(String formFieldName, Resource resource, OutputStream out) throws IORuntimeException {
		final String fileName = resource.getName();

		// Content-Disposition
		if (null == fileName) {
			// Content-Disposition: form-data; name="参数名"[换行]
			write(out, StrUtil.format(CONTENT_DISPOSITION_TEMPLATE, formFieldName));
		} else {
			// Content-Disposition: form-data; name="参数名"; filename="文件名"[换行]
			write(out, StrUtil.format(CONTENT_DISPOSITION_FILE_TEMPLATE, formFieldName, fileName));
		}

		// Content-Type
		if (resource instanceof HttpResource) {
			final String contentType = ((HttpResource) resource).getContentType();
			if (StrUtil.isNotBlank(contentType)) {
				// Content-Type: 类型[换行]
				write(out, StrUtil.format(CONTENT_TYPE_FILE_TEMPLATE, contentType));
			}
		} else {
			// 根据name的扩展名指定互联网媒体类型，默认二进制流数据
			write(out, StrUtil.format(CONTENT_TYPE_FILE_TEMPLATE,
					HttpUtil.getMimeType(fileName, ContentType.OCTET_STREAM.getValue())));
		}

		// 内容
		write(out, "\r\n");
		resource.writeTo(out);
	}

	/**
	 * 上传表单结束
	 *
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 */
	private void formEnd(OutputStream out) throws IORuntimeException {
		write(out, BOUNDARY_END);
	}

	/**
	 * 写出对象
	 *
	 * @param out  输出流
	 * @param objs 写出的对象（转换为字符串）
	 */
	private void write(OutputStream out, Object... objs) {
		IoUtil.write(out, this.charset, false, objs);
	}
}
