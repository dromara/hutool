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

package org.dromara.hutool.core.classloader;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 外部Jar的类加载器
 *
 * @author Looly
 */
public class JarClassLoader extends URLClassLoader {

	/**
	 * 加载Jar到ClassPath
	 *
	 * @param dir jar文件或所在目录
	 * @return JarClassLoader
	 */
	public static JarClassLoader load(final File dir) {
		final JarClassLoader loader = new JarClassLoader();
		loader.addJar(dir);//查找加载所有jar
		loader.addURL(dir);//查找加载所有class
		return loader;
	}

	/**
	 * 加载Jar到ClassPath
	 *
	 * @param jarFile jar文件或所在目录
	 * @return JarClassLoader
	 */
	public static JarClassLoader loadJar(final File jarFile) {
		final JarClassLoader loader = new JarClassLoader();
		loader.addJar(jarFile);
		return loader;
	}

	/**
	 * 加载Jar文件到指定loader中
	 *
	 * @param loader  {@link URLClassLoader}
	 * @param jarFile 被加载的jar
	 * @throws HutoolException IO异常包装和执行异常
	 */
	public static void loadJar(final URLClassLoader loader, final File jarFile) throws HutoolException {
		try {
			final Method method = MethodUtil.getMethod(URLClassLoader.class, "addURL", URL.class);
			if (null != method) {
				final List<File> jars = loopJar(jarFile);
				for (final File jar : jars) {
					MethodUtil.invoke(loader, method, jar.toURI().toURL());
				}
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 加载Jar文件到System ClassLoader中
	 *
	 * @param jarFile 被加载的jar
	 * @return System ClassLoader
	 */
	public static URLClassLoader loadJarToSystemClassLoader(final File jarFile) {
		final URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		loadJar(urlClassLoader, jarFile);
		return urlClassLoader;
	}

	// ------------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 */
	public JarClassLoader() {
		this(new URL[]{});
	}

	/**
	 * 构造
	 *
	 * @param urls 被加载的URL
	 */
	public JarClassLoader(final URL[] urls) {
		super(urls, ClassLoaderUtil.getClassLoader());
	}

	/**
	 * 构造
	 *
	 * @param urls        被加载的URL
	 * @param classLoader 类加载器
	 */
	public JarClassLoader(final URL[] urls, final ClassLoader classLoader) {
		super(urls, classLoader);
	}
	// ------------------------------------------------------------------- Constructor end

	/**
	 * 加载Jar文件，或者加载目录
	 *
	 * @param jarFileOrDir jar文件或者jar文件所在目录
	 * @return this
	 */
	@SuppressWarnings("resource")
	public JarClassLoader addJar(final File jarFileOrDir) {
		// loopJar方法中，如果传入的是jar文件，直接返回此文件
		final List<File> jars = loopJar(jarFileOrDir);
		for (final File jar : jars) {
			addURL(jar);
		}
		return this;
	}

	@Override
	public void addURL(final URL url) {
		super.addURL(url);
	}

	/**
	 * 增加class所在目录或文件<br>
	 * 如果为目录，此目录用于搜索class文件，如果为文件，需为jar文件
	 *
	 * @param dir 目录
	 * @return this
	 * @since 4.4.2
	 */
	public JarClassLoader addURL(final File dir) {
		super.addURL(UrlUtil.getURL(dir));
		return this;
	}

	// ------------------------------------------------------------------- Private method start

	/**
	 * 递归获得Jar文件
	 *
	 * @param file jar文件或者包含jar文件的目录
	 * @return jar文件列表
	 */
	private static List<File> loopJar(final File file) {
		return FileUtil.loopFiles(file, JarClassLoader::isJarFile);
	}

	/**
	 * 是否为jar文件
	 *
	 * @param file 文件
	 * @return 是否为jar文件
	 * @since 4.4.2
	 */
	private static boolean isJarFile(final File file) {
		return FileUtil.isFile(file) &&
			FileNameUtil.isType(file.getName(), "jar");
	}
	// ------------------------------------------------------------------- Private method end
}
