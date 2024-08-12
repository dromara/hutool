/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.exception.ValidateException;

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
