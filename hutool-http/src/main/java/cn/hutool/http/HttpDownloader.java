package cn.hutool.http;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Assert;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 下载封装，下载统一使用{@code GET}请求，默认支持30x跳转
 *
 * @since 5.6.4
 * @author looly
 */
public class HttpDownloader {

	/**
	 * 下载远程文本
	 *
	 * @param url           请求的url
	 * @param customCharset 自定义的字符集，可以使用{@code CharsetUtil#charset} 方法转换
	 * @param streamPress   进度条 {@link StreamProgress}
	 * @return 文本
	 */
	public static String downloadString(String url, Charset customCharset, StreamProgress streamPress) {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		download(url, out, true, streamPress);
		return null == customCharset ? out.toString() : out.toString(customCharset);
	}

	/**
	 * 下载远程文件数据，支持30x跳转
	 *
	 * @param url 请求的url
	 * @return 文件数据
	 */
	public static byte[] downloadBytes(String url) {
		return requestDownload(url, -1).bodyBytes();
	}

	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout        超时，单位毫秒，-1表示默认超时
	 * @param streamProgress 进度条
	 * @return 文件大小
	 */
	public static long downloadFile(String url, File destFile, int timeout, StreamProgress streamProgress) {
		return requestDownload(url, timeout).writeBody(destFile, streamProgress);
	}

	/**
	 * 下载远程文件，返回文件
	 *
	 * @param url            请求的url
	 * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout        超时，单位毫秒，-1表示默认超时
	 * @param streamProgress 进度条
	 * @return 文件
	 */
	public static File downloadForFile(String url, File destFile, int timeout, StreamProgress streamProgress) {
		return requestDownload(url, timeout).writeBodyForFile(destFile, streamProgress);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url            请求的url
	 * @param out            将下载内容写到输出流中 {@link OutputStream}
	 * @param isCloseOut     是否关闭输出流
	 * @param streamProgress 进度条
	 * @return 文件大小
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
		Assert.notNull(out, "[out] is null !");

		return requestDownload(url, -1).writeBody(out, isCloseOut, streamProgress);
	}

	/**
	 * 请求下载文件
	 *
	 * @param url      请求下载文件地址
	 * @param timeout  超时时间
	 * @return HttpResponse
	 * @since 5.4.1
	 */
	private static HttpResponse requestDownload(String url, int timeout) {
		Assert.notBlank(url, "[url] is blank !");

		final HttpResponse response = HttpUtil.createGet(url, true)
				.timeout(timeout)
				.executeAsync();

		if (response.isOk()) {
			return response;
		}

		throw new HttpException("Server response error with status code: [{}]", response.getStatus());
	}
}
