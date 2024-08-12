/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.http.client.engine.jdk;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.util.SystemUtil;
import org.dromara.hutool.http.HttpException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * 针对{@link HttpURLConnection}相关工具
 *
 * @author looly
 * @since 6.0.0
 */
public class HttpUrlConnectionUtil {

	private static final String[] METHODS = {
			"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
	};

	/**
	 * 增加支持的METHOD方法<br>
	 * 此方法通过注入方式修改{@link HttpURLConnection}中的methods静态属性，增加PATCH方法<br>
	 * see: https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch
	 */
	public static void allowPatchQuietly() {
		try{
			allowPatch();
		} catch (final Exception ignore){
			// ignore
		}
	}

	/**
	 * 增加支持的METHOD方法<br>
	 * 此方法通过注入方式修改{@link HttpURLConnection}中的methods静态属性，增加PATCH方法<br>
	 * see: https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch
	 */
	public static void allowPatch() {
		doAllowPatch();
	}

	/**
	 * 增加支持的METHOD方法<br>
	 * 此方法通过注入方式修改{@link HttpURLConnection}中的methods静态属性，增加PATCH方法<br>
	 * see: https://stackoverflow.com/questions/25163131/httpurlconnection-invalid-http-method-patch
	 *
	 * @since 5.7.4
	 */
	synchronized private static void doAllowPatch() {
		// 注意此方法在jdk9+中抛出异常，须添加`--add-opens=java.base/java.lang=ALL-UNNAMED`启动参数
		final Field methodsField = FieldUtil.getField(HttpURLConnection.class, "methods");
		if (null == methodsField) {
			throw new HttpException("None static field [methods] with Java version: [{}]", SystemUtil.get("java.version"));
		}

		// 首先去除修饰符，否则设置值失败
		ModifierUtil.removeFinalModify(methodsField);
		final Object staticFieldValue = FieldUtil.getStaticFieldValue(methodsField);
		if (!ArrayUtil.equals(METHODS, staticFieldValue)) {
			// 去除final修饰
			FieldUtil.setStaticFieldValue(methodsField, METHODS);
		}
	}

	/**
	 * 初始化http或https请求参数<br>
	 * 有些时候https请求会出现com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl的实现，此为sun内部api，按照普通http请求处理
	 *
	 * @param url   请求的URL，必须为http
	 * @param proxy 代理，无代理传{@code null}
	 * @return {@link HttpURLConnection}，https返回{@link HttpsURLConnection}
	 * @throws IORuntimeException IO异常
	 */
	public static HttpURLConnection openHttp(final URL url, final Proxy proxy) throws IORuntimeException {
		final URLConnection conn = openConnection(url, proxy);
		if (!(conn instanceof HttpURLConnection)) {
			// 防止其它协议造成的转换异常
			throw new HttpException("'{}' of URL [{}] is not a http connection, make sure URL is format for http.",
					conn.getClass().getName(), url);
		}

		return (HttpURLConnection) conn;
	}

	/**
	 * 建立连接
	 *
	 * @return {@link URLConnection}
	 * @throws IORuntimeException IO异常
	 */
	private static URLConnection openConnection(final URL url, final Proxy proxy) throws IORuntimeException {
		try {
			return (null == proxy) ? url.openConnection() : url.openConnection(proxy);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
