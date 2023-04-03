/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.io.StreamProgress;
import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 下载封装，下载统一使用{@code GET}请求，默认支持30x跳转
 *
 * @author looly
 * @since 5.6.4
 */
@SuppressWarnings("resource")
public class HttpDownloader {

	/**
	 * 下载远程文本
	 *
	 * @param url           请求的url
	 * @param customCharset 自定义的字符集，可以使用{@code CharsetUtil#charset} 方法转换
	 * @param streamPress   进度条 {@link StreamProgress}
	 * @return 文本
	 */
	public static String downloadString(final String url, final Charset customCharset, final StreamProgress streamPress) {
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
	public static byte[] downloadBytes(final String url) {
		return requestDownload(url, -1).bodyBytes();
	}

	/**
	 * 下载远程文件
	 *
	 * @param url             请求的url
	 * @param targetFileOrDir 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @return 文件
	 */
	public static File downloadFile(final String url, final File targetFileOrDir) {
		return downloadFile(url, targetFileOrDir, -1);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url             请求的url
	 * @param targetFileOrDir 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout         超时，单位毫秒，-1表示默认超时
	 * @return 文件
	 */
	public static File downloadFile(final String url, final File targetFileOrDir, final int timeout) {
		return downloadFile(url, targetFileOrDir, timeout, null);
	}

	/**
	 * 下载远程文件
	 *
	 * @param url             请求的url
	 * @param targetFileOrDir 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param timeout         超时，单位毫秒，-1表示默认超时
	 * @param streamProgress  进度条
	 * @return 文件
	 */
	public static File downloadFile(final String url, final File targetFileOrDir, final int timeout, final StreamProgress streamProgress) {
		return requestDownload(url, timeout).body().write(targetFileOrDir, streamProgress);
	}

	/**
	 * 下载文件-避免未完成的文件<br>
	 * 来自：https://gitee.com/dromara/hutool/pulls/407<br>
	 * 此方法原理是先在目标文件同级目录下创建临时文件，下载之，等下载完毕后重命名，避免因下载错误导致的文件不完整。
	 *
	 * @param url             请求的url
	 * @param targetFileOrDir 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
	 * @param tempFileSuffix  临时文件后缀，默认".temp"
	 * @param timeout         超时，单位毫秒，-1表示默认超时
	 * @param streamProgress  进度条
	 * @return 文件
	 * @since 5.7.12
	 */
	public File downloadFile(final String url, final File targetFileOrDir, final String tempFileSuffix, final int timeout, final StreamProgress streamProgress) {
		return requestDownload(url, timeout).body().write(targetFileOrDir, tempFileSuffix, streamProgress);
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
	public static long download(final String url, final OutputStream out, final boolean isCloseOut, final StreamProgress streamProgress) {
		Assert.notNull(out, "[out] is null !");

		return requestDownload(url, -1).body().write(out, isCloseOut, streamProgress);
	}

	/**
	 * 请求下载文件
	 *
	 * @param url     请求下载文件地址
	 * @param timeout 超时时间
	 * @return HttpResponse
	 * @since 5.4.1
	 */
	private static Response requestDownload(final String url, final int timeout) {
		Assert.notBlank(url, "[url] is blank !");

		final Response response = ClientEngineFactory.get()
				.setConfig(ClientConfig.of().setConnectionTimeout(timeout).setReadTimeout(timeout))
				.send(Request.of(url));

		if (response.isOk()) {
			return response;
		}

		throw new HttpException("Server response error with status code: [{}]", response.getStatus());
	}
}
