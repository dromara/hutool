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

import java.io.Closeable;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;

/**
 * Zip资源表示，如Zip流资源或Zip文件资源
 *
 * @author looly
 * @since 6.0.0
 */
public interface ZipResource extends Closeable {

	/**
	 * 读取并处理Zip文件中的每一个{@link ZipEntry}
	 *
	 * @param consumer    {@link ZipEntry}处理器
	 * @param maxSizeDiff 检查ZipBomb文件差异倍数，-1表示不检查ZipBomb
	 */
	void read(final Consumer<ZipEntry> consumer, final int maxSizeDiff);

	/**
	 * 获取指定路径的文件流<br>
	 * 如果是文件模式，则直接获取Entry对应的流，如果是流模式，则遍历entry后，找到对应流返回
	 *
	 * @param path 路径
	 * @return 文件流
	 */
	InputStream get(String path);

	/**
	 * 获取指定{@link ZipEntry}对应的文件流
	 *
	 * @param entry {@link ZipEntry}
	 * @return 文件流
	 */
	InputStream get(ZipEntry entry);
}
