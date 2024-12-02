/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.StreamProgress;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.body.ResponseBody;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * HTTP下载器，两种使用方式：<br>
 * 1. 一次性使用：
 * <pre>{@code HttpDownloader.of(url).downloadFile(file)}</pre>
 * 2. 多次下载复用：
 * <pre>{@code
 *   HttpDownloader downloader = HttpDownloader.of(url).setCloseEngine(false);
 *   downloader.downloadFile(file);
 *   downloader.downloadFile(file2);
 *   downloader.close();
 * }</pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpDownloader {

	/**
	 * 创建下载器
	 *
	 * @param url 请求地址
	 * @return 下载器
	 */
	public static HttpDownloader of(final String url) {
		return new HttpDownloader(url);
	}

	private final Request request;
	private ClientConfig config;
	private ClientEngine engine;
	private StreamProgress streamProgress;
	private boolean closeEngine = true;

	/**
	 * 构造
	 *
	 * @param url 请求地址
	 */
	public HttpDownloader(final String url) {
		this.request = Request.of(url);
	}

	/**
	 * 设置请求头
	 *
	 * @param headers 请求头
	 * @return this
	 */
	public HttpDownloader header(final Map<String, String> headers) {
		this.request.header(headers);
		return this;
	}

	/**
	 * 设置配置
	 *
	 * @param config 配置
	 * @return this
	 */
	public HttpDownloader setConfig(final ClientConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * 设置超时
	 *
	 * @param milliseconds 超时毫秒数
	 * @return this
	 */
	public HttpDownloader setTimeout(final int milliseconds) {
		if (null == this.config) {
			this.config = ClientConfig.of();
		}
		this.config.setTimeout(milliseconds);
		return this;
	}

	/**
	 * 设置引擎，用于自定义引擎
	 *
	 * @param engine 引擎
	 * @return this
	 */
	public HttpDownloader setEngine(final ClientEngine engine) {
		this.engine = engine;
		return this;
	}

	/**
	 * 设置进度条
	 *
	 * @param streamProgress 进度条
	 * @return this
	 */
	public HttpDownloader setStreamProgress(final StreamProgress streamProgress) {
		this.streamProgress = streamProgress;
		return this;
	}

	/**
	 * 设置是否关闭引擎，默认为true，即自动关闭引擎
	 *
	 * @param closeEngine 是否关闭引擎
	 * @return this
	 */
	public HttpDownloader setCloseEngine(final boolean closeEngine) {
		this.closeEngine = closeEngine;
		return this;
	}

	/**
	 * 下载文件
	 *
	 * @param targetFileOrDir 目标文件或目录，当为目录时，自动使用文件名作为下载文件名
	 * @return 下载文件
	 */
	public File downloadFile(final File targetFileOrDir) {
		return downloadFile(targetFileOrDir, null);
	}

	/**
	 * 下载文件
	 *
	 * @param targetFileOrDir 目标文件或目录，当为目录时，自动使用文件名作为下载文件名
	 * @param tempFileSuffix  临时文件后缀
	 * @return 下载文件
	 */
	public File downloadFile(final File targetFileOrDir, final String tempFileSuffix) {
		try (final ResponseBody body = send()) {
			return body.write(targetFileOrDir, tempFileSuffix, this.streamProgress);
		} catch (final IOException e) {
			throw new HttpException(e);
		} finally {
			if (this.closeEngine) {
				IoUtil.closeQuietly(this.engine);
			}
		}
	}

	/**
	 * 下载文件
	 *
	 * @param out        输出流
	 * @param isCloseOut 是否关闭输出流，true关闭
	 * @return 下载的字节数
	 */
	public long download(final OutputStream out, final boolean isCloseOut) {
		try (final ResponseBody body = send()) {
			return body.write(out, isCloseOut, this.streamProgress);
		} catch (final IOException e) {
			throw new HttpException(e);
		} finally {
			if (this.closeEngine) {
				IoUtil.closeQuietly(this.engine);
			}
		}
	}

	/**
	 * 发送请求，获取响应
	 *
	 * @return 响应
	 */
	private ResponseBody send() {
		if (null == this.engine) {
			this.engine = ClientEngineFactory.createEngine();
		}
		if (null != this.config) {
			this.engine.init(this.config);
		}
		return engine.send(this.request).body();
	}
}
