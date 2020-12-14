package cn.hutool.http.body;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Multipart/form-data数据的请求体封装
 *
 * @author looly
 * @since 5.3.5
 */
public class MultipartBody implements RequestBody{

	private static final String BOUNDARY = "--------------------Hutool_" + RandomUtil.randomString(16);
	private static final String BOUNDARY_END = StrUtil.format("--{}--\r\n", BOUNDARY);
	private static final String CONTENT_DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n\r\n";
	private static final String CONTENT_DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";

	private static final String CONTENT_TYPE_MULTIPART_PREFIX = ContentType.MULTIPART.getValue() + "; boundary=";
	private static final String CONTENT_TYPE_FILE_TEMPLATE = "Content-Type: {}\r\n\r\n";

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
	 * @param form 表单
	 * @param charset 编码
	 * @return MultipartBody
	 */
	public static MultipartBody create(Map<String, Object> form, Charset charset){
		return new MultipartBody(form, charset);
	}

	/**
	 * 获取Multipart的Content-Type类型
	 *
	 * @return Multipart的Content-Type类型
	 */
	public static String getContentType(){
		return CONTENT_TYPE_MULTIPART_PREFIX + BOUNDARY;
	}

	/**
	 * 构造
	 *
	 * @param form     表单
	 * @param charset  编码
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
	 * 添加Multipart表单的数据项
	 *
	 * @param formFieldName 表单名
	 * @param value      值，可以是普通值、资源（如文件等）
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

		write(out, "--", BOUNDARY, StrUtil.CRLF);

		if(value instanceof Resource){
			// 文件资源（二进制资源）
			final Resource resource = (Resource)value;
			final String fileName = resource.getName();
			write(out, StrUtil.format(CONTENT_DISPOSITION_FILE_TEMPLATE, formFieldName, ObjectUtil.defaultIfNull(fileName, formFieldName)));
			// 根据name的扩展名指定互联网媒体类型，默认二进制流数据
			write(out, StrUtil.format(CONTENT_TYPE_FILE_TEMPLATE, HttpUtil.getMimeType(fileName, "application/octet-stream")));
			resource.writeTo(out);
		} else{
			// 普通数据
			write(out, StrUtil.format(CONTENT_DISPOSITION_TEMPLATE, formFieldName));
			write(out, value);
		}

		write(out, StrUtil.CRLF);
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
