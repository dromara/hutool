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

package org.dromara.hutool.io.file;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.util.CharsetUtil;
import org.dromara.hutool.text.StrUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * {@link FileSystem}相关工具类封装<br>
 * 参考：https://blog.csdn.net/j16421881/article/details/78858690
 *
 * @author looly
 * @since 5.7.15
 */
public class FileSystemUtil {

	/**
	 * 创建 {@link FileSystem}
	 *
	 * @param path 文件路径，可以是目录或Zip文件等
	 * @return {@link FileSystem}
	 */
	public static FileSystem of(final String path) {
		try {
			return FileSystems.newFileSystem(
					Paths.get(path).toUri(),
					MapUtil.of("create", "true"));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建 Zip的{@link FileSystem}，默认UTF-8编码
	 *
	 * @param path 文件路径，可以是目录或Zip文件等
	 * @return {@link FileSystem}
	 */
	public static FileSystem createZip(final String path) {
		return createZip(path, null);
	}

	/**
	 * 创建 Zip的{@link FileSystem}
	 *
	 * @param path 文件路径，可以是目录或Zip文件等
	 * @param charset 编码
	 * @return {@link FileSystem}
	 */
	public static FileSystem createZip(final String path, Charset charset) {
		if(null == charset){
			charset = CharsetUtil.UTF_8;
		}
		final HashMap<String, String> env = new HashMap<>();
		env.put("create", "true");
		env.put("encoding", charset.name());

		try {
			return FileSystems.newFileSystem(
					URI.create("jar:" + Paths.get(path).toUri()), env);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取目录的根路径，或Zip文件中的根路径
	 *
	 * @param fileSystem {@link FileSystem}
	 * @return 根 {@link Path}
	 */
	public static Path getRoot(final FileSystem fileSystem) {
		return fileSystem.getPath(StrUtil.SLASH);
	}
}
