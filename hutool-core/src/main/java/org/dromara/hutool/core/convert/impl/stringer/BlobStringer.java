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

package org.dromara.hutool.core.convert.impl.stringer;

import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.InputStream;
import java.sql.Blob;
import java.util.function.Function;

/**
 * Blob转String
 *
 * @author looly
 * @since 6.0.0
 */
public class BlobStringer implements Function<Object, String> {

	/**
	 * 单例
	 */
	public static ClobStringer INSTANCE = new ClobStringer();


	@Override
	public String apply(final Object o) {
		return blobToStr((Blob) o);
	}

	/**
	 * Blob字段值转字符串
	 *
	 * @param blob    {@link java.sql.Blob}
	 * @return 字符串
	 * @since 5.4.5
	 */
	private static String blobToStr(final java.sql.Blob blob) {
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			return IoUtil.read(in, CharsetUtil.UTF_8);
		} catch (final java.sql.SQLException e) {
			throw new ConvertException(e);
		} finally {
			IoUtil.closeQuietly(in);
		}
	}
}
