/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.resource;

import org.dromara.hutool.core.compress.ZipUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.net.url.UrlUtil;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;

/**
 * Jar包资源对象
 *
 * @author looly
 */
public class JarResource extends UrlResource {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param uri JAR的URI
	 */
	public JarResource(final URI uri) {
		super(uri);
	}

	/**
	 * 构造
	 *
	 * @param url JAR的URL
	 */
	public JarResource(final URL url) {
		super(url);
	}

	/**
	 * 构造
	 *
	 * @param url  JAR的URL
	 * @param name 资源名称
	 */
	public JarResource(final URL url, final String name) {
		super(url, name);
	}

	/**
	 * 获取URL对应的{@link JarFile}对象
	 *
	 * @return {@link JarFile}
	 * @throws IORuntimeException IO异常
	 */
	public JarFile getJarFile() throws IORuntimeException {
		try {
			return doGetJarFile();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取{@link JarFile}<br>
	 * 首席按通过openConnection方式获取，如果得到的不是{@link JarURLConnection}，<br>
	 * 则尝试去除WAR、JAR等协议分隔符，裁剪分隔符前段来直接获取{@link JarFile}。
	 *
	 * @return {@link JarFile}
	 * @throws IOException IO异常
	 */
	private JarFile doGetJarFile() throws IOException {
		final URLConnection con = getUrl().openConnection();
		if (con instanceof JarURLConnection) {
			final JarURLConnection jarCon = (JarURLConnection) con;
			return jarCon.getJarFile();
		} else {
			final String urlFile = getUrl().getFile();
			int separatorIndex = urlFile.indexOf(UrlUtil.WAR_URL_SEPARATOR);
			if (separatorIndex == -1) {
				separatorIndex = urlFile.indexOf(UrlUtil.JAR_URL_SEPARATOR);
			}
			if (separatorIndex != -1) {
				return ZipUtil.ofJar(urlFile.substring(0, separatorIndex));
			} else {
				return new JarFile(urlFile);
			}
		}
	}
}
