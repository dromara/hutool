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

package org.dromara.hutool.http;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.net.url.UrlQueryUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.http.server.SimpleServer;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Http请求工具类
 *
 * @author xiaoleilu
 */
public class HttpUtil {

	/**
	 * 检测是否https
	 *
	 * @param url URL
	 * @return 是否https
	 */
	public static boolean isHttps(final String url) {
		return StrUtil.startWithIgnoreCase(url, "https:");
	}

	/**
	 * 检测是否http
	 *
	 * @param url URL
	 * @return 是否http
	 * @since 5.3.8
	 */
	public static boolean isHttp(final String url) {
		return StrUtil.startWithIgnoreCase(url, "http:");
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString     网址
	 * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final Charset customCharset) {
		return send(Request.of(urlString).charset(customCharset)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 */
	public static String get(final String urlString) {
		return get(urlString, HttpGlobalConfig.getTimeout());
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param timeout   超时时长，-1表示默认超时，单位毫秒
	 * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
	 * @since 3.2.0
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final int timeout) {
		return ClientEngineFactory.get()
				.setConfig(ClientConfig.of().setConnectionTimeout(timeout).setReadTimeout(timeout))
				.send(Request.of(urlString)).bodyStr();
	}

	/**
	 * 发送get请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String get(final String urlString, final Map<String, Object> paramMap) {
		return send(Request.of(urlString).form(paramMap))
				.bodyStr();
	}

	/**
	 * 发送post请求
	 *
	 * @param urlString 网址
	 * @param paramMap  post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String post(final String urlString, final Map<String, Object> paramMap) {
		return send(Request.of(urlString).method(Method.POST).form(paramMap))
				.bodyStr();
	}

	/**
	 * 发送post请求<br>
	 * 请求体body参数支持两种类型：
	 *
	 * <pre>
	 * 1. 标准参数，例如 a=1&amp;b=2 这种格式
	 * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，Hutool会自动绑定其对应的Content-Type
	 * </pre>
	 *
	 * @param urlString 网址
	 * @param body      post表单数据
	 * @return 返回数据
	 */
	@SuppressWarnings("resource")
	public static String post(final String urlString, final String body) {
		return send(Request.of(urlString).method(Method.POST).body(body))
				.bodyStr();
	}

	/**
	 * 使用默认HTTP引擎，发送HTTP请求
	 *
	 * @param request HTTP请求
	 * @return HTTP响应
	 */
	public static Response send(final Request request){
		return ClientEngineFactory.get().send(request);
	}

	/**
	 * 将表单数据加到URL中（用于GET表单提交）<br>
	 * 表单的键值对会被url编码，但是url中原参数不会被编码
	 *
	 * @param url            URL
	 * @param form           表单数据
	 * @param charset        编码
	 * @param isEncodeParams 是否对键和值做转义处理
	 * @return 合成后的URL
	 */
	public static String urlWithForm(String url, final Map<String, Object> form, final Charset charset, final boolean isEncodeParams) {
		if (isEncodeParams && StrUtil.contains(url, '?')) {
			// 在需要编码的情况下，如果url中已经有部分参数，则编码之
			url = UrlQueryUtil.encodeQuery(url, charset);
		}

		// url和参数是分别编码的
		return urlWithForm(url, UrlQueryUtil.toQuery(form, charset), charset, false);
	}

	/**
	 * 将表单数据字符串加到URL中（用于GET表单提交）
	 *
	 * @param url         URL
	 * @param queryString 表单数据字符串
	 * @param charset     编码
	 * @param isEncode    是否对键和值做转义处理
	 * @return 拼接后的字符串
	 */
	public static String urlWithForm(final String url, final String queryString, final Charset charset, final boolean isEncode) {
		if (StrUtil.isBlank(queryString)) {
			// 无额外参数
			if (StrUtil.contains(url, '?')) {
				// url中包含参数
				return isEncode ? UrlQueryUtil.encodeQuery(url, charset) : url;
			}
			return url;
		}

		// 始终有参数
		final StringBuilder urlBuilder = new StringBuilder(url.length() + queryString.length() + 16);
		final int qmIndex = url.indexOf('?');
		if (qmIndex > 0) {
			// 原URL带参数，则对这部分参数单独编码（如果选项为进行编码）
			urlBuilder.append(isEncode ? UrlQueryUtil.encodeQuery(url, charset) : url);
			if (false == StrUtil.endWith(url, '&')) {
				// 已经带参数的情况下追加参数
				urlBuilder.append('&');
			}
		} else {
			// 原url无参数，则不做编码
			urlBuilder.append(url);
			if (qmIndex < 0) {
				// 无 '?' 追加之
				urlBuilder.append('?');
			}
		}
		urlBuilder.append(isEncode ? UrlQueryUtil.encodeQuery(queryString, charset) : queryString);
		return urlBuilder.toString();
	}

	/**
	 * 创建简易的Http服务器
	 *
	 * @param port 端口
	 * @return {@link SimpleServer}
	 * @since 5.2.6
	 */
	public static SimpleServer createServer(final int port) {
		return new SimpleServer(port);
	}

	/**
	 * 构建简单的账号秘密验证信息，构建后类似于：
	 * <pre>
	 *     Basic YWxhZGRpbjpvcGVuc2VzYW1l
	 * </pre>
	 *
	 * @param username 账号
	 * @param password 密码
	 * @param charset  编码（如果账号或密码中有非ASCII字符适用）
	 * @return 密码验证信息
	 * @since 5.4.6
	 */
	public static String buildBasicAuth(final String username, final String password, final Charset charset) {
		final String data = username.concat(":").concat(password);
		return "Basic " + Base64.encode(data, charset);
	}
}
