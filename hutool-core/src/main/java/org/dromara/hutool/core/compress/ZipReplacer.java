/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Zip文件替换，用户替换源Zip文件，并生成新的文件
 *
 * @author looly
 * @since 6.0.0
 */
public class ZipReplacer implements Closeable {

	private final ZipReader zipReader;
	private final boolean ignoreCase;

	private final Map<String, Resource> replacedResources = new HashMap<>();

	/**
	 * 构造
	 *
	 * @param zipReader  ZipReader
	 * @param ignoreCase 是否忽略path大小写
	 */
	public ZipReplacer(final ZipReader zipReader, final boolean ignoreCase) {
		this.zipReader = zipReader;
		this.ignoreCase = ignoreCase;
	}

	/**
	 * 增加替换的内容，如果路径不匹配，则不做替换，也不加入
	 *
	 * @param entryPath 路径
	 * @param resource  被压缩的内容
	 * @return this
	 */
	public ZipReplacer addReplace(final String entryPath, final Resource resource) {
		replacedResources.put(entryPath, resource);
		return this;
	}

	/**
	 * 写出到{@link ZipWriter}
	 *
	 * @param writer {@link ZipWriter}
	 */
	public void write(final ZipWriter writer) {
		zipReader.read((entry) -> {
			String entryName;
			for (final String key : replacedResources.keySet()) {
				entryName = entry.getName();
				if (isSamePath(entryName, key, ignoreCase)) {
					writer.add(key, replacedResources.get(key).getStream());
				} else {
					writer.add(entryName, zipReader.get(entryName));
				}
			}
		});
	}

	@Override
	public void close() throws IOException {
		this.zipReader.close();
	}

	/**
	 * 判断路径是否相等
	 *
	 * @param entryPath  路径A
	 * @param targetPath 路径B
	 * @param ignoreCase 是否忽略大小写
	 * @return ture 路径相等
	 */
	private static boolean isSamePath(String entryPath, String targetPath, final boolean ignoreCase) {
		entryPath = StrUtil.removePrefix(FileUtil.normalize(entryPath), StrUtil.SLASH);
		targetPath = StrUtil.removePrefix(FileUtil.normalize(targetPath), StrUtil.SLASH);
		return StrUtil.equals(entryPath, targetPath, ignoreCase);
	}
}
