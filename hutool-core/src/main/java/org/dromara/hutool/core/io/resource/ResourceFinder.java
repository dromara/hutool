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
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.text.AntPathMatcher;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

/**
 * 资源查找器
 */
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
		// 根目录，如 "/WEB-INF/*.xml" 返回 "/WEB-INF/"
		final String rootDirPath = determineRootDir(locationPattern);
		// 子表达式，如"/WEB-INF/*.xml" 返回 "*.xml"
		final String subPattern = locationPattern.substring(rootDirPath.length());

		final MultiResource result = new MultiResource();
		// 遍历根目录下所有资源，并过滤保留符合条件的资源
		for (final Resource rootResource : ResourceUtil.getResources(rootDirPath, classLoader)) {
			if (rootResource instanceof JarResource) {
				// 在jar包中
				try {
					result.addAll(findInJar((JarResource) rootResource, subPattern));
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			} else if (rootResource instanceof FileResource) {
				// 文件夹中
				result.addAll(findInDir((FileResource) rootResource, subPattern));
			} else {
				throw new HutoolException("Unsupported resource type: {}", rootResource.getClass().getName());
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
	protected MultiResource findInJar(final JarResource rootResource, final String subPattern) throws IOException {
		final URL rootDirURL = rootResource.getUrl();
		final URLConnection conn = rootDirURL.openConnection();

		final JarFile jarFile;
		String rootEntryPath;
		final boolean closeJarFile;

		if (conn instanceof JarURLConnection) {
			final JarURLConnection jarCon = (JarURLConnection) conn;
			UrlUtil.useCachesIfNecessary(jarCon);
			jarFile = jarCon.getJarFile();
			final JarEntry jarEntry = jarCon.getJarEntry();
			rootEntryPath = (jarEntry != null ? jarEntry.getName() : StrUtil.EMPTY);
			closeJarFile = !jarCon.getUseCaches();
		} else {
			// 去除子路径后重新获取jar文件
			final String urlFile = rootDirURL.getFile();
			try {
				int separatorIndex = urlFile.indexOf(UrlUtil.WAR_URL_SEPARATOR);
				if (separatorIndex == -1) {
					separatorIndex = urlFile.indexOf(UrlUtil.JAR_URL_SEPARATOR);
				}
				if (separatorIndex != -1) {
					final  String jarFileUrl = urlFile.substring(0, separatorIndex);
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
						result.add(ResourceUtil.getResource(UrlUtil.getURL(rootDirURL, relativePath)));
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

	/**
	 * 遍历目录查找指定表达式匹配的文件列表
	 *
	 * @param resource   文件资源
	 * @param subPattern 子表达式
	 * @return 满足条件的文件
	 */
	protected MultiResource findInDir(final FileResource resource, final String subPattern) {
		final MultiResource result = new MultiResource();
		final File rootDir = resource.getFile();
		if(!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()){
			// 保证给定文件存在、为目录且可读
			return result;
		}

		final String rootPath = rootDir.getAbsolutePath();
		final String fullPattern = FileNameUtil.normalize(rootPath + StrUtil.SLASH + subPattern);
		FileUtil.walkFiles(rootDir, (file -> {
			final String currentPath = StrUtil.normalize(file.getAbsolutePath());
			if(file.isDirectory()){
				// 检查目录是否满足表达式开始规则，满足则继续向下查找，否则跳过
				return pathMatcher.matchStart(fullPattern, StrUtil.addSuffixIfNot(currentPath, StrUtil.SLASH));
			}

			if (pathMatcher.match(fullPattern, currentPath)) {
				result.add(new FileResource(file));
				return true;
			}

			return false;
		}));

		return result;
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
