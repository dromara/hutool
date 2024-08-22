/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.lang.Assert;

import java.net.URL;

/**
 * URL协议相关工具类<br>
 * 用于判断URL的协议类型，如jar或file等
 *
 * @author looly
 * @since 6.0.0
 */
public class UrlProtocolUtil {
	/**
	 * URL 协议表示文件: "file"
	 */
	public static final String URL_PROTOCOL_FILE = "file";
	/**
	 * URL 协议表示Jar文件: "jar"
	 */
	public static final String URL_PROTOCOL_JAR = "jar";
	/**
	 * URL 协议表示zip文件: "zip"
	 */
	public static final String URL_PROTOCOL_ZIP = "zip";
	/**
	 * URL 协议表示WebSphere文件: "wsjar"
	 */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";
	/**
	 * URL 协议表示JBoss zip文件: "vfszip"
	 */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";
	/**
	 * URL 协议表示JBoss文件: "vfsfile"
	 */
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";
	/**
	 * URL 协议表示JBoss VFS资源: "vfs"
	 */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/**
	 * 提供的URL是否为文件<br>
	 * 文件协议包括"file", "vfsfile" 或 "vfs".
	 *
	 * @param url {@link URL}
	 * @return 是否为文件
	 * @since 3.0.9
	 */
	public static boolean isFileOrVfsURL(final URL url) {
		Assert.notNull(url, "URL must be not null");
		return isFileURL(url) || isVfsURL(url);
	}

	/**
	 * 提供的URL是否为文件<br>
	 * 文件协议包括"file".
	 *
	 * @param url {@link URL}
	 * @return 是否为文件
	 * @since 6.0.0
	 */
	public static boolean isFileURL(final URL url) {
		Assert.notNull(url, "URL must be not null");
		final String protocol = url.getProtocol();
		return URL_PROTOCOL_FILE.equals(protocol);
	}

	/**
	 * 提供的URL是否为文件<br>
	 * 文件协议包括"vfsfile" 或 "vfs".
	 *
	 * @param url {@link URL}
	 * @return 是否为文件
	 * @since 6.0.0
	 */
	public static boolean isVfsURL(final URL url) {
		Assert.notNull(url, "URL must be not null");
		final String protocol = url.getProtocol();
		return (URL_PROTOCOL_VFSFILE.equals(protocol) || //
			URL_PROTOCOL_VFS.equals(protocol));
	}

	/**
	 * 提供的URL是否为jar包URL 协议包括： "jar", "zip", "vfszip" 或 "wsjar".
	 *
	 * @param url {@link URL}
	 * @return 是否为jar包URL
	 */
	public static boolean isJarURL(final URL url) {
		Assert.notNull(url, "URL must be not null");
		final String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) || //
			URL_PROTOCOL_ZIP.equals(protocol) || //
			URL_PROTOCOL_VFSZIP.equals(protocol) || //
			URL_PROTOCOL_WSJAR.equals(protocol));
	}

	/**
	 * 提供的URL是否为Jar文件URL 判断依据为file协议且扩展名为.jar
	 *
	 * @param url the URL to check
	 * @return whether the URL has been identified as a JAR file URL
	 * @since 4.1
	 */
	public static boolean isJarFileURL(final URL url) {
		Assert.notNull(url, "URL must be not null");
		return (URL_PROTOCOL_FILE.equals(url.getProtocol()) && //
			url.getPath().toLowerCase().endsWith(FileNameUtil.EXT_JAR));
	}
}
