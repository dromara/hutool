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
