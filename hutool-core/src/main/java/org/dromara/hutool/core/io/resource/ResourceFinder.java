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

import org.dromara.hutool.core.collection.iter.EnumerationIter;
import org.dromara.hutool.core.compress.ZipUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.net.url.URLUtil;
import org.dromara.hutool.core.text.AntPathMatcher;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

public class ResourceFinder {

	private final ClassLoader classLoader;
	private final AntPathMatcher pathMatcher;

	/**
	 * 构造
	 *
	 * @param classLoader 类加载器，用于定义查找资源的范围
	 */
	public ResourceFinder(final ClassLoader classLoader) {
		this.classLoader = classLoader;
		this.pathMatcher = new AntPathMatcher();
	}

	/**
	 * 查找给定表达式对应的资源
	 *
	 * @param locationPattern 路径表达式
	 * @return {@link MultiResource}
	 */
	public MultiResource find(final String locationPattern) {
		// 根目录
		final String rootDirPath = determineRootDir(locationPattern);
		// 子表达式
		final String subPattern = locationPattern.substring(rootDirPath.length());

		final MultiResource result = new MultiResource();
		// 遍历根目录下所有资源，并过滤保留符合条件的资源
		for (final Resource rootResource : ResourceUtil.getResources(rootDirPath, classLoader)) {
			if (URLUtil.isJarURL(rootResource.getUrl())) {
				try {
					result.addAll(findInJar(rootResource, subPattern));
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			} else {
				result.addAll(findInDir(rootResource, subPattern));
			}
		}

		return result;
	}

	/**
	 * 查找jar包中的资源
	 *
	 * @param rootResource 根资源，为jar包文件
	 * @param subPattern   子表达式，如 *.xml
	 * @return 符合条件的资源
	 * @throws IOException IO异常
	 */
	protected MultiResource findInJar(final Resource rootResource, final String subPattern) throws IOException {
		final URL rootDirURL = rootResource.getUrl();
		final URLConnection conn = rootDirURL.openConnection();

		final JarFile jarFile;
		final String jarFileUrl;
		String rootEntryPath;
		final boolean closeJarFile;

		if (conn instanceof JarURLConnection) {
			final JarURLConnection jarCon = (JarURLConnection) conn;
			URLUtil.useCachesIfNecessary(jarCon);
			jarFile = jarCon.getJarFile();
			final JarEntry jarEntry = jarCon.getJarEntry();
			rootEntryPath = (jarEntry != null ? jarEntry.getName() : StrUtil.EMPTY);
			closeJarFile = !jarCon.getUseCaches();
		} else {
			//
			final String urlFile = rootDirURL.getFile();
			try {
				int separatorIndex = urlFile.indexOf(URLUtil.WAR_URL_SEPARATOR);
				if (separatorIndex == -1) {
					separatorIndex = urlFile.indexOf(URLUtil.JAR_URL_SEPARATOR);
				}
				if (separatorIndex != -1) {
					jarFileUrl = urlFile.substring(0, separatorIndex);
					rootEntryPath = urlFile.substring(separatorIndex + 2);  // both separators are 2 chars
					jarFile = ZipUtil.ofJar(jarFileUrl);
				} else {
					jarFile = new JarFile(urlFile);
					rootEntryPath = StrUtil.EMPTY;
				}
				closeJarFile = true;
			} catch (final ZipException ex) {
				return new MultiResource();
			}
		}

		rootEntryPath = StrUtil.addSuffixIfNot(rootEntryPath, StrUtil.SLASH);
		// 遍历jar中的entry，筛选之
		final MultiResource result = new MultiResource();

		try {
			String entryPath;
			for (final JarEntry entry : new EnumerationIter<>(jarFile.entries())) {
				entryPath = entry.getName();
				if (entryPath.startsWith(rootEntryPath)) {
					final String relativePath = entryPath.substring(rootEntryPath.length());
					if (pathMatcher.match(subPattern, relativePath)) {
						result.add(new UrlResource(URLUtil.getURL(rootDirURL, relativePath)));
					}
				}
			}
		} finally {
			if (closeJarFile) {
				IoUtil.closeQuietly(jarFile);
			}
		}

		return result;
	}

	protected MultiResource findInDir(final Resource rootResource, final String subPattern) {

	}

	/**
	 * 根据给定的路径表达式，找到跟路径<br>
	 * 根路径即不包含表达式的路径，如 "/WEB-INF/*.xml" 返回 "/WEB-INF/"
	 *
	 * @param location 路径表达式
	 */
	protected String determineRootDir(final String location) {
		final int prefixEnd = location.indexOf(':') + 1;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd && pathMatcher.isPattern(location.substring(prefixEnd, rootDirEnd))) {
			rootDirEnd = location.lastIndexOf(CharUtil.SLASH, rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
	}
}
