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

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.exceptions.ValidateException;

import java.util.zip.ZipEntry;

/**
 * Zip安全相关工具类，如检查Zip bomb漏洞等
 *
 * @author looly
 * @since 6.0.0
 */
public class ZipSecurityUtil {

	/**
	 * 检查Zip bomb漏洞
	 *
	 * @param entry       {@link ZipEntry}
	 * @param maxSizeDiff 检查ZipBomb文件差异倍数，-1表示不检查ZipBomb
	 * @return 检查后的{@link ZipEntry}
	 */
	public static ZipEntry checkZipBomb(final ZipEntry entry, final int maxSizeDiff) {
		if (null == entry) {
			return null;
		}
		if (maxSizeDiff < 0 || entry.isDirectory()) {
			// 目录不检查
			return entry;
		}

		final long compressedSize = entry.getCompressedSize();
		final long uncompressedSize = entry.getSize();
		//Console.log(entry.getName(), compressedSize, uncompressedSize);
		if (compressedSize < 0 || uncompressedSize < 0 ||
			// 默认压缩比例是100倍，一旦发现压缩率超过这个阈值，被认为是Zip bomb
			compressedSize * maxSizeDiff < uncompressedSize) {
			throw new ValidateException("Zip bomb attack detected, invalid sizes: compressed {}, uncompressed {}, name {}",
				compressedSize, uncompressedSize, entry.getName());
		}
		return entry;
	}
}
